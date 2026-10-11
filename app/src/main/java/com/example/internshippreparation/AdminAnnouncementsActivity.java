package com.example.internshippreparation;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminAnnouncementsActivity extends AppCompatActivity {

    private DatabaseHelper db;

    private LinearLayout announcementContainer;

    private TextView btnBack;
    private Button btnAddAnnouncement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_announcements);

        db = new DatabaseHelper(this);

        btnBack = findViewById(R.id.btnBack);
        btnAddAnnouncement = findViewById(R.id.btnAddAnnouncement);

        announcementContainer = findViewById(R.id.announcementContainer);

        // BACK
        btnBack.setOnClickListener(v -> finish());

        // ADD ANNOUNCEMENT
        btnAddAnnouncement.setOnClickListener(v -> showAddDialog());

        // LOAD ANNOUNCEMENTS
        loadAnnouncements();
    }

    // =========================================================
    // LOAD ANNOUNCEMENTS
    // =========================================================

    private void loadAnnouncements() {

        announcementContainer.removeAllViews();

        Cursor cursor = db.getRows("announcements");

        if (cursor.getCount() == 0) {

            TextView emptyText = new TextView(this);

            emptyText.setText("No announcements available.");
            emptyText.setTextColor(0xFF777777);
            emptyText.setTextSize(13);
            emptyText.setPadding(10, 25, 10, 25);

            announcementContainer.addView(emptyText);

            cursor.close();
            return;
        }

        while (cursor.moveToNext()) {

            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
            );

            String title = cursor.getString(
                    cursor.getColumnIndexOrThrow("title")
            );

            String content = cursor.getString(
                    cursor.getColumnIndexOrThrow("content")
            );

            String date = cursor.getString(
                    cursor.getColumnIndexOrThrow("date")
            );

            addAnnouncementCard(id, title, content, date);
        }

        cursor.close();
    }

    // =========================================================
    // CREATE ANNOUNCEMENT CARD
    // =========================================================

    private void addAnnouncementCard(
            int id,
            String title,
            String content,
            String date
    ) {

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(14, 14, 14, 14);
        card.setBackgroundResource(R.drawable.bg_admin_card);

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(0, 0, 0, 12);

        card.setLayoutParams(cardParams);

        // TITLE
        TextView tvTitle = new TextView(this);

        tvTitle.setText(title);
        tvTitle.setTextColor(0xFF002147);
        tvTitle.setTextSize(15);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);

        card.addView(tvTitle);


        // CONTENT
        TextView tvContent = new TextView(this);

        tvContent.setText(content);
        tvContent.setTextColor(0xFF777777);
        tvContent.setTextSize(11);

        LinearLayout.LayoutParams contentParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        contentParams.setMargins(0, 5, 0, 0);

        tvContent.setLayoutParams(contentParams);

        card.addView(tvContent);


        // DATE
        TextView tvDate = new TextView(this);

        tvDate.setText("Date: " + date);
        tvDate.setTextColor(0xFFED1C24);
        tvDate.setTextSize(11);

        LinearLayout.LayoutParams dateParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        dateParams.setMargins(0, 7, 0, 8);

        tvDate.setLayoutParams(dateParams);

        card.addView(tvDate);


        // BUTTON ROW
        LinearLayout buttonRow = new LinearLayout(this);

        buttonRow.setOrientation(LinearLayout.HORIZONTAL);

        // EDIT BUTTON
        Button btnEdit = new Button(this);

        btnEdit.setText("✎  Edit");
        btnEdit.setTextSize(11);
        btnEdit.setTextColor(0xFFED1C24);
        btnEdit.setAllCaps(false);
        btnEdit.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(0xFFF1E9FF)
        );

        LinearLayout.LayoutParams editParams =
                new LinearLayout.LayoutParams(
                        0,
                        42,
                        1
                );

        editParams.setMargins(0, 0, 6, 0);

        btnEdit.setLayoutParams(editParams);

        buttonRow.addView(btnEdit);


        // DELETE BUTTON
        Button btnDelete = new Button(this);

        btnDelete.setText("Delete");
        btnDelete.setTextSize(11);
        btnDelete.setTextColor(0xFFED1C24);
        btnDelete.setAllCaps(false);
        btnDelete.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(0xFFFFFFFF)
        );

        LinearLayout.LayoutParams deleteParams =
                new LinearLayout.LayoutParams(
                        0,
                        42,
                        1
                );

        deleteParams.setMargins(6, 0, 0, 0);

        btnDelete.setLayoutParams(deleteParams);

        buttonRow.addView(btnDelete);

        card.addView(buttonRow);


        // EDIT FUNCTION
        btnEdit.setOnClickListener(v ->
                showEditDialog(id, title, content, date)
        );


        // DELETE FUNCTION
        btnDelete.setOnClickListener(v ->
                confirmDelete(id, title)
        );


        announcementContainer.addView(card);
    }

    // =========================================================
    // ADD
    // =========================================================

    private void showAddDialog() {

        LinearLayout layout = new LinearLayout(this);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 5, 20, 5);


        EditText etTitle = new EditText(this);

        etTitle.setHint("Announcement title");


        EditText etContent = new EditText(this);

        etContent.setHint("Announcement content");

        etContent.setMinLines(3);

        etContent.setGravity(
                android.view.Gravity.TOP
        );


        EditText etDate = new EditText(this);

        etDate.setHint("Date e.g. 15 October 2026");


        layout.addView(etTitle);
        layout.addView(etContent);
        layout.addView(etDate);


        new AlertDialog.Builder(this)

                .setTitle("Add Announcement")

                .setView(layout)

                .setNegativeButton(
                        "Cancel",
                        null
                )

                .setPositiveButton(
                        "Save",
                        null
                )

                .create();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add Announcement")
                .setView(layout)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", null)
                .create();

        dialog.setOnShowListener(d -> {

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener(v -> {

                        String title =
                                etTitle.getText()
                                        .toString()
                                        .trim();

                        String content =
                                etContent.getText()
                                        .toString()
                                        .trim();

                        String date =
                                etDate.getText()
                                        .toString()
                                        .trim();


                        if (title.isEmpty()) {

                            etTitle.setError(
                                    "Please enter announcement title"
                            );

                            return;
                        }

                        if (content.isEmpty()) {

                            etContent.setError(
                                    "Please enter announcement content"
                            );

                            return;
                        }

                        if (date.isEmpty()) {

                            etDate.setError(
                                    "Please enter date"
                            );

                            return;
                        }


                        boolean success =
                                db.addAnnouncement(
                                        title,
                                        content,
                                        date
                                );


                        if (success) {

                            Toast.makeText(
                                    this,
                                    "Announcement added successfully",
                                    Toast.LENGTH_SHORT
                            ).show();

                            dialog.dismiss();

                            loadAnnouncements();

                        } else {

                            Toast.makeText(
                                    this,
                                    "Failed to add announcement",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                    });

        });

        dialog.show();
    }

    // =========================================================
    // EDIT
    // =========================================================

    private void showEditDialog(
            int id,
            String oldTitle,
            String oldContent,
            String oldDate
    ) {

        LinearLayout layout = new LinearLayout(this);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 5, 20, 5);


        EditText etTitle = new EditText(this);

        etTitle.setText(oldTitle);
        etTitle.setHint("Announcement title");


        EditText etContent = new EditText(this);

        etContent.setText(oldContent);
        etContent.setHint("Announcement content");
        etContent.setMinLines(3);
        etContent.setGravity(
                android.view.Gravity.TOP
        );


        EditText etDate = new EditText(this);

        etDate.setText(oldDate);
        etDate.setHint("Date");


        layout.addView(etTitle);
        layout.addView(etContent);
        layout.addView(etDate);


        AlertDialog dialog =
                new AlertDialog.Builder(this)

                        .setTitle("Edit Announcement")

                        .setView(layout)

                        .setNegativeButton(
                                "Cancel",
                                null
                        )

                        .setPositiveButton(
                                "Save",
                                null
                        )

                        .create();


        dialog.setOnShowListener(d -> {

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener(v -> {

                        String title =
                                etTitle.getText()
                                        .toString()
                                        .trim();

                        String content =
                                etContent.getText()
                                        .toString()
                                        .trim();

                        String date =
                                etDate.getText()
                                        .toString()
                                        .trim();


                        if (title.isEmpty()
                                || content.isEmpty()
                                || date.isEmpty()) {

                            Toast.makeText(
                                    this,
                                    "Please complete all fields",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }


                        boolean success =
                                db.updateAnnouncement(
                                        id,
                                        title,
                                        content,
                                        date
                                );


                        if (success) {

                            Toast.makeText(
                                    this,
                                    "Announcement updated",
                                    Toast.LENGTH_SHORT
                            ).show();

                            dialog.dismiss();

                            loadAnnouncements();

                        } else {

                            Toast.makeText(
                                    this,
                                    "Failed to update announcement",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                    });

        });

        dialog.show();
    }

    // =========================================================
    // DELETE
    // =========================================================

    private void confirmDelete(
            int id,
            String title
    ) {

        new AlertDialog.Builder(this)

                .setTitle("Delete Announcement")

                .setMessage(
                        "Are you sure you want to delete \"" +
                                title +
                                "\"?"
                )

                .setNegativeButton(
                        "Cancel",
                        null
                )

                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            boolean success =
                                    db.deleteAnnouncement(id);

                            if (success) {

                                Toast.makeText(
                                        this,
                                        "Announcement deleted",
                                        Toast.LENGTH_SHORT
                                ).show();

                                loadAnnouncements();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Failed to delete announcement",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        })

                .show();
    }
}