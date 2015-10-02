package ch.epfl.sweng.quizapp;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Created by Gautier on 02/10/2015.
 */
public class QuizActivityTest extends ActivityInstrumentationTestCase2<QuizActivity>
{
    public QuizActivityTest()
    {
        super(QuizActivity.class);
    }

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }


    /**
     * Testing if the question is correctly shown
     */
    public void testQuestionBody()
    {
        Provider.setQuizClient(new TestQuizClient());
        getActivity();
        onView(withId(R.id.questionBodyView)).check(matches(withText("Testing question")));
    }

    /**
     * Testing the nextButton
     */
    public void testNextButton()
    {
        Provider.setQuizClient(new TestQuizClient());
        getActivity();

        onView(withId(R.id.nextButton)).check(matches(not(isEnabled())));
        onView(withText("Answer 1")).perform(click());
        onView(withId(R.id.nextButton)).check(matches(isEnabled()));
    }

    /**
     * Testing the radioButton in case of correct answers
     */
    public void testRadioButtonCorrectAnswer()
    {
        Provider.setQuizClient(new TestQuizClient());
        getActivity();

        onView(withText("Answer 1")).check(matches(isEnabled()));
        onView(withText("Answer 1")).perform(click());
        onView(withText("Answer 1")).check(matches(not(isEnabled())));
        onView(withId(R.id.nextButton)).check(matches(isEnabled()));
    }

    /**
     * Testing the radioButton in case of wrong answers
     */
    public void testRadioButtonWrongAnswer()
    {
        Provider.setQuizClient(new TestQuizClient());
        getActivity();

        onView(withText("Answer 3")).check(matches(isEnabled()));
        onView(withText("Answer 3")).perform(click());
        onView(withText("Answer 3")).check(matches(not(isEnabled())));
        onView(withId(R.id.nextButton)).check(matches(not(isEnabled())));
    }



}

/**
 * A moke of the QuizClient to return a preconfigured QuizQuestion
 */
class TestQuizClient implements QuizClient
{
    public TestQuizClient()
    {}

    @Override
    public QuizQuestion fetchRandomQuestion() throws QuizClientException
    {
        ArrayList<String> defaultAnswers = new ArrayList<>();
        defaultAnswers.add("Answer 1");
        defaultAnswers.add("Answer 2");
        defaultAnswers.add("Answer 3");
        defaultAnswers.add("Answer 4");

        ArrayList<String> defaultTags = new ArrayList<>();
        defaultTags.add("Tag 1");

        return new QuizQuestion(0, "Manquat", "Testing question", defaultAnswers, 0, defaultTags);
    }
}
