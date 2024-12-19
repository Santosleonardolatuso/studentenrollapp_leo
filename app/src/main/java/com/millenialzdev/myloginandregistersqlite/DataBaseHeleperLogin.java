package com.millenialzdev.myloginandregistersqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHeleperLogin extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DB_ENROLLMENT";

    public DataBaseHeleperLogin(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabel untuk user
        db.execSQL("CREATE TABLE Users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)");

        // Tabel untuk mata kuliah
        db.execSQL("CREATE TABLE Subjects (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, credits INTEGER)");

        // Tabel untuk pendaftaran
        db.execSQL("CREATE TABLE Enrollments (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, subject_id INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES Users(id), FOREIGN KEY(subject_id) REFERENCES Subjects(id))");

        // Tambahkan data default ke tabel Subjects
        db.execSQL("INSERT INTO Subjects(name, credits) VALUES ('Mathematics', 3), ('Physics', 4), ('Chemistry', 4), " +
                "('Computer Science', 3), ('Biology', 3)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Subjects");
        db.execSQL("DROP TABLE IF EXISTS Enrollments");
        onCreate(db);
    }

    // Simpan user baru
    public boolean simpanUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        long insert = db.insert("Users", null, values);
        return insert != -1;
    }

    // Cek login
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE username = ? AND password = ?", new String[]{username, password});
        return cursor.getCount() > 0;
    }

    // Tambahkan pendaftaran mata kuliah
    public boolean addEnrollment(int userId, int subjectId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Cek total SKS yang sudah diambil
        String query = "SELECT SUM(Subjects.credits) AS total_credits " +
                "FROM Enrollments INNER JOIN Subjects ON Enrollments.subject_id = Subjects.id " +
                "WHERE Enrollments.user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        int totalCredits = 0;
        if (cursor.moveToFirst()) {
            totalCredits = cursor.getInt(cursor.getColumnIndexOrThrow("total_credits"));
        }
        cursor.close();

        // Maksimal SKS adalah 24
        if (totalCredits + getCreditsBySubject(subjectId) > 24) {
            return false; // Tidak bisa menambah
        }

        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("subject_id", subjectId);
        long insert = db.insert("Enrollments", null, values);
        return insert != -1;
    }

    // Ambil SKS mata kuliah berdasarkan ID
    public int getCreditsBySubject(int subjectId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT credits FROM Subjects WHERE id = ?", new String[]{String.valueOf(subjectId)});
        int credits = 0;
        if (cursor.moveToFirst()) {
            credits = cursor.getInt(cursor.getColumnIndexOrThrow("credits"));
        }
        cursor.close();
        return credits;
    }

    // Ambil ringkasan mata kuliah yang diambil user
    public Cursor getEnrollmentSummary(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Subjects.name, Subjects.credits " +
                "FROM Enrollments INNER JOIN Subjects ON Enrollments.subject_id = Subjects.id " +
                "WHERE Enrollments.user_id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }
}
