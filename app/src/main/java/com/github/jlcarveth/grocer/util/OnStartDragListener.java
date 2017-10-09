package com.github.jlcarveth.grocer.util;

import android.support.v7.widget.RecyclerView;

/**
 * Created by John on 10/1/2017.
 * Listener for manual initiation of drag
 */

public interface OnStartDragListener {

    /**
     * Called when a view is requesting the start of a drag.
     *
     * @param viewHolder the holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
