package com.example.braintrainer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView textViewCountDownTimer;
    Boolean timeIsOver = false;
    private TextView questions;
    private TextView score;
    private TextView answer1;
    private TextView answer2;
    private TextView answer3;
    private TextView answer4;
    private ArrayList<TextView> answers;
    private int numOfRightAnsewr;
    private int thisScope;
    private String scopeStr;
    private int bestScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewCountDownTimer = findViewById(R.id.textViewCountDownTimer);
        answer1 = findViewById(R.id.textView7);
        answer2 = findViewById(R.id.textView8);
        answer3 = findViewById(R.id.textView9);
        answer4 = findViewById(R.id.textView10);

        score = findViewById(R.id.score);
        questions = findViewById(R.id.question);

        answers = new ArrayList<>();
        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);
        answers.add(answer4);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        bestScore = preferences.getInt("score", 0);
        thisScope = 0;
        scopeStr = bestScore + "/" + thisScope;
        score.setText(scopeStr);

        newTimePeriod();
        newQuestion();
    }

    private void newQuestion() {
            String question;
            int answer;

            int a = (int) (Math.random() * 100);
            int b = (int) (Math.random() * 100);
            int c = (int) (Math.random() * 100);
            if (c < 50) {
                question = a + "+" + b;
                answer = a + b;
            } else if (a < b) {
                question = b + "-" + a;
                answer = b - a;
            } else {
                question = a + "-" + b;
                answer = a - b;
            }
            String rightanswer = Integer.toString(answer);
            questions.setText(question);
            numOfRightAnsewr = (int) (Math.random() * answers.size());
            for (int i = 0; i < answers.size(); i++) {
                if (i == numOfRightAnsewr) {
                    answers.get(i).setText(rightanswer);
                } else {
                    int wrongAnswer = (int) (Math.random() * 100);
                    String wrongAnswerStr = Integer.toString(wrongAnswer);
                    answers.get(i).setText(wrongAnswerStr);
                }
            }
        }


    public void getAnswer(View view) {
        TextView textView = (TextView) view;
        String tag = textView.getTag().toString();

        if ( Integer.parseInt(tag) == numOfRightAnsewr) {
            Toast.makeText(this,"Вeрно!", Toast.LENGTH_SHORT).show();
            thisScope ++;
            scopeStr = bestScore + "/" + thisScope;
            score.setText(scopeStr);

        }
        else {
            Toast.makeText(this, "Не верно!", Toast.LENGTH_SHORT).show();
        }
        if (!timeIsOver) {
            newQuestion();
        }

    }


    private void newTimePeriod() {
        CountDownTimer newTimer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long l) {
                int seconds = (int)(l / 1000);
                textViewCountDownTimer.setText((Integer.toString(seconds)));
            }
            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this, "Время вышло! Вы набрали" + thisScope + " баллов.", Toast.LENGTH_SHORT).show();
                timeIsOver = true;
                if (bestScore < thisScope) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    preferences.edit().putInt("score", thisScope).apply();
                }
            }
        };
        newTimer.start();
    }


}
