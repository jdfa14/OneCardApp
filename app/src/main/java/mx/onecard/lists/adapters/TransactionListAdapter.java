package mx.onecard.lists.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mx.onecard.lists.items.Transaction;
import mx.onecard.onecardapp.R;

/**
 * Created by OneCardAdmon on 30/07/2015.
 * Adaptador para imprimir en lista las transacciones de una tarjeta
 */
public class TransactionListAdapter extends ArrayAdapter<Transaction>{

    private int resource;

    public TransactionListAdapter(Context context, int resource, List<Transaction> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(resource, null); // TODO Crear layou para renglon
        }
        Transaction transaction = getItem(position);

        TextView dateTextView = (TextView) convertView.findViewById(R.id.item_trans_date);
        TextView descriptionTextView = (TextView) convertView.findViewById(R.id.item_trans_description);
        TextView amountTextView = (TextView) convertView.findViewById(R.id.item_trans_amount);

        dateTextView.setText(transaction.getDateApplied());
        descriptionTextView.setText(transaction.getDescription());
        amountTextView.setText(transaction.getTransactionAmount());

        return convertView;
    }
}
