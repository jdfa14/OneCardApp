package mx.onecard.lists.Helper;

import android.support.v7.widget.RecyclerView;

/**
 * Created by OneCardAdmon on 28/08/2015.
 * Clase que funcionara de escuchador del evento de scrol para esconder vistas o mostrarlas
 */
public abstract class HidingViewOnScrollListener extends RecyclerView.OnScrollListener {
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance;
    private boolean visibility = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (scrolledDistance > HIDE_THRESHOLD && visibility) {
            onHide();
            visibility = false;
            scrolledDistance = 0;
        } else if (scrolledDistance < -HIDE_THRESHOLD && !visibility) {
            onShow();
            visibility = true;
            scrolledDistance = 0;
        }

        if((visibility && dy>0) || (!visibility && dy<0)) {
            scrolledDistance += dy;
        }
    }

    public abstract void onHide();
    public abstract void onShow();
}
