package com.example.internshippreparation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText newPassword, retypePassword;
    Button btnResetPassword;
    DatabaseHelper db;

    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        newPassword = findViewById(R.id.newPassword);
        retypePassword = findViewById(R.id.retypePassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        db = new DatabaseHelper(this);

        userEmail = getIntent().getStringExtra("RESET_EMAIL");

        btnResetPassword.setOnClickListener(v -> {

            String password = newPassword.getText().toString().trim();
            String confirmPassword = retypePassword.getText().toString().trim();

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(
                        ResetPasswordActivity.this,
                        "Please enter your new password",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(
                        ResetPasswordActivity.this,
                        "Passwords do not match",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            boolean updated = db.updatePassword(userEmail, password);

            if (updated) {

                Toast.makeText(
                        ResetPasswordActivity.this,
                        "Password reset successfully",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent = new Intent(
                        ResetPasswordActivity.this,
                        LoginActivity.class
                );

                intent.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                );

                startActivity(intent);
                finish();

            } else {

                Toast.makeText(
                        ResetPasswordActivity.this,
                        "Failed to reset password",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}