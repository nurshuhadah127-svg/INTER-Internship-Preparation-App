package com.example.internshippreparation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "internprep.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Users table
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "student_id TEXT," +
                "email TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "role TEXT NOT NULL DEFAULT 'student')");

        // Checklist table
        db.execSQL("CREATE TABLE checklist (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "task TEXT NOT NULL, " +
                "completed INTEGER DEFAULT 0)");

        // Notes table
        db.execSQL("CREATE TABLE notes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "title TEXT, " +
                "content TEXT," +
                "date TEXT)"
                );

        // Announcements table
        db.execSQL("CREATE TABLE announcements (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "content TEXT, " +
                "date TEXT)");

        // Resources table
        db.execSQL("CREATE TABLE resources (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "description TEXT, " +
                "category TEXT)");

        // Downloads table
        db.execSQL("CREATE TABLE downloads (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "description TEXT, " +
                "file_url TEXT)");

        // Useful links table
        db.execSQL("CREATE TABLE useful_links (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "url TEXT NOT NULL)");
    }


    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        if (oldVersion < 2) {

            db.execSQL(
                    "ALTER TABLE notes ADD COLUMN date TEXT"
            );
        }
    }


    // ============================================================
    // REGISTER NEW STUDENT
    // ============================================================

    public boolean registerUser(
            String name,
            String studentId,
            String email,
            String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("student_id", studentId);
        values.put("email", email);
        values.put("password", password);
        values.put("role", "student");

        long result = db.insert(
                "users",
                null,
                values
        );

        return result != -1;
    }


    // ============================================================
    // CREATE DEFAULT ADMIN ACCOUNT
    // ============================================================

    public void createDefaultAdmin() {

        SQLiteDatabase db = this.getWritableDatabase();

        String adminEmail = "fcomadmin@gmail.com";
        String adminPassword = "admin123";

        // Remove existing admin account
        db.delete(
                "users",
                "email = ?",
                new String[]{adminEmail}
        );

        // Create admin account
        ContentValues values = new ContentValues();

        values.put("name", "FCOM Admin");

        // Admin does not need Student ID
        values.putNull("student_id");

        values.put("email", adminEmail);
        values.put("password", adminPassword);
        values.put("role", "admin");

        db.insert(
                "users",
                null,
                values
        );
    }


    // ============================================================
    // LOGIN
    // ============================================================

    public Cursor loginUser(
            String email,
            String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM users WHERE email = ? AND password = ?",
                new String[]{
                        email,
                        password
                }
        );
    }


    // ============================================================
    // CHECK EMAIL
    // ============================================================

    public boolean emailExists(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM users WHERE email = ?",
                new String[]{email}
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();

        return exists;
    }


    // ============================================================
    // GET USER BY EMAIL
    // ============================================================

    public Cursor getUserByEmail(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM users WHERE email = ?",
                new String[]{email}
        );
    }


    // ============================================================
    // GET USER BY ID
    // ============================================================

    public Cursor getUserById(int userId) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT id, name, student_id, email, role " +
                        "FROM users WHERE id = ?",
                new String[]{
                        String.valueOf(userId)
                }
        );
    }


    // ============================================================
    // ADD PERSONAL NOTE
    // ============================================================

    public boolean addNote(
            int userId,
            String title,
            String content,
            String date) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("user_id", userId);
        values.put("title", title);
        values.put("content", content);
        values.put("date", date);

        long result = db.insert(
                "notes",
                null,
                values
        );

        return result != -1;
    }

    public boolean updateNote(
            int noteId,
            String title,
            String content,
            String date) {

        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("content", content);
        values.put("date", date);

        return getWritableDatabase().update(
                "notes",
                values,
                "id=?",
                new String[]{
                        String.valueOf(noteId)
                }
        ) > 0;
    }


    public boolean deleteNote(int noteId) {

        return getWritableDatabase().delete(
                "notes",
                "id=?",
                new String[]{
                        String.valueOf(noteId)
                }
        ) > 0;
    }

    // ============================================================
    // GET PERSONAL NOTES
    // ============================================================

    public ArrayList<String[]> getNotes(int userId) {

        ArrayList<String[]> notes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, title, content, date " +
                        " FROM notes " +
                        "WHERE user_id = ? " +
                        "ORDER BY id DESC",
                new String[]{
                        String.valueOf(userId)
                }
        );

        while (cursor.moveToNext()) {

            String id = cursor.getString(
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

            notes.add(new String[]{
                    id,
                    title,
                    content,
                    date
            });
        }

        cursor.close();

        return notes;
    }

    // =========================================================
// ANNOUNCEMENTS
// =========================================================

    public Cursor getRows(String table) {

        return getReadableDatabase().query(
                table,
                null,
                null,
                null,
                null,
                null,
                "id DESC"
        );
    }


    public boolean addAnnouncement(
            String title,
            String content,
            String date
    ) {

        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("content", content);
        values.put("date", date);

        return getWritableDatabase().insert(
                "announcements",
                null,
                values
        ) != -1;
    }


    public boolean updateAnnouncement(
            int id,
            String title,
            String content,
            String date
    ) {

        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("content", content);
        values.put("date", date);

        return getWritableDatabase().update(
                "announcements",
                values,
                "id=?",
                new String[]{
                        String.valueOf(id)
                }
        ) > 0;
    }

    public boolean deleteAnnouncement(int id) {

        return getWritableDatabase().delete(
                "announcements",
                "id=?",
                new String[]{
                        String.valueOf(id)
                }
        ) > 0;
    }

    public boolean addResource(
            String title,
            String description
    ) {

        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("description", description);

        return getWritableDatabase().insert(
                "resources",
                null,
                values
        ) != -1;
    }

    public boolean updateResource(
            int id,
            String title,
            String description
    ) {

        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("description", description);

        return getWritableDatabase().update(
                "resources",
                values,
                "id=?",
                new String[]{
                        String.valueOf(id)
                }
        ) > 0;
    }

    public boolean deleteResource(int id) {

        return getWritableDatabase().delete(
                "resources",
                "id=?",
                new String[]{
                        String.valueOf(id)
                }
        ) > 0;
    }

    public boolean addLink(
            String title,
            String url
    ) {

        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("url", url);

        return getWritableDatabase().insert(
                "useful_links",
                null,
                values
        ) != -1;
    }


    public boolean updateLink(
            int id,
            String title,
            String url
    ) {

        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("url", url);

        return getWritableDatabase().update(
                "useful_links",
                values,
                "id=?",
                new String[]{
                        String.valueOf(id)
                }
        ) > 0;
    }


    public boolean deleteLink(int id) {

        return getWritableDatabase().delete(
                "useful_links",
                "id=?",
                new String[]{
                        String.valueOf(id)
                }
        ) > 0;
    }

    public boolean verifyUserForPasswordReset(String email, String studentId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM users WHERE email = ? AND student_id = ? AND role = 'student'",
                new String[]{email, studentId}
        );

        boolean exists = cursor.moveToFirst();

        cursor.close();

        return exists;
    }


    public boolean updatePassword(String email, String newPassword) {

        ContentValues values = new ContentValues();

        values.put("password", newPassword);

        return getWritableDatabase().update(
                "users",
                values,
                "email = ?",
                new String[]{email}
        ) > 0;
    }
}