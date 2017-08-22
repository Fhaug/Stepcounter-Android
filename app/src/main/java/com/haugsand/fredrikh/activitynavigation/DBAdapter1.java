package com.haugsand.fredrikh.activitynavigation;

/**
 * Created by FredrikH on 13.04.2017.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;
import android.util.Log;

public class DBAdapter1 {

    private static final String TAG = "DBAdapter1"; //used for logging database version changes

    // Field Names:
    public static final String KEY_ROWID = "_id";
    public static final String KEY_STEPS = "steps";
    public static final String KEY_DATE = "date";
    public static final String KEY_CALORIES = "calories";

    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_STEPS, KEY_DATE, KEY_CALORIES};

    // Column Numbers for each Field Name:
    public static final int COL_ROWID = 0;
    public static final int COL_STEPS = 1;
    public static final int COL_DATE = 2;
    public static final int COL_CALORIES = 3;

    // DataBase info:
    public static final String DATABASE_NAME = "TOTAL_DAILY_STEPS";
    public static final String DATABASE_TABLE = "TOTAL_STEPS";
    public static final int DATABASE_VERSION = 11; // The version number must be incremented each time a change to DB structure occurs.

    //SQL statement to create database
    private static final String DATABASE_CREATE_SQL =
            "CREATE TABLE " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_STEPS + " TEXT, "
                    + KEY_DATE + " TEXT, "
                    + KEY_CALORIES + " TEXT "
                    + ");";

    private final Context context;
    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;


    public DBAdapter1(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAdapter1 open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to be inserted into the database.
    public long insertRow(String task, String date, String calories) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_STEPS, task);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_CALORIES, calories);

        // Insert the data into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the database.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public int getRow(long rowId) {
        int num = 50;
        Cursor c = 	db.rawQuery("SELECT 1 FROM TOTAL_STEPS WHERE 0 =?", new String[]{String.valueOf(rowId)});
        System.out.println("C:" + c);
        if (c != null  && c.moveToFirst()) {
            num = c.getInt(0);
            System.out.println("NUM:" + num);

            c.close();
        }

        return num;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRow(long rowId, String steps, String date, String calories) {
        String where = KEY_ROWID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_STEPS, steps);
        newValues.put(KEY_DATE, date);
        newValues.put(KEY_CALORIES, calories);
        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }


}
