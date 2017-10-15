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

import com.github.jlcarveth.grocer.R;
import com.github.jlcarveth.grocer.model.GroceryItem;
import com.github.jlcarveth.grocer.util.DataHandler;
import com.github.jlcarveth.grocer.util.FragmentEventListener;
import com.github.jlcarveth.grocer.util.StorageHandler;

/**
 * Fragment Superclass defining a the DialogFragment for inserting and updating Grocery data to the
 * database. As of now, two classes extend this class. AddDialogFragment and EditDialogFragment
 */
public class InputDialogFragment extends DialogFragment {

    private EditText nameField, noteField, qtyField;

    private StorageHandler storageHandler;

    private DataHandler dataHandler;

    private FragmentEventListener fragmentEventListener;

    public String message;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_add_dialog, null);

        message = "What ya doin?";

        // Attaches the Fragment event listener to the Dialog
        fragmentEventListener = (FragmentEventListener) getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag("GROCERY");

        nameField = view.findViewById(R.id.ad_name_input);
        noteField = view.findViewById(R.id.ad_note_input);
        qtyField = view.findViewById(R.id.ad_qty_input);

        storageHandler = new StorageHandler(getContext());
        dataHandler = new DataHandler(storageHandler);


        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(view);
        builder.setCancelable(false);

        builder.setMessage(message)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {

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

    public EditText getNameField() { return nameField; }
    public EditText getNoteField() { return noteField; }
    public EditText getQtyField() { return qtyField; }

    public DataHandler getDataHandler() { return dataHandler; }

    public FragmentEventListener getFragmentEventListener() { return fragmentEventListener; }

    @Override
    public void onStart() {
        super.onStart();

        final AlertDialog dialog = (AlertDialog) getDialog();

        if (dialog != null) {
            System.out.println("Not null atleast");
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    positiveAction(dialog);
                }
            });
        } else { System.out.println("Null I guess"); }
    }

    /**
     * Method to be run when the positive button is clicked.
     */
    public void positiveAction(AlertDialog dialog) {
        String name = nameField.getText().toString().trim();
        String note = noteField.getText().toString().trim();
        String qty = qtyField.getText().toString().trim();

        // Arbitrary method. This class shouldn't really be instantiated.
        System.out.println(name + note + qty);
    }
}
