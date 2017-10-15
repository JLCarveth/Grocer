package com.github.jlcarveth.grocer.layout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.jlcarveth.grocer.R;
import com.github.jlcarveth.grocer.model.GroceryItem;
import com.github.jlcarveth.grocer.util.DataHandler;
import com.github.jlcarveth.grocer.util.FragmentEventListener;
import com.github.jlcarveth.grocer.util.StorageHandler;

import junit.framework.Test;

import org.w3c.dom.Text;

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
