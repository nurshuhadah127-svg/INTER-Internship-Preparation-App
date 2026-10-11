package com.example.internshippreparation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DocumentsActivity extends AppCompatActivity {

    private TextView btnBack;

    private CheckBox checkResume;
    private CheckBox checkCoverLetter;
    private CheckBox checkUniversity;
    private CheckBox checkCompany;

    private SharedPreferences preferences;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        checkResume = findViewById(R.id.checkResume);
        checkCoverLetter = findViewById(R.id.checkCoverLetter);
        checkUniversity = findViewById(R.id.checkUniversity);
        checkCompany = findViewById(R.id.checkCompany);

        userId = getIntent().getIntExtra("USER_ID", -1);

        preferences = getSharedPreferences("DocumentsData", MODE_PRIVATE);

        loadDocumentStatus();

        checkResume.setOnCheckedChangeListener(
                (buttonView, isChecked) ->
                        saveDocumentStatus("resume", isChecked)
        );

        checkCoverLetter.setOnCheckedChangeListener(
                (buttonView, isChecked) ->
                        saveDocumentStatus("cover_letter", isChecked)
        );

        checkUniversity.setOnCheckedChangeListener(
                (buttonView, isChecked) ->
                        saveDocumentStatus("university", isChecked)
        );

        checkCompany.setOnCheckedChangeListener(
                (buttonView, isChecked) ->
                        saveDocumentStatus("company", isChecked)
        );

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadDocumentStatus() {

        String prefix = "user_" + userId + "_";

        checkResume.setChecked(
                preferences.getBoolean(prefix + "resume", false)
        );

        checkCoverLetter.setChecked(
                preferences.getBoolean(prefix + "cover_letter", false)
        );

        checkUniversity.setChecked(
                preferences.getBoolean(prefix + "university", false)
        );

        checkCompany.setChecked(
                preferences.getBoolean(prefix + "company", false)
        );
    }

    private void saveDocumentStatus(String document, boolean completed) {

        String key = "user_" + userId + "_" + document;

        preferences.edit()
                .putBoolean(key, completed)
                .apply();
    }
}