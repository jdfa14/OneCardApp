package mx.onecard.lists.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import mx.onecard.interfaces.list.OnClickListener;
import mx.onecard.lists.items.Card;
import mx.onecard.onecardapp.R;

/**
 * Created by OneCardAdmon on 26/08/2015.
 * Adaptador para Recycler de tarjetas
 */
public class CardAdapter<VH extends CardAdapter.ViewHolder> extends RecyclerView.Adapter<VH>{

    private LayoutInflater mInflater;
    private List<Card> dataset = Collections.emptyList();
    private OnClickListener mListener;
    private int resourceLayout;

    public CardAdapter(Context context,int resourceLayoutId ,Collection<Card> dataset, OnClickListener onClickListener){
        //this.dataset.addAll(dataset);

        this.mListener = onClickListener;
        mInflater = LayoutInflater.from(context);
        this.resourceLayout = resourceLayoutId;

        this.dataset = new ArrayList<>(dataset);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return (VH) new ViewHolder(mInflater.inflate(resourceLayout,parent,false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Card item = dataset.get(position);
        holder.cardImageImageView.setImageResource(item.getImageResourceId());
        holder.balanceTextView.setText(item.getBalance());
        holder.numberTextView.setText(item.getCardNumberMedium());
        holder.productTextView.setText(item.getProduct());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cardImageImageView;
        TextView productTextView;
        TextView numberTextView;
        TextView balanceTextView;

        public ViewHolder(final View itemView) {
            super(itemView);
            cardImageImageView = (ImageView) itemView.findViewById(R.id.item_cards_card_ImageView);
            productTextView = (TextView) itemView.findViewById(R.id.item_cards_product_label);
            numberTextView = (TextView) itemView.findViewById(R.id.item_cards_card_number_label);
            balanceTextView = (TextView) itemView.findViewById(R.id.item_cards_balance_label);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(v, dataset.get(getLayoutPosition()).getCardDigits());
                    }
                }
            });
        }
    }
}
