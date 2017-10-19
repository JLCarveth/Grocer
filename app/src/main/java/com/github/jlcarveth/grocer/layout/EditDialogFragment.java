package com.github.jlcarveth.grocer.layout;

import android.app.Dialog;
import android.os.Bundle;
import android.app.AlertDialog;
import android.widget.EditText;

import com.github.jlcarveth.grocer.model.GroceryItem;
import com.github.jlcarveth.grocer.util.DataHandler;
import com.github.jlcarveth.grocer.util.FragmentEventListener;

/**
 * Similar to the AddDialogFragment. Presents a dialog to the user allowing them to edit the
 * selected item. Input is then sent to DB and the row is updated.
 */
public class EditDialogFragment extends InputDialogFragment {

    public EditText nameField, noteField, qtyField;

    public DataHandler dataHandler;

    public FragmentEventListener fragmentEventListener;

    /**
     * The item whose data is used to fill the fields
     */
    private GroceryItem groceryItem;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog = (AlertDialog) super.onCreateDialog(savedInstanceState);
        message = "Edit an Item.";
        dialog.setMessage(message);

        nameField = super.getNameField();
        noteField = super.getNoteField();
        qtyField = super.getQtyField();

        dataHandler = super.getDataHandler();

        fragmentEventListener = super.getFragmentEventListener();

        nameField.setText((String) getArguments().get("name"));
        noteField.setText((String) getArguments().get("note"));
        qtyField.setText((String) getArguments().get("qty"));

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void positiveAction(AlertDialog dialog) {
        // Old name for DB updating
        String oldName = (String) getArguments().get("name");
        // Add the data here
        String name = nameField.getText().toString();
        String note = noteField.getText().toString();
        String qty = qtyField.getText().toString();

        GroceryItem temp = new GroceryItem(name, note, qty);

        dataHandler.updateGroceryItem(oldName, temp);

        // Update the GroceryFragment list.
        fragmentEventListener.updateData();

        dialog.dismiss();
    }
}