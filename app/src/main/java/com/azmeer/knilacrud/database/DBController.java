package com.azmeer.knilacrud.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DBController {

    private static final String TAG = "DBController";
    protected final static int VERSION = 1;
    // name of database
    protected final static String NOM = "azmeerKnilaApp.db";

    protected static SQLiteDatabase mDb;

    protected DatabaseHandler mHandler = null;

    public DBController(Context pContext) {
        this.mHandler = new DatabaseHandler(pContext, NOM, null, VERSION);
    }

    //public SQLiteDatabase open() {
    public synchronized SQLiteDatabase open() {
        if (mDb == null) {
            mDb = mHandler.getWritableDatabase();
        } else {
            mDb.close();
            mDb = mHandler.getWritableDatabase();
        }

        //return sInstance;
        return mDb;
    }

    public synchronized void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }

}
