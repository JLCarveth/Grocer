package com.github.jlcarveth.grocer.layout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.github.jlcarveth.grocer.R;
import com.github.jlcarveth.grocer.model.GroceryItem;
import com.github.jlcarveth.grocer.util.DataHandler;
import com.github.jlcarveth.grocer.util.FragmentEventListener;
import com.github.jlcarveth.grocer.util.StorageHandler;

/**
 * DialogFragment for handling user input to the DB
 * Presents a form to the user.
 */
public class AddDialogFragment extends DialogFragment {

    private EditText nameField, noteField, qtyField;

    private StorageHandler storageHandler;

    private DataHandler dataHandler;

    private FragmentEventListener fragmentEventListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_dialog, null);

        fragmentEventListener = (FragmentEventListener) getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag("GROCERY");

        nameField = view.findViewById(R.id.ad_name_input);
        noteField = view.findViewById(R.id.ad_note_input);
        qtyField = view.findViewById(R.id.ad_qty_input);

        storageHandler = new StorageHandler(getContext());
        dataHandler = new DataHandler(storageHandler);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(view);
        builder.setCancelable(false);

        builder.setMessage("Message")
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Add the data here
                        String name = nameField.getText().toString().trim();
                        String note = noteField.getText().toString().trim();
                        String qty = qtyField.getText().toString().trim();

                        if (name.length() == 0 && name.equalsIgnoreCase("")) {
                            System.out.println("It has been called.");
                            //Do Nothing.
                            nameField.setError("Error, Empty Input");
                            Toast.makeText(getContext(),
                                    "Name field cannot be empty/",
                                    Toast.LENGTH_LONG);
                        } else {
                            GroceryItem temp = new GroceryItem(name, note);
                            dataHandler.insertGroceryItem(temp);

                            // Update the GroceryFragment list.
                            fragmentEventListener.updateData();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
