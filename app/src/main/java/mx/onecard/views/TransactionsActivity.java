package mx.onecard.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import mx.onecard.lists.adapters.TransactionAdapter;
import mx.onecard.lists.adapters.TransactionListAdapter;
import mx.onecard.lists.items.Card;
import mx.onecard.onecardapp.R;
import mx.onecard.parse.User;

//TODO pasar a RecyclerView para optimizacion
public class TransactionsActivity extends AppCompatActivity {
    private static final String TAG = TransactionsActivity.class.getSimpleName();

    private User mUser = User.getActualUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transactions);
        Bundle extras = getIntent().getExtras();


        if(savedInstanceState == null){

            if(extras == null || !extras.containsKey("id")) {
                Log.e(TAG,"Activity started without extras. This activity needs a valid Card Index");
                finish();
            }

            assert extras != null;
            Card mCard = mUser.mCardsMap.get(extras.getInt("id"));

            TransactionListAdapter mAdapter = new TransactionListAdapter(this, R.layout.item_transaction, mCard.getType1());

            //ListView mTransactionsListView = (ListView) findViewById(R.id.trans_transaction_listview);
            ;
            ImageView mCardImageView = (ImageView) findViewById(R.id.trans_card_imageview);
            TextView mBalanceTextView = (TextView) findViewById(R.id.trans_balance_textView);
            TextView mCardNumberTextView = (TextView) findViewById(R.id.trans_card_number_label);
            Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

            if(mToolbar != null) {
                setSupportActionBar(mToolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true); // Para mostrar logo
            }

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.trans_transaction_recyclerview);
            recyclerView.setAdapter(new TransactionAdapter(mCard.getType1(),null));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            //mTransactionsListView.setAdapter(mAdapter);

            mCardImageView.setImageResource(mCard.getImageResourceId());
            mBalanceTextView.setText(mCard.getBalance());
            mCardNumberTextView.setText(mCard.getCardNumber());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transactions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
