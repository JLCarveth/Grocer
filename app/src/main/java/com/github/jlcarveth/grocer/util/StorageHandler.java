package com.github.jlcarveth.grocer.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.jlcarveth.grocer.model.GroceryContract;

/**
 * Created by John on 9/30/2017.
 *
 * A storage wrapper for SQLite3 library. The DB Schema / Contract file
 * is named GroceryContract, found in the same package.
 * @author John L. Carveth
 */

public class StorageHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "grocerdb";
    private static final int DB_VERSION = 2;

    public StorageHandler(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GroceryContract.GroceryEntry.SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(GroceryContract.GroceryEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
