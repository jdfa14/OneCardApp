package mx.onecard.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;


import mx.onecard.interfaces.list.OnClickListener;
import mx.onecard.lists.adapters.CardAdapter;
import mx.onecard.onecardapp.R;
import mx.onecard.parse.User;
import mx.onecard.parse.User.OnUpdate;

public class CardBalanceFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, OnUpdate, OnClickListener {
    private static CardBalanceFragment instance = new CardBalanceFragment();

    //private CardsListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private User mUser;
    private RecyclerView mRecyclerView;
    private CardAdapter mCardAdapter;
    //private ListView mCardListView;

    public static CardBalanceFragment getInstance() {
        return instance;
    }

    public static CardBalanceFragment newInstance() {
        return new CardBalanceFragment();
    }

    public CardBalanceFragment() {
        mUser = User.getActualUser();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card_balance, container, false);

        if (savedInstanceState == null) {
            mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.frag_balance_swipeRefreshLayout);
            //mCardListView = (ListView) v.findViewById(R.id.frag_balance_cards_listview);

            //mAdapter = new CardsListAdapter(getActivity(), R.layout.item_card, Arrays.<Card>asList((Card[]) User.getActualUser().mCardsMap.values().toArray()));
            mCardAdapter = new CardAdapter(getActivity(),R.layout.item_card, User.getActualUser().mCardsMap.values(),this);

            mRecyclerView = (RecyclerView) v.findViewById(R.id.frag_balance_cards_recyclerview);
            mRecyclerView.setAdapter(mCardAdapter);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            //mCardListView.setOnItemClickListener(new CardListItemClickListener());
            //mCardListView.setOnScrollListener(this);
            //mCardListView.setAdapter(mAdapter);

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
    public void onPostUpdate(int responceCode, String message) {
        //mAdapter.notifyDataSetChanged();
        mCardAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(View v, int position) {
        // El adapter coloca el ID en la bariable position, donde el ID es el numero de la tarjeta (4 digitos)
        Intent intent = new Intent(getActivity(), TransactionsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", position);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
