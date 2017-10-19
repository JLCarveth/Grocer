package com.github.jlcarveth.grocer.layout;

import android.app.Dialog;
import android.os.Bundle;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.widget.EditText;

import com.github.jlcarveth.grocer.model.GroceryItem;
import com.github.jlcarveth.grocer.util.DataHandler;
import com.github.jlcarveth.grocer.util.FragmentEventListener;

/**
 * DialogFragment for handling user input to the DB
 * Presents a form to the user.
 */
public class AddDialogFragment extends InputDialogFragment {

    public EditText nameField, noteField, qtyField;

    public DataHandler dataHandler;

    public FragmentEventListener fragmentEventListener;

    public String message;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog = (AlertDialog) super.onCreateDialog(savedInstanceState);
        message = "Add an Item";

        nameField = super.getNameField();
        noteField = super.getNoteField();
        qtyField = super.getQtyField();

        dataHandler = super.getDataHandler();

        fragmentEventListener = super.getFragmentEventListener();

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void positiveAction(AlertDialog dialog) {
        String name = nameField.getText().toString().trim();
        String note = noteField.getText().toString().trim();
        String qty = qtyField.getText().toString().trim();

        System.out.println("This has been called."+nameField.getText().length());
        if (!TextUtils.isEmpty(name)) {
            System.out.println("Not Empty.");

            GroceryItem gi = new GroceryItem(name,note,qty);

            dataHandler.insertGroceryItem(gi);

            fragmentEventListener.updateData();
            dialog.dismiss();
        } else {
            System.out.println("Empty");
            //dialog.dismiss();
            dialog.show();
            dialog.setMessage("Name field cannot be empty.");
        }
    }
}
