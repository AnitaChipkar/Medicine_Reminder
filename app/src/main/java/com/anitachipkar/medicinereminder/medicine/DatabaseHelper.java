package com.anitachipkar.medicinereminder.medicine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "medicine_reminder_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Medicines.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Medicines.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
    public long insertNote(String medicine) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Medicines.COLUMN_MEDICINE, medicine);

        // insert row
        long id = db.insert(Medicines.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public Medicines getMedicine(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Medicines.TABLE_NAME,
                new String[]{Medicines.COLUMN_ID, Medicines.COLUMN_MEDICINE,Medicines.COLUMN_QUANTITY, Medicines.COLUMN_TIMESTAMP},
                Medicines.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Medicines medicines = new Medicines(
                cursor.getInt(cursor.getColumnIndex(Medicines.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Medicines.COLUMN_MEDICINE)),
                cursor.getInt(cursor.getColumnIndex(Medicines.COLUMN_QUANTITY)),
                cursor.getString(cursor.getColumnIndex(Medicines.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return medicines;
    }

    public List<Medicines> getAllMedicines() {
        List<Medicines> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Medicines.TABLE_NAME + " ORDER BY " +
                Medicines.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Medicines medicines = new Medicines();
                medicines.setId(cursor.getInt(cursor.getColumnIndex(Medicines.COLUMN_ID)));
                medicines.setMedicine(cursor.getString(cursor.getColumnIndex(Medicines.COLUMN_MEDICINE)));
                medicines.setQuantity(cursor.getInt(cursor.getColumnIndex(Medicines.COLUMN_MEDICINE)));
                medicines.setTimestamp(cursor.getString(cursor.getColumnIndex(Medicines.COLUMN_TIMESTAMP)));

                notes.add(medicines);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getMedicineCount() {
        String countQuery = "SELECT  * FROM " + Medicines.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateMedicine(Medicines medicines) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Medicines.COLUMN_MEDICINE, medicines.getMedicine());
        values.put(Medicines.COLUMN_QUANTITY, medicines.getQuantity());

        // updating row
        return db.update(Medicines.TABLE_NAME, values, Medicines.COLUMN_ID + " = ?",
                new String[]{String.valueOf(medicines.getId())});
    }

    public void deleteMedicine(Medicines medicines) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Medicines.TABLE_NAME, Medicines.COLUMN_ID + " = ?",
                new String[]{String.valueOf(medicines.getId())});
        db.close();
    }
}