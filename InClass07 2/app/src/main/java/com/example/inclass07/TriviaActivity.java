package com.example.inclass07;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TriviaActivity extends AppCompatActivity implements MyAdapter.InteractWithTriviaActivity {

    ProgressBar triviaProgressBar;
    TextView qNumber;
    TextView timerCount;
    TextView questionView;
    TextView loadView;



    Button quitButton;
    Button nextButton;


    ImageView imageView;

    static int i=0;

    ArrayList<Quiz> quiz;
    ArrayList<Integer> resultsList =new ArrayList<>();
    int questionNumber=0;
    static String STATS_KEY="STATS";
    private static final int REQ_CODE =8005;
    private static final int REQ =8015;
    static String RESTART="restart";

    RecyclerView rvOptions;
    RecyclerView.Adapter rvAdapter;
    RecyclerView.LayoutManager rvLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        imageView =findViewById(R.id.imageView);
        qNumber =findViewById(R.id.qNumTextView);
        timerCount =findViewById(R.id.timerView);
        questionView =findViewById(R.id.questionView);
        quitButton=findViewById(R.id.statsQuitBtn);
        nextButton=findViewById(R.id.nextButton);


        resultsList.clear();
        i=0;

        new CountDownTimer(2*60*1000, 1000) {
            @Override
            public void onTick(long l) {
                long minute = l/1000/60;
                long second = (l - minute*60*1000)/1000;
                timerCount.setText(minute+" : "+second);
            }

            @Override
            public void onFinish() {
                Intent in =new Intent(TriviaActivity.this,StatsActivity.class);

                in.putExtra(STATS_KEY,resultsList);
                startActivityForResult(in,REQ_CODE);
            }
        }.start();

        if(getIntent()!= null && getIntent().getExtras()!=null)
        {
            quiz = (ArrayList<Quiz>) getIntent().getExtras().getSerializable(MainActivity.QUIZ_KEY);

            questionNumber =(quiz.get(i).id)+1;
            qNumber.setText("Q"+questionNumber);
            questionView.setText(quiz.get(i).text);

               new GetImageAsync().execute(quiz.get(i).image);

            rvOptions = findViewById(R.id.optionsRecyclerView);

            rvOptions.setHasFixedSize(true);

            rvLayoutManager = new LinearLayoutManager(this);
            rvOptions.setLayoutManager(rvLayoutManager);

            rvAdapter = new MyAdapter(quiz.get(i).choice,this,questionNumber);
            rvOptions.setAdapter(rvAdapter);

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    i=i+1;
                    resultsList.add(0);
                    if(i<quiz.size()) {

                        questionNumber = (quiz.get(i).id) + 1;
                        qNumber.setText("Q" + questionNumber);
                        questionView.setText(quiz.get(i).text);
                        if(quiz.get(i).image !="") {
                            new GetImageAsync().execute(quiz.get(i).image);
                        }
                        else
                        {
                            imageView =findViewById(R.id.imageView);
                            imageView.setImageResource(R.drawable.plain_image);
                        }
                        rvOptions = findViewById(R.id.optionsRecyclerView);

                        rvOptions.setHasFixedSize(true);

                        rvLayoutManager = new LinearLayoutManager(TriviaActivity.this);
                        rvOptions.setLayoutManager(rvLayoutManager);

                        rvAdapter = new MyAdapter(quiz.get(i).choice, TriviaActivity.this,questionNumber);
                        rvOptions.setAdapter(rvAdapter);
                    }
                    else
                    {
                        Intent in =new Intent(TriviaActivity.this,StatsActivity.class);

                        in.putExtra(STATS_KEY,resultsList);
                        startActivityForResult(in,REQ_CODE);

                    }
                }
            });


        }
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(TriviaActivity.this,MainActivity.class);
                startActivityForResult(intent,REQ);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==REQ_CODE && resultCode == RESULT_OK && data !=null)
        {
            int v = data.getExtras().getInt(StatsActivity.KEY);

            Intent i =new Intent();
            i.putExtra(RESTART,v);
            setResult(RESULT_OK,i);

            finish();
        }

    }

    @Override
    public void selectChoice(int position,int questionNumber) {

        i =questionNumber-1;

        if((position +1) == (quiz.get(i).answer))
        {
            resultsList.add(1);


        }
        else {
            resultsList.add(0);

        }

        i=i+1;
        if(i<quiz.size()) {

            questionNumber = (quiz.get(i).id) + 1;
            qNumber.setText("Q" + questionNumber);
            questionView.setText(quiz.get(i).text);

            if(quiz.get(i).image !="") {
                new GetImageAsync().execute(quiz.get(i).image);
            }
            else
            {
                imageView =findViewById(R.id.imageView);
                imageView.setImageResource(R.drawable.plain_image);
            }
            rvOptions = findViewById(R.id.optionsRecyclerView);

            rvOptions.setHasFixedSize(true);

            rvLayoutManager = new LinearLayoutManager(TriviaActivity.this);
            rvOptions.setLayoutManager(rvLayoutManager);

            rvAdapter = new MyAdapter(quiz.get(i).choice, TriviaActivity.this,questionNumber);
            rvOptions.setAdapter(rvAdapter);
        }
        else
        {

            Intent in =new Intent(TriviaActivity.this,StatsActivity.class);

            in.putExtra(STATS_KEY,resultsList);
            startActivityForResult(in,REQ_CODE);

        }

    }

    private class GetImageAsync extends AsyncTask<String, Void, Void> {
        ImageView imageView;

        Bitmap bitmap = null;


        @Override
        protected void onPreExecute() {
            triviaProgressBar =findViewById(R.id.quesProgressBar);
            loadView=findViewById(R.id.loadingView);
            triviaProgressBar.setVisibility(View.VISIBLE);
            loadView.setVisibility(View.VISIBLE);

            imageView =findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.plain_image);
        }

        @Override
        protected Void doInBackground(String... strings) {
            HttpURLConnection connection = null;
            bitmap =null ;

                try {
                    URL url = new URL(strings[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {

            triviaProgressBar =findViewById(R.id.quesProgressBar);
            loadView=findViewById(R.id.loadingView);
            triviaProgressBar.setVisibility(View.INVISIBLE);
            loadView.setVisibility(View.INVISIBLE);
            imageView =findViewById(R.id.imageView);

            if (bitmap != null && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
            if(bitmap == null)
            {
                imageView.setImageResource(R.drawable.plain_image);

            }
        }
    }


}
