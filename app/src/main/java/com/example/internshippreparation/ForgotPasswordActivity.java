package com.example.internshippreparation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText email, studentId;
    Button btnVerify;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.email);
        studentId = findViewById(R.id.studentId);
        btnVerify = findViewById(R.id.btnVerify);

        db = new DatabaseHelper(this);

        btnVerify.setOnClickListener(v -> {

            String userEmail = email.getText().toString().trim();
            String userStudentId = studentId.getText().toString().trim();

            if (userEmail.isEmpty() || userStudentId.isEmpty()) {
                Toast.makeText(
                        ForgotPasswordActivity.this,
                        "Please enter your email and Student ID",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            boolean verified = db.verifyUserForPasswordReset(
                    userEmail,
                    userStudentId
            );

            if (verified) {

                Intent intent = new Intent(
                        ForgotPasswordActivity.this,
                        ResetPasswordActivity.class
                );

                intent.putExtra("RESET_EMAIL", userEmail);

                startActivity(intent);

            } else {

                Toast.makeText(
                        ForgotPasswordActivity.this,
                        "Email or Student ID is incorrect",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}