package com.example.internshippreparation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ChecklistActivity extends AppCompatActivity {

    TextView btnBack;
    TextView tvProgress;

    ProgressBar progressChecklist;

    CheckBox checkResume;
    CheckBox checkCoverLetter;
    CheckBox checkDocuments;
    CheckBox checkResearch;
    CheckBox checkApply;
    CheckBox checkInterview;
    CheckBox checkAttend;
    CheckBox checkPlacement;

    SharedPreferences preferences;

    int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checklist);


        // =================================================
        // CONNECT XML
        // =================================================

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        tvProgress = findViewById(R.id.tvProgress);

        progressChecklist =
                findViewById(R.id.progressChecklist);

        checkResume =
                findViewById(R.id.checkResume);

        checkCoverLetter =
                findViewById(R.id.checkCoverLetter);

        checkDocuments =
                findViewById(R.id.checkDocuments);

        checkResearch =
                findViewById(R.id.checkResearch);

        checkApply =
                findViewById(R.id.checkApply);

        checkInterview =
                findViewById(R.id.checkInterview);

        checkAttend =
                findViewById(R.id.checkAttend);

        checkPlacement =
                findViewById(R.id.checkPlacement);


        // =================================================
        // GET USER ID
        // =================================================

        userId =
                getIntent().getIntExtra(
                        "USER_ID",
                        -1
                );


        // =================================================
        // SHARED PREFERENCES
        // =================================================

        preferences =
                getSharedPreferences(
                        "ChecklistData",
                        MODE_PRIVATE
                );


        // =================================================
        // LOAD SAVED CHECKLIST
        // =================================================

        loadChecklist();


        // =================================================
        // BACK BUTTON
        // =================================================

        btnBack.setOnClickListener(v -> finish());


        // =================================================
        // CHECKBOX LISTENERS
        // =================================================

        checkResume.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (buttonView.isPressed()) {

                        saveChecklist();

                    }
                }
        );


        checkCoverLetter.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (buttonView.isPressed()) {

                        saveChecklist();

                    }
                }
        );


        checkDocuments.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (buttonView.isPressed()) {

                        saveChecklist();

                    }
                }
        );


        checkResearch.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (buttonView.isPressed()) {

                        saveChecklist();

                    }
                }
        );


        checkApply.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (buttonView.isPressed()) {

                        saveChecklist();

                    }
                }
        );


        checkInterview.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (buttonView.isPressed()) {

                        saveChecklist();

                    }
                }
        );


        checkAttend.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (buttonView.isPressed()) {

                        saveChecklist();

                    }
                }
        );


        checkPlacement.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (buttonView.isPressed()) {

                        saveChecklist();

                    }
                }
        );


        // =================================================
        // UPDATE PROGRESS
        // =================================================

        updateProgress();
    }


    // =====================================================
    // LOAD CHECKLIST
    // =====================================================

    private void loadChecklist() {

        String prefix = "user_" + userId + "_";


        checkResume.setChecked(
                preferences.getBoolean(
                        prefix + "resume",
                        true
                )
        );


        checkCoverLetter.setChecked(
                preferences.getBoolean(
                        prefix + "coverLetter",
                        true
                )
        );


        checkDocuments.setChecked(
                preferences.getBoolean(
                        prefix + "documents",
                        false
                )
        );


        checkResearch.setChecked(
                preferences.getBoolean(
                        prefix + "research",
                        true
                )
        );


        checkApply.setChecked(
                preferences.getBoolean(
                        prefix + "apply",
                        false
                )
        );


        checkInterview.setChecked(
                preferences.getBoolean(
                        prefix + "interview",
                        false
                )
        );


        checkAttend.setChecked(
                preferences.getBoolean(
                        prefix + "attend",
                        false
                )
        );


        checkPlacement.setChecked(
                preferences.getBoolean(
                        prefix + "placement",
                        false
                )
        );


        updateProgress();
    }


    // =====================================================
    // SAVE CHECKLIST
    // =====================================================

    private void saveChecklist() {

        String prefix = "user_" + userId + "_";


        SharedPreferences.Editor editor =
                preferences.edit();


        editor.putBoolean(
                prefix + "resume",
                checkResume.isChecked()
        );


        editor.putBoolean(
                prefix + "coverLetter",
                checkCoverLetter.isChecked()
        );


        editor.putBoolean(
                prefix + "documents",
                checkDocuments.isChecked()
        );


        editor.putBoolean(
                prefix + "research",
                checkResearch.isChecked()
        );


        editor.putBoolean(
                prefix + "apply",
                checkApply.isChecked()
        );


        editor.putBoolean(
                prefix + "interview",
                checkInterview.isChecked()
        );


        editor.putBoolean(
                prefix + "attend",
                checkAttend.isChecked()
        );


        editor.putBoolean(
                prefix + "placement",
                checkPlacement.isChecked()
        );


        editor.apply();


        updateProgress();
    }


    // =====================================================
    // UPDATE PROGRESS
    // =====================================================

    private void updateProgress() {

        int completed = 0;


        if (checkResume.isChecked()) {
            completed++;
        }


        if (checkCoverLetter.isChecked()) {
            completed++;
        }


        if (checkDocuments.isChecked()) {
            completed++;
        }


        if (checkResearch.isChecked()) {
            completed++;
        }


        if (checkApply.isChecked()) {
            completed++;
        }


        if (checkInterview.isChecked()) {
            completed++;
        }


        if (checkAttend.isChecked()) {
            completed++;
        }


        if (checkPlacement.isChecked()) {
            completed++;
        }


        progressChecklist.setMax(8);

        progressChecklist.setProgress(completed);


        tvProgress.setText(
                completed + "/8 completed"
        );
    }
}