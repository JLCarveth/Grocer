package com.github.jlcarveth.grocer.layout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jlcarveth.grocer.R;
import com.github.jlcarveth.grocer.model.GroceryItem;
import com.github.jlcarveth.grocer.util.DataHandler;
import com.github.jlcarveth.grocer.util.FragmentEventListener;
import com.github.jlcarveth.grocer.util.OnStartDragListener;
import com.github.jlcarveth.grocer.util.SimpleItemTouchHelperCallback;
import com.github.jlcarveth.grocer.util.StorageHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class GroceryFragment extends Fragment implements FragmentEventListener, OnStartDragListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private OnListFragmentInteractionListener mListener;

    protected RecyclerView recyclerView;

    protected GroceryRecyclerViewAdapter adapter;

    protected List<GroceryItem> dataset;

    /**
     * StorageHandler
     */
    private StorageHandler storageHandler;
    /**
     * DataHandler for storing and retrieving data
     */
    private DataHandler dataHandler;

    /**
     * ItemTouchHelper for Item Sliding / Dragging
     */
    private ItemTouchHelper ith;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroceryFragment() {
        // Fill the List with data
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static GroceryFragment newInstance(int columnCount) {
        GroceryFragment fragment = new GroceryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.item);

        storageHandler = new StorageHandler(context);
        dataHandler = new DataHandler(storageHandler);

        dataset = new LinkedList<GroceryItem>();

        // Fill the dataset with db data
        dataset = dataHandler.getGroceryList();

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        // Set the RecyclerView's adapter, and give it some data.
        // TODO Replace empty LinkedList with List generated from SQLite data
        adapter = new GroceryRecyclerViewAdapter(dataset, mListener, this, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        ith = new ItemTouchHelper(callback);
        ith.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        // I know this is being called, and that ViewHolder isn't null.
        ith.startDrag(viewHolder);
    }

    /**
     * Called whenever data has been changed in the DB
     */
    public void updateData() {
        // Apparently, you can't reassign the dataset.
        // Must be done this way.
        dataset.clear();
        dataset.addAll(dataHandler.getGroceryList());
        //dataHandler.sortGroceryList(dataset);

        adapter.notifyDataSetChanged();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(GroceryItem item);
    }

}
