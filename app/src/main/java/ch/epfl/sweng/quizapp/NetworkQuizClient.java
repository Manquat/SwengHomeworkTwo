package ch.epfl.sweng.quizapp;

/**
 * A {@link QuizClient} implementation that uses a {@link NetworkProvider} to
 * communicate with a SwEng quiz server.
 *
 */
public class NetworkQuizClient implements QuizClient {

    /**
     * Creates a new NetworkQuizClient instance that communicates with a SwEng
     * server at a given location, through a {@link NetworkProvider} object.
     * @param serverUrl the base URL of the SwEng server
     * (e.g., "https://sweng-quiz.appspot.com").
     * @param networkProvider a NetworkProvider object through which to channel
     * the server communication.
     */
    public NetworkQuizClient(String serverUrl, NetworkProvider networkProvider) {
        // TODO
    }

    @Override
    public QuizQuestion fetchRandomQuestion() throws QuizClientException {

        // TODO
        return null;
    }

    // TODO
}