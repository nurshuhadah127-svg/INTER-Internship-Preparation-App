package com.example.internshippreparation;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminResourcesActivity extends AppCompatActivity {

    private DatabaseHelper db;

    private LinearLayout resourceContainer;

    private TextView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_resources);

        db = new DatabaseHelper(this);

        btnBack = findViewById(R.id.btnBack);

        resourceContainer =
                findViewById(R.id.resourceContainer);

        // BACK
        btnBack.setOnClickListener(v -> finish());

        // ADD RESOURCE
        findViewById(R.id.btnAddResource)
                .setOnClickListener(v -> showAddDialog());

        // LOAD RESOURCES
        loadResources();
    }


    // =========================================================
    // LOAD RESOURCES
    // =========================================================

    private void loadResources() {

        resourceContainer.removeAllViews();

        Cursor cursor =
                db.getRows("resources");

        if (cursor.getCount() == 0) {

            TextView emptyText =
                    new TextView(this);

            emptyText.setText(
                    "No resources available."
            );

            emptyText.setTextColor(
                    0xFF777777
            );

            emptyText.setTextSize(13);

            emptyText.setPadding(
                    10,
                    25,
                    10,
                    25
            );

            resourceContainer.addView(
                    emptyText
            );

            cursor.close();
            return;
        }


        while (cursor.moveToNext()) {

            int id =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    "id"
                            )
                    );

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
                    id,
                    title,
                    description
            );
        }

        cursor.close();
    }


    // =========================================================
    // RESOURCE CARD
    // =========================================================

    private void addResourceCard(
            int id,
            String title,
            String description
    ) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.HORIZONTAL
        );

        card.setGravity(
                android.view.Gravity.CENTER_VERTICAL
        );

        card.setPadding(
                12,
                12,
                12,
                12
        );

        card.setBackgroundResource(
                R.drawable.bg_admin_card
        );


        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(
                0,
                0,
                0,
                12
        );

        card.setLayoutParams(
                cardParams
        );


        // ICON

        TextView icon =
                new TextView(this);

        icon.setText("📄");

        icon.setTextSize(22);

        icon.setGravity(
                android.view.Gravity.CENTER
        );

        icon.setBackgroundResource(
                R.drawable.bg_admin_icon
        );


        LinearLayout.LayoutParams iconParams =
                new LinearLayout.LayoutParams(
                        50,
                        50
                );

        icon.setLayoutParams(
                iconParams
        );

        card.addView(icon);


        // TEXT CONTAINER

        LinearLayout textContainer =
                new LinearLayout(this);

        textContainer.setOrientation(
                LinearLayout.VERTICAL
        );


        LinearLayout.LayoutParams textParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        textParams.setMargins(
                12,
                0,
                8,
                0
        );

        textContainer.setLayoutParams(
                textParams
        );


        // TITLE

        TextView tvTitle =
                new TextView(this);

        tvTitle.setText(title);

        tvTitle.setTextColor(
                0xFF002147
        );

        tvTitle.setTextSize(15);

        tvTitle.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );


        textContainer.addView(
                tvTitle
        );


        // DESCRIPTION

        TextView tvDescription =
                new TextView(this);

        tvDescription.setText(
                description
        );

        tvDescription.setTextColor(
                0xFF777777
        );

        tvDescription.setTextSize(11);


        LinearLayout.LayoutParams descParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        descParams.setMargins(
                0,
                4,
                0,
                0
        );

        tvDescription.setLayoutParams(
                descParams
        );


        textContainer.addView(
                tvDescription
        );


        // CATEGORY

        TextView tvCategory =
                new TextView(this);

        tvCategory.setText(
                "Category: Internship Preparation"
        );

        tvCategory.setTextColor(
                0xFFED1C24
        );

        tvCategory.setTextSize(10);


        LinearLayout.LayoutParams categoryParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        categoryParams.setMargins(
                0,
                4,
                0,
                0
        );

        tvCategory.setLayoutParams(
                categoryParams
        );


        textContainer.addView(
                tvCategory
        );

        card.addView(
                textContainer
        );


        // EDIT BUTTON

        TextView btnEdit =
                new TextView(this);

        btnEdit.setText("✎");

        btnEdit.setTextColor(
                0xFFED1C24
        );

        btnEdit.setTextSize(22);

        btnEdit.setGravity(
                android.view.Gravity.CENTER
        );


        LinearLayout.LayoutParams editParams =
                new LinearLayout.LayoutParams(
                        40,
                        40
                );

        btnEdit.setLayoutParams(
                editParams
        );


        card.addView(
                btnEdit
        );


        // EDIT CLICK

        btnEdit.setOnClickListener(
                v -> showEditDialog(
                        id,
                        title,
                        description
                )
        );


        // LONG PRESS = DELETE

        card.setOnLongClickListener(
                v -> {

                    showDeleteDialog(
                            id,
                            title
                    );

                    return true;
                }
        );


        resourceContainer.addView(
                card
        );
    }


    // =========================================================
    // ADD RESOURCE
    // =========================================================

    private void showAddDialog() {

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        layout.setPadding(
                20,
                5,
                20,
                5
        );


        EditText etTitle =
                new EditText(this);

        etTitle.setHint(
                "Resource title"
        );


        EditText etDescription =
                new EditText(this);

        etDescription.setHint(
                "Resource description"
        );

        etDescription.setMinLines(3);

        etDescription.setGravity(
                android.view.Gravity.TOP
        );


        layout.addView(
                etTitle
        );

        layout.addView(
                etDescription
        );


        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("Add Resource")
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


        dialog.setOnShowListener(
                d -> {

                    dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE
                            )
                            .setOnClickListener(
                                    v -> {

                                        String title =
                                                etTitle
                                                        .getText()
                                                        .toString()
                                                        .trim();

                                        String description =
                                                etDescription
                                                        .getText()
                                                        .toString()
                                                        .trim();


                                        if (title.isEmpty()) {

                                            etTitle.setError(
                                                    "Enter resource title"
                                            );

                                            return;
                                        }


                                        if (description.isEmpty()) {

                                            etDescription.setError(
                                                    "Enter description"
                                            );

                                            return;
                                        }


                                        boolean success =
                                                db.addResource(
                                                        title,
                                                        description
                                                );


                                        if (success) {

                                            Toast.makeText(
                                                    this,
                                                    "Resource added",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            dialog.dismiss();

                                            loadResources();

                                        } else {

                                            Toast.makeText(
                                                    this,
                                                    "Failed to add resource",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }

                                    }
                            );
                }
        );


        dialog.show();
    }


    // =========================================================
    // EDIT RESOURCE
    // =========================================================

    private void showEditDialog(
            int id,
            String oldTitle,
            String oldDescription
    ) {

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        layout.setPadding(
                20,
                5,
                20,
                5
        );


        EditText etTitle =
                new EditText(this);

        etTitle.setText(
                oldTitle
        );


        EditText etDescription =
                new EditText(this);

        etDescription.setText(
                oldDescription
        );

        etDescription.setMinLines(3);

        etDescription.setGravity(
                android.view.Gravity.TOP
        );


        layout.addView(
                etTitle
        );

        layout.addView(
                etDescription
        );


        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("Edit Resource")
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


        dialog.setOnShowListener(
                d -> {

                    dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE
                            )
                            .setOnClickListener(
                                    v -> {

                                        String title =
                                                etTitle
                                                        .getText()
                                                        .toString()
                                                        .trim();

                                        String description =
                                                etDescription
                                                        .getText()
                                                        .toString()
                                                        .trim();


                                        if (title.isEmpty()
                                                || description.isEmpty()) {

                                            Toast.makeText(
                                                    this,
                                                    "Please complete all fields",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            return;
                                        }


                                        boolean success =
                                                db.updateResource(
                                                        id,
                                                        title,
                                                        description
                                                );


                                        if (success) {

                                            Toast.makeText(
                                                    this,
                                                    "Resource updated",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            dialog.dismiss();

                                            loadResources();

                                        } else {

                                            Toast.makeText(
                                                    this,
                                                    "Failed to update resource",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }

                                    }
                            );
                }
        );


        dialog.show();
    }


    // =========================================================
    // DELETE RESOURCE
    // =========================================================

    private void showDeleteDialog(
            int id,
            String title
    ) {

        new AlertDialog.Builder(this)

                .setTitle(
                        "Delete Resource"
                )

                .setMessage(
                        "Delete \"" +
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
                                    db.deleteResource(
                                            id
                                    );


                            if (success) {

                                Toast.makeText(
                                        this,
                                        "Resource deleted",
                                        Toast.LENGTH_SHORT
                                ).show();

                                loadResources();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Failed to delete resource",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                        }
                )

                .show();
    }
}