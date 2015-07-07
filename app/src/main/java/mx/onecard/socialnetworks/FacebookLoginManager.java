package mx.onecard.socialnetworks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by JesusDavid on 04/07/2015.
 * Clase que agrupara el funcionamiento de inicio de sesion en facebook
 */
public class FacebookLoginManager implements FacebookCallback<LoginResult>, GraphRequest.GraphJSONObjectCallback {
    private static FacebookLoginManager instance = new FacebookLoginManager();

    private FacebookLoginListener mFLListener;
    private FacebookRequestListener mFRListener;
    private List<String> defaultPermissions = Arrays.asList("email", "public_profile");
    private CallbackManager mCallbackManager;
    private LoginManager mLoginManager;

    public static FacebookLoginManager getInstance() {
        return instance;
    }

    protected FacebookLoginManager() {
        mCallbackManager = CallbackManager.Factory.create();
        mLoginManager = LoginManager.getInstance();
        mLoginManager.registerCallback(mCallbackManager, this);

    }

    public void logInWithReadPermissions(Activity activity) {
        logInWithReadPermissions(activity, defaultPermissions);
    }

    public void logInWithReadPermissions(Fragment fragment) {
        logInWithReadPermissions(fragment, defaultPermissions);
    }

    public void logInWithReadPermissions(Activity activity, List<String> permissions) {
        if (!isLogedIn()) {
            mLoginManager.logInWithReadPermissions(activity, permissions);
        }
    }

    public void logInWithReadPermissions(Fragment fragment, List<String> permissions) {
        if (!isLogedIn()) {
            mLoginManager.logInWithReadPermissions(fragment, permissions);
        }
    }

    public boolean isLogedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    public void logOut() {
        mLoginManager.logOut();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void newMeRequest(Bundle parameters, FacebookRequestListener mFRListener) {
        this.mFRListener = mFRListener;
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), this);
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void registerFacebookLoginListener(FacebookLoginListener mFLListener) {
        this.mFLListener = mFLListener;
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        if (mFLListener != null)
            mFLListener.onSuccess(loginResult);
    }

    @Override
    public void onCancel() {
        if (mFLListener != null)
            mFLListener.onCancel();
    }

    @Override
    public void onError(FacebookException e) {
        if (mFLListener != null)
            mFLListener.onError(e);
    }

    @Override
    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
        if (mFRListener != null)
            mFRListener.onCompleted(jsonObject, graphResponse);
    }

    public interface FacebookLoginListener {
        void onSuccess(LoginResult loginResult);

        void onCancel();

        void onError(FacebookException e);
    }

    public interface FacebookRequestListener {
        void onCompleted(JSONObject jsonObject, GraphResponse graphResponse);
    }
}
