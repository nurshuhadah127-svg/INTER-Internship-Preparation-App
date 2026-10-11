package com.example.internshippreparation;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdminDocumentsActivity extends AppCompatActivity {

    private static final int PICK_FILE = 1001;

    private static final String SUPABASE_URL =
            "https://ssukmsmrmtvqatqaaucl.supabase.co";

    private static final String SUPABASE_PUBLISHABLE_KEY =
            "sb_publishable_T07KjnVeT1IUOi-S93ZsFA_SPA9DzPe";

    private static final String TABLE_URL =
            SUPABASE_URL + "/rest/v1/documents";

    private static final String STORAGE_URL =
            SUPABASE_URL + "/storage/v1/object/documents/";

    private LinearLayout documentsContainer;
    private Button btnUpload;

    private Uri selectedFileUri;

    private final OkHttpClient client =
            new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_documents);

        documentsContainer =
                findViewById(R.id.documentsContainer);

        btnUpload =
                findViewById(R.id.btnUpload);

        TextView btnBack =
                findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        btnUpload.setOnClickListener(v -> showUploadDialog());

        loadDocuments();
    }


    // =========================================================
    // LOAD DOCUMENTS
    // =========================================================

    private void loadDocuments() {

        documentsContainer.removeAllViews();

        TextView loading =
                new TextView(this);

        loading.setText("Loading documents...");
        loading.setTextColor(Color.GRAY);
        loading.setTextSize(14);
        loading.setGravity(Gravity.CENTER);
        loading.setPadding(0, 50, 0, 50);

        documentsContainer.addView(loading);

        String url =
                TABLE_URL +
                        "?select=*&order=created_at.desc";

        Request request =
                new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader(
                                "apikey",
                                SUPABASE_PUBLISHABLE_KEY
                        )
                        .addHeader(
                                "Authorization",
                                "Bearer " +
                                        SUPABASE_PUBLISHABLE_KEY
                        )
                        .build();

        client.newCall(request).enqueue(
                new Callback() {

                    @Override
                    public void onFailure(
                            Call call,
                            IOException e) {

                        runOnUiThread(() -> {

                            Toast.makeText(
                                    AdminDocumentsActivity.this,
                                    "Unable to load documents",
                                    Toast.LENGTH_SHORT
                            ).show();

                        });
                    }


                    @Override
                    public void onResponse(
                            Call call,
                            Response response)
                            throws IOException {

                        if (response.body() == null) {
                            return;
                        }

                        String body =
                                response.body().string();

                        if (!response.isSuccessful()) {

                            runOnUiThread(() -> {

                                Toast.makeText(
                                        AdminDocumentsActivity.this,
                                        "Supabase error: " +
                                                response.code(),
                                        Toast.LENGTH_SHORT
                                ).show();

                            });

                            return;
                        }

                        try {

                            JSONArray documents =
                                    new JSONArray(body);

                            runOnUiThread(() ->
                                    displayDocuments(documents)
                            );

                        } catch (Exception e) {

                            runOnUiThread(() -> {

                                Toast.makeText(
                                        AdminDocumentsActivity.this,
                                        "Error reading documents",
                                        Toast.LENGTH_SHORT
                                ).show();

                            });
                        }
                    }
                }
        );
    }


    // =========================================================
    // DISPLAY DOCUMENTS
    // =========================================================

    private void displayDocuments(
            JSONArray documents) {

        documentsContainer.removeAllViews();

        if (documents.length() == 0) {

            TextView empty =
                    new TextView(this);

            empty.setText(
                    "No documents available."
            );

            empty.setTextColor(Color.GRAY);
            empty.setTextSize(14);
            empty.setGravity(Gravity.CENTER);
            empty.setPadding(0, 50, 0, 50);

            documentsContainer.addView(empty);

            return;
        }

        try {

            for (int i = 0;
                 i < documents.length();
                 i++) {

                JSONObject object =
                        documents.getJSONObject(i);

                String title =
                        object.optString(
                                "title",
                                "Untitled"
                        );

                String description =
                        object.optString(
                                "description",
                                ""
                        );

                String category =
                        object.optString(
                                "category",
                                ""
                        );

                String program =
                        object.optString(
                                "program",
                                ""
                        );

                String fileName =
                        object.optString(
                                "file_name",
                                ""
                        );

                addDocumentCard(
                        title,
                        description,
                        category,
                        program,
                        fileName
                );
            }

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Unable to display documents",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    // =========================================================
    // DOCUMENT CARD
    // =========================================================

    private void addDocumentCard(
            String title,
            String description,
            String category,
            String program,
            String fileName) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.HORIZONTAL
        );

        card.setGravity(
                Gravity.CENTER_VERTICAL
        );

        card.setPadding(
                14,
                14,
                14,
                14
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
                10
        );

        card.setLayoutParams(cardParams);


        TextView icon =
                new TextView(this);

        icon.setText("📄");
        icon.setTextSize(24);
        icon.setGravity(Gravity.CENTER);

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


        TextView titleView =
                new TextView(this);

        titleView.setText(title);

        titleView.setTextColor(
                Color.rgb(0, 33, 71)
        );

        titleView.setTextSize(14);
        titleView.setTypeface(
                null,
                Typeface.BOLD
        );

        textContainer.addView(titleView);


        TextView categoryView =
                new TextView(this);

        String categoryText =
                category;

        if (program != null &&
                !program.trim().isEmpty()) {

            categoryText =
                    category +
                            " • " +
                            program;
        }

        categoryView.setText(
                categoryText
        );

        categoryView.setTextColor(
                Color.GRAY
        );

        categoryView.setTextSize(11);

        LinearLayout.LayoutParams catParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        catParams.setMargins(
                0,
                3,
                0,
                0
        );

        categoryView.setLayoutParams(
                catParams
        );

        textContainer.addView(categoryView);


        TextView fileView =
                new TextView(this);

        fileView.setText(
                fileName
        );

        fileView.setTextColor(
                Color.GRAY
        );

        fileView.setTextSize(10);

        textContainer.addView(fileView);

        card.addView(textContainer);


        documentsContainer.addView(card);
    }


    // =========================================================
    // UPLOAD DIALOG
    // =========================================================

    private void showUploadDialog() {

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        layout.setPadding(
                30,
                10,
                30,
                10
        );


        EditText titleInput =
                new EditText(this);

        titleInput.setHint(
                "Document title"
        );

        layout.addView(titleInput);


        EditText descriptionInput =
                new EditText(this);

        descriptionInput.setHint(
                "Description"
        );

        descriptionInput.setMinLines(2);

        layout.addView(descriptionInput);


        TextView categoryLabel =
                new TextView(this);

        categoryLabel.setText(
                "Category"
        );

        categoryLabel.setTextColor(
                Color.rgb(0, 33, 71)
        );

        categoryLabel.setTypeface(
                null,
                Typeface.BOLD
        );

        categoryLabel.setPadding(
                0,
                15,
                0,
                5
        );

        layout.addView(categoryLabel);


        Spinner categorySpinner =
                new Spinner(this);

        String[] categories = {

                "General Information",
                "Briefing Materials",
                "Placement Documents",
                "Insurance & Legal",
                "Reporting & Assessment",
                "Sample Documents"

        };

        ArrayAdapter<String> categoryAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        categories
                );

        categorySpinner.setAdapter(
                categoryAdapter
        );

        layout.addView(categorySpinner);


        TextView programLabel =
                new TextView(this);

        programLabel.setText(
                "Program (only for Placement Documents)"
        );

        programLabel.setTextColor(
                Color.rgb(0, 33, 71)
        );

        programLabel.setTypeface(
                null,
                Typeface.BOLD
        );

        programLabel.setPadding(
                0,
                15,
                0,
                5
        );

        layout.addView(programLabel);


        Spinner programSpinner =
                new Spinner(this);

        String[] programs = {

                "General",
                "CC101",
                "CM201",
                "CT204",
                "CT206"

        };

        ArrayAdapter<String> programAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        programs
                );

        programSpinner.setAdapter(
                programAdapter
        );

        layout.addView(programSpinner);


        Button chooseFile =
                new Button(this);

        chooseFile.setText(
                "Choose File"
        );

        chooseFile.setTextColor(
                Color.WHITE
        );

        chooseFile.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        Color.rgb(237, 28, 36)
                )
        );

        layout.addView(chooseFile);


        TextView selectedFile =
                new TextView(this);

        selectedFile.setText(
                "No file selected"
        );

        selectedFile.setTextColor(
                Color.GRAY
        );

        selectedFile.setPadding(
                0,
                8,
                0,
                8
        );

        layout.addView(selectedFile);


        chooseFile.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    Intent.ACTION_OPEN_DOCUMENT
                            );

                    intent.addCategory(
                            Intent.CATEGORY_OPENABLE
                    );

                    intent.setType(
                            "application/pdf"
                    );

                    startActivityForResult(
                            intent,
                            PICK_FILE
                    );

                }
        );


        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(
                                "Upload Document"
                        )
                        .setView(layout)
                        .setNegativeButton(
                                "Cancel",
                                null
                        )
                        .setPositiveButton(
                                "Upload",
                                null
                        )
                        .create();


        dialog.setOnShowListener(
                d -> {

                    Button uploadButton =
                            dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE
                            );

                    uploadButton.setTextColor(
                            Color.rgb(
                                    237,
                                    28,
                                    36
                            )
                    );


                    uploadButton.setOnClickListener(
                            v -> {

                                String title =
                                        titleInput
                                                .getText()
                                                .toString()
                                                .trim();

                                String description =
                                        descriptionInput
                                                .getText()
                                                .toString()
                                                .trim();

                                String category =
                                        categorySpinner
                                                .getSelectedItem()
                                                .toString();

                                String program =
                                        programSpinner
                                                .getSelectedItem()
                                                .toString();


                                if (title.isEmpty()) {

                                    titleInput.setError(
                                            "Enter document title"
                                    );

                                    return;
                                }


                                if (selectedFileUri == null) {

                                    Toast.makeText(
                                            this,
                                            "Please choose a PDF file",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    return;
                                }


                                if (!category.equals(
                                        "Placement Documents"
                                )) {

                                    program = "";
                                }


                                dialog.dismiss();

                                uploadDocument(
                                        selectedFileUri,
                                        title,
                                        description,
                                        category,
                                        program
                                );
                            }
                    );
                }
        );


        dialog.show();
    }


    // =========================================================
    // FILE RESULT
    // =========================================================

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (requestCode == PICK_FILE &&
                resultCode == RESULT_OK &&
                data != null) {

            selectedFileUri =
                    data.getData();

            Toast.makeText(
                    this,
                    "File selected",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    // =========================================================
    // UPLOAD DOCUMENT
    // =========================================================

    private void uploadDocument(
            Uri uri,
            String title,
            String description,
            String category,
            String program) {

        Toast.makeText(
                this,
                "Uploading document...",
                Toast.LENGTH_SHORT
        ).show();


        new Thread(() -> {

            try {

                ContentResolver resolver =
                        getContentResolver();

                InputStream inputStream =
                        resolver.openInputStream(uri);

                if (inputStream == null) {
                    throw new Exception(
                            "Unable to read file"
                    );
                }


                byte[] fileBytes =
                        readBytes(inputStream);

                inputStream.close();


                String originalName =
                        getFileName(uri);

                if (originalName == null ||
                        originalName.trim().isEmpty()) {

                    originalName =
                            "document.pdf";
                }


                String safeName =
                        originalName
                                .replaceAll(
                                        "[^a-zA-Z0-9._-]",
                                        "_"
                                );


                String filePath =
                        System.currentTimeMillis()
                                + "_" +
                                safeName;


                // ---------------------------------------------
                // UPLOAD TO STORAGE
                // ---------------------------------------------

                String storageUrl =
                        STORAGE_URL +
                                filePath;


                MediaType mediaType =
                        MediaType.parse(
                                "application/pdf"
                        );


                RequestBody requestBody =
                        RequestBody.create(
                                fileBytes,
                                mediaType
                        );


                Request storageRequest =
                        new Request.Builder()
                                .url(storageUrl)
                                .put(requestBody)
                                .addHeader(
                                        "apikey",
                                        SUPABASE_PUBLISHABLE_KEY
                                )
                                .addHeader(
                                        "Authorization",
                                        "Bearer " +
                                                SUPABASE_PUBLISHABLE_KEY
                                )
                                .addHeader(
                                        "Content-Type",
                                        "application/pdf"
                                )
                                .addHeader(
                                        "x-upsert",
                                        "false"
                                )
                                .build();


                Response storageResponse =
                        client.newCall(
                                storageRequest
                        ).execute();


                if (!storageResponse.isSuccessful()) {

                    throw new Exception(
                            "Storage upload failed: " +
                                    storageResponse.code()
                    );
                }


                // ---------------------------------------------
                // INSERT DATABASE RECORD
                // ---------------------------------------------

                JSONObject object =
                        new JSONObject();

                object.put(
                        "title",
                        title
                );

                object.put(
                        "description",
                        description
                );

                object.put(
                        "category",
                        category
                );

                if (program.isEmpty()) {

                    object.put(
                            "program",
                            JSONObject.NULL
                    );

                } else {

                    object.put(
                            "program",
                            program
                    );
                }

                object.put(
                        "file_name",
                        originalName
                );

                object.put(
                        "file_path",
                        filePath
                );


                RequestBody jsonBody =
                        RequestBody.create(
                                object.toString(),
                                MediaType.parse(
                                        "application/json"
                                )
                        );


                Request databaseRequest =
                        new Request.Builder()
                                .url(TABLE_URL)
                                .post(jsonBody)
                                .addHeader(
                                        "apikey",
                                        SUPABASE_PUBLISHABLE_KEY
                                )
                                .addHeader(
                                        "Authorization",
                                        "Bearer " +
                                                SUPABASE_PUBLISHABLE_KEY
                                )
                                .addHeader(
                                        "Content-Type",
                                        "application/json"
                                )
                                .addHeader(
                                        "Prefer",
                                        "return=minimal"
                                )
                                .build();


                Response databaseResponse =
                        client.newCall(
                                databaseRequest
                        ).execute();


                if (!databaseResponse.isSuccessful()) {

                    throw new Exception(
                            "Database insert failed: " +
                                    databaseResponse.code()
                    );
                }


                runOnUiThread(() -> {

                    Toast.makeText(
                            AdminDocumentsActivity.this,
                            "Document uploaded successfully!",
                            Toast.LENGTH_LONG
                    ).show();

                    selectedFileUri = null;

                    loadDocuments();
                });


            } catch (Exception e) {

                runOnUiThread(() -> {

                    Toast.makeText(
                            AdminDocumentsActivity.this,
                            "Upload failed: " +
                                    e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();

                });

            }

        }).start();
    }


    // =========================================================
    // READ FILE BYTES
    // =========================================================

    private byte[] readBytes(
            InputStream inputStream)
            throws IOException {

        java.io.ByteArrayOutputStream buffer =
                new java.io.ByteArrayOutputStream();

        byte[] data =
                new byte[8192];

        int nRead;

        while ((nRead =
                inputStream.read(data, 0, data.length))
                != -1) {

            buffer.write(
                    data,
                    0,
                    nRead
            );
        }

        return buffer.toByteArray();
    }


    // =========================================================
    // GET FILE NAME
    // =========================================================

    private String getFileName(Uri uri) {

        Cursor cursor =
                getContentResolver()
                        .query(
                                uri,
                                null,
                                null,
                                null,
                                null
                        );

        if (cursor != null) {

            int nameIndex =
                    cursor.getColumnIndex(
                            OpenableColumns.DISPLAY_NAME
                    );

            if (nameIndex >= 0 &&
                    cursor.moveToFirst()) {

                String name =
                        cursor.getString(
                                nameIndex
                        );

                cursor.close();

                return name;
            }

            cursor.close();
        }

        return null;
    }
}