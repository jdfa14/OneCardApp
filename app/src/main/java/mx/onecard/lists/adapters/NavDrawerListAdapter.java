package mx.onecard.lists.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mx.onecard.lists.items.NavMenu;
import mx.onecard.onecardapp.R;

/**
 * Created by OneCardAdmon on 29/06/2015.
 * Adaptador para los elementos de la lista
 */
public class NavDrawerListAdapter extends ArrayAdapter<NavMenu> {
    private int layoutResource;
    public NavDrawerListAdapter(Context context,int layoutResource , List objects) {
        super(context,layoutResource, objects);
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResource, null);
        }
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        TextView name = (TextView) convertView.findViewById(R.id.name);

        NavMenu item = getItem(position);
        icon.setImageResource(item.getIconId());
        name.setText(item.getName());

        return convertView;
    }
}
