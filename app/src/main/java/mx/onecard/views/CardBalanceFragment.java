package mx.onecard.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import mx.onecard.lists.adapters.CardsListAdapter;
import mx.onecard.onecardapp.R;
import mx.onecard.onecardapp.TransactionsActivity;
import mx.onecard.parse.User;
import mx.onecard.parse.User.OnUpdate;

public class CardBalanceFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, OnUpdate {
    private static CardBalanceFragment instance = new CardBalanceFragment();

    private CardsListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mCardListView;
    private ListView mCardListView2;
    private ListView mCardListView3;

    public static CardBalanceFragment getInstance() {
        return instance;
    }

    public static CardBalanceFragment newInstance() {
        return new CardBalanceFragment();
    }

    public CardBalanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card_balance, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.frag_balance_swipeRefreshLayout);
        mCardListView = (ListView) v.findViewById(R.id.frag_balance_cards_listview);
        mCardListView2 = (ListView) v.findViewById(R.id.frag_balance_cards_listview2);
        mCardListView3 = (ListView) v.findViewById(R.id.frag_balance_cards_listview3);

        if (savedInstanceState == null) {
            mAdapter = new CardsListAdapter(getActivity(), R.layout.item_card, User.getActualUser().getCards());

            mCardListView.setOnItemClickListener(new CardListItemClickListener());
            mCardListView.setOnScrollListener(this);

            mCardListView.setAdapter(mAdapter);

            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeResources(
                    android.R.color.holo_blue_light,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

            User.getActualUser().Update(getActivity(), this);

        }
        return v;
    }

    //IMPLEMENTATIONS
    // OnRefreshListener
    @Override
    public void onRefresh() {
        User.getActualUser().Update(getActivity(), this);
    }

    // OnScrollListener
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int topRowVerticalPosition = (view == null || view.getChildCount() == 0) ? 0 : view.getChildAt(0).getTop();
        mSwipeRefreshLayout.setEnabled((firstVisibleItem == 0 && topRowVerticalPosition >= 0));
    }

    //OnUpdate
    @Override
    public void onPreUpdate() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onPostUpdate() {
        mCardListView2.setAdapter(new CardsListAdapter(getActivity(), R.layout.item_card_2, User.getActualUser().getCards()));
        mCardListView3.setAdapter(new CardsListAdapter(getActivity(), R.layout.item_card_3, User.getActualUser().getCards()));
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    //CLASSES
    private class CardListItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), TransactionsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("index",position);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

}
