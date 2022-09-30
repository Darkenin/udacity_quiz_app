package com.raphael.udacity_quiz_app;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String SCORE_INDEX = "score";
    private static final String EDIT_NANO = "nano";
    private static final String EDIT_TWT = "tweet";
    private static final String EDIT_QUEEN = "queen";

    int score;
    private EditText nanoField;
    private EditText twField;
    private EditText queen;
    private Button send;
    private Button checkAnswer;
    private TextView preview;
    private RadioGroup radioGroup;
    private RadioButton kotlin;
    private RadioButton java;
    private CheckBox checkOne;
    private CheckBox checkBoxTwo;
    public CheckBox checkBoxThree;
    public String checked;
    public String message;
    private String courseSelected;
    private String name;
    private String twttr;
    private String qeeenT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (score == 0) score = 0;
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_INDEX, 0);
            name = savedInstanceState.getString(EDIT_NANO, "");
            twttr = savedInstanceState.getString(EDIT_TWT, "");
            qeeenT = savedInstanceState.getString(EDIT_QUEEN, "");
        }
        courseSelected = "";
        nanoField = findViewById(R.id.name_field);
        twField = findViewById(R.id.tweet);
        send = findViewById(R.id.send);
        preview = findViewById(R.id.preview);
        queen = findViewById(R.id.edit_monarch);
        checkAnswer = findViewById(R.id.prevAnswer);
        radioGroup = findViewById(R.id.groupradio);
        kotlin = findViewById(R.id.kotlin);
        checkOne = findViewById(R.id.blackbox_checkbox);
        checkBoxTwo = findViewById(R.id.blackbox_checkbox2);
        checkBoxThree = findViewById(R.id.blackbox_checkbox3);
        java = findViewById(R.id.java);
        send.setOnClickListener(v -> {
            submitOrder();
        });

        checkAnswer.setOnClickListener(v -> {
            if (score == 6) score = 0;
            preview.setText(checkField());
            nanoField.setText(null);
            twField.setText(null);
            queen.setText(null);
            checkBoxTwo.setChecked(false);
            checkOne.setChecked(false);
        });

        radioGroup.setOnCheckedChangeListener(
                (group, checkedId) -> {
                    RadioButton
                            radioButton
                            = (RadioButton) group
                            .findViewById(checkedId);
                    if (kotlin.isChecked()) {
                        courseSelected = kotlin.getText().toString();
                    } else if (java.isChecked()) {
                        courseSelected = java.getText().toString();
                    }
                });

        checkOne.setOnClickListener(v -> {
            checked += "\n" + getResources().getString(R.string.hippo);
        });

        checkBoxTwo.setOnClickListener(v -> {
            checked += "\n" + getResources().getString(R.string.mud);
        });

        checkBoxThree.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
            checkBoxThree.setChecked(false);
        });
        correctScore(score);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCORE_INDEX, score);
        outState.putString(EDIT_NANO, nanoField.getText().toString());
        outState.putString(EDIT_TWT, twField.getText().toString());
        outState.putString(EDIT_QUEEN, queen.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        correctScore(score);
        nanoField.getText().toString();
        queen.setText(qeeenT);
        nanoField.setText(name);
        twField.setText(twttr);
    }

    public void submitOrder() {
        Editable nameEditable = nanoField.getText();
        String name = nameEditable.toString();
        Editable tweet = twField.getText();
        twttr = tweet.toString();
        Editable queenEdit = queen.getText();
        qeeenT = queenEdit.toString();
        String message = quizField(name, twttr, qeeenT, courseSelected, checked, score);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @NonNull
    private String quizField(String nano, String tweet, String queen,
                             String rad_btn, String checked, int score) {
        String prevAns = getResources().getString(R.string.nano, nano);
        prevAns += "\n" + getResources().getString(R.string.type, tweet);
        prevAns += "\n" + getResources().getString(R.string.amp, checked);
        prevAns += "\n" + getResources().getString(R.string.queen2, queen);
        prevAns += "\n" + getResources().getString(R.string.radiobtn, rad_btn);
        prevAns += "\n" + "Scored:" + score;
        return prevAns;
    }

    private String checkField() {
        Editable nameEditable = nanoField.getText();
        name = nameEditable.toString();
        Editable tweet = twField.getText();
        String twttr = tweet.toString();
        Editable queenEdit = queen.getText();
        String queenT = queenEdit.toString();
        if (name.contains("nanodegree")) score++;
        if (twttr.equalsIgnoreCase("@")) score++;
        if (queenT.contains("8")) score++;
        if (courseSelected.equalsIgnoreCase("java programming")) score++;
        if (checkOne.isChecked()) score++;
        if (checkBoxTwo.isChecked()) score++;
        else {
            Toast.makeText(getApplicationContext(), message + " scores " + score,
                    Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getApplicationContext(), "Player scored " + score + "/6",
                Toast.LENGTH_SHORT).show();
        courseSelected = "";
        correctScore(score);
        return message;
    }

    private void correctScore(int correctAnswer) {
        TextView scoreView = findViewById(
                R.id.score);
        scoreView.setText(String.valueOf(correctAnswer + "/6"));
    }
}