package com.example.courseworkv2.workerFile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class WorkerDataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "WorkersDataBase.db"; //название бд
    private static final int DATABASE_VERSION = 1; //версия бд
    private static final String TABLE_NAME = "WorkersData";  //Имя БД
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SECOND_NAME = "second_name";
    private static final String COLUMN_FATHER_NAME = "father_name";
    private static final String COLUMN_PHONE_NUMBER ="phone_number" ;
    private static final String COLUMN_MAIL ="mail" ;
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
    private Context context;

    private static final String COLUMN_ID_TABLE = "_id";
    public WorkerDataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID_TABLE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_SECOND_NAME + " TEXT, " +
                COLUMN_FATHER_NAME + " TEXT, " +
                COLUMN_PHONE_NUMBER + " TEXT, " +
                COLUMN_MAIL + " TEXT, " +
                COLUMN_AGE + " INTEGER, " +
                COLUMN_DATE_OF_BIRTH + " TEXT);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addWorker(Worker worker) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, worker.getName());
        cv.put(COLUMN_SECOND_NAME, worker.getSecondName());
        cv.put(COLUMN_FATHER_NAME, worker.getFatherName());
        cv.put(COLUMN_PHONE_NUMBER, worker.getPhoneNumber());
        cv.put(COLUMN_MAIL, worker.getMail());
        cv.put(COLUMN_AGE, worker.getAge());
        cv.put(COLUMN_DATE_OF_BIRTH, worker.getDateOfBirth());
        long result = db.insert(TABLE_NAME, null, cv);
        db.close();
        return result != -1;
    }

    public boolean deleteWorker(String name,String secondName,String fatherName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME,
                COLUMN_NAME + " =? AND " + COLUMN_SECOND_NAME + " =? AND " + COLUMN_FATHER_NAME + " =?",
                new String[]{name,secondName,fatherName});
        db.close();
        return result > 0;
    }

    public  ArrayList<Worker> getAllWorker() {
        ArrayList<Worker> workerList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                Worker student = new Worker(cursor.getString(1),
                        cursor.getString(3),
                        cursor.getString(2),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getInt(6),
                        cursor.getString(7));
                workerList.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return workerList;
    }
    public boolean workerInBase(String name,String secondName,String fatherName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NAME, COLUMN_SECOND_NAME,
                        COLUMN_FATHER_NAME, COLUMN_PHONE_NUMBER, COLUMN_MAIL, COLUMN_AGE, COLUMN_DATE_OF_BIRTH},
                COLUMN_NAME + " =? AND " + COLUMN_SECOND_NAME + " =? AND " + COLUMN_FATHER_NAME + " =?",
                new String[]{name, secondName, fatherName}, null, null, null);
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
    public boolean updateWorker(Worker worker) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, worker.getName());
        cv.put(COLUMN_SECOND_NAME, worker.getSecondName());
        cv.put(COLUMN_FATHER_NAME, worker.getFatherName());
        cv.put(COLUMN_PHONE_NUMBER, worker.getPhoneNumber());
        cv.put(COLUMN_MAIL, worker.getMail());
        cv.put(COLUMN_AGE, worker.getAge());
        cv.put(COLUMN_DATE_OF_BIRTH, worker.getDateOfBirth());
        // Обновляем запись, где номер телефона равен oldPhone
        int result = db.update(TABLE_NAME, cv, COLUMN_NAME + " =? AND " + COLUMN_SECOND_NAME + " =? AND " + COLUMN_FATHER_NAME + " =?",
                new String[]{worker.getName(), worker.getSecondName(),  worker.getFatherName()});
        db.close();
        return result > 0;
    }
    public  ArrayList<Worker> getAllWorkerByName(String name) {
        ArrayList<Worker> workerList = new ArrayList<>();
        workerList = getAllWorker();
        ArrayList<Worker> workerListByNames = new ArrayList<>();
        for (int i = 0 ; i < workerList.size();i++){
            if (workerList.get(i).getName().equals(name))
                workerListByNames.add(workerList.get(i));
        }
        return workerListByNames;
    }
    public  ArrayList<Worker> getAllWorkerBySecondName(String secondName) {
        ArrayList<Worker> workerList = new ArrayList<>();
        workerList = getAllWorker();
        ArrayList<Worker> workerListByNames = new ArrayList<>();
        for (int i = 0 ; i < workerList.size();i++){
            if (workerList.get(i).getSecondName().equals(secondName))
                workerListByNames.add(workerList.get(i));
        }
        return workerListByNames;
    }
    public  ArrayList<Worker> getAllWorkerByFatherName(String fatherName) {
        ArrayList<Worker> workerList = new ArrayList<>();
        workerList = getAllWorker();
        ArrayList<Worker> workerListByNames = new ArrayList<>();
        for (int i = 0 ; i < workerList.size();i++){
            if (workerList.get(i).getFatherName().equals(fatherName))
                workerListByNames.add(workerList.get(i));
        }
        return workerListByNames;
    }
    public  ArrayList<Worker> getAllWorkerByFullName(String name,String secondName, String fatherName) {
        ArrayList<Worker> workerList = new ArrayList<>();
        workerList = getAllWorker();
        ArrayList<Worker> workerListByNames = new ArrayList<>();
        for (int i = 0 ; i < workerList.size();i++){
            if (workerList.get(i).getFatherName().equals(fatherName) && workerList.get(i).getSecondName().equals(secondName) && workerList.get(i).getName().equals(name))
                workerListByNames.add(workerList.get(i));
        }
        return workerListByNames;
    }
}
