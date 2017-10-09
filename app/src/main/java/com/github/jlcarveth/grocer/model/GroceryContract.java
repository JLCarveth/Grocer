package com.github.jlcarveth.grocer.model;

import android.provider.BaseColumns;

/**
 * Created by John on 9/30/2017.
 */

public final class GroceryContract {
    private  GroceryContract() {}

    public static class GroceryEntry implements BaseColumns {

        public static final String TABLE_NAME = "groceries";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_COST = "cost";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + GroceryEntry.TABLE_NAME + " (" +
                        GroceryEntry._ID + " INTEGER PRIMARY KEY," +
                        GroceryEntry.COLUMN_NAME + " TEXT," +
                        GroceryEntry.COLUMN_NOTE + " TEXT," +
                        GroceryEntry.COLUMN_COST + " INT)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + GroceryEntry.TABLE_NAME;
    }
}