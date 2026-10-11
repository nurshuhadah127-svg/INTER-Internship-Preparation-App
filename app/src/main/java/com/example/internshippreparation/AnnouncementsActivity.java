package com.example.internshippreparation;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AnnouncementsActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout announcementContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_announcements);

        db = new DatabaseHelper(this);

        announcementContainer =
                findViewById(R.id.announcementContainer);

        findViewById(R.id.btnBack)
                .setOnClickListener(v -> finish());

        loadAnnouncements();
    }

    private void loadAnnouncements() {

        announcementContainer.removeAllViews();

        Cursor cursor =
                db.getRows("announcements");

        if (cursor.getCount() == 0) {

            TextView empty =
                    new TextView(this);

            empty.setText(
                    "No announcements available."
            );

            empty.setTextColor(
                    0xFF777777
            );

            empty.setTextSize(13);

            empty.setPadding(
                    10,
                    25,
                    10,
                    25
            );

            announcementContainer.addView(empty);

            cursor.close();
            return;
        }

        while (cursor.moveToNext()) {

            String title =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "title"
                            )
                    );

            String content =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "content"
                            )
                    );

            String date =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "date"
                            )
                    );

            addAnnouncementCard(
                    title,
                    content,
                    date
            );
        }

        cursor.close();
    }

    private void addAnnouncementCard(
            String title,
            String content,
            String date
    ) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                15,
                15,
                15,
                15
        );

        card.setBackgroundResource(
                R.drawable.bg_admin_card
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                0,
                0,
                12
        );

        card.setLayoutParams(params);

        TextView tvTitle =
                new TextView(this);

        tvTitle.setText(title);
        tvTitle.setTextColor(0xFF002147);
        tvTitle.setTextSize(16);
        tvTitle.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        card.addView(tvTitle);

        TextView tvContent =
                new TextView(this);

        tvContent.setText(content);
        tvContent.setTextColor(0xFF666666);
        tvContent.setTextSize(12);

        LinearLayout.LayoutParams contentParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        contentParams.setMargins(
                0,
                6,
                0,
                0
        );

        tvContent.setLayoutParams(contentParams);

        card.addView(tvContent);

        TextView tvDate =
                new TextView(this);

        tvDate.setText(
                "Date: " + date
        );

        tvDate.setTextColor(0xFFED1C24);
        tvDate.setTextSize(11);

        LinearLayout.LayoutParams dateParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        dateParams.setMargins(
                0,
                7,
                0,
                0
        );

        tvDate.setLayoutParams(dateParams);

        card.addView(tvDate);

        announcementContainer.addView(card);
    }
}