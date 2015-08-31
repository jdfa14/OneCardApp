package mx.onecard.lists.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OneCardAdmon on 11/08/2015.
 * Clase abstracta para crear un recycler view adapter con items seleccionables
 */
public abstract class SingleSelectableAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private static final String TAG = SingleSelectableAdapter.class.getSimpleName(); // Para imprimir logs

    private int selectedItem = -1;

    public SingleSelectableAdapter(){

    }

    private void itemChanged(int position){
        if(position != -1){
            notifyItemChanged(position);
        }
    }

    public void selectItem(int position){
        int oldPosition = selectedItem;
        if(selectedItem != position) {
            selectedItem = position;

            itemChanged(position);
            itemChanged(oldPosition);
        }
    }

    public boolean isSelected(int position){
        return position == selectedItem;
    }

    public void clearSelections(){
        selectItem(-1);
    }
}
