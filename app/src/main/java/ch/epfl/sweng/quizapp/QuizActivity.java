package ch.epfl.sweng.quizapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class QuizActivity extends AppCompatActivity {

    private TextView questionText_;
    private RadioGroup answerGroup_;
    private Button nextButton_;

    private QuizQuestion quizQuestion_;

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
                    checkedButton.setTextColor(Color.GREEN);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
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

            new downloadFromServerTask().execute(new NetworkQuizClient("https://sweng-quiz.appspot.com/quizquestions/random", new DefaultNetworkProvider()));
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(R.string.network_error_title)
                    .setMessage(R.string.network_error_message)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

                builder.setTitle("QuizException")
                        .setMessage(R.string.network_error_message)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(QuizQuestion quizQuestion)
        {
            quizQuestion_ = quizQuestion;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
