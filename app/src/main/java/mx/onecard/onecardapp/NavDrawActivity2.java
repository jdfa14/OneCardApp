package mx.onecard.onecardapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import mx.onecard.parse.User;

/**
 * Created by OneCardAdmon on 05/08/2015.
 * Crea
 */
public class NavDrawActivity2 extends NavDrawActivity {

    private boolean exiting = false;

    private Toolbar mToolbar;
    private User mUser;
    private NavigationDrawerMenuFragment mDrawerMenuFragment;

    public NavDrawActivity2() {
        // empty Constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_draw_2);

        if (savedInstanceState == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            mUser = User.getActualUser();
            mUser.Update(this,null); // Updating User
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mDrawerMenuFragment = (NavigationDrawerMenuFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_menu);
            mDrawerMenuFragment.setUp((DrawerLayout) findViewById(R.id.nav_drawerLayout), mToolbar, R.id.fragment_navigation_drawer_menu);
        }else{
            mDrawerMenuFragment.restore();
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerMenuFragment.onPostCreate(savedInstanceState);
    }


    @Override
    public void onBackPressed() {
        if(mDrawerMenuFragment.isDrawerOpen()){
            mDrawerMenuFragment.closeDrawer();
        }else if (exiting) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, R.string.app_exit_confirmation_message, Toast.LENGTH_LONG).show();
            exiting = true;
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exiting = false;
                }
            }, 2000);
        }
    }
}
