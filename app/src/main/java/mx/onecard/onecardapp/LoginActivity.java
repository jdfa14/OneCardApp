package mx.onecard.onecardapp;

import android.content.Intent;
import android.content.IntentSender;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;


public class LoginActivity extends ActionBarActivity
        implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;

    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean signedInUser;
    private ConnectionResult mConnectionResult;
    private SignInButton mSignInButton;
    private ImageView mProfileImageView;
    private TextView mUsernameTextView, mEmailTextView;
    private LinearLayout mProfileFrame, mSignInFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);

        mSignInButton = (SignInButton) findViewById(R.id.BtnLoginGoogle);
        mSignInButton.setOnClickListener(this);

        mProfileImageView = (ImageView) findViewById(R.id.ivUserImage);
        mUsernameTextView = (TextView) findViewById(R.id.tvUsername);
        mEmailTextView = (TextView) findViewById(R.id.tvEmail);

        mProfileFrame = (LinearLayout) findViewById(R.id.llProfileFrame);
        mSignInFrame = (LinearLayout)findViewById(R.id.llSigninFrame);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    private void resolveSignInError(){
        if(mConnectionResult.hasResolution()){
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this,RC_SIGN_IN);
            }catch (IntentSender.SendIntentException e){
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        signedInUser = false;
        Toast.makeText(this,"Log in with Google", Toast.LENGTH_LONG).show();
        getProfileInformation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        updateFrames(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.BtnLoginGoogle:
                googlePlusLogin();
                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(!connectionResult.hasResolution()){
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(),this,0).show();
            return;
        }

        if(!mIntentInProgress){
            mConnectionResult = connectionResult;
            if(signedInUser){
                resolveSignInError();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case RC_SIGN_IN :
                if(resultCode == RESULT_OK){
                    signedInUser = false;
                }
                mIntentInProgress = false;
                if(!mGoogleApiClient.isConnecting()){
                    mGoogleApiClient.connect();
                }
                break;
        }
    }

    private void updateFrames(boolean isSignedIn){
        if(isSignedIn){
            mSignInFrame.setVisibility(View.GONE);
            mProfileFrame.setVisibility(View.VISIBLE);
        }else{
            mSignInFrame.setVisibility(View.VISIBLE);
            mProfileFrame.setVisibility(View.GONE);
        }
    }

    private void getProfileInformation(){
        try{
            Person mPerson;
            if((mPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient)) != null){
                String personName = mPerson.getDisplayName();
                String personPhotoURL = mPerson.getImage().getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                mUsernameTextView.setText(personName);
                mEmailTextView.setText(email);

                new LoadProfileImage(mProfileImageView).execute(personPhotoURL);
                updateFrames(true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void signIn(View v){
        googlePlusLogin();
    }

    private void logout(View v){
        googlePlusLogout();
    }

    private void googlePlusLogin(){
        if(!mGoogleApiClient.isConnecting()){
            signedInUser = true;
        }
    }

    private void googlePlusLogout(){
        if(mGoogleApiClient.isConnected()){
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            updateFrames(false);
            signedInUser = false;
        }
    }
}
