package com.example.internshippreparation;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DocumentListActivity extends AppCompatActivity {

    private TextView btnBack;
    private TextView tvCategoryTitle;
    private TextView tvCategorySubtitle;

    private EditText etSearch;

    private LinearLayout documentsContainer;

    private String category;
    private String program;

    private boolean isGeneralPlacement;

    private SupabaseManager supabaseManager;

    private final List<DocumentItem> documentList =
            new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_document_list
        );


        // =====================================================
        // CONNECT XML
        // =====================================================

        btnBack =
                findViewById(R.id.btnBack);

        tvCategoryTitle =
                findViewById(R.id.tvCategoryTitle);

        tvCategorySubtitle =
                findViewById(R.id.tvCategorySubtitle);

        etSearch =
                findViewById(R.id.etSearch);

        documentsContainer =
                findViewById(R.id.documentsContainer);


        // =====================================================
        // SUPABASE
        // =====================================================

        supabaseManager =
                new SupabaseManager();


        // =====================================================
        // GET CATEGORY
        // =====================================================

        category =
                getIntent().getStringExtra(
                        "CATEGORY"
                );


        if (category == null ||
                category.trim().isEmpty()) {

            category =
                    "General Information";
        }


        // =====================================================
        // GET PROGRAM
        // =====================================================

        program =
                getIntent().getStringExtra(
                        "PROGRAM"
                );


        // =====================================================
        // CHECK GENERAL PLACEMENT
        // =====================================================

        isGeneralPlacement =
                getIntent().getBooleanExtra(
                        "GENERAL_PLACEMENT",
                        false
                );


        // =====================================================
        // HEADER
        // =====================================================

        if (isGeneralPlacement) {

            tvCategoryTitle.setText(
                    "General Placement Documents"
            );

            tvCategorySubtitle.setText(
                    "Documents for all students"
            );

        } else if (program != null &&
                !program.trim().isEmpty()) {

            tvCategoryTitle.setText(
                    program
            );

            tvCategorySubtitle.setText(
                    "Placement documents for " +
                            program +
                            " students"
            );

        } else {

            tvCategoryTitle.setText(
                    category
            );

            tvCategorySubtitle.setText(
                    "Industrial training documents"
            );
        }


        // =====================================================
        // BACK
        // =====================================================

        btnBack.setOnClickListener(
                v -> finish()
        );


        // =====================================================
        // SEARCH
        // =====================================================

        etSearch.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }


                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        searchDocuments(
                                s.toString()
                        );

                    }


                    @Override
                    public void afterTextChanged(
                            Editable s) {
                    }

                }
        );


        // =====================================================
        // LOAD FROM SUPABASE
        // =====================================================

        loadDocumentsFromSupabase();

    }


    // =========================================================
    // LOAD DOCUMENTS FROM SUPABASE
    // =========================================================

    private void loadDocumentsFromSupabase() {

        documentsContainer.removeAllViews();


        TextView loading =
                new TextView(this);

        loading.setText(
                "Loading documents..."
        );

        loading.setTextSize(14);

        loading.setTextColor(
                Color.GRAY
        );

        loading.setGravity(
                Gravity.CENTER
        );

        loading.setPadding(
                0,
                dpToPx(40),
                0,
                dpToPx(40)
        );


        documentsContainer.addView(
                loading
        );


        supabaseManager.getDocuments(
                new SupabaseManager.DocumentsCallback() {

                    @Override
                    public void onSuccess(
                            JSONArray documents) {

                        runOnUiThread(() -> {

                            documentList.clear();


                            try {

                                for (
                                        int i = 0;
                                        i < documents.length();
                                        i++
                                ) {

                                    JSONObject object =
                                            documents.getJSONObject(i);


                                    // =====================================
                                    // GET DATA
                                    // =====================================

                                    String title =
                                            object.optString(
                                                    "title",
                                                    ""
                                            );


                                    String description =
                                            object.optString(
                                                    "description",
                                                    ""
                                            );


                                    String documentCategory =
                                            object.optString(
                                                    "category",
                                                    ""
                                            );


                                    String documentProgram =
                                            object.optString(
                                                    "program",
                                                    ""
                                            );


                                    String fileName =
                                            object.optString(
                                                    "file_name",
                                                    ""
                                            );


                                    String filePath =
                                            object.optString(
                                                    "file_path",
                                                    ""
                                            );


                                    String fileUrl =
                                            object.optString(
                                                    "file_url",
                                                    ""
                                            );


                                    // =====================================
                                    // CHECK CATEGORY
                                    // =====================================

                                    boolean categoryMatches =
                                            documentCategory != null
                                                    && category != null
                                                    && documentCategory.trim()
                                                    .equalsIgnoreCase(category.trim());


                                    // =====================================
                                    // CHECK PROGRAM
                                    // =====================================

                                    boolean programMatches;


                                    if (isGeneralPlacement) {

                                        // General Placement Documents:
                                        // program must be empty / NULL
                                        programMatches =
                                                documentProgram == null
                                                        || documentProgram.trim().isEmpty()
                                                        || documentProgram.equalsIgnoreCase("NULL");

                                    } else if (
                                            program != null &&
                                                    !program
                                                            .trim()
                                                            .isEmpty()
                                    ) {

                                        // Specific programme:
                                        // must match selected programme
                                        programMatches =
                                                documentProgram
                                                        .equalsIgnoreCase(
                                                                program
                                                        );

                                    } else {

                                        // Other categories:
                                        // no programme filtering
                                        programMatches = true;
                                    }


                                    // =====================================
                                    // ADD DOCUMENT
                                    // =====================================

                                    if (
                                            categoryMatches &&
                                                    programMatches
                                    ) {


                                        // If file_url is empty,
                                        // create public Storage URL

                                        if (
                                                fileUrl == null ||
                                                        fileUrl
                                                                .trim()
                                                                .isEmpty()
                                        ) {

                                            fileUrl =
                                                    supabaseManager
                                                            .getPublicFileUrl(
                                                                    filePath
                                                            );

                                        }


                                        // Get file type

                                        String fileType =
                                                getFileType(
                                                        fileName,
                                                        filePath
                                                );


                                        documentList.add(
                                                new DocumentItem(
                                                        title,
                                                        description,
                                                        fileType,
                                                        fileUrl
                                                )
                                        );

                                    }

                                }


                                displayDocuments(
                                        documentList
                                );


                            } catch (Exception e) {

                                Toast.makeText(
                                        DocumentListActivity.this,
                                        "Error reading documents",
                                        Toast.LENGTH_SHORT
                                ).show();


                                displayDocuments(
                                        new ArrayList<>()
                                );

                            }

                        });

                    }


                    @Override
                    public void onError(
                            String error) {

                        runOnUiThread(() -> {

                            documentsContainer
                                    .removeAllViews();


                            TextView errorText =
                                    new TextView(
                                            DocumentListActivity.this
                                    );


                            errorText.setText(
                                    "Unable to load documents.\n\n"
                                            + error
                            );


                            errorText.setTextSize(
                                    14
                            );


                            errorText.setTextColor(
                                    Color.GRAY
                            );


                            errorText.setGravity(
                                    Gravity.CENTER
                            );


                            errorText.setPadding(
                                    dpToPx(20),
                                    dpToPx(40),
                                    dpToPx(20),
                                    dpToPx(40)
                            );


                            documentsContainer.addView(
                                    errorText
                            );


                            Toast.makeText(
                                    DocumentListActivity.this,
                                    "Supabase connection failed",
                                    Toast.LENGTH_SHORT
                            ).show();

                        });

                    }

                }
        );

    }


    // =========================================================
    // DISPLAY DOCUMENTS
    // =========================================================

    private void displayDocuments(
            List<DocumentItem> documents) {

        documentsContainer.removeAllViews();


        if (documents.isEmpty()) {

            TextView emptyText =
                    new TextView(this);


            emptyText.setText(
                    "No documents available."
            );


            emptyText.setTextColor(
                    Color.GRAY
            );


            emptyText.setTextSize(
                    14
            );


            emptyText.setGravity(
                    Gravity.CENTER
            );


            emptyText.setPadding(
                    0,
                    dpToPx(50),
                    0,
                    dpToPx(50)
            );


            documentsContainer.addView(
                    emptyText
            );


            return;
        }


        for (
                DocumentItem document :
                documents
        ) {

            addDocumentCard(
                    document
            );

        }

    }


    // =========================================================
    // DOCUMENT CARD
    // =========================================================

    private void addDocumentCard(
            DocumentItem document) {


        LinearLayout card =
                new LinearLayout(this);


        card.setOrientation(
                LinearLayout.VERTICAL
        );


        card.setPadding(
                dpToPx(12),
                dpToPx(12),
                dpToPx(12),
                dpToPx(12)
        );


        card.setBackgroundResource(
                R.drawable.bg_document_item
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
                dpToPx(12)
        );


        card.setLayoutParams(
                cardParams
        );


        // =====================================================
        // TOP ROW
        // =====================================================

        LinearLayout topRow =
                new LinearLayout(this);


        topRow.setOrientation(
                LinearLayout.HORIZONTAL
        );


        topRow.setGravity(
                Gravity.CENTER_VERTICAL
        );


        // =====================================================
        // FILE TYPE
        // =====================================================

        TextView fileType =
                new TextView(this);


        fileType.setText(
                document.fileType
        );


        fileType.setTextColor(
                Color.WHITE
        );


        fileType.setTextSize(
                10
        );


        fileType.setTypeface(
                null,
                Typeface.BOLD
        );


        fileType.setGravity(
                Gravity.CENTER
        );


        fileType.setBackgroundResource(
                R.drawable.bg_document_icon
        );


        LinearLayout.LayoutParams fileTypeParams =
                new LinearLayout.LayoutParams(
                        dpToPx(48),
                        dpToPx(48)
                );


        fileType.setLayoutParams(
                fileTypeParams
        );


        topRow.addView(
                fileType
        );


        // =====================================================
        // TEXT CONTAINER
        // =====================================================

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
                dpToPx(12),
                0,
                dpToPx(8),
                0
        );


        textContainer.setLayoutParams(
                textParams
        );


        // =====================================================
        // TITLE
        // =====================================================

        TextView title =
                new TextView(this);


        title.setText(
                document.title
        );


        title.setTextColor(
                Color.rgb(
                        0,
                        33,
                        71
                )
        );


        title.setTextSize(
                15
        );


        title.setTypeface(
                null,
                Typeface.BOLD
        );


        title.setMaxLines(
                2
        );


        textContainer.addView(
                title
        );


        // =====================================================
        // DESCRIPTION
        // =====================================================

        TextView description =
                new TextView(this);


        description.setText(
                document.description
        );


        description.setTextColor(
                Color.rgb(
                        117,
                        117,
                        117
                )
        );


        description.setTextSize(
                11
        );


        description.setMaxLines(
                2
        );


        LinearLayout.LayoutParams descriptionParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );


        descriptionParams.setMargins(
                0,
                dpToPx(3),
                0,
                0
        );


        description.setLayoutParams(
                descriptionParams
        );


        textContainer.addView(
                description
        );


        topRow.addView(
                textContainer
        );


        card.addView(
                topRow
        );


        // =====================================================
        // VIEW / DOWNLOAD BUTTON
        // =====================================================

        Button viewButton =
                new Button(this);


        viewButton.setText(
                "View / Download"
        );


        viewButton.setTextColor(
                Color.WHITE
        );


        viewButton.setTextSize(
                13
        );


        viewButton.setTypeface(
                null,
                Typeface.BOLD
        );


        viewButton.setAllCaps(
                false
        );


        viewButton.setGravity(
                Gravity.CENTER
        );


        viewButton.setPadding(
                dpToPx(10),
                0,
                dpToPx(10),
                0
        );


        viewButton.setBackgroundResource(
                R.drawable.bg_document_button
        );


        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dpToPx(45)
                );


        buttonParams.setMargins(
                0,
                dpToPx(12),
                0,
                0
        );


        viewButton.setLayoutParams(
                buttonParams
        );


        viewButton.setOnClickListener(v -> {

            openDocument(
                    document.fileUrl,
                    document.title
            );

        });


        card.addView(
                viewButton
        );


        documentsContainer.addView(
                card
        );

    }


    // =========================================================
    // OPEN DOCUMENT
    // =========================================================

    private void openDocument(
            String fileUrl,
            String title) {


        if (
                fileUrl == null ||
                        fileUrl.trim().isEmpty()
        ) {

            Toast.makeText(
                    this,
                    "File is not available yet.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        try {

            Intent intent =
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(fileUrl)
                    );


            startActivity(
                    intent
            );

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Unable to open " + title,
                    Toast.LENGTH_SHORT
            ).show();

        }

    }

    // =========================================================
    // SEARCH
    // =========================================================

    private void searchDocuments(
            String query) {


        String searchText =
                query.toLowerCase().trim();


        List<DocumentItem> filtered =
                new ArrayList<>();


        for (
                DocumentItem document :
                documentList
        ) {


            if (
                    document.title
                            .toLowerCase()
                            .contains(searchText)

                            ||

                            document.description
                                    .toLowerCase()
                                    .contains(searchText)
            ) {

                filtered.add(
                        document
                );

            }

        }


        displayDocuments(
                filtered
        );

    }

    // =========================================================
    // GET FILE TYPE
    // =========================================================

    private String getFileType(
            String fileName,
            String filePath) {


        String file =
                fileName;


        if (
                file == null ||
                        file.trim().isEmpty()
        ) {

            file =
                    filePath;

        }


        if (
                file == null ||
                        file.trim().isEmpty()
        ) {

            return "FILE";

        }


        file =
                file.toLowerCase();


        if (file.endsWith(".pdf")) {

            return "PDF";

        }


        if (file.endsWith(".docx")) {

            return "DOCX";

        }


        if (file.endsWith(".doc")) {

            return "DOC";

        }


        if (file.endsWith(".pptx")) {

            return "PPT";

        }


        if (file.endsWith(".ppt")) {

            return "PPT";

        }


        if (file.endsWith(".xlsx")) {

            return "XLSX";

        }


        if (file.endsWith(".xls")) {

            return "XLS";

        }


        if (file.endsWith(".mp4")) {

            return "MP4";

        }


        return "FILE";

    }


    // =========================================================
    // DP TO PX
    // =========================================================

    private int dpToPx(
            int dp) {


        float density =
                getResources()
                        .getDisplayMetrics()
                        .density;


        return Math.round(
                dp * density
        );

    }


    // =========================================================
    // DOCUMENT ITEM
    // =========================================================

    private static class DocumentItem {

        String title;

        String description;

        String fileType;

        String fileUrl;


        DocumentItem(
                String title,
                String description,
                String fileType,
                String fileUrl) {

            this.title =
                    title;

            this.description =
                    description;

            this.fileType =
                    fileType;

            this.fileUrl =
                    fileUrl;

        }

    }

}