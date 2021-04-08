package com.example.inclass07;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {


    Button tryAgainButton;
    Button statsQuitButton;

    TextView percentageView;
    ProgressBar percentProgressBar;

    static String KEY="key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        if(getIntent()!=null && getIntent().getExtras()!=null)
        {
            ArrayList<Integer> result = getIntent().getExtras().getIntegerArrayList(TriviaActivity.STATS_KEY);



            double crtCount = 0;

            for(int i=0;i<result.size();i++)
            {


                if(result.get(i)==1)
                {
                    crtCount = crtCount + 1;
                }
            }

            double totalNoQuestions= result.size();
            int percent = (int) ((crtCount/totalNoQuestions)* 100);
            percentageView= findViewById(R.id.tv_percentage);
            percentProgressBar =findViewById(R.id.progressBarPercent);

            percentageView.setText(percent + "%");
            percentProgressBar.setProgress(percent);

        }

        statsQuitButton =findViewById(R.id.statsQuitBtn);

        statsQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setResult(RESULT_OK);
                finish();
            }
        });

        tryAgainButton =findViewById(R.id.btn_tryAgain);

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent();
                it.putExtra(KEY,1);
                setResult(RESULT_OK,it);
                finish();
            }
        });


    }
}
