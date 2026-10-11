package com.example.internshippreparation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResumeActivity extends AppCompatActivity {

    TextView btnBack;

    Button btnResumeGuide;
    Button btnCoverLetterGuide;

    TextView btnDownloadResume;
    TextView btnDownloadCoverLetter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_resume);


        // ==========================================
        // CONNECT XML
        // ==========================================

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        btnResumeGuide =
                findViewById(R.id.btnResumeGuide);

        btnCoverLetterGuide =
                findViewById(R.id.btnCoverLetterGuide);

        btnDownloadResume =
                findViewById(R.id.btnDownloadResume);

        btnDownloadCoverLetter =
                findViewById(R.id.btnDownloadCoverLetter);


        // ==========================================
        // BACK
        // ==========================================

        btnBack.setOnClickListener(v -> {

            finish();

        });


        // ==========================================
        // RESUME GUIDE
        // ==========================================

        btnResumeGuide.setOnClickListener(v -> {

            Toast.makeText(
                    ResumeActivity.this,
                    "Resume Guide will be available here.",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // ==========================================
        // COVER LETTER GUIDE
        // ==========================================

        btnCoverLetterGuide.setOnClickListener(v -> {

            Toast.makeText(
                    ResumeActivity.this,
                    "Cover Letter Guide will be available here.",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // ==========================================
        // DOWNLOAD RESUME TEMPLATE
        // ==========================================

        btnDownloadResume.setOnClickListener(v -> {

            Toast.makeText(
                    ResumeActivity.this,
                    "Resume Template will be available here.",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // ==========================================
        // DOWNLOAD COVER LETTER TEMPLATE
        // ==========================================

        btnDownloadCoverLetter.setOnClickListener(v -> {

            Toast.makeText(
                    ResumeActivity.this,
                    "Cover Letter Template will be available here.",
                    Toast.LENGTH_SHORT
            ).show();

        });

    }
}