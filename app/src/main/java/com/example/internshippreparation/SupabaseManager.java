package com.example.internshippreparation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SupabaseManager {

    private static final String SUPABASE_URL =
            "https://ssukmsmrmtvqatqaaucl.supabase.co";

    private static final String SUPABASE_PUBLISHABLE_KEY =
            "sb_publishable_T07KjnVeT1IUOi-S93ZsFA_SPA9DzPe";

    private static final String TABLE_URL =
            SUPABASE_URL + "/rest/v1/documents";

    private static final String STORAGE_URL =
            SUPABASE_URL + "/storage/v1/object/public/documents/";

    private final OkHttpClient client;

    public SupabaseManager() {

        client = new OkHttpClient();

    }


    // =========================================================
    // GET ALL DOCUMENTS
    // =========================================================

    public void getDocuments(
            final DocumentsCallback callback) {

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

                        callback.onError(
                                e.getMessage()
                        );

                    }


                    @Override
                    public void onResponse(
                            Call call,
                            Response response)
                            throws IOException {

                        if (!response.isSuccessful()) {

                            callback.onError(
                                    "Supabase error: " +
                                            response.code()
                            );

                            return;
                        }


                        if (response.body() == null) {

                            callback.onError(
                                    "Empty response from Supabase"
                            );

                            return;
                        }


                        String responseBody =
                                response.body().string();


                        try {

                            JSONArray jsonArray =
                                    new JSONArray(
                                            responseBody
                                    );


                            callback.onSuccess(
                                    jsonArray
                            );

                        } catch (Exception e) {

                            callback.onError(
                                    "Invalid Supabase response"
                            );

                        }

                    }

                }
        );

    }


    // =========================================================
    // CREATE PUBLIC FILE URL
    // =========================================================

    public String getPublicFileUrl(
            String filePath) {

        if (filePath == null ||
                filePath.trim().isEmpty()) {

            return "";

        }


        return STORAGE_URL +
                filePath;
    }


    // =========================================================
    // CALLBACK
    // =========================================================

    public interface DocumentsCallback {

        void onSuccess(
                JSONArray documents
        );


        void onError(
                String error
        );

    }

}