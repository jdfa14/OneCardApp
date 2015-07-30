package mx.onecard.onecardapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import mx.onecard.lists.adapters.TransactionListAdapter;
import mx.onecard.lists.item.Card;
import mx.onecard.parse.User;


public class TransactionsActivity extends AppCompatActivity implements User.OnUpdate{
    private static final String TAG = "TransactionsActivity";

    private User mUser = User.getActualUser();
    private Card mCard;
    private TransactionListAdapter mAdapter;
    private ListView mTransactionsListView;
    private ImageView mCardImageView;
    private TextView mBalanceTextView;
    private TextView mCardNumberTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transactions);
        Bundle extras = getIntent().getExtras();
        if(extras == null || !extras.containsKey("index")) {
            Log.e(TAG,"Activity started without extras. This activity needs a valid Card Index");
            finish();
        }

        if(savedInstanceState == null){
            assert extras != null;
            mCard = mUser.getCards().get(extras.getInt("index"));
            mAdapter = new TransactionListAdapter(this,R.layout.item_transaction,mCard.getType1());

            mTransactionsListView = (ListView) findViewById(R.id.trans_transaction_listview);
            mCardImageView = (ImageView) findViewById(R.id.trans_card_imageview);
            mBalanceTextView = (TextView) findViewById(R.id.trans_balance_textView);
            mCardNumberTextView = (TextView) findViewById(R.id.trans_card_number_label);

            mTransactionsListView.setAdapter(mAdapter);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPreUpdate() {

    }

    @Override
    public void onPostUpdate() {

    }


}
