package com.example.internshippreparation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UsefulLinksActivity extends AppCompatActivity {

    private TextView btnBack;

    private Button btnUptm;
    private Button btnJobstreet;
    private Button btnLinkedin;
    private Button btnMyfuturejobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useful_links);

        // Back button
        btnBack = findViewById(R.id.btnBack);

        // Buttons
        btnUptm = findViewById(R.id.btnUptm);
        btnJobstreet = findViewById(R.id.btnJobstreet);
        btnLinkedin = findViewById(R.id.btnLinkedin);
        btnMyfuturejobs = findViewById(R.id.btnMyfuturejobs);

        // Back
        btnBack.setOnClickListener(v -> finish());

        // Open links
        btnUptm.setOnClickListener(v ->
                openLink("https://www.uptm.edu.my/"));

        btnJobstreet.setOnClickListener(v ->
                openLink("https://www.jobstreet.com.my/"));

        btnLinkedin.setOnClickListener(v ->
                openLink("https://www.linkedin.com/"));

        btnMyfuturejobs.setOnClickListener(v ->
                openLink("https://www.myfuturejobs.gov.my/"));
    }

    private void openLink(String url) {

        try {

            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
            );

            startActivity(intent);

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Unable to open this link.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}