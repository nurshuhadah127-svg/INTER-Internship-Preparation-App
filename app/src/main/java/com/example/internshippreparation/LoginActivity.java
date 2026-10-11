package com.example.internshippreparation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText password;

    Button btnLogin;
    TextView registerLink;
    TextView forgotPasswordLink;
    CheckBox checkRemember;

    DatabaseHelper databaseHelper;

    boolean passwordVisible = false;

    // Remember Me
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        btnLogin = findViewById(R.id.btnLogin);
        registerLink = findViewById(R.id.registerLink);
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink);
        checkRemember = findViewById(R.id.checkRemember);

        databaseHelper = new DatabaseHelper(this);

        // SharedPreferences for Remember Me
        sharedPreferences = getSharedPreferences(
                "LoginPreferences",
                MODE_PRIVATE
        );

        // Create admin account for testing
        databaseHelper.createDefaultAdmin();

        // Load saved email
        String savedEmail = sharedPreferences.getString(
                "saved_email",
                ""
        );

        boolean rememberMe = sharedPreferences.getBoolean(
                "remember_me",
                false
        );

        if (rememberMe && !savedEmail.isEmpty()) {
            email.setText(savedEmail);
            checkRemember.setChecked(true);
        }

        // Login button
        btnLogin.setOnClickListener(v -> loginUser());

        // Register link
        registerLink.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);
        });

        forgotPasswordLink.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    ForgotPasswordActivity.class
            );

            startActivity(intent);
        });

        // Show / Hide Password
        password.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_UP) {

                int drawableRight = password.getRight()
                        - password.getCompoundDrawables()[2].getBounds().width();

                if (event.getRawX() >= drawableRight) {

                    if (passwordVisible) {

                        // Hide password
                        password.setTransformationMethod(
                                PasswordTransformationMethod.getInstance()
                        );

                        passwordVisible = false;

                        password.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.ic_visibility_off,
                                0
                        );

                    } else {

                        // Show password
                        password.setTransformationMethod(
                                HideReturnsTransformationMethod.getInstance()
                        );

                        passwordVisible = true;

                        password.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.ic_visibility,
                                0
                        );
                    }

                    password.setSelection(password.length());

                    return true;
                }
            }

            return false;
        });
    }


    private void loginUser() {

        String userEmail =
                email.getText().toString().trim();

        String userPassword =
                password.getText().toString().trim();


        if (userEmail.isEmpty()) {

            email.setError("Please enter your email");
            email.requestFocus();
            return;
        }


        if (userPassword.isEmpty()) {

            password.setError("Please enter your password");
            password.requestFocus();
            return;
        }


        Cursor cursor =
                databaseHelper.loginUser(
                        userEmail,
                        userPassword
                );


        if (cursor.moveToFirst()) {

            int userId =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow("id")
                    );

            String name =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("name")
                    );

            String role =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("role")
                    );

            cursor.close();


            // Remember Me
            if (checkRemember.isChecked()) {

                sharedPreferences.edit()
                        .putString("saved_email", userEmail)
                        .putBoolean("remember_me", true)
                        .apply();

            } else {

                sharedPreferences.edit()
                        .remove("saved_email")
                        .putBoolean("remember_me", false)
                        .apply();
            }


            Toast.makeText(
                    LoginActivity.this,
                    "Login successful",
                    Toast.LENGTH_SHORT
            ).show();


            if (role.equalsIgnoreCase("admin")) {

                Intent intent = new Intent(
                        LoginActivity.this,
                        AdminDashboardActivity.class
                );

                intent.putExtra("USER_ID", userId);
                intent.putExtra("USER_NAME", name);

                startActivity(intent);

            } else {

                Intent intent = new Intent(
                        LoginActivity.this,
                        StudentDashboardActivity.class
                );

                intent.putExtra("USER_ID", userId);
                intent.putExtra("USER_NAME", name);

                startActivity(intent);
            }

            finish();

        } else {

            cursor.close();

            Toast.makeText(
                    LoginActivity.this,
                    "Invalid email or password",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}