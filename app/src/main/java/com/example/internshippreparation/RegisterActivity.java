package com.example.internshippreparation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etName;
    EditText etStudentId;
    EditText etEmail;
    EditText etPassword;
    EditText etConfirmPassword;

    Button btnRegister;

    TextView btnBack;
    TextView tvLogin;

    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);


        // Connect XML components

        btnBack = findViewById(R.id.btnBack);

        etName = findViewById(R.id.etName);

        etStudentId = findViewById(R.id.etStudentId);

        etEmail = findViewById(R.id.etEmail);

        etPassword = findViewById(R.id.etPassword);

        etConfirmPassword =
                findViewById(R.id.etConfirmPassword);

        btnRegister =
                findViewById(R.id.btnRegister);

        tvLogin =
                findViewById(R.id.tvLogin);


        // Connect database

        databaseHelper =
                new DatabaseHelper(this);


        // Back button

        btnBack.setOnClickListener(v -> {

            finish();

        });


        // Register button

        btnRegister.setOnClickListener(v -> {

            registerUser();

        });


        // Login link

        tvLogin.setOnClickListener(v -> {

            finish();

        });
    }


    private void registerUser() {

        String name =
                etName.getText()
                        .toString()
                        .trim();


        String studentId =
                etStudentId.getText()
                        .toString()
                        .trim();


        String email =
                etEmail.getText()
                        .toString()
                        .trim();


        String password =
                etPassword.getText()
                        .toString()
                        .trim();


        String confirmPassword =
                etConfirmPassword.getText()
                        .toString()
                        .trim();


        // Check name

        if (name.isEmpty()) {

            etName.setError(
                    "Please enter your name"
            );

            etName.requestFocus();

            return;
        }


        // Check student ID

        if (studentId.isEmpty()) {

            etStudentId.setError(
                    "Please enter your Student ID"
            );

            etStudentId.requestFocus();

            return;
        }


        // Check email

        if (email.isEmpty()) {

            etEmail.setError(
                    "Please enter your email"
            );

            etEmail.requestFocus();

            return;
        }


        // Check password

        if (password.isEmpty()) {

            etPassword.setError(
                    "Please enter your password"
            );

            etPassword.requestFocus();

            return;
        }


        // Check password length

        if (password.length() < 6) {

            etPassword.setError(
                    "Password must be at least 6 characters"
            );

            etPassword.requestFocus();

            return;
        }


        // Check confirm password

        if (confirmPassword.isEmpty()) {

            etConfirmPassword.setError(
                    "Please confirm your password"
            );

            etConfirmPassword.requestFocus();

            return;
        }


        // Check matching password

        if (!password.equals(confirmPassword)) {

            etConfirmPassword.setError(
                    "Passwords do not match"
            );

            etConfirmPassword.requestFocus();

            return;
        }


        // Check existing email

        if (databaseHelper.emailExists(email)) {

            etEmail.setError(
                    "Email already exists"
            );

            etEmail.requestFocus();

            return;
        }


        // Register student

        boolean registered =
                databaseHelper.registerUser(
                        name,
                        studentId,
                        email,
                        password
                );


        if (registered) {

            Toast.makeText(
                    RegisterActivity.this,
                    "Registration successful",
                    Toast.LENGTH_SHORT
            ).show();


            // Return to Login

            Intent intent =
                    new Intent(
                            RegisterActivity.this,
                            LoginActivity.class
                    );

            startActivity(intent);

            finish();

        } else {

            Toast.makeText(
                    RegisterActivity.this,
                    "Registration failed",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}