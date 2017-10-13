package com.github.jlcarveth.grocer.layout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.github.jlcarveth.grocer.R;
import com.github.jlcarveth.grocer.model.GroceryItem;
import com.github.jlcarveth.grocer.util.DataHandler;
import com.github.jlcarveth.grocer.util.FragmentEventListener;
import com.github.jlcarveth.grocer.util.StorageHandler;

/**
 * Similar to the AddDialogFragment. Presents a dialog to the user allowing them to edit the
 * selected item. Input is then sent to DB and the row is updated.
 */
public class EditDialogFragment extends DialogFragment {

    private EditText nameField, noteField, qtyField;

    private StorageHandler storageHandler;

    private DataHandler dataHandler;

    private FragmentEventListener fragmentEventListener;

    /**
     * The item whose data is used to fill the fields
     */
    private GroceryItem groceryItem;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Layout is the same as AddDialogFragment
        View view = inflater.inflate(R.layout.fragment_edit_dialog, null);

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

        Bundle args = getArguments();
        final String oldName = (String) args.get("name");
        nameField.setText((String)args.get("name"));
        noteField.setText((String)args.get("note"));

        // Don't forget to update the DB data as well.

        builder.setView(view);

        builder.setMessage("Message")
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.out.println(id);
                        // Add the data here
                        String name = nameField.getText().toString();
                        String note = noteField.getText().toString();

                        GroceryItem temp = new GroceryItem(name, note);

                        dataHandler.updateGroceryItem(oldName, temp);

                        // Update the GroceryFragment list.
                        fragmentEventListener.updateData();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
