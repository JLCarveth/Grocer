package com.github.jlcarveth.grocer.model;

import android.support.annotation.NonNull;

/**
 * Represents a single item on a grocery list.
 * Created by John on 10/3/2017.
 */

public class GroceryItem implements Comparable<GroceryItem> {

    private String name;

    private String note;

    private String qty;

    private int cost;

    private boolean checked;

    public GroceryItem(String name, String note) {
        this.name = name;
        this.note = note;
        cost = -1;
        qty = "";
        checked = false;
    }

    public GroceryItem(String name, String note, String qty) {
        this.name = name;
        this.note = note;
        this.qty = qty;
        cost = -1;
        checked = false;
    }

    public GroceryItem(String name, String note, String qty, int cost, boolean c) {
        this.name = name;
        this.note = note;
        this.qty = qty;
        this.cost = cost;
        checked = c;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String toString() {
        return ("{"+getName() + ", " + getNote() + ", " + getCost() + ", " + isChecked() + "}");
    }

    @Override
    public int compareTo(@NonNull GroceryItem b) {
        return this.getName().compareTo(b.getName());
    }
}
