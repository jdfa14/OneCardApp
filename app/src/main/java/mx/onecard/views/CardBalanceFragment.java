package mx.onecard.views;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.onecard.onecardapp.R;

public class CardBalanceFragment extends Fragment {
    private static CardBalanceFragment instance = new CardBalanceFragment();

    public static CardBalanceFragment getInstance(){
        return instance;
    }

    public static CardBalanceFragment newInstance() {
        CardBalanceFragment fragment = new CardBalanceFragment();
        return fragment;
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
        return inflater.inflate(R.layout.fragment_card_balance, container, false);
    }
}
