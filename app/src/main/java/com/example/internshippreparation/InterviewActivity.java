package com.example.internshippreparation;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InterviewActivity extends AppCompatActivity {

    private TextView btnBack;
    private Button btnInterviewTips;
    private Button btnCommonQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);

        btnBack = findViewById(R.id.btnBack);
        btnInterviewTips = findViewById(R.id.btnInterviewTips);
        btnCommonQuestions = findViewById(R.id.btnCommonQuestions);

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Interview Tips
        btnInterviewTips.setOnClickListener(v -> showInterviewTips());

        // Common Questions
        btnCommonQuestions.setOnClickListener(v -> showCommonQuestions());
    }

    private void showInterviewTips() {

        String tips =
                "1. Research the company\n\n" +
                        "Learn about the company before attending the interview.\n\n" +

                        "2. Prepare your introduction\n\n" +
                        "Prepare a short introduction about yourself, your course and your skills.\n\n" +

                        "3. Prepare common questions\n\n" +
                        "Practice answering common internship interview questions.\n\n" +

                        "4. Dress appropriately\n\n" +
                        "Wear suitable and neat clothes for the interview.\n\n" +

                        "5. Be confident\n\n" +
                        "Speak clearly and try to stay calm during the interview.";

        new AlertDialog.Builder(this)
                .setTitle("Interview Tips")
                .setMessage(tips)
                .setPositiveButton("Close", null)
                .show();
    }

    private void showCommonQuestions() {

        String questions =
                "1. Tell me about yourself.\n\n" +
                        "2. Why do you want this internship?\n\n" +
                        "3. What are your strengths?\n\n" +
                        "4. What are your weaknesses?\n\n" +
                        "5. Why did you choose this company?\n\n" +
                        "6. What do you know about our company?\n\n" +
                        "7. What do you expect to learn during your internship?";

        new AlertDialog.Builder(this)
                .setTitle("Common Interview Questions")
                .setMessage(questions)
                .setPositiveButton("Close", null)
                .show();
    }
}