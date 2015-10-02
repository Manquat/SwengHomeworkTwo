package ch.epfl.sweng.quizapp;

/**
 * Created by Gautier on 02/10/2015.
 */
public final class Provider {

    private Provider()
    {}

    static String url_ = "https://sweng-quiz.appspot.com/quizquestions/random";
    static protected NetworkProvider networkProvider_ = new DefaultNetworkProvider();
    static protected QuizClient quizClient_ = new NetworkQuizClient(url_, networkProvider_);

    static String getUrl()
    {
        return url_;
    }

    static public NetworkProvider getNetworkProvider()
    {
        return networkProvider_;
    }

    static public QuizClient getQuizClient()
    {
        return quizClient_;
    }

    static public void setUrl(String url)
    {
        url_ = url;
    }

    static public void setNetworkProvider(NetworkProvider networkProvider)
    {
        networkProvider_ = networkProvider;
    }

    static public void setQuizClient(QuizClient quizClient)
    {
        quizClient_ = quizClient;
    }
}
