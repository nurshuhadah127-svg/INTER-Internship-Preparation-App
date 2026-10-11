package com.example.internshippreparation;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResourcesActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout resourceContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_resources);

        db = new DatabaseHelper(this);

        resourceContainer =
                findViewById(R.id.resourceContainer);

        findViewById(R.id.btnBack)
                .setOnClickListener(v -> finish());

        loadResources();
    }

    private void loadResources() {

        resourceContainer.removeAllViews();

        Cursor cursor =
                db.getRows("resources");

        if (cursor.getCount() == 0) {

            TextView empty =
                    new TextView(this);

            empty.setText(
                    "No resources available."
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

            resourceContainer.addView(empty);

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

            String description =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "description"
                            )
                    );

            addResourceCard(
                    title,
                    description
            );
        }

        cursor.close();
    }

    private void addResourceCard(
            String title,
            String description
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

        TextView tvDescription =
                new TextView(this);

        tvDescription.setText(description);
        tvDescription.setTextColor(0xFF666666);
        tvDescription.setTextSize(12);

        LinearLayout.LayoutParams descParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        descParams.setMargins(
                0,
                6,
                0,
                0
        );

        tvDescription.setLayoutParams(descParams);

        card.addView(tvDescription);

        resourceContainer.addView(card);
    }
}