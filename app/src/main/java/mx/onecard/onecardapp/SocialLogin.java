package mx.onecard.onecardapp;

import android.app.ProgressDialog;
import android.content.IntentSender;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SocialLogin extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //Google Stuff
    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private SignInButton mGoogleLoginBtn;
    //Twitter stuff
    private TwitterLoginButton mTwitterLoginBtn;
    //Facebook stuff
    private LoginButton mFacebookLoginBtn;
    private CallbackManager mCallbackManager;


    private static final String TAG = "SocialLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login);
        final Animation animation = AnimationUtils.loadAnimation(this,R.anim.fade_in_translate_top);
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

        List<String> permissions = new ArrayList<String>();
        permissions.add("public_profile");
        permissions.add("email");
        //Facebook STUFF
        mCallbackManager = CallbackManager.Factory.create();
        mFacebookLoginBtn = (LoginButton) findViewById(R.id.facebook_login_button);
        mFacebookLoginBtn.setReadPermissions(permissions);
        mFacebookLoginBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                                Log.v(TAG, jsonObject.toString());
                            }
                        }
                );
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,verified");
                request.setParameters(parameters);
                request.executeAsync();
                Log.v(TAG, "Facebook Login succeded!");
            }

            @Override
            public void onCancel() {
                Log.v(TAG, "Canceled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.v(TAG, "Error");
            }
        });

        //Twitter Stuff
        mTwitterLoginBtn = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        mTwitterLoginBtn.setCallback(new Callback<TwitterSession>() {
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
                        Log.v(TAG, "email: " + email);
                        Log.v(TAG, "userName: " + userName);
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
                        Log.v(TAG, "email: " + result.data);
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Log.v(TAG, "Email Request: " + e.toString());
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Log.v(TAG, "Login Error:" + exception.toString());
            }
        });

        // Google Stuff
        mGoogleLoginBtn = (SignInButton) findViewById(R.id.google_login_button);
        mGoogleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGplus();
            }
        });
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
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
        //Facebook
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
        //Twitter
        mTwitterLoginBtn.onActivityResult(requestCode, resultCode, data);
        //Google
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    // GOOGLE FUNCTIONS

    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
        Log.v(TAG,"Google: onConnected");
        // Get user's information
        //getProfileInformation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        Log.v(TAG, "Google: onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = connectionResult;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

}
