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

    private TextView questionText_;
    private RadioGroup answerGroup_;
    private Button nextButton_;

    private QuizQuestion quizQuestion_;

    private String url_;

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

        url_ = getString(R.string.url);

        connectToServer();
    }

    public void onClickNextButton(View view)
    {
        connectToServer();
    }

    protected void connectToServer()
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            // create a new thread that will connect to the server
            new downloadFromServerTask().execute(new NetworkQuizClient(url_, new DefaultNetworkProvider()));
        }
        else
        {
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, getString(R.string.network_error_toast), Toast.LENGTH_LONG);
            toast.show();
            nextButton_.setEnabled(true);
        }
    }

    private class downloadFromServerTask extends AsyncTask<NetworkQuizClient, Void, QuizQuestion>
    {

        @Override
        protected QuizQuestion doInBackground(NetworkQuizClient... networkQuizClients)
        {
            try
            {
                return networkQuizClients[0].fetchRandomQuestion();
            }
            catch (QuizClientException quizException)
            {
                /*ArrayList<String> tempAnswers = new ArrayList<>();
                tempAnswers.add("Try again !");

                ArrayList<String> tempTags = new ArrayList<>();
                tempTags.add("Error");
                tempTags.add("JSON Parse");

                return new QuizQuestion(0000, "Manquat","Error while parsing the JSON", tempAnswers, 0, tempTags);*/

                return null;
            }
        }

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
