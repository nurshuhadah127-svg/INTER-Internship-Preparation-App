package com.example.internshippreparation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private TextView btnBack;

    private EditText etEditName;
    private EditText etEditStudentId;
    private EditText etEditEmail;
    private EditText etEditPassword;

    private Button btnSaveChanges;
    private Button btnCancel;

    private DatabaseHelper databaseHelper;

    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);


        // ==========================================
        // CONNECT XML
        // ==========================================

        btnBack =
                findViewById(R.id.btnBack);

        etEditName =
                findViewById(R.id.etEditName);

        etEditStudentId =
                findViewById(R.id.etEditStudentId);

        etEditEmail =
                findViewById(R.id.etEditEmail);

        etEditPassword =
                findViewById(R.id.etEditPassword);

        btnSaveChanges =
                findViewById(R.id.btnSaveChanges);

        btnCancel =
                findViewById(R.id.btnCancel);


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


        if (userId == -1) {

            Toast.makeText(
                    this,
                    "User information not found.",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }


        // ==========================================
        // LOAD CURRENT PROFILE
        // ==========================================

        loadCurrentProfile();


        // ==========================================
        // BACK BUTTON
        // ==========================================

        btnBack.setOnClickListener(v -> finish());


        // ==========================================
        // CANCEL
        // ==========================================

        btnCancel.setOnClickListener(v -> finish());


        // ==========================================
        // SAVE CHANGES
        // ==========================================

        btnSaveChanges.setOnClickListener(
                v -> saveChanges()
        );
    }


    // ==========================================
    // LOAD CURRENT PROFILE
    // ==========================================

    private void loadCurrentProfile() {

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


            etEditName.setText(name);


            if (studentId != null) {

                etEditStudentId.setText(
                        studentId
                );
            }


            etEditEmail.setText(email);


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


    // ==========================================
    // SAVE CHANGES
    // ==========================================

    private void saveChanges() {


        String name =
                etEditName
                        .getText()
                        .toString()
                        .trim();


        String studentId =
                etEditStudentId
                        .getText()
                        .toString()
                        .trim();


        String email =
                etEditEmail
                        .getText()
                        .toString()
                        .trim();


        String password =
                etEditPassword
                        .getText()
                        .toString()
                        .trim();


        // ==========================================
        // NAME VALIDATION
        // ==========================================

        if (name.isEmpty()) {

            etEditName.setError(
                    "Please enter your name."
            );

            etEditName.requestFocus();

            return;
        }


        // ==========================================
        // STUDENT ID VALIDATION
        // ==========================================

        if (studentId.isEmpty()) {

            etEditStudentId.setError(
                    "Please enter your Student ID."
            );

            etEditStudentId.requestFocus();

            return;
        }


        // ==========================================
        // EMAIL VALIDATION
        // ==========================================

        if (email.isEmpty()) {

            etEditEmail.setError(
                    "Please enter your email."
            );

            etEditEmail.requestFocus();

            return;
        }


        if (!Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            etEditEmail.setError(
                    "Please enter a valid email."
            );

            etEditEmail.requestFocus();

            return;
        }


        // ==========================================
        // PASSWORD VALIDATION
        // ==========================================

        if (!password.isEmpty() &&
                password.length() < 6) {

            etEditPassword.setError(
                    "Password must be at least 6 characters."
            );

            etEditPassword.requestFocus();

            return;
        }


        // ==========================================
        // OPEN DATABASE
        // ==========================================

        SQLiteDatabase db =
                databaseHelper.getWritableDatabase();


        // ==========================================
        // CHECK EMAIL DUPLICATE
        // ==========================================

        Cursor emailCursor =
                db.rawQuery(
                        "SELECT id FROM users " +
                                "WHERE email = ? AND id != ?",
                        new String[]{
                                email,
                                String.valueOf(userId)
                        }
                );


        if (emailCursor.moveToFirst()) {

            emailCursor.close();

            etEditEmail.setError(
                    "This email is already registered."
            );

            etEditEmail.requestFocus();

            return;
        }


        emailCursor.close();


        // ==========================================
        // UPDATE VALUES
        // ==========================================

        ContentValues values =
                new ContentValues();


        values.put(
                "name",
                name
        );


        values.put(
                "student_id",
                studentId
        );


        values.put(
                "email",
                email
        );


        // Only update password
        // if user entered a new one

        if (!password.isEmpty()) {

            values.put(
                    "password",
                    password
            );
        }


        // ==========================================
        // UPDATE DATABASE
        // ==========================================

        int result =
                db.update(
                        "users",
                        values,
                        "id = ?",
                        new String[]{
                                String.valueOf(userId)
                        }
                );


        // ==========================================
        // RESULT
        // ==========================================

        if (result > 0) {

            Toast.makeText(
                    this,
                    "Profile updated successfully!",
                    Toast.LENGTH_SHORT
            ).show();


            finish();

        } else {

            Toast.makeText(
                    this,
                    "No changes were made.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}