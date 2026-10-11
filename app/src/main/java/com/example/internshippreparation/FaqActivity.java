package com.example.internshippreparation;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FaqActivity extends AppCompatActivity {

    private TextView btnBack;

    private LinearLayout faq1;
    private LinearLayout faq2;
    private LinearLayout faq3;
    private LinearLayout faq4;
    private LinearLayout faq5;
    private LinearLayout faq6;

    private TextView answerFaq1;
    private TextView answerFaq2;
    private TextView answerFaq3;
    private TextView answerFaq4;
    private TextView answerFaq5;
    private TextView answerFaq6;

    private TextView iconFaq1;
    private TextView iconFaq2;
    private TextView iconFaq3;
    private TextView iconFaq4;
    private TextView iconFaq5;
    private TextView iconFaq6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        // Back button
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        // FAQ cards
        faq1 = findViewById(R.id.faq1);
        faq2 = findViewById(R.id.faq2);
        faq3 = findViewById(R.id.faq3);
        faq4 = findViewById(R.id.faq4);
        faq5 = findViewById(R.id.faq5);
        faq6 = findViewById(R.id.faq6);

        // Answers
        answerFaq1 = findViewById(R.id.answerFaq1);
        answerFaq2 = findViewById(R.id.answerFaq2);
        answerFaq3 = findViewById(R.id.answerFaq3);
        answerFaq4 = findViewById(R.id.answerFaq4);
        answerFaq5 = findViewById(R.id.answerFaq5);
        answerFaq6 = findViewById(R.id.answerFaq6);

        // Icons
        iconFaq1 = findViewById(R.id.iconFaq1);
        iconFaq2 = findViewById(R.id.iconFaq2);
        iconFaq3 = findViewById(R.id.iconFaq3);
        iconFaq4 = findViewById(R.id.iconFaq4);
        iconFaq5 = findViewById(R.id.iconFaq5);
        iconFaq6 = findViewById(R.id.iconFaq6);

        // Back
        btnBack.setOnClickListener(v -> finish());

        // FAQ click actions
        faq1.setOnClickListener(v ->
                toggleFaq(answerFaq1, iconFaq1));

        faq2.setOnClickListener(v ->
                toggleFaq(answerFaq2, iconFaq2));

        faq3.setOnClickListener(v ->
                toggleFaq(answerFaq3, iconFaq3));

        faq4.setOnClickListener(v ->
                toggleFaq(answerFaq4, iconFaq4));

        faq5.setOnClickListener(v ->
                toggleFaq(answerFaq5, iconFaq5));

        faq6.setOnClickListener(v ->
                toggleFaq(answerFaq6, iconFaq6));
    }

    private void toggleFaq(TextView answer, TextView icon) {

        if (answer.getVisibility() == View.GONE) {

            answer.setVisibility(View.VISIBLE);
            icon.setText("−");

        } else {

            answer.setVisibility(View.GONE);
            icon.setText("+");
        }
    }
}