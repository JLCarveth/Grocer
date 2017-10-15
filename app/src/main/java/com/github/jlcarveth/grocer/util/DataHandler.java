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
            String qty = cursor.getString(cursor.getColumnIndexOrThrow(GroceryContract.GroceryEntry.COLUMN_QTY));
            int cost = cursor.getInt(cursor.getColumnIndexOrThrow(GroceryContract.GroceryEntry.COLUMN_COST));
            int checked = cursor.getInt(cursor.getColumnIndexOrThrow(GroceryContract.GroceryEntry.COLUMN_CHKD));

            boolean chk = (checked == 1) ? true : false;

            GroceryItem temp = new GroceryItem(name, note, qty, cost, chk);
            temp.setCost(cost);
            data.add(temp);

            System.out.println(temp + " retrieved from table.");
        }
        return data;
    }

    public void clearGroceryList() {
        databaseWritable.delete(GroceryContract.GroceryEntry.TABLE_NAME, null, null);
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
            String dbQty = cursor.getString(cursor.getColumnIndexOrThrow(GroceryContract.GroceryEntry.COLUMN_QTY));
            int dbCost = cursor.getInt(cursor.getColumnIndexOrThrow(GroceryContract.GroceryEntry.COLUMN_COST));
            int dbChk = cursor.getInt(cursor.getColumnIndexOrThrow(GroceryContract.GroceryEntry.COLUMN_CHKD));

            boolean checked = (dbChk == 1) ? true : false;

            GroceryItem gi = new GroceryItem(dbName,dbNote,dbQty,dbCost,checked);
            return gi;
        }
        throw new NoSuchElementException("Element with such name was not found within the database.");

    }

    public void insertGroceryItem(GroceryItem item) {
        System.out.println("Insert has been called...");
        ContentValues values = new ContentValues();

        values.put(GroceryContract.GroceryEntry.COLUMN_NAME, item.getName());
        values.put(GroceryContract.GroceryEntry.COLUMN_NOTE, item.getNote());
        values.put(GroceryContract.GroceryEntry.COLUMN_QTY, item.getQty());
        values.put(GroceryContract.GroceryEntry.COLUMN_COST, item.getCost());
        //SQlite doesn't support booleans directly. Convert to int.
        values.put(GroceryContract.GroceryEntry.COLUMN_CHKD, (item.isChecked()) ? 1 : 0);


        long newRow = databaseWritable.insert(
                GroceryContract.GroceryEntry.TABLE_NAME,    // Table Name
                null,                                       // nullColumnHack, set null if not
                                                            // inserting empty row
                values);                                    // The values being inserted
    }

    public boolean updateGroceryItem(String oldItem, GroceryItem newItem) {
        System.out.println("Updating," + oldItem + newItem.getName() + newItem.getNote());
        ContentValues values = new ContentValues();

        values.put(GroceryContract.GroceryEntry.COLUMN_NAME, newItem.getName());
        values.put(GroceryContract.GroceryEntry.COLUMN_NOTE, newItem.getNote());
        //values.put(GroceryContract.GroceryEntry.COLUMN_QTY, item.getQty());

        int updateFlag = databaseWritable.update(GroceryContract.GroceryEntry.TABLE_NAME,
                values,
                GroceryContract.GroceryEntry.COLUMN_NAME + "=" + "'" + oldItem + "'",
                null);
        return updateFlag > 0;
    }

    /**
     * Searches the DB for the equivalent item, and removes it if it exists.
     * @param groceryItem the item to remove from the SQLite DB
     * @return True if the item was removed.
     */
    public boolean removeEntry(GroceryItem groceryItem) {
        int removalFlag = databaseWritable.delete(GroceryContract.GroceryEntry.TABLE_NAME,
                "name=" + "'" + groceryItem.getName() + "'", null);

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

        for (int i=0; i < data.size(); i++) {
            GroceryItem item = data.get(i);

            if (item.isChecked()) {
                data.remove(i);
                removeEntry(item);
            }
        }

        //Temp to clear errors
        Collections.sort(data);

        return data;
    }

    /**
     * Returns true if an row in the DB had its checked value changed.
     * @param groceryItem the entry to check or uncheck
     * @return true if a row was changed by this function.
     */
    public boolean checkEntry(GroceryItem groceryItem) {
        System.out.println(groceryItem);
        ContentValues values = new ContentValues();
        //values.put(GroceryContract.GroceryEntry.COLUMN_NAME, groceryItem.getName());
        //values.put(GroceryContract.GroceryEntry.COLUMN_NOTE, groceryItem.getNote());
        //values.put(GroceryContract.GroceryEntry.COLUMN_QTY, groceryItem.getQty());
        //values.put(GroceryContract.GroceryEntry.COLUMN_COST, groceryItem.getCost());

        //When this method is called, the check state is inverted.
        //boolean itemChk = groceryItem.isChecked();
        //itemChk = !itemChk;

        values.put(GroceryContract.GroceryEntry.COLUMN_CHKD, (groceryItem.isChecked()) ? 1 : 0);

        int updateFlag = databaseWritable.update(GroceryContract.GroceryEntry.TABLE_NAME,
                values,
                "name=" + "'"+groceryItem.getName()+"'",
                null);

        return (updateFlag > 0);
    }
}
