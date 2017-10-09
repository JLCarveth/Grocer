package com.github.jlcarveth.grocer.util;

import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by John on 10/1/2017.
 */

public interface ItemTouchHelperViewHolder {

    /**
     * Called when the {@link ItemTouchHelper} first registers an item as being moved or swiped.
     */
    void onItemSelected();

    /**
     * Called when the {@link ItemTouchHelper} has completed the move/swipe, and the item state should
     * be cleared.
     */
    void onItemClear();
}
