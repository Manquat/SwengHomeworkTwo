package ch.epfl.sweng.quizapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A {@link QuizClient} implementation that uses a {@link NetworkProvider} to
 * communicate with a SwEng quiz server.
 *
 */
public class NetworkQuizClient implements QuizClient
{
    private final String serverUrl_;
    private final NetworkProvider netProvider_;
    /**
     * Creates a new NetworkQuizClient instance that communicates with a SwEng
     * server at a given location, through a {@link NetworkProvider} object.
     * @param serverUrl the base URL of the SwEng server
     * (e.g., "https://sweng-quiz.appspot.com").
     * @param networkProvider a NetworkProvider object through which to channel
     * the server communication.
     */
    public NetworkQuizClient(String serverUrl, NetworkProvider networkProvider)
    {
        serverUrl_ = serverUrl;
        netProvider_ = networkProvider;
    }

    @Override
    public QuizQuestion fetchRandomQuestion() throws QuizClientException
    {
        JSONObject jsonObject;
        QuizQuestion quizQuestion;
        try
        {
            URL url = new URL(serverUrl_);
            HttpURLConnection urlConnection = netProvider_.getConnection(url);

            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

            // for efficiency we create a string builder to convert it afterward in string
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder(inputStream.available());
            String line;

            while ((line = bufferedReader.readLine()) != null) // append the line until it's the end of the stream
            {
                stringBuilder.append(line);
            }

            // transform the stringBuilder in a JSONObject
            jsonObject = new JSONObject(stringBuilder.toString());
            quizQuestion = QuizQuestion.parseFromJSON(jsonObject);
        }
        catch (JSONException | IOException exception)
        {
            throw new QuizClientException(exception);
        }

        return quizQuestion;
    }
}