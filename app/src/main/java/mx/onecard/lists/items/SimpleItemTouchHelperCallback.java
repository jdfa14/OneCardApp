package mx.onecard.lists.items;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import mx.onecard.interfaces.ItemTouchHelperAdapter;

/**
 * Created by OneCardAdmon on 13/08/2015.
 * Helper para manejar los eventos que pasan en una lista RecyclerView
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final ItemTouchHelperAdapter mAdapter;
    private boolean dragAndMove = true;
    private boolean swipeAndDismiss = true;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter mAdapter){
        this.mAdapter = mAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return dragAndMove;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return swipeAndDismiss;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    public void setDragAndMove(boolean dragAndMove) {
        this.dragAndMove = dragAndMove;
    }

    public void setSwipeAndDismiss(boolean swipeAndDismiss) {
        this.swipeAndDismiss = swipeAndDismiss;
    }
}
