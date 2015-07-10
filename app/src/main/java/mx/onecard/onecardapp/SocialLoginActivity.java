package mx.onecard.onecardapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.ArrayList;
import java.util.List;

import mx.onecard.socialnetworks.SocialNetworkSessionHandler;


public class SocialLoginActivity extends AppCompatActivity implements
        SocialNetworkSessionHandler.OnResponseListener {

    private final int REQUEST_LOGIN = 1250;

    //Twitter stuff
    private TwitterLoginButton mTwitterLoginBtn;

    // Handler for login
    private SocialNetworkSessionHandler mSocialNetworkSessionHandler;

    private static final String TAG = "SocialLoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_translate_top);
        final View layout = findViewById(R.id.login_linear);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        layout.startAnimation(animation);

        // Instancia de SessionHandler
        mSocialNetworkSessionHandler = SocialNetworkSessionHandler.getInstance(this);
        mSocialNetworkSessionHandler.setListener(this);

        findViewById(R.id.facebook_login_button2).setOnClickListener(v -> {
            List<String> permissions = new ArrayList<>();
            permissions.add("public_profile");
            permissions.add("email");
            mSocialNetworkSessionHandler.loginWithFacebook(SocialLoginActivity.this, permissions);
        });
        findViewById(R.id.google_login_button2).setOnClickListener(v -> mSocialNetworkSessionHandler.loginWithGooglePlus());

        //Twitter Stuff
        mTwitterLoginBtn = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        mTwitterLoginBtn.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) { // TwitterSession
                //Obtenemos una session abeirta de twitter en el dispositivo
                Log.v(TAG, "Twitter: Login Succeeded");
                mSocialNetworkSessionHandler.login(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.v(TAG, "Login Error:" + exception.toString());
                exception.printStackTrace(); // No twitter sesion detected or logged
            }
        });

        // Google Stuff

    }

    @Override
    protected void onStart() {
        super.onStart();
        mSocialNetworkSessionHandler.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSocialNetworkSessionHandler.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_social_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_LOGIN){
            if(resultCode == RESULT_CANCELED){
                mSocialNetworkSessionHandler.logOut();
            }else if (data != null){
                //TODO aqui mandar llamar a inicio de sesion por usuario y password EN data
                String email = data.getStringExtra("email");
                String password = data.getStringExtra("password");
                login(email,password);
            }
            return;
        }
        mSocialNetworkSessionHandler.onActivityResult(requestCode, resultCode, data);
        mTwitterLoginBtn.onActivityResult(requestCode, resultCode, data);
    }

    //ACTIVITY FUNCTIONS
    protected void login(String email, String password){
        //TODO Comunicación con el servidor. Se invoca metodo y se
        onServerResponse();
    }

    protected void onServerResponse(){
        Intent intent = new Intent(this,NavDrawerActivity.class);
        startActivity(intent);
        finish();
    }

    //TODO debe ser una intetrface activada desde el mSocialNetworkSessionHandler
    public void onResponseListener(SocialNetworkSessionHandler.RESPONSE response) {
        switch (response) {
            case NO_RESPONSE: {

                break;
            }
            case FAIL_ERROR: {
                break;
            }
            case FAIL_CANT_CONNECT: {
                break;
            }
            case FAIL_WRONG_CREDENTIALS: {
                break;
            }
            case SUCCESS_NEW_USER: {
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtras(mSocialNetworkSessionHandler.getData());
                startActivityForResult(intent, REQUEST_LOGIN);
                break;
            }
            case SUCCESS_REGISTERED_USER: {
                break;
            }
        }
    }
}
