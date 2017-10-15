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
public class AddDialogFragment extends DialogFragment {

    private EditText nameField, noteField, qtyField;

    private StorageHandler storageHandler;

    private DataHandler dataHandler;

    private FragmentEventListener fragmentEventListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_add_dialog, null);

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

        builder.setMessage("Add an Item.")
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

    @Override
    public void onStart() {
        super.onStart();

        final AlertDialog dialog = (AlertDialog) getDialog();

        if (dialog != null) {
            System.out.println("Not null atleast");
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
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
            });
        } else { System.out.println("Null I guess"); }
    }
}
