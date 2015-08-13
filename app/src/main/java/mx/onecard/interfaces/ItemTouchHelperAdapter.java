package mx.onecard.interfaces;

/**
 * Created by OneCardAdmon on 13/08/2015.
 * Adapter para eventos de Swipe and dismiss
 */
public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
