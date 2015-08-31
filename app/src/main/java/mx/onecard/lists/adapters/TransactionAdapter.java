package mx.onecard.lists.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import mx.onecard.interfaces.list.OnClickListener;
import mx.onecard.lists.items.Transaction;
import mx.onecard.onecardapp.R;

public class TransactionAdapter<VH extends TransactionAdapter.ViewHolder> extends RecyclerView.Adapter<VH> {
    List<Transaction> dataset = Collections.emptyList();
    OnClickListener mListener;

    public TransactionAdapter(List<Transaction> transactions, OnClickListener onClickListener){
        dataset = transactions;
        mListener = onClickListener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return (VH) new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Transaction transaction = dataset.get(position);
        holder.amountTextView.setText(transaction.getTransactionAmount());
        holder.dateTextView.setText(transaction.dateApplied);
        holder.descriptionTextView.setText(transaction.description);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView dateTextView;
        TextView descriptionTextView;
        TextView amountTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = (TextView) itemView.findViewById(R.id.item_trans_date);
            descriptionTextView = (TextView) itemView.findViewById(R.id.item_trans_description);
            amountTextView = (TextView) itemView.findViewById(R.id.item_trans_amount);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null) {
                mListener.onItemClick(v, getLayoutPosition());
            }
        }
    }
}