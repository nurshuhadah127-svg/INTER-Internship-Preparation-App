package com.example.internshippreparation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProgramSelectionActivity extends AppCompatActivity {

    private TextView tvBack;

    private View cardGeneral;
    private View cardCC101;
    private View cardCM201;
    private View cardCT204;
    private View cardCT206;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_program_selection);

        Toast.makeText(
                this,
                "Program Selection Opened",
                Toast.LENGTH_SHORT
        ).show();

        // Back button
        tvBack = findViewById(R.id.tvBack);

        // Program cards
        cardGeneral = findViewById(R.id.cardGeneral);
        cardCC101 = findViewById(R.id.cardCC101);
        cardCM201 = findViewById(R.id.cardCM201);
        cardCT204 = findViewById(R.id.cardCT204);
        cardCT206 = findViewById(R.id.cardCT206);

        // Back
        tvBack.setOnClickListener(v -> finish());

        // General Placement Documents
        cardGeneral.setOnClickListener(v -> {
            openDocuments("", true);
        });

        // CC101
        cardCC101.setOnClickListener(v -> {
            openDocuments("CC101", false);
        });

        // CM201
        cardCM201.setOnClickListener(v -> {
            openDocuments("CM201", false);
        });

        // CT204
        cardCT204.setOnClickListener(v -> {
            openDocuments("CT204", false);
        });

        // CT206
        cardCT206.setOnClickListener(v -> {
            openDocuments("CT206", false);
        });
    }

    private void openDocuments(String program, boolean generalPlacement) {

        Intent intent = new Intent(
                ProgramSelectionActivity.this,
                DocumentListActivity.class
        );

        // Semua adalah Placement Documents
        intent.putExtra("CATEGORY", "Placement Documents");

        if (generalPlacement) {
            intent.putExtra("GENERAL_PLACEMENT", true);
        } else {
            intent.putExtra("PROGRAM", program);
        }

        startActivity(intent);
    }
}