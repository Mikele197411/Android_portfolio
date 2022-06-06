package com.mshilkov.clubolimp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mshilkov.clubolimp.data.CLubOlympContract.memberEntry;
public class OlimpDBHelper extends SQLiteOpenHelper {
    public OlimpDBHelper( Context context) {
        super(context,
                CLubOlympContract.DB_NAME, null,
                CLubOlympContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MEBERS_TABLE = "CREATE TABLE " + memberEntry.TABLE_NAME + "("
                + memberEntry._ID + " INTEGER PRIMARY KEY,"
                + memberEntry.KEY_FIRSTNAME + " TEXT,"
                + memberEntry.KEY_LASTNAME + " TEXT,"
                + memberEntry.KEY_GENDER + " INTEGER NOT NULL,"
                + memberEntry.KEY_SPORT + " TEXT" + ")";
        db.execSQL(CREATE_MEBERS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + CLubOlympContract.DB_NAME);
        onCreate(db);
    }
}
