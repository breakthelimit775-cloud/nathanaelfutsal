package com.example.nathanaelfutsal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "nathanaelfutsal.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_SCHEDULE = "jadwal";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JAM = "jam";
    public static final String COLUMN_STATUS = "status";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_SCHEDULE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_JAM + " TEXT,"
                + COLUMN_STATUS + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_QUERY);

        isiDataAwal(db);
    }

    private void isiDataAwal(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_JAM, "08:00 - 09:00");
        values.put(COLUMN_STATUS, "dipesan");
        db.insert(TABLE_SCHEDULE, null, values);

        values.put(COLUMN_JAM, "09:00 - 10:00");
        values.put(COLUMN_STATUS, "tersedia");
        db.insert(TABLE_SCHEDULE, null, values);

        values.put(COLUMN_JAM, "10:00 - 11:00");
        values.put(COLUMN_STATUS, "tersedia");
        db.insert(TABLE_SCHEDULE, null, values);

        values.put(COLUMN_JAM, "11:00 - 12:00");
        values.put(COLUMN_STATUS, "dipesan");
        db.insert(TABLE_SCHEDULE, null, values);

        values.put(COLUMN_JAM, "13:00 - 14:00");
        values.put(COLUMN_STATUS, "tersedia");
        db.insert(TABLE_SCHEDULE, null, values);

        values.put(COLUMN_JAM, "14:00 - 15:00");
        values.put(COLUMN_STATUS, "tersedia");
        db.insert(TABLE_SCHEDULE, null, values);

        values.put(COLUMN_JAM, "15:00 - 16:00");
        values.put(COLUMN_STATUS, "dipesan");
        db.insert(TABLE_SCHEDULE, null, values);

        values.put(COLUMN_JAM, "16:00 - 17:00");
        values.put(COLUMN_STATUS, "tersedia");
        db.insert(TABLE_SCHEDULE, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
        onCreate(db);
    }

    public ArrayList<JadwalModel> getAllSchedules() {
        ArrayList<JadwalModel> scheduleList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SCHEDULE, null);
        if (cursor.moveToFirst()) {
            do {
                String jam = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JAM));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));
                scheduleList.add(new JadwalModel(jam, status));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return scheduleList;
    }
    public void updateScheduleStatus(String jam, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, newStatus);
        db.update(TABLE_SCHEDULE, values, COLUMN_JAM + " = ?", new String[]{jam});
        db.close();
    }
}