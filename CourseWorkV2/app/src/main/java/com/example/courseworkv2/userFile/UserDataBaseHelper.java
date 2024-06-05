package com.example.courseworkv2.userFile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserDataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UsersDataBase.db"; //название бд
    private static final int DATABASE_VERSION = 1; //версия бд
    private static final String TABLE_NAME = "UsersData";  //Имя БД
    private Context context;

    private static final String COLUMN_ID_TABLE = "_id";
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_PASSWORD = "pasword";
    private static final String COLUMN_STATUS = "adminStatus";

    public UserDataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID_TABLE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LOGIN + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_STATUS + " BIN);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LOGIN, user.getLogin());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        cv.put(COLUMN_STATUS, user.getIsAdmin());
        long result = db.insert(TABLE_NAME, null, cv);
        db.close();
        return result != -1;
    }


    public boolean deleteUser(String login) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_LOGIN + " =?", new String[]{login});
        db.close();
        return result > 0;
    }

    public Boolean userInBase(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_LOGIN, COLUMN_PASSWORD, COLUMN_STATUS},
                COLUMN_LOGIN + " =?", new String[]{login}, null,
                null, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return false;
    }

    public boolean updateUserPasword(String login, String newPasword, boolean adminStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LOGIN, login);
        cv.put(COLUMN_PASSWORD, newPasword);
        cv.put(COLUMN_STATUS, adminStatus);
        // Обновляем запись, где номер телефона равен oldPhone
        int result = db.update(TABLE_NAME, cv, COLUMN_LOGIN + " = ?", new String[]{login});
        db.close();
        return result > 0;
    }

    public String getPassword(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID_TABLE, COLUMN_LOGIN, COLUMN_PASSWORD, COLUMN_STATUS},
                COLUMN_LOGIN + " =?", new String[]{login}, null,
                null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String password = cursor.getString(2);
            cursor.close();
            db.close();
            return password;
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }

    public Boolean getStatus(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID_TABLE, COLUMN_LOGIN, COLUMN_PASSWORD, COLUMN_STATUS},
                COLUMN_LOGIN + " =?", new String[]{login}, null,
                null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int status = cursor.getInt(3);
            cursor.close();
            db.close();
            if (status == 1)
                return true;
            else
                return false;
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }

    public boolean isDatabaseEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT EXISTS (SELECT 1 FROM " + TABLE_NAME + ") AS result", null);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        cursor.close();
        return result == 0;
    }


}
