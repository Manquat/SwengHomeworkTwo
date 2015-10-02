package ch.epfl.sweng.quizapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class QuizActivity extends AppCompatActivity {

    protected TextView questionText_;
    protected RadioGroup answerGroup_;
    protected Button nextButton_;

    protected QuizQuestion quizQuestion_;

    /**
     * Instansiate the activity and fetch a question for the first using the connnectToServer function
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionText_ = (TextView) findViewById(R.id.questionBodyView);
        nextButton_ = (Button) findViewById(R.id.nextButton);

        // initialize the RadioGroup
        answerGroup_ = (RadioGroup) findViewById(R.id.radioGroup);
        answerGroup_.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedButton = (RadioButton) findViewById(checkedId);
                if (answerGroup_.indexOfChild(checkedButton) == quizQuestion_.getSolutionIndex()) //correct answer
                {
                    for (int i=0; i < answerGroup_.getChildCount(); i++)
                    {
                        RadioButton tempRadioButton = (RadioButton) answerGroup_.getChildAt(i);
                        tempRadioButton.setTextColor(Color.RED);
                        tempRadioButton.setEnabled(false);
                        tempRadioButton.setPaintFlags(tempRadioButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    checkedButton.setTextColor(Color.GREEN);
                    checkedButton.setPaintFlags(checkedButton.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    nextButton_.setEnabled(true);
                } else // wrong answer
                {
                    checkedButton.setTextColor(Color.RED);
                    checkedButton.setEnabled(false);
                    checkedButton.setPaintFlags(checkedButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        });

        connectToServer();
    }

    /**
     * Call when the user click on the next button,
     * just call the connectToServer function
     * @param view not used but necessary for been a listener
     */
    public void onClickNextButton(View view)
    {
        connectToServer();
    }

    /**
     * Connect to the server to fetching the question
     * Use an asynchronous task to avoid the freezing of the app
     */
    protected void connectToServer()
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            // create a new thread that will connect to the server
            new downloadFromServerTask().execute(Provider.getQuizClient());
        }
        else
        {
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, getString(R.string.network_error_toast), Toast.LENGTH_LONG);
            toast.show();
            nextButton_.setEnabled(true);
        }
    }

    protected class downloadFromServerTask extends AsyncTask<QuizClient, Void, QuizQuestion>
    {

        /**
         * Contact the server from the QuizClient and cast the response in a QuizQuestion
         * @param quizClients the quiz client used to fetching the question
         * @return The quiz question return by the server, it would be null if the server is unreachable
         * or return something wrong
         */
        @Override
        protected QuizQuestion doInBackground(QuizClient... quizClients)
        {
            try
            {
                return quizClients[0].fetchRandomQuestion();
            }
            catch (QuizClientException quizException)
            {
                return null;
            }
        }

        /**
         * Use the quizQuestion to refresh the activity accordingly
         * @param quizQuestion The QuizQuestion that would be display
         */
        @Override
        protected void onPostExecute(QuizQuestion quizQuestion)
        {
            quizQuestion_ = quizQuestion;

            if (quizQuestion_ == null)
            {
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, getString(R.string.network_error_toast), Toast.LENGTH_LONG);
                toast.show();
                nextButton_.setEnabled(true);

                return;
            }

            refreshActivity();
        }
    }

    /**
     * refresh the Activity to match the question store in the quizQuestion attribute
     */
    protected void refreshActivity()
    {
        questionText_.setText(quizQuestion_.getBody());

        // the next button is again disable
        nextButton_.setEnabled(false);

        // clear the precedents container
        answerGroup_.removeAllViews();

        for (int i=0; i < quizQuestion_.getAnswers().size(); i++)
        {
            RadioButton tempButton = new RadioButton(this);
            tempButton.setText(quizQuestion_.getAnswers().get(i));
            answerGroup_.addView(tempButton);
        }
    }
}
