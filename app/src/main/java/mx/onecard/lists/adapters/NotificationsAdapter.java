package mx.onecard.lists.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import mx.onecard.interfaces.ItemTouchHelperAdapter;
import mx.onecard.interfaces.ListInterfaces;
import mx.onecard.lists.items.NotificationItem;
import mx.onecard.onecardapp.R;
import mx.onecard.parse.User;

/**
 * Created by OneCardAdmon on 12/08/2015.
 * Adaptador para el dataset de las notificaciones
 */
public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private LayoutInflater inflater;
    private List<NotificationItem> dataset = Collections.emptyList();
    private Context context;
    private ListInterfaces.OnClickListener mOnClickListener;

    public NotificationsAdapter(Context context, List<NotificationItem> dataset, ListInterfaces.OnClickListener mOnClickListener){
        this.context = context;
        this.dataset = dataset;
        this.mOnClickListener = mOnClickListener;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NotificationItem item = dataset.get(position);
        holder.title.setText(item.getTittle());
        holder.title.setCompoundDrawablesWithIntrinsicBounds(item.getImgResourceId(), 0, 0, 0);
        holder.description.setText(item.getContent());
        holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(item.getColorId()));
        switch(item.getType()){
            case NotificationItem.WARNING :
                holder.title.setTextColor(Color.WHITE);
                holder.description.setTextColor(Color.WHITE);
                break;
            case NotificationItem.NEW :
                holder.title.setTextColor(Color.BLACK);
                holder.description.setTextColor(Color.BLACK);
                break;
            case NotificationItem.NOTICE :
                holder.title.setTextColor(Color.BLACK);
                holder.description.setTextColor(Color.BLACK);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(dataset, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(dataset, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        // TODO Consumir servicio de eliminar notificacion
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView description;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_notification_title_textview);
            description = (TextView) itemView.findViewById(R.id.item_notification_description_textview);
            cardView = (CardView) itemView.findViewById(R.id.item_notification_cardview);
        }
    }
}
