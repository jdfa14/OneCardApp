package mx.onecard.onecardapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mx.onecard.lists.adapters.NavDrawerAdapter2;
import mx.onecard.lists.item.NavMenu;
import mx.onecard.views.CardBalanceFragment;

/**
 * Created by OneCardAdmon on 05/08/2015.
 * Fragmento que sostendra el menu para le NavigationDrawer
 */
public class NavigationDrawerMenuFragment extends Fragment implements NavDrawerAdapter2.OnClickListener{

    private RecyclerView mRecyclerView;

    private static final String PREF_FILE_NAME = "navigation_drawer_preference";
    private static final String KEY_LEARNING = "user_learned_drawer";

    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View mDrawerView;
    private NavDrawerAdapter2 mDrawerAdapter;

    private boolean mFlagFromSaved;
    private boolean mFlagLearned;
    private String[] mMenuTitles;

    public NavigationDrawerMenuFragment(){
        // default empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFlagLearned = Boolean.valueOf(loadFromPreferences(getActivity(),KEY_LEARNING,"false"));
        if(savedInstanceState != null){
            mFlagFromSaved = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer_menu,container,false);


        mMenuTitles = getResources().getStringArray(R.array.navigation_drawer_options);
        List<NavMenu> items = new ArrayList<NavMenu>();
        items.add(new NavMenu(mMenuTitles[0], R.drawable.ic_home_white_36dp));
        items.add(new NavMenu(mMenuTitles[1], R.drawable.ic_credit_card_white_36dp));
        items.add(new NavMenu(mMenuTitles[2], R.drawable.ic_settings_white_36dp));

        mDrawerAdapter = new NavDrawerAdapter2(getActivity(),items,this);

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.nav_frag_recyclerview);
        mRecyclerView.setAdapter(mDrawerAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }

    public void setUp(DrawerLayout mDrawerLayout, final Toolbar toolbar, int drawerViewId) {
        this.mDrawerLayout = mDrawerLayout;
        mDrawerView = getActivity().findViewById(drawerViewId);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), this.mDrawerLayout,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!mFlagLearned){
                    mFlagLearned = true;
                    saveToPreferences(getActivity(),KEY_LEARNING,mFlagLearned +"");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha((float) (1-slideOffset*0.4));
            }
        };

        if(!mFlagLearned && !mFlagFromSaved){
            mDrawerLayout.openDrawer(mDrawerView);
        }
        this.mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        this.mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });
    }

    public void saveToPreferences(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public String loadFromPreferences(Context context, String preferenceName, String defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,defaultValue);
    }

    public void onPostCreate(Bundle savedInstanceState) {
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemClick(View v, int position) {
        selectMenuItem(position);
    }

    private void selectMenuItem(int position) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_frameLayout_container, CardBalanceFragment.getInstance()).commit();
        mDrawerLayout.closeDrawers();
    }
}
