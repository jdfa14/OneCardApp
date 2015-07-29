package mx.onecard.onecardapp;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import mx.onecard.lists.adapters.NavDrawerListAdapter;
import mx.onecard.lists.item.NavMenu;
import mx.onecard.onecardapp.R;
import mx.onecard.views.CardBalanceFragment;

public class NavDrawActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerListView;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mMenuTitles;

    // Fragmentos
    private Fragment[] mFragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        mFragments = new Fragment[]{
                new CardBalanceFragment()
        };

        mDrawerTitle = getTitle();
        mMenuTitles = getResources().getStringArray(R.array.navigation_drawer_options);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawerLayout);
        mDrawerListView = (ListView) findViewById(R.id.nav_listView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        ArrayList<NavMenu> items = new ArrayList<NavMenu>();
        items.add(new NavMenu(mMenuTitles[0], R.drawable.home_icon));
        items.add(new NavMenu(mMenuTitles[1], R.drawable.credit_card_icon));
        items.add(new NavMenu(mMenuTitles[2], R.drawable.settings_icon));

        mDrawerListView.setAdapter(new NavDrawerListAdapter(this, R.layout.item_nav_drawer, items));
        mDrawerListView.setOnItemClickListener(new DrawerItemClickListener());
        // mDrawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.draw_list_item, getResources().getStringArray(R.array.transaction_type)));

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                //super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // forza a invocar onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);


        if (savedInstanceState == null) {
            mTitle = mMenuTitles[0];
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (id){
            case R.id.action_websearch :
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerListView);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        //Transicion a fragmento
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_frameLayout_container,mFragments[0]).commit();

        // Feedback visual de seleccion
        mDrawerListView.setItemChecked(position, true);
        // Cambiamos el nombre de la activity
        mTitle = mMenuTitles[position];
        // Cerramos el menu
        mDrawerLayout.closeDrawer(mDrawerListView);
    }

    //TODO agregar un alert para verificar que quiera salir de la app
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}