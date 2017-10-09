package com.github.jlcarveth.grocer.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.jlcarveth.grocer.model.GroceryContract;
import com.github.jlcarveth.grocer.model.GroceryItem;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A class built to take data from StorageHandler and output formatted data from the database.
 * Offers a variety of methods to store / change / fetch data from the database, and return the data
 * in different forms.
 *
 * Created by John on 10/3/2017.
 */

public class DataHandler {

    private StorageHandler storageHandler;

    private SQLiteDatabase databaseReadable, databaseWritable;

    public DataHandler(StorageHandler storageHandler) {
        this.storageHandler = storageHandler;
        databaseReadable = storageHandler.getReadableDatabase();

        databaseWritable = storageHandler.getWritableDatabase();
    }

    /**
     * Gets all entries from the groceries table and stores the data in a list of
     * {@link GroceryItem} objects.
     * @return a list of {@link GroceryItem} objects from the DB
     */
    public List<GroceryItem> getGroceryList() {
        Cursor cursor = databaseReadable.rawQuery("select * from "+ GroceryContract.GroceryEntry.TABLE_NAME, null);

        List data = new LinkedList<GroceryItem>();

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(GroceryContract.GroceryEntry.COLUMN_NAME));
            String note = cursor.getString(cursor.getColumnIndexOrThrow(GroceryContract.GroceryEntry.COLUMN_NOTE));
            int cost = cursor.getInt(cursor.getColumnIndexOrThrow(GroceryContract.GroceryEntry.COLUMN_COST));

            GroceryItem temp = new GroceryItem(name, note, cost);
            data.add(temp);

            System.out.println(temp + " retrieved from table.");
        }
        return data;
    }

    /**
     * Helpful method for inserting a List into the DB
     * @param data the data to be inserted into the DB
     */
    public void addGroceryList(List<GroceryItem> data) {
        Iterator<GroceryItem> it = data.iterator();

        while (it.hasNext()) {
            GroceryItem gi = (GroceryItem) it.next();

            ContentValues values = new ContentValues();
            values.put(GroceryContract.GroceryEntry.COLUMN_NAME, gi.getName());
            values.put(GroceryContract.GroceryEntry.COLUMN_NOTE, gi.getNote());
            values.put(GroceryContract.GroceryEntry.COLUMN_COST, gi.getCost());

            long newRow = databaseWritable.insert(GroceryContract.GroceryEntry.TABLE_NAME, null, values);
        }
    }

    /**
     * Returns a data entry with the matching name, if it exists.
     * @param name the name of the data in the row we want
     * @throws NoSuchElementException if the element was not found within the data.
     * @return A GroceryItem object representing the data matching `name`, if it exists.
     */
    public GroceryItem getGroceryItem(String name)
            throws NoSuchElementException {
        Cursor cursor = databaseReadable.rawQuery("select * from groceries where name = '"+name+"'", null);

        if (cursor != null) {
            cursor.moveToFirst();
            String dbName = cursor.getString(cursor.getColumnIndexOrThrow(GroceryContract.GroceryEntry.COLUMN_NAME));
            String dbNote = cursor.getString(cursor.getColumnIndexOrThrow(GroceryContract.GroceryEntry.COLUMN_NOTE));
            int dbCost = cursor.getInt(cursor.getColumnIndexOrThrow(GroceryContract.GroceryEntry.COLUMN_COST));

            GroceryItem gi = new GroceryItem(dbName,dbNote,dbCost);
            return gi;
        }
        throw new NoSuchElementException("Element with such name was not found within the database.");

    }

    public void insertGroceryItem(GroceryItem item) {
        System.out.println("Insert has been called...");
        ContentValues values = new ContentValues();

        values.put(GroceryContract.GroceryEntry.COLUMN_NAME, item.getName());
        values.put(GroceryContract.GroceryEntry.COLUMN_NOTE, item.getNote());
        values.put(GroceryContract.GroceryEntry.COLUMN_COST, item.getCost());

        long newRow = databaseWritable.insert(
                GroceryContract.GroceryEntry.TABLE_NAME,    // Table Name
                null,                                       // nullColumnHack, set null if not
                                                            // inserting empty row
                values);                                    // The values being inserted
    }

    /**
     * Searches the DB for the equivalent item, and removes it if it exists.
     * @param groceryItem the item to remove from the SQLite DB
     * @return True if the item was removed.
     */
    public boolean removeEntry(GroceryItem groceryItem) {
        int removalFlag = databaseWritable.delete(GroceryContract.GroceryEntry.TABLE_NAME,
                GroceryContract.GroceryEntry.COLUMN_NAME + '=' + "'" + groceryItem.getName() + "'", null);

        // database.delete returns the number of rows affected, 0 if no rows are affected.
        return (removalFlag > 0);
    }

    /**
     * Sorts the grocery list (Alphabetically, for now), removing checked entries.
     * @return a sorted list, with checked items removed.
     */
    public List<GroceryItem> sortGroceryList(List<GroceryItem> data) {
        if (data.isEmpty()) {
            return data;
        }
        System.out.println("Sorting Called.");
        System.out.println("Before Sort: " + data.toString());

        for (int i=0; i < data.size(); i++) {
            GroceryItem item = data.get(i);

            if (item.isChecked()) {
                data.remove(i);
            }
        }

        //Temp to clear errors
        Collections.sort(data);

        System.out.println("After Sort: " + data.toString());
        return data;
    }
}
