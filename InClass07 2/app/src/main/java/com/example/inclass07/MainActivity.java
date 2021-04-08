package com.example.inclass07;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {




    TextView triviaContentView;
    ImageView triviaImageView;
    Button exitButton;
    Button startButton;
    ProgressBar progressBar;

    ArrayList<Quiz> questionsList =new ArrayList<>();
    static String QUIZ_KEY="QUIZ";


    String triviaUrl="https://dev.theappsdr.com/apis/trivia_json/index.php";

    private static final int REQ_CODE =8006;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isConnected()) {
            new getJSONData().execute();
        }
        else
        {

        }

        exitButton =findViewById(R.id.exitButton);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    class getJSONData extends AsyncTask<Void,Void, ArrayList<Quiz>>
    {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar=findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            triviaContentView =findViewById(R.id.contentTriviaView);
            triviaContentView.setVisibility(View.VISIBLE);
            triviaContentView.setText("Loading Trivia");
            triviaImageView=findViewById(R.id.triviaImageView);
            triviaImageView.setVisibility(View.INVISIBLE);
        }


        @Override
        protected ArrayList<Quiz> doInBackground(Void... voids) {

            HttpURLConnection connection=null;
            ArrayList<Quiz> quiz =new ArrayList<>();

            try {
                URL url =new URL(triviaUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if(connection.getResponseCode()== HttpURLConnection.HTTP_OK)
                {

                    String jsonQuiz = IOUtils.toString(connection.getInputStream(),"UTF-8");
                    JSONObject root =new JSONObject(jsonQuiz);
                    JSONArray questionsArray = root.getJSONArray("questions");
                    for(int i=0;i<questionsArray.length();i++)
                    {

                        JSONObject questionsJSON = questionsArray.getJSONObject(i);

                        Quiz quizItem =new Quiz();

                        quizItem.id= questionsJSON.getInt("id");
                        quizItem.text =questionsJSON.getString("text");


                        if(questionsJSON.has("image")) {
                            quizItem.image = questionsJSON.getString("image");
                        }
                        else
                        {
                            quizItem.image ="";
                        }

                        JSONObject choicesObject=questionsJSON.getJSONObject("choices");

                        JSONArray choicesArray = choicesObject.getJSONArray("choice");

                        ArrayList<String> choicesList =new ArrayList<>();


                        for(int j=0;j<choicesArray.length();j++) {
                            choicesList.add(choicesArray.get(j).toString());
                        }
                        quizItem.choice=choicesList;

                        quizItem.answer =choicesObject.getInt("answer");
                         quiz.add(quizItem);
                    }


                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return quiz;
        }


        @Override
        protected void onPostExecute(ArrayList<Quiz> quiz) {
            super.onPostExecute(quiz);


            progressBar=findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);


            triviaImageView=findViewById(R.id.triviaImageView);
            triviaImageView.setVisibility(View.VISIBLE);

            triviaContentView =findViewById(R.id.contentTriviaView);
            triviaContentView.setVisibility(View.VISIBLE);

            triviaContentView.setText("Trivia Ready");

            questionsList=quiz;



            startButton =findViewById(R.id.startButton);
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(MainActivity.this,TriviaActivity.class);
                    intent.putExtra(QUIZ_KEY,questionsList);
                    startActivityForResult(intent,REQ_CODE);
                }
            });




        }
    }

    @Override
    protected void onActivityResult(int request, int result, @Nullable Intent data) {
        super.onActivityResult(request, result, data);

        if(request == REQ_CODE && result ==RESULT_OK && data !=null)
        {
            int response = data.getExtras().getInt(TriviaActivity.RESTART);

            if (response == 1)
            {
                Intent intent =new Intent(MainActivity.this,TriviaActivity.class);
                intent.putExtra(QUIZ_KEY,questionsList);
                startActivityForResult(intent,REQ_CODE);
            }

        }


    }
}
