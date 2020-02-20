package com.jvetter2.guessinggame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    ImageView animalImageView;
    TextView scoreTextView;
    String correctButton;
    String[] animalList;
    Boolean soundOn;
    int correctAnswer;
    int questionNumber;
    Double currentScore;
    Double totalNumber;
    ArrayList<Integer> excludeNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        currentScore = 0.0;
        totalNumber = 10.0;
        questionNumber = 1;
        excludeNumbers = new ArrayList<>();

        Intent intent = getIntent();
        soundOn = intent.getBooleanExtra(getString(R.string.sound_variable), true);

        Resources res = getResources();
        animalList = res.getStringArray(R.array.animals);
        setNextQuestion();
    }

    public void setNextQuestion() {
        setAnswer();
        setCorrectButton();
        setButtons();

        animalImageView = findViewById(R.id.animalImageView);
        int resID = getResources().getIdentifier(animalList[correctAnswer] , "drawable", getPackageName());
        animalImageView.setImageResource(resID);

        scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText(questionNumber + "/" + 10);
    }

    public void setAnswer() {
        // Calculate correct answer number
        Random random = new Random();
        correctAnswer = random.nextInt(animalList.length);

        while (!excludeNumbers.isEmpty() && excludeNumbers.contains(correctAnswer)) {
            correctAnswer = random.nextInt(animalList.length);
        }

        excludeNumbers.add(correctAnswer);
    }

    public void setCorrectButton() {
        // Set which button to be correct
        Random r = new Random();
        int randomNumber = r.nextInt(3) + 1;
        correctButton = "answer" + String.valueOf(randomNumber);
    }

    public void answerQuestion(View view) {
        Button buttonClicked = (Button) view;
        checkAnswer(buttonClicked);
    }

    public void checkAnswer(final Button buttonClicked) {
        Toast toast = new Toast(getApplicationContext());
        MediaPlayer playSound = MediaPlayer.create(getApplicationContext(), R.raw.wrong);
        questionNumber++;

        if (!soundOn) {
            playSound.setVolume(0, 0);
        }

        if (buttonClicked.getText().toString().equalsIgnoreCase(animalList[correctAnswer])) {
            currentScore++;
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            animation.reset();
            animation.setAnimationListener(new Animation.AnimationListener(){

                @Override
                public void onAnimationStart(Animation animation){}

                @Override
                public void onAnimationRepeat(Animation animation){}

                @Override
                public void onAnimationEnd(Animation animation){
                    if (questionNumber > totalNumber) {
                        endGame();
                    } else {
                        setNextQuestion();
                    }
                }
            });
            buttonClicked.clearAnimation();
            buttonClicked.startAnimation(animation);

        } else {
            toast.makeText(getApplicationContext(), getString(R.string.wrong_answer_text), toast.LENGTH_SHORT).show();
            playSound.start();

            if (questionNumber > totalNumber) {
                endGame();
            } else {
                setNextQuestion();
            }
        }
    }

    public void setButtons() {
        // Set text on each button based on correct answer
        String[] buttonNames = {"answer1", "answer2", "answer3", "answer4"};
        int i = 1;

        for (String button : buttonNames) {
            int resID = getResources().getIdentifier(button, "id", getPackageName());
            Button buttonNumber = findViewById(resID);

            if (button.equalsIgnoreCase(correctButton)) {
                buttonNumber.setText(animalList[correctAnswer]);
                i++;
            } else {
                if ((correctAnswer + i) < animalList.length) {
                    buttonNumber.setText(animalList[correctAnswer + i]);
                    i++;
                } else {
                    buttonNumber.setText(animalList[i]);
                    i++;
                }
            }
        }
    }

    public void endGame() {
        int finalScore = Integer.valueOf((int) ((currentScore/totalNumber) * 100));

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_gallery)
                .setMessage("10 Questions, " + finalScore + "% correct")
                .setCancelable(false)
                .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent homeScreen = new Intent(getApplicationContext(), MainActivity.class);
                        homeScreen.putExtra(getString(R.string.sound_variable), soundOn);
                        startActivity(homeScreen);
                    }
                }).show();
    }
}