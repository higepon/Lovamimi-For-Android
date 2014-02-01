package com.lovamimi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SecretsCache extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String SECRETS_TABLE_NAME = "secrets";
    private static final String DATABASE_NAME = "lovamimi";
    private static final String SECRETS_TABLE_CREATE =
            "CREATE TABLE " + SECRETS_TABLE_NAME + " (" +
                    "ID TEXT, " +
                    "BODY TEXT, " +
                    "ICON_NAME TEXT);";

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
        values.put("ICON_NAME", secret.iconName);
        db.insertOrThrow(SECRETS_TABLE_NAME, null, values);
    }

    public Secret getSecret(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String args[] = {id};
        Cursor c = db.query(SECRETS_TABLE_NAME, null, "ID = ?", args, null, null, null);
        if(c.moveToFirst()){
            return new Secret(
                    c.getString(c.getColumnIndex("ID")),
                    c.getString(c.getColumnIndex("BODY")),
                    null,
                    c.getString(c.getColumnIndex("ICON_NAME")),
                    0, 0);
        } else {
            return null;
        }
    }

    public List<Secret> getAllSecrets() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(SECRETS_TABLE_NAME, null, null, null, null, null, null);
        List<Secret> ret = new ArrayList<Secret>();
        if(c.moveToFirst()){
            do {
                Secret secret = new Secret(c.getString(c.getColumnIndex("ID")),
                        c.getString(c.getColumnIndex("BODY")),
                        null,
                        c.getString(c.getColumnIndex("ICON_NAME")), 0, 0);
                ret.add(secret);
            } while(c.moveToNext());
        }
        return ret;
    }
}