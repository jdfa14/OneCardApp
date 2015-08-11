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

import mx.onecard.lists.item.NavMenu;
import mx.onecard.onecardapp.R;

public class NavDrawerAdapter2 extends SingleSelectableAdapter<NavDrawerAdapter2.NavDrawerViewHolder> {

    private LayoutInflater inflater;
    private List<NavMenu> data = Collections.emptyList();
    private Context context;
    private NavDrawerAdapter2.OnClickListener mOnClickListener;

    public NavDrawerAdapter2(Context context, List<NavMenu> data, NavDrawerAdapter2.OnClickListener onClickListener) {
        super();
        this.data = data;
        mOnClickListener = onClickListener;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public NavDrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NavDrawerViewHolder(inflater.inflate(R.layout.item_nav_drawer, parent, false));
    }

    @Override
    public void onBindViewHolder(NavDrawerViewHolder holder, int position) {
        final NavMenu item = data.get(position);
        holder.name.setText(item.getName());
        holder.icon.setImageResource(item.getIconId());
        holder.overlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
        //holder.itemView.setSelected(selectedItem == position);
        Toast.makeText(context, "New bindViewHolder call " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return data.size();
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
            itemView.setOnLongClickListener(this);
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

    public interface OnClickListener {
        void onItemClick(View v, int position);
    }
}
