package com.example.internshippreparation;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StudentInformationActivity extends AppCompatActivity {

    private TextView btnBack;

    private TextView tvStudentName;
    private TextView tvStudentId;
    private TextView tvStudentEmail;
    private TextView tvStudentRole;

    private Button btnClose;

    private DatabaseHelper databaseHelper;

    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_student_information
        );

        btnBack = findViewById(R.id.btnBack);

        tvStudentName = findViewById(
                R.id.tvStudentName
        );

        tvStudentId = findViewById(
                R.id.tvStudentId
        );

        tvStudentEmail = findViewById(
                R.id.tvStudentEmail
        );

        tvStudentRole = findViewById(
                R.id.tvStudentRole
        );

        btnClose = findViewById(
                R.id.btnClose
        );

        databaseHelper = new DatabaseHelper(this);

        studentId = getIntent().getIntExtra(
                "STUDENT_ID",
                -1
        );

        if (studentId == -1) {

            Toast.makeText(
                    this,
                    "Student information not found.",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        loadStudentInformation();

        btnBack.setOnClickListener(
                v -> finish()
        );

        btnClose.setOnClickListener(
                v -> finish()
        );
    }

    private void loadStudentInformation() {

        Cursor cursor = databaseHelper
                .getReadableDatabase()
                .query(
                        "users",
                        null,
                        "id = ? AND role = ?",
                        new String[]{
                                String.valueOf(studentId),
                                "student"
                        },
                        null,
                        null,
                        null
                );

        if (cursor != null &&
                cursor.moveToFirst()) {

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            "name"
                    )
            );

            String studentNumber =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "student_id"
                            )
                    );

            String email = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            "email"
                    )
            );

            String role = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            "role"
                    )
            );

            tvStudentName.setText(
                    name == null || name.isEmpty()
                            ? "Not provided"
                            : name
            );

            tvStudentId.setText(
                    studentNumber == null ||
                            studentNumber.isEmpty()
                            ? "Not provided"
                            : studentNumber
            );

            tvStudentEmail.setText(
                    email == null || email.isEmpty()
                            ? "Not provided"
                            : email
            );

            tvStudentRole.setText(
                    role == null || role.isEmpty()
                            ? "Student"
                            : role
            );

            cursor.close();

        } else {

            if (cursor != null) {
                cursor.close();
            }

            Toast.makeText(
                    this,
                    "Unable to load student information.",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        }
    }
}