package mx.onecard.lists.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mx.onecard.lists.items.Card;
import mx.onecard.onecardapp.R;

/**
 * Created by OneCardAdmon on 10/07/2015.
 * Adapter para imprimir las tarjetas y sus datos por renglon desde item
 */

public class CardsListAdapter extends ArrayAdapter<Card> {

    int resource;

    public CardsListAdapter(Context context, int resource, List<Card> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(resource, null);
        }

        ImageView cardImage = (ImageView) convertView.findViewById(R.id.item_cards_card_ImageView);
        TextView balance = (TextView) convertView.findViewById(R.id.item_cards_balance_label);
        TextView product = (TextView) convertView.findViewById(R.id.item_cards_product_label);
        TextView cardNumber = (TextView) convertView.findViewById(R.id.item_cards_card_number_label);

        Card card = getItem(position);
        //cardImage.setImageResource(card.getImageResourceId());
        balance.setText(card.getBalance());
        product.setText(card.getProduct());
        cardNumber.setText(card.getCardNumber());

        return convertView;
    }
}
