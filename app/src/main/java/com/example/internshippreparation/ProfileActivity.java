package com.example.internshippreparation;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView btnProfileBack;

    private TextView tvProfileName;
    private TextView tvProfileFullName;
    private TextView tvProfileStudentId;
    private TextView tvProfileEmail;
    private TextView tvProfileRole;

    private Button btnInterview;
    private Button btnTimeline;
    private Button btnFaq;

    private Button btnEditProfile;
    private Button btnProfileLogout;
    private Button btnProfileBottomBack;

    private DatabaseHelper databaseHelper;

    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);


        // ==========================================
        // CONNECT XML
        // ==========================================

        btnProfileBack =
                findViewById(R.id.btnProfileBack);

        tvProfileName =
                findViewById(R.id.tvProfileName);

        tvProfileFullName =
                findViewById(R.id.tvProfileFullName);

        tvProfileStudentId =
                findViewById(R.id.tvProfileStudentId);

        tvProfileEmail =
                findViewById(R.id.tvProfileEmail);

        tvProfileRole =
                findViewById(R.id.tvProfileRole);


        // New feature buttons

        btnInterview =
                findViewById(R.id.btnInterview);

        btnTimeline =
                findViewById(R.id.btnTimeline);

        btnFaq =
                findViewById(R.id.btnFaq);


        btnEditProfile =
                findViewById(R.id.btnEditProfile);

        btnProfileLogout =
                findViewById(R.id.btnProfileLogout);

        btnProfileBottomBack =
                findViewById(R.id.btnProfileBottomBack);


        // ==========================================
        // DATABASE
        // ==========================================

        databaseHelper =
                new DatabaseHelper(this);


        // ==========================================
        // GET USER ID
        // ==========================================

        userId =
                getIntent().getIntExtra(
                        "USER_ID",
                        -1
                );


        // ==========================================
        // LOAD PROFILE
        // ==========================================

        loadProfile();


        // ==========================================
        // TOP BACK
        // ==========================================

        btnProfileBack.setOnClickListener(
                v -> finish()
        );


        // ==========================================
        // INTERVIEW PREPARATION
        // ==========================================

        btnInterview.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ProfileActivity.this,
                    InterviewActivity.class
            );

            startActivity(intent);
        });


        // ==========================================
        // TIMELINE
        // ==========================================

        btnTimeline.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ProfileActivity.this,
                    TimelineActivity.class
            );

            startActivity(intent);
        });


        // ==========================================
        // FAQ
        // ==========================================

        btnFaq.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ProfileActivity.this,
                    FaqActivity.class
            );

            startActivity(intent);
        });


        // ==========================================
        // EDIT PROFILE
        // ==========================================

        btnEditProfile.setOnClickListener(v -> {

                    Intent intent = new Intent(
                            ProfileActivity.this,
                            EditProfileActivity.class
                    );

                    intent.putExtra(
                            "USER_ID",
                            userId
                    );

                    startActivity(intent);
        });


        // ==========================================
        // LOGOUT
        // ==========================================

        btnProfileLogout.setOnClickListener(
                v -> logout()
        );


        // ==========================================
        // BOTTOM BACK
        // ==========================================

        btnProfileBottomBack.setOnClickListener(
                v -> finish()
        );
    }


    // ==========================================
    // LOGOUT FUNCTION
    // ==========================================

    private void logout() {

        Toast.makeText(
                this,
                "Logged out successfully",
                Toast.LENGTH_SHORT
        ).show();


        Intent intent = new Intent(
                ProfileActivity.this,
                LoginActivity.class
        );


        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );


        startActivity(intent);

        finish();
    }


    // ==========================================
    // LOAD PROFILE
    // ==========================================

    private void loadProfile() {

        if (userId == -1) {

            Toast.makeText(
                    this,
                    "User information not found.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        Cursor cursor =
                databaseHelper.getUserById(userId);


        if (cursor != null &&
                cursor.moveToFirst()) {


            String name =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "name"
                            )
                    );


            String studentId =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "student_id"
                            )
                    );


            String email =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "email"
                            )
                    );


            String role =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "role"
                            )
                    );


            // Name

            tvProfileName.setText(name);

            tvProfileFullName.setText(name);


            // Student ID

            if (studentId == null ||
                    studentId.trim().isEmpty()) {

                tvProfileStudentId.setText(
                        "Not provided"
                );

            } else {

                tvProfileStudentId.setText(
                        studentId
                );
            }


            // Email

            tvProfileEmail.setText(email);


            // Role

            if (role != null &&
                    role.equalsIgnoreCase("admin")) {

                tvProfileRole.setText("Admin");

            } else {

                tvProfileRole.setText("Student");
            }


            cursor.close();


        } else {

            Toast.makeText(
                    this,
                    "Unable to load profile.",
                    Toast.LENGTH_SHORT
            ).show();


            if (cursor != null) {
                cursor.close();
            }
        }
    }
}