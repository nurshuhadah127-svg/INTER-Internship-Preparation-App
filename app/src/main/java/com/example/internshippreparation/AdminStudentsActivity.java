package com.example.internshippreparation;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminStudentsActivity extends AppCompatActivity {

    private TextView btnBack;
    private EditText etSearchStudent;
    private LinearLayout studentsContainer;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_students);

        btnBack = findViewById(R.id.btnBack);
        etSearchStudent = findViewById(R.id.etSearchStudent);
        studentsContainer = findViewById(R.id.studentsContainer);

        databaseHelper = new DatabaseHelper(this);

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Load all students
        loadStudents("");

        // Search students
        etSearchStudent.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after
            ) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count
            ) {
                loadStudents(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void loadStudents(String searchText) {

        studentsContainer.removeAllViews();

        Cursor cursor;

        if (searchText.isEmpty()) {

            cursor = databaseHelper
                    .getReadableDatabase()
                    .query(
                            "users",
                            null,
                            "role = ?",
                            new String[]{"student"},
                            null,
                            null,
                            "id DESC"
                    );

        } else {

            String searchPattern = "%" + searchText + "%";

            cursor = databaseHelper
                    .getReadableDatabase()
                    .query(
                            "users",
                            null,
                            "role = ? AND (name LIKE ? OR student_id LIKE ? OR email LIKE ?)",
                            new String[]{
                                    "student",
                                    searchPattern,
                                    searchPattern,
                                    searchPattern
                            },
                            null,
                            null,
                            "id DESC"
                    );
        }

        if (cursor != null && cursor.moveToFirst()) {

            do {

                int studentId = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name")
                );

                String studentNumber = cursor.getString(
                        cursor.getColumnIndexOrThrow("student_id")
                );

                String email = cursor.getString(
                        cursor.getColumnIndexOrThrow("email")
                );

                addStudentCard(
                        studentId,
                        name,
                        studentNumber,
                        email
                );

            } while (cursor.moveToNext());

            cursor.close();

        } else {

            if (cursor != null) {
                cursor.close();
            }

            TextView emptyText = new TextView(this);

            emptyText.setText("No students found.");
            emptyText.setTextColor(
                    android.graphics.Color.GRAY
            );
            emptyText.setTextSize(14);
            emptyText.setGravity(Gravity.CENTER);
            emptyText.setPadding(
                    0,
                    dp(30),
                    0,
                    dp(30)
            );

            studentsContainer.addView(emptyText);
        }
    }

    private void addStudentCard(
            int studentId,
            String name,
            String studentNumber,
            String email
    ) {

        // =========================
        // MAIN CARD
        // =========================

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(
                LinearLayout.HORIZONTAL
        );

        card.setGravity(
                Gravity.CENTER_VERTICAL
        );

        card.setPadding(
                dp(12),
                dp(10),
                dp(12),
                dp(10)
        );

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(82)
                );

        cardParams.setMargins(
                0,
                0,
                0,
                dp(12)
        );

        card.setLayoutParams(cardParams);

        card.setBackgroundResource(
                R.drawable.bg_admin_card
        );

        card.setClickable(true);
        card.setFocusable(true);

        // =========================
        // USER ICON
        // =========================

        TextView icon = new TextView(this);

        icon.setText("👤");
        icon.setTextSize(20);
        icon.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams iconParams =
                new LinearLayout.LayoutParams(
                        dp(45),
                        dp(45)
                );

        icon.setLayoutParams(iconParams);

        icon.setBackgroundResource(
                R.drawable.bg_admin_icon
        );

        card.addView(icon);

        // =========================
        // INFORMATION CONTAINER
        // =========================

        LinearLayout infoLayout =
                new LinearLayout(this);

        infoLayout.setOrientation(
                LinearLayout.VERTICAL
        );

        infoLayout.setGravity(
                Gravity.CENTER_VERTICAL
        );

        infoLayout.setPadding(
                dp(12),
                0,
                dp(5),
                0
        );

        LinearLayout.LayoutParams infoParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                );

        infoLayout.setLayoutParams(infoParams);

        // =========================
        // STUDENT NAME
        // =========================

        TextView tvName = new TextView(this);

        tvName.setText(
                name == null || name.isEmpty()
                        ? "Unknown Student"
                        : name
        );

        tvName.setTextColor(
                android.graphics.Color.rgb(
                        0,
                        33,
                        71
                )
        );

        tvName.setTextSize(14);

        tvName.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        tvName.setMaxLines(1);

        tvName.setEllipsize(
                android.text.TextUtils.TruncateAt.END
        );

        infoLayout.addView(tvName);

        // =========================
        // STUDENT ID
        // =========================

        TextView tvStudentId =
                new TextView(this);

        tvStudentId.setText(
                "Student ID: " +
                        (
                                studentNumber == null ||
                                        studentNumber.isEmpty()
                                        ? "Not provided"
                                        : studentNumber
                        )
        );

        tvStudentId.setTextColor(
                android.graphics.Color.GRAY
        );

        tvStudentId.setTextSize(11);

        tvStudentId.setMaxLines(1);

        tvStudentId.setEllipsize(
                android.text.TextUtils.TruncateAt.END
        );

        infoLayout.addView(tvStudentId);

        // =========================
        // EMAIL
        // =========================

        TextView tvEmail = new TextView(this);

        tvEmail.setText(
                email == null
                        ? ""
                        : email
        );

        tvEmail.setTextColor(
                android.graphics.Color.GRAY
        );

        tvEmail.setTextSize(11);

        tvEmail.setMaxLines(1);

        tvEmail.setEllipsize(
                android.text.TextUtils.TruncateAt.END
        );

        infoLayout.addView(tvEmail);

        card.addView(infoLayout);

        // =========================
        // RIGHT ARROW
        // =========================

        TextView arrow = new TextView(this);

        arrow.setText("›");

        arrow.setTextColor(
                android.graphics.Color.rgb(
                        237,
                        28,
                        36
                )
        );

        arrow.setTextSize(30);

        arrow.setGravity(
                Gravity.CENTER
        );

        LinearLayout.LayoutParams arrowParams =
                new LinearLayout.LayoutParams(
                        dp(30),
                        LinearLayout.LayoutParams.MATCH_PARENT
                );

        arrow.setLayoutParams(arrowParams);

        card.addView(arrow);

        // =========================
        // CLICK CARD
        // =========================

        card.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminStudentsActivity.this,
                    StudentInformationActivity.class
            );

            intent.putExtra(
                    "STUDENT_ID",
                    studentId
            );

            startActivity(intent);
        });

        // =========================
        // ADD CARD TO SCREEN
        // =========================

        studentsContainer.addView(card);
    }

    // =========================
    // DP CONVERTER
    // =========================

    private int dp(int value) {

        return (int) (
                value *
                        getResources()
                                .getDisplayMetrics()
                                .density
                        + 0.5f
        );
    }
}