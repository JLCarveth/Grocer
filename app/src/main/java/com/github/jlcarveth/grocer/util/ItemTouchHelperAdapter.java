package com.github.jlcarveth.grocer.util;

import android.support.v7.widget.RecyclerView;

/**
 * Created by John on 10/1/2017.
 */

public interface ItemTouchHelperAdapter {
    /**
     * Called when an item has been dragged far enough  to trigger a move.
     *
     * @param fromPosition The start position of the moved item.
     * @param toPosition   Then resolved position of the moved item.
     * @return True if the item was moved to the new adapter position.
     *
     * @see RecyclerView#getAdapterPositionFor(RecyclerView.ViewHolder)
     * @see RecyclerView.ViewHolder#getAdapterPosition()
     **/
    boolean onItemMove(int fromPosition, int toPosition);

    /**
     * Called when an item has been dismissed by a swipe.
     * @param position the position of the item dismissed.
     */
    void onItemDismiss(int position);
}
