package com.example.internshippreparation;

import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    private TextView btnBack;

    private EditText etNoteTitle;
    private EditText etNoteContent;

    private Button btnSaveNote;

    private LinearLayout notesContainer;

    private DatabaseHelper databaseHelper;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notes);

        // Find views
        btnBack = findViewById(R.id.btnBack);

        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteContent = findViewById(R.id.etNoteContent);

        btnSaveNote = findViewById(R.id.btnSaveNote);

        notesContainer = findViewById(R.id.notesContainer);

        // Database
        databaseHelper = new DatabaseHelper(this);

        // Get logged-in user ID
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Save note
        btnSaveNote.setOnClickListener(v -> saveNote());

        // Load saved notes
        loadNotes();
    }

    private void saveNote() {

        String title = etNoteTitle.getText()
                .toString()
                .trim();

        String content = etNoteContent.getText()
                .toString()
                .trim();

        // Check title
        if (title.isEmpty()) {

            etNoteTitle.setError("Please enter a note title");
            etNoteTitle.requestFocus();

            return;
        }

        // Check content
        if (content.isEmpty()) {

            etNoteContent.setError("Please write your note");
            etNoteContent.requestFocus();

            return;
        }

        // Check user ID
        if (userId == -1) {

            Toast.makeText(
                    this,
                    "User information not found.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        /*
         * Your existing DatabaseHelper addNote()
         * accepts:
         *
         * addNote(int userId, String note)
         *
         * Therefore, combine the title and content
         * into one note.
         */
        String currentDate =
                new java.text.SimpleDateFormat(
                        "dd MMMM yyyy",
                        java.util.Locale.getDefault()
                ).format(new java.util.Date());

        boolean inserted =
                databaseHelper.addNote(
                        userId,
                        title,
                        content,
                        currentDate
                );

        if (inserted) {

            Toast.makeText(
                    this,
                    "Note saved successfully!",
                    Toast.LENGTH_SHORT
            ).show();

            // Clear fields
            etNoteTitle.setText("");
            etNoteContent.setText("");

            // Refresh notes
            loadNotes();

        } else {

            Toast.makeText(
                    this,
                    "Unable to save note.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void loadNotes() {

        notesContainer.removeAllViews();

        if (userId == -1) {
            return;
        }

        /*
         * Your existing DatabaseHelper returns
         * ArrayList<String>, not Cursor.
         */
        ArrayList<String[]> notes =
                databaseHelper.getNotes(userId);

        if (notes == null || notes.isEmpty()) {

            TextView emptyMessage =
                    new TextView(this);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            emptyMessage.setLayoutParams(params);

            emptyMessage.setText(
                    "No notes yet.\nCreate your first internship note above."
            );

            emptyMessage.setTextColor(
                    Color.parseColor("#777080")
            );

            emptyMessage.setTextSize(14);

            emptyMessage.setGravity(
                    Gravity.CENTER
            );

            emptyMessage.setPadding(
                    20,
                    30,
                    20,
                    30
            );

            notesContainer.addView(
                    emptyMessage
            );

            return;
        }

        // Display every saved note
        for (String[] note : notes) {

            addNoteCard(note);
        }
    }

    private void addNoteCard(String[] note) {

        // Main card
        LinearLayout card = new LinearLayout(this);

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(0, 0, 0, 12);

        card.setLayoutParams(cardParams);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(18, 18, 18, 18);
        card.setBackgroundResource(R.drawable.bg_content_card);

        // Title
        TextView titleView = new TextView(this);

        titleView.setText(note[1]);
        titleView.setTextColor(
                Color.parseColor("#002147")
        );
        titleView.setTextSize(17);
        titleView.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(titleView);

        // Content
        TextView contentView = new TextView(this);

        contentView.setText(note[2]);
        contentView.setTextColor(
                Color.parseColor("#2E2345")
        );
        contentView.setTextSize(15);
        contentView.setPadding(0, 10, 0, 10);

        card.addView(contentView);

        // Date
        TextView dateView = new TextView(this);

        String date = note[3];

        if (date == null || date.equals("null") || date.isEmpty()) {
            date = "Date: Not available";
        } else {
            date = "Date: " + date;
        }

        dateView.setText(date);
        dateView.setTextColor(
                Color.parseColor("#777777")
        );
        dateView.setTextSize(13);

        card.addView(dateView);

        // Buttons container
        LinearLayout buttonLayout =
                new LinearLayout(this);

        buttonLayout.setOrientation(
                LinearLayout.HORIZONTAL
        );

        buttonLayout.setGravity(
                Gravity.END
        );

        LinearLayout.LayoutParams buttonLayoutParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        buttonLayoutParams.setMargins(
                0,
                12,
                0,
                0
        );

        buttonLayout.setLayoutParams(
                buttonLayoutParams
        );

        // Edit button
        Button editButton = new Button(this);

        editButton.setText("Edit");
        editButton.setTextColor(Color.WHITE);
        editButton.setTextSize(12);
        editButton.setAllCaps(false);
        editButton.setMinWidth(0);
        editButton.setMinimumWidth(0);
        editButton.setPadding(5, 0, 5, 0);
        editButton.setBackgroundResource(
                R.drawable.bg_red_button
        );

        LinearLayout.LayoutParams editParams =
                new LinearLayout.LayoutParams(
                        110,
                        48
                );

        editParams.setMargins(
                0,
                0,
                8,
                0
        );

        editButton.setLayoutParams(editParams);

        // Delete button
        Button deleteButton = new Button(this);

        deleteButton.setText("Delete");
        deleteButton.setTextColor(Color.WHITE);
        deleteButton.setTextSize(12);
        deleteButton.setAllCaps(false);
        deleteButton.setMinWidth(0);
        deleteButton.setMinimumWidth(0);
        deleteButton.setPadding(5, 0, 5, 0);
        deleteButton.setBackgroundResource(
                R.drawable.bg_red_button
        );

        LinearLayout.LayoutParams deleteParams =
                new LinearLayout.LayoutParams(
                        110,
                        48
                );

        deleteButton.setLayoutParams(deleteParams);

        // Edit click
        editButton.setOnClickListener(v ->
                showEditDialog(note)
        );

        // Delete click
        deleteButton.setOnClickListener(v -> {

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Delete Note")
                    .setMessage("Are you sure you want to delete this note?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Delete", (dialog, which) -> {

                        int noteId = Integer.parseInt(note[0]);

                        boolean deleted =
                                databaseHelper.deleteNote(noteId);

                        if (deleted) {

                            Toast.makeText(
                                    this,
                                    "Note deleted successfully!",
                                    Toast.LENGTH_SHORT
                            ).show();

                            loadNotes();

                        } else {

                            Toast.makeText(
                                    this,
                                    "Unable to delete note.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    })
                    .show();
        });

        buttonLayout.addView(editButton);
        buttonLayout.addView(deleteButton);

        card.addView(buttonLayout);

        notesContainer.addView(card);
    }

    private void showEditDialog(String[] note) {

        LinearLayout layout = new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        layout.setPadding(
                40,
                10,
                40,
                10
        );

        EditText titleInput = new EditText(this);

        titleInput.setHint("Note title");
        titleInput.setText(note[1]);

        layout.addView(titleInput);

        EditText contentInput = new EditText(this);

        contentInput.setHint("Write your note here...");
        contentInput.setText(note[2]);
        contentInput.setGravity(Gravity.TOP);
        contentInput.setMinLines(4);

        layout.addView(contentInput);

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Edit Note")
                .setView(layout)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialog, which) -> {

                    String newTitle =
                            titleInput.getText()
                                    .toString()
                                    .trim();

                    String newContent =
                            contentInput.getText()
                                    .toString()
                                    .trim();

                    if (newTitle.isEmpty()) {

                        Toast.makeText(
                                this,
                                "Please enter a note title.",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    if (newContent.isEmpty()) {

                        Toast.makeText(
                                this,
                                "Please write your note.",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    String currentDate =
                            new java.text.SimpleDateFormat(
                                    "dd MMMM yyyy",
                                    java.util.Locale.getDefault()
                            ).format(
                                    new java.util.Date()
                            );

                    int noteId =
                            Integer.parseInt(note[0]);

                    boolean updated =
                            databaseHelper.updateNote(
                                    noteId,
                                    newTitle,
                                    newContent,
                                    currentDate
                            );

                    if (updated) {

                        Toast.makeText(
                                this,
                                "Note updated successfully!",
                                Toast.LENGTH_SHORT
                        ).show();

                        loadNotes();

                    } else {

                        Toast.makeText(
                                this,
                                "Unable to update note.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .show();
    }
}