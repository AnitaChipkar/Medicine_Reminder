package com.anitachipkar.medicinereminder.medicine;

public class Medicines {
    public static final String TABLE_NAME = "medicines";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MEDICINE = "medicine";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String medicine;
    private int quantity;
    private String timestamp;



    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_MEDICINE + " TEXT," + COLUMN_QUANTITY + " INTEGER,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Medicines() {
    }

    public Medicines(int id, String medicine, int quantity, String timestamp) {
        this.id = id;
        this.medicine = medicine;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}