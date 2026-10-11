package com.example.internshippreparation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private TextView tvSubtitle;

    private EditText etSearch;

    private LinearLayout cardAnnouncements;
    private LinearLayout cardDownloads;
    private LinearLayout cardUsefulLinks;
    private LinearLayout cardChecklist;
    private LinearLayout cardNotes;
    private LinearLayout cardResources;

    private LinearLayout navHome;
    private LinearLayout navAnnouncements;
    private LinearLayout navDocuments;
    private LinearLayout navMore;

    private int userId;
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_dashboard);


        // =====================================================
        // GET USER INFORMATION
        // =====================================================

        userId = getIntent().getIntExtra(
                "USER_ID",
                -1
        );

        userName = getIntent().getStringExtra(
                "USER_NAME"
        );


        // =====================================================
        // CONNECT XML VIEWS
        // =====================================================

        tvWelcome = findViewById(R.id.tvWelcome);

        tvSubtitle = findViewById(R.id.tvSubtitle);

        etSearch = findViewById(R.id.etSearch);


        cardAnnouncements =
                findViewById(R.id.cardAnnouncements);

        cardDownloads =
                findViewById(R.id.cardDownloads);

        cardUsefulLinks =
                findViewById(R.id.cardUsefulLinks);

        cardChecklist =
                findViewById(R.id.cardChecklist);

        cardNotes =
                findViewById(R.id.cardNotes);

        cardResources =
                findViewById(R.id.cardResources);


        navHome =
                findViewById(R.id.navHome);

        navAnnouncements =
                findViewById(R.id.navAnnouncements);

        navDocuments =
                findViewById(R.id.navDocuments);

        navMore =
                findViewById(R.id.navMore);


        // =====================================================
        // DISPLAY USER NAME
        // =====================================================

        if (userName != null &&
                !userName.trim().isEmpty()) {

            tvWelcome.setText(
                    "Hello, " + userName + " 👋"
            );
        }


        // =====================================================
        // ANNOUNCEMENTS
        // =====================================================

        cardAnnouncements.setOnClickListener(v -> {

            Intent intent = new Intent(
                    StudentDashboardActivity.this,
                    AnnouncementsActivity.class
            );

            intent.putExtra(
                    "USER_ID",
                    userId
            );

            startActivity(intent);
        });


        // =====================================================
        // FORMS & BROCHURES
        // =====================================================

        cardDownloads.setOnClickListener(v -> {

            Intent intent = new Intent(
                    StudentDashboardActivity.this,
                    FormsBrochuresActivity.class
            );

            intent.putExtra(
                    "USER_ID",
                    userId
            );

            startActivity(intent);
        });


        // =====================================================
        // USEFUL LINKS
        // =====================================================

        cardUsefulLinks.setOnClickListener(v -> {

            Intent intent = new Intent(
                    StudentDashboardActivity.this,
                    UsefulLinksActivity.class
            );

            intent.putExtra(
                    "USER_ID",
                    userId
            );

            startActivity(intent);
        });


        // =====================================================
        // CHECKLIST
        // =====================================================

        cardChecklist.setOnClickListener(v -> {

            Intent intent = new Intent(
                    StudentDashboardActivity.this,
                    ChecklistActivity.class
            );

            intent.putExtra(
                    "USER_ID",
                    userId
            );

            startActivity(intent);
        });


        // =====================================================
        // PERSONAL NOTES
        // =====================================================

        cardNotes.setOnClickListener(v -> {

            Intent intent = new Intent(
                    StudentDashboardActivity.this,
                    NotesActivity.class
            );

            intent.putExtra(
                    "USER_ID",
                    userId
            );

            startActivity(intent);
        });


        // =====================================================
        // RESOURCES
        // =====================================================

        cardResources.setOnClickListener(v -> {

            Intent intent = new Intent(
                    StudentDashboardActivity.this,
                    ResourcesActivity.class
            );

            intent.putExtra(
                    "USER_ID",
                    userId
            );

            startActivity(intent);
        });


        // =====================================================
        // BOTTOM HOME
        // =====================================================

        navHome.setOnClickListener(v -> {

            // Already on Student Dashboard

        });


        // =====================================================
        // BOTTOM ANNOUNCEMENTS
        // =====================================================

        navAnnouncements.setOnClickListener(v -> {

            Intent intent = new Intent(
                    StudentDashboardActivity.this,
                    AnnouncementsActivity.class
            );

            intent.putExtra(
                    "USER_ID",
                    userId
            );

            startActivity(intent);
        });


        // =====================================================
        // BOTTOM DOCUMENTS
        // =====================================================

        navDocuments.setOnClickListener(v -> {

            Intent intent = new Intent(
                    StudentDashboardActivity.this,
                    FormsBrochuresActivity.class
            );

            intent.putExtra(
                    "USER_ID",
                    userId
            );

            startActivity(intent);
        });


        // =====================================================
        // MORE → PROFILE
        // =====================================================

        navMore.setOnClickListener(v -> {

            Intent intent = new Intent(
                    StudentDashboardActivity.this,
                    ProfileActivity.class
            );

            intent.putExtra(
                    "USER_ID",
                    userId
            );

            intent.putExtra(
                    "USER_NAME",
                    userName
            );

            startActivity(intent);
        });

    }
}