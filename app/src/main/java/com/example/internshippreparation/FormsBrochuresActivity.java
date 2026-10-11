package com.example.internshippreparation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FormsBrochuresActivity extends AppCompatActivity {

    private TextView btnBack;

    private EditText etSearchDocuments;

    private LinearLayout cardGeneral;
    private LinearLayout cardBriefing;
    private LinearLayout cardPlacement;
    private LinearLayout cardInsurance;
    private LinearLayout cardReporting;
    private LinearLayout cardSamples;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_forms_brochures
        );


        // Connect XML

        btnBack =
                findViewById(R.id.btnBack);

        etSearchDocuments =
                findViewById(R.id.etSearchDocuments);

        cardGeneral =
                findViewById(R.id.cardGeneral);

        cardBriefing =
                findViewById(R.id.cardBriefing);

        cardPlacement =
                findViewById(R.id.cardPlacement);

        cardInsurance =
                findViewById(R.id.cardInsurance);

        cardReporting =
                findViewById(R.id.cardReporting);

        cardSamples =
                findViewById(R.id.cardSamples);


        // Back button

        btnBack.setOnClickListener(v -> {

            finish();

        });


        // General Information

        cardGeneral.setOnClickListener(v -> {

            openDocumentList(
                    "General Information"
            );

        });


        // Briefing Materials

        cardBriefing.setOnClickListener(v -> {

            openDocumentList(
                    "Briefing Materials"
            );

        });


        // Placement Documents

        cardPlacement.setOnClickListener(v -> {

            openDocumentList(
                    "Placement Documents"
            );

        });


        // Insurance & Legal

        cardInsurance.setOnClickListener(v -> {

            openDocumentList(
                    "Insurance & Legal"
            );

        });


        // Reporting & Assessment

        cardReporting.setOnClickListener(v -> {

            openDocumentList(
                    "Reporting & Assessment"
            );

        });


        // Sample Documents

        cardSamples.setOnClickListener(v -> {

            openDocumentList(
                    "Sample Documents"
            );

        });

    }


    // ==========================================
    // OPEN DOCUMENT LIST
    // ==========================================

    private void openDocumentList(String category) {

        if (category.equals("Placement Documents")) {

            Intent intent = new Intent(
                    FormsBrochuresActivity.this,
                    ProgramSelectionActivity.class
            );

            startActivity(intent);

        } else {

            Intent intent = new Intent(
                    FormsBrochuresActivity.this,
                    DocumentListActivity.class
            );

            intent.putExtra("CATEGORY", category);

            startActivity(intent);
        }
    }

}