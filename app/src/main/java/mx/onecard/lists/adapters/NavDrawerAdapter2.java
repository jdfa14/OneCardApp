package mx.onecard.lists.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import mx.onecard.interfaces.list.OnClickListener;
import mx.onecard.lists.items.NavMenu;
import mx.onecard.onecardapp.R;

public class NavDrawerAdapter2 extends SingleSelectableAdapter<NavDrawerAdapter2.NavDrawerViewHolder> {

    private LayoutInflater inflater;
    private List<NavMenu> dataset = Collections.emptyList();
    private Context context;
    private OnClickListener mOnClickListener;

    public NavDrawerAdapter2(Context context, List<NavMenu> dataset, OnClickListener onClickListener) {
        this.dataset = dataset;
        mOnClickListener = onClickListener;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        NavMenu.TYPE type = dataset.get(position).type;
        switch (type){
            case ACTION:
                return 0;
            case TRANSITION:
                return 1;
            case DELIMITER:
                return 2;
            default:
                return -1;
        }
    }

    @Override
    public NavDrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NavDrawerViewHolder(inflater.inflate(R.layout.item_nav_drawer, parent, false));
    }

    @Override
    public void onBindViewHolder(NavDrawerViewHolder holder, int position) {
        NavMenu item = dataset.get(position);
        holder.name.setText(item.name);
        holder.icon.setImageResource(item.iconId);
        holder.overlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class NavDrawerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView name;
        public ImageView icon;
        public View overlay;

        public NavDrawerViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            name = (TextView) itemView.findViewById(R.id.name);
            overlay = itemView.findViewById(R.id.selected_overlay);

            itemView.setOnClickListener(this);
            //itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // TODO handle here transitions dependind on position
            //Toast.makeText(context, getAdapterPosition() + "," + getLayoutPosition(), Toast.LENGTH_SHORT).show();

            int selectedItem = getLayoutPosition();
            selectItem(selectedItem);

            if (mOnClickListener != null) {
                mOnClickListener.onItemClick(v, selectedItem);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            Toast.makeText(context, "Long Clicking: " + getLayoutPosition(), Toast.LENGTH_SHORT).show();
            return true;
        }
    }

}
