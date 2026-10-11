package com.example.internshippreparation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private LinearLayout cardAnnouncements;
    private LinearLayout cardResources;
    private LinearLayout cardDocuments;
    private LinearLayout cardUsefulLinks;
    private LinearLayout cardStudents;

    private Button btnAdminLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_dashboard);

        cardAnnouncements = findViewById(R.id.cardAnnouncements);
        cardResources = findViewById(R.id.cardResources);
        cardDocuments = findViewById(R.id.cardDocuments);
        cardUsefulLinks = findViewById(R.id.cardUsefulLinks);
        cardStudents = findViewById(R.id.cardStudents);

        btnAdminLogout = findViewById(R.id.btnAdminLogout);

        cardAnnouncements.setOnClickListener(v -> {
            startActivity(new Intent(
                    AdminDashboardActivity.this,
                    AdminAnnouncementsActivity.class
            ));
        });

        cardResources.setOnClickListener(v -> {
            startActivity(new Intent(
                    AdminDashboardActivity.this,
                    AdminResourcesActivity.class
            ));
        });

        cardDocuments.setOnClickListener(v -> {
            startActivity(new Intent(
                    AdminDashboardActivity.this,
                    AdminDocumentsActivity.class
            ));
        });

        cardUsefulLinks.setOnClickListener(v -> {
            startActivity(new Intent(
                    AdminDashboardActivity.this,
                    AdminUsefulLinksActivity.class
            ));
        });

        cardStudents.setOnClickListener(v -> {
            startActivity(new Intent(
                    AdminDashboardActivity.this,
                    AdminStudentsActivity.class
            ));
        });


        btnAdminLogout.setOnClickListener(v -> logout());
    }

    private void logout() {

        Intent intent = new Intent(
                AdminDashboardActivity.this,
                LoginActivity.class
        );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        finish();
    }
}