package ch.epfl.sweng.quizapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class QuizActivity extends AppCompatActivity {

    private TextView questionText_;
    private RadioButton answerButton1_;
    private RadioButton answerButton2_;
    private RadioButton answerButton3_;
    private RadioButton answerButton4_;

    private QuizQuestion quizQuestion_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionText_ = (TextView) findViewById(R.id.questionBodyView);
        answerButton1_ = (RadioButton) findViewById((R.id.radioButton1));
        answerButton2_ = (RadioButton) findViewById((R.id.radioButton2));
        answerButton3_ = (RadioButton) findViewById((R.id.radioButton3));
        answerButton4_ = (RadioButton) findViewById((R.id.radioButton4));

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
        answerButton1_.setText(quizQuestion_.getAnswers().get(0));
        answerButton2_.setText(quizQuestion_.getAnswers().get(1));
        answerButton3_.setText(quizQuestion_.getAnswers().get(2));
        answerButton4_.setText(quizQuestion_.getAnswers().get(3));
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
