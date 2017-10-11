package com.github.jlcarveth.grocer.layout;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jlcarveth.grocer.layout.GroceryFragment.OnListFragmentInteractionListener;
import com.github.jlcarveth.grocer.R;
import com.github.jlcarveth.grocer.model.GroceryItem;
import com.github.jlcarveth.grocer.util.DataHandler;
import com.github.jlcarveth.grocer.util.ItemTouchHelperAdapter;
import com.github.jlcarveth.grocer.util.ItemTouchHelperViewHolder;
import com.github.jlcarveth.grocer.util.OnStartDragListener;
import com.github.jlcarveth.grocer.util.StorageHandler;

import java.util.Collections;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link GroceryItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class GroceryRecyclerViewAdapter extends RecyclerView.Adapter<GroceryRecyclerViewAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private final List<GroceryItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final OnStartDragListener dragListener;

    private StorageHandler storageHandler;
    private DataHandler dataHandler;

    private GroceryFragment fragment;


    public GroceryRecyclerViewAdapter(List<GroceryItem> items,
                                      OnListFragmentInteractionListener mListener,
                                      OnStartDragListener dragListener,
                                      GroceryFragment fragment) {
        mValues = items;
        this.mListener = mListener;

        this.dragListener = dragListener;

        this.fragment = fragment;
        storageHandler = new StorageHandler(fragment.getContext());
        dataHandler = new DataHandler(storageHandler);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_grocery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GroceryItem item = mValues.get(position);
        System.out.println("onBindViewHolder pos: " + position);
        holder.mNameView.setText(item.getName());
        holder.mNoteView.setText(item.getNote());
        holder.mCheckbox.setChecked(item.isChecked());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.mDragBars.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEventCompat.getActionMasked(motionEvent)== MotionEvent.ACTION_DOWN) {
                    System.out.println("Starting Drag with View Holder " + holder);
                    dragListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mValues, fromPosition, toPosition);
        notifyItemMoved(fromPosition,toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        dataHandler.removeEntry(mValues.get(position));
        mValues.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mNoteView;

        public final CheckBox mCheckbox;

        public final ImageView mDragBars;

        public GroceryItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mNameView = (TextView) view.findViewById(R.id.gl_name);
            mNoteView = (TextView) view.findViewById(R.id.gl_note);
            mCheckbox = (CheckBox) view.findViewById(R.id.gl_checkbox);
            mDragBars = (ImageView) view.findViewById(R.id.gl_burger);

            mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    // Change the value in the list
                    mValues.get(getAdapterPosition()).setChecked(isChecked);
                    dataHandler.checkEntry(mValues.get(getAdapterPosition()));
                }
            });
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
