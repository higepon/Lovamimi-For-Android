package com.lovamimi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SecretsCache extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String SECRETS_TABLE_NAME = "secrets";
    private static final String DATABASE_NAME = "lovamimi";
    private static final String SECRETS_TABLE_CREATE =
            "CREATE TABLE " + SECRETS_TABLE_NAME + " (" +
                    "ID TEXT, " +
                    "BODY TEXT);";

    public SecretsCache(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SECRETS_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SECRETS_TABLE_CREATE);
    }

    public void insertSecret(Secret secret) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID", secret.sid);
        values.put("BODY", secret.body);
        db.insertOrThrow(SECRETS_TABLE_NAME, null, values);
    }

    public Secret getSecret(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String args[] = {id};
        Cursor c = db.query(SECRETS_TABLE_NAME, null, "ID = ?", args, null, null, null);
        if(c.moveToFirst()){
            return new Secret(c.getString(c.getColumnIndex("ID")), c.getString(c.getColumnIndex("BODY")), null, null, 0, 0);
        } else {
            return null;
        }
    }
}