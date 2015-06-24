package mx.onecard.onecardapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;


public class SocialLogin extends ActionBarActivity {
    private TwitterLoginButton loginButton;

    private static final String TAG = "SocialLogin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login);
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) { // TwitterSession
                // Do something with result, which provides a TwitterSession for making API calls
                AccountService mTwitterAcc = Twitter.getApiClient(result.data).getAccountService();
                mTwitterAcc.verifyCredentials(true, true, new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {
                        String imageUrl = result.data.profileImageUrl;
                        String email = result.data.email;
                        String userName = result.data.name;
                        Log.v(TAG, "imgURL: " + imageUrl);
                        Log.v(TAG,"email: " + email);
                        Log.v(TAG,"userName: " + userName);
                    }

                    @Override
                    public void failure(TwitterException e) {

                    }
                });

                // Preguntar por EMAIL permisos
                TwitterAuthClient mAuthClient = new TwitterAuthClient();
                mAuthClient.requestEmail(result.data, new Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        Log.v(TAG,"email: " + result.data);
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Log.e(TAG, "Email Request: " + e.toString());
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_social_login, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }
}
