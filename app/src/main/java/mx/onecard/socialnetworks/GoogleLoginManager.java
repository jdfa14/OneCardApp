package mx.onecard.socialnetworks;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

/**
 * Created by JesusDavid on 06/07/2015.
 * Clase que agrupara el funcionamiento de inicio de sesion en facebook
 */
public class GoogleLoginManager implements OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = "GoogleLoginManager";

    protected static GoogleLoginManager instance;

    private static final int RC_SIGN_IN = 0;

    private GoogleApiClient mGoogleApiClient;
    private GoogleLoginListener mListener;

    private Activity mActivity;

    private boolean mIsResolving = false;
    private boolean mShouldResolve = false;
    private ACTION type = ACTION.CONNECTING;

    public enum ACTION {
        CONNECTING,         // Cuando solo se esta conectando para un request
        LOGING_IN           // Cuando se esta iniciando sesion
    }

    public static GoogleLoginManager getInstance(Activity activity) {
        if (instance == null) {
            synchronized (GoogleLoginManager.class) {
                if (instance == null) {
                    instance = new GoogleLoginManager(activity);
                }
            }
        } else {
            instance.setActivity(activity);
        }
        return instance;
    }

    protected GoogleLoginManager(Activity activity) {
        setActivity(activity);
    }

    public void logIn() {
        //Connecting
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        mShouldResolve = true;
        type = ACTION.LOGING_IN;
        mGoogleApiClient.connect();
    }

    public void logOut() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
    }

    public void revokeAccessAndDisconnnect() {
        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
        Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
    }

    public boolean isLogged() {
        return mGoogleApiClient.isConnected();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != Activity.RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }

    public void onStart() {
        mGoogleApiClient.connect();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
    }

    public void setActivity(Activity mActivity) {
        this.mActivity = mActivity;
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PLUS_LOGIN))        //.addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    public void registerGoogleLoginListener(GoogleLoginListener mListener) {
        this.mListener = mListener;
    }

    public String getEmail() {
        if(mGoogleApiClient != null)
            return Plus.AccountApi.getAccountName(mGoogleApiClient);
        return null;
    }

    public Person getCurrentPerson(){
        if(mGoogleApiClient != null)
            return Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        return null;
    }

    @Override
    public void onConnected(Bundle bundle) {
        mShouldResolve = false;
        if (mListener != null) {
            mListener.onConnected(bundle, type);
        }
        type = ACTION.CONNECTING;
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (mListener != null)
            mListener.onConnectionSuspended(i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(mActivity, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                Toast.makeText(mActivity, "Error: " + connectionResult, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(mActivity, "Error: No se pudo conectar", Toast.LENGTH_LONG).show();
        }

        if (mListener != null)
            mListener.onConnectionFailed(connectionResult);
    }

    public interface GoogleLoginListener {
        void onConnected(Bundle bundle, ACTION action);

        void onConnectionSuspended(int i);

        void onConnectionFailed(ConnectionResult connectionResult);
    }
}
