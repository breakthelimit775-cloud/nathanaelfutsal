package com.example.nathanaelfutsal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "futsal_booking.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_SCHEDULES = "schedules";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JAM = "jam";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_BUKTI = "bukti_transfer";
    public static final String COLUMN_USER_EMAIL = "user_email";

    private static final String TABLE_CREATE_SCHEDULES =
            "CREATE TABLE " + TABLE_SCHEDULES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_JAM + " TEXT, " +
                    COLUMN_STATUS + " TEXT, " +
                    COLUMN_BUKTI + " TEXT, " +
                    COLUMN_USER_EMAIL + " TEXT" +
                    ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_SCHEDULES);
        initializeSchedules(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULES);
        onCreate(db);
    }

    private void initializeSchedules(SQLiteDatabase db) {
        for (int i = 8; i <= 23; i++) {
            String jamMulai = String.format("%02d:00", i);
            String jamSelesai = String.format("%02d:00", i + 1);
            String jamSlot = jamMulai + " - " + jamSelesai;
            insertSchedule(db, jamSlot, "available", "", "");
        }
    }

    private void insertSchedule(SQLiteDatabase db, String jam, String status, String bukti, String userEmail) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_JAM, jam);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_BUKTI, bukti);
        values.put(COLUMN_USER_EMAIL, userEmail);
        db.insert(TABLE_SCHEDULES, null, values);
    }

    public ArrayList<JadwalModel> getAllSchedules() {
        ArrayList<JadwalModel> schedules = new ArrayList<JadwalModel>();
        String selectQuery = "SELECT * FROM " + TABLE_SCHEDULES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                JadwalModel model = new JadwalModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JAM)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BUKTI))
                );
                schedules.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return schedules;
    }

    public boolean updateScheduleStatus(String jam, String status, String bukti, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_BUKTI, bukti);
        values.put(COLUMN_USER_EMAIL, userEmail);

        int rowsAffected = db.update(TABLE_SCHEDULES, values, COLUMN_JAM + " = ?", new String[]{jam});
        db.close();
        return rowsAffected > 0;
    }

    public ArrayList<JadwalModel> getHistoryTransaksi(String userEmail) {
        ArrayList<JadwalModel> historyList = new ArrayList<JadwalModel>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_SCHEDULES +
                " WHERE " + COLUMN_USER_EMAIL + " = ? AND " +
                COLUMN_STATUS + " != 'available' ORDER BY " + COLUMN_ID + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{userEmail});

        if (cursor.moveToFirst()) {
            do {
                JadwalModel model = new JadwalModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JAM)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BUKTI))
                );
                historyList.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return historyList;
    }
}