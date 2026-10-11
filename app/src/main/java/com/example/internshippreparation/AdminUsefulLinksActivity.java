package com.example.internshippreparation;

import android.app.AlertDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminUsefulLinksActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout linksContainer;
    private TextView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_useful_links);

        db = new DatabaseHelper(this);

        btnBack = findViewById(R.id.btnBack);
        linksContainer = findViewById(R.id.linksContainer);

        btnBack.setOnClickListener(v -> finish());

        findViewById(R.id.btnAddUsefulLink)
                .setOnClickListener(v -> showAddDialog());

        loadLinks();
    }


    // =========================================================
    // LOAD LINKS
    // =========================================================

    private void loadLinks() {

        linksContainer.removeAllViews();

        Cursor cursor = db.getRows("useful_links");

        if (cursor.getCount() == 0) {

            TextView emptyText = new TextView(this);

            emptyText.setText("No useful links available.");
            emptyText.setTextColor(0xFF777777);
            emptyText.setTextSize(13);
            emptyText.setPadding(10, 25, 10, 25);

            linksContainer.addView(emptyText);

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

            String url = cursor.getString(
                    cursor.getColumnIndexOrThrow("url")
            );

            addLinkCard(id, title, url);
        }

        cursor.close();
    }


    // =========================================================
    // CREATE LINK CARD
    // =========================================================

    private void addLinkCard(
            int id,
            String title,
            String url
    ) {

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(
                android.view.Gravity.CENTER_VERTICAL
        );
        card.setPadding(12, 12, 12, 12);

        card.setBackgroundResource(
                R.drawable.bg_admin_card
        );

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(0, 0, 0, 12);

        card.setLayoutParams(cardParams);


        // ICON

        TextView icon = new TextView(this);

        icon.setText("🔗");
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

        icon.setLayoutParams(iconParams);

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

        textParams.setMargins(12, 0, 8, 0);

        textContainer.setLayoutParams(textParams);


        // TITLE

        TextView tvTitle = new TextView(this);

        tvTitle.setText(title);
        tvTitle.setTextColor(0xFF002147);
        tvTitle.setTextSize(15);
        tvTitle.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        textContainer.addView(tvTitle);


        // URL

        TextView tvUrl = new TextView(this);

        tvUrl.setText(url);
        tvUrl.setTextColor(0xFFED1C24);
        tvUrl.setTextSize(11);

        tvUrl.setMaxLines(2);

        textContainer.addView(tvUrl);

        card.addView(textContainer);


        // EDIT BUTTON

        TextView btnEdit = new TextView(this);

        btnEdit.setText("✎");
        btnEdit.setTextColor(0xFFED1C24);
        btnEdit.setTextSize(22);
        btnEdit.setGravity(
                android.view.Gravity.CENTER
        );

        LinearLayout.LayoutParams editParams =
                new LinearLayout.LayoutParams(
                        40,
                        40
                );

        btnEdit.setLayoutParams(editParams);

        card.addView(btnEdit);


        // EDIT

        btnEdit.setOnClickListener(v ->
                showEditDialog(
                        id,
                        title,
                        url
                )
        );


        // CLICK LINK

        card.setOnClickListener(v -> {

            try {

                android.content.Intent intent =
                        new android.content.Intent(
                                android.content.Intent.ACTION_VIEW,
                                Uri.parse(url)
                        );

                startActivity(intent);

            } catch (Exception e) {

                Toast.makeText(
                        this,
                        "Unable to open link",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });


        // LONG PRESS = DELETE

        card.setOnLongClickListener(v -> {

            showDeleteDialog(
                    id,
                    title
            );

            return true;
        });


        linksContainer.addView(card);
    }


    // =========================================================
    // ADD LINK
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
                "Link title"
        );


        EditText etUrl =
                new EditText(this);

        etUrl.setHint(
                "URL e.g. https://www.uptm.edu.my"
        );

        etUrl.setInputType(
                android.text.InputType.TYPE_CLASS_TEXT |
                        android.text.InputType.TYPE_TEXT_VARIATION_URI
        );


        layout.addView(etTitle);
        layout.addView(etUrl);


        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("Add Useful Link")
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

            dialog.getButton(
                    AlertDialog.BUTTON_POSITIVE
            ).setOnClickListener(v -> {

                String title =
                        etTitle.getText()
                                .toString()
                                .trim();

                String url =
                        etUrl.getText()
                                .toString()
                                .trim();


                if (title.isEmpty()) {

                    etTitle.setError(
                            "Enter link title"
                    );

                    return;
                }


                if (url.isEmpty()) {

                    etUrl.setError(
                            "Enter URL"
                    );

                    return;
                }


                if (!url.startsWith("http://")
                        && !url.startsWith("https://")) {

                    etUrl.setError(
                            "URL must start with http:// or https://"
                    );

                    return;
                }


                boolean success =
                        db.addLink(
                                title,
                                url
                        );


                if (success) {

                    Toast.makeText(
                            this,
                            "Useful link added",
                            Toast.LENGTH_SHORT
                    ).show();

                    dialog.dismiss();

                    loadLinks();

                } else {

                    Toast.makeText(
                            this,
                            "Failed to add useful link",
                            Toast.LENGTH_SHORT
                    ).show();
                }

            });

        });


        dialog.show();
    }


    // =========================================================
    // EDIT LINK
    // =========================================================

    private void showEditDialog(
            int id,
            String oldTitle,
            String oldUrl
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

        etTitle.setText(oldTitle);


        EditText etUrl =
                new EditText(this);

        etUrl.setText(oldUrl);

        etUrl.setInputType(
                android.text.InputType.TYPE_CLASS_TEXT |
                        android.text.InputType.TYPE_TEXT_VARIATION_URI
        );


        layout.addView(etTitle);
        layout.addView(etUrl);


        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("Edit Useful Link")
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

            dialog.getButton(
                    AlertDialog.BUTTON_POSITIVE
            ).setOnClickListener(v -> {

                String title =
                        etTitle.getText()
                                .toString()
                                .trim();

                String url =
                        etUrl.getText()
                                .toString()
                                .trim();


                if (title.isEmpty()
                        || url.isEmpty()) {

                    Toast.makeText(
                            this,
                            "Please complete all fields",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }


                if (!url.startsWith("http://")
                        && !url.startsWith("https://")) {

                    Toast.makeText(
                            this,
                            "URL must start with http:// or https://",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }


                boolean success =
                        db.updateLink(
                                id,
                                title,
                                url
                        );


                if (success) {

                    Toast.makeText(
                            this,
                            "Useful link updated",
                            Toast.LENGTH_SHORT
                    ).show();

                    dialog.dismiss();

                    loadLinks();

                } else {

                    Toast.makeText(
                            this,
                            "Failed to update link",
                            Toast.LENGTH_SHORT
                    ).show();
                }

            });

        });


        dialog.show();
    }


    // =========================================================
    // DELETE LINK
    // =========================================================

    private void showDeleteDialog(
            int id,
            String title
    ) {

        new AlertDialog.Builder(this)

                .setTitle(
                        "Delete Useful Link"
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
                                    db.deleteLink(id);

                            if (success) {

                                Toast.makeText(
                                        this,
                                        "Useful link deleted",
                                        Toast.LENGTH_SHORT
                                ).show();

                                loadLinks();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Failed to delete link",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                        }
                )

                .show();
    }
}