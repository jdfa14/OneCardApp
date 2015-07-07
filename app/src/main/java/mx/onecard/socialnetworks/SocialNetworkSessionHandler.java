package mx.onecard.socialnetworks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.facebook.FacebookException;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by OneCardAdmon on 30/06/2015.
 * Manejará el inicio de sesion y obtendrá los parametros necesarios
 * para validar losd atos
 */
public class SocialNetworkSessionHandler implements FacebookLoginManager.FacebookLoginListener, FacebookLoginManager.FacebookRequestListener, GoogleLoginManager.GoogleLoginListener {
    private static final String TAG = "SNSH";

    private static SocialNetworkSessionHandler instance;                // Instancia para SINGLETON
    private OnResponseListener mListener;
    private FacebookLoginManager mFacebookLoginManager;                 // Facebook API
    private GoogleLoginManager mGoogleLoginManager;                     // Google API
    private TwitterSession twitterSession;                              // Sesion de twitter

    private SOCIAL toBeRequested = SOCIAL.NOTHING;

    private Bundle data;

    public enum SOCIAL {
        USERAUTH,               // email and psw
        FACEBOOK,           // facebook login
        GOOGLE,             // google login
        TWITTER,             //
        NOTHING
    }

    public Bundle getData() {
        return data;
    }

    public enum RESPONSE {
        NO_RESPONSE,
        FAIL_ERROR,
        FAIL_WRONG_CREDENTIALS,
        FAIL_CANT_CONNECT,
        SUCCESS_NEW_USER,
        SUCCESS_REGISTERED_USER
    }

    public static SocialNetworkSessionHandler getInstance(Activity activity) {
        if (instance == null) {
            synchronized (SocialNetworkSessionHandler.class) {
                if (instance == null) {
                    instance = new SocialNetworkSessionHandler(activity);
                }
            }
        } else {
            instance.setActivity(activity);
        }

        return instance;
    }

    public void setListener(OnResponseListener mListener) {
        this.mListener = mListener;
    }

    protected SocialNetworkSessionHandler(Activity activity) {
        // Constructor vacio necesario para instanciar el objeto
        classInit(activity);
    }

    protected SocialNetworkSessionHandler(Fragment fragment) {
       classInit(fragment.getActivity());
    }

    private void classInit(Activity activity){
        mFacebookLoginManager = FacebookLoginManager.getInstance();
        mFacebookLoginManager.registerFacebookLoginListener(this);
        mGoogleLoginManager = GoogleLoginManager.getInstance(activity);
        mGoogleLoginManager.registerGoogleLoginListener(this);
    }

    public void setActivity(Activity activity){
        mGoogleLoginManager.setActivity(activity);
    }

    // FACEBOOK STUFF
    //****** START ******//
    public void loginWithFacebook(Activity activity) {// El activity o fragment se necesitan para desplegar intents y regresar a ellos
        toBeRequested = SOCIAL.FACEBOOK;
        mFacebookLoginManager.logInWithReadPermissions(activity);
    }

    public void loginWithFacebook(Fragment fragment) {
        toBeRequested = SOCIAL.FACEBOOK;
        mFacebookLoginManager.logInWithReadPermissions(fragment);
    }

    public void loginWithFacebook(Activity activity, List<String> params) {
        toBeRequested = SOCIAL.FACEBOOK;
        mFacebookLoginManager.logInWithReadPermissions(activity, params);
    }

    public void loginWithFacebook(Fragment fragment, List<String> params) {
        toBeRequested = SOCIAL.FACEBOOK;
        mFacebookLoginManager.logInWithReadPermissions(fragment, params);
    }

    public void logOutFromFacebook() {
        toBeRequested = SOCIAL.FACEBOOK;
        mFacebookLoginManager.logOut();
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        login(SOCIAL.FACEBOOK);
    }

    @Override
    public void onCancel() {
        //TODO definir que se hara aqui
    }

    @Override
    public void onError(FacebookException e) {
        //TODO definir que se desplegara aqui
    }

    @Override
    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
        try {
            if(jsonObject != null) {
                registerUser(jsonObject.getString("email"));
            }
        } catch (JSONException e) {
            setError(e.getMessage());
        }
    }
    //****** END ******//

    // GOOGLE STUFF
    //****** START ******//

    public void loginWithGooglePlus(){
        mGoogleLoginManager.logIn();
    }

    public void onStart(){
        mGoogleLoginManager.onStart();
    }

    public void onStop(){
        mGoogleLoginManager.onStop();
    }

    @Override
    public void onConnected(Bundle bundle, GoogleLoginManager.ACTION action) {
        switch (action){
            case CONNECTING:// Nada por hacer
                break;
            case LOGING_IN:
                login(SOCIAL.GOOGLE);
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //****** END ******//
    public void login(TwitterSession twitterSession) {
        this.twitterSession = twitterSession;
        login(SOCIAL.TWITTER);
    }

    private void login(SOCIAL kind) {
        data = new Bundle();                        // Nuevo set de parametros
        JSONObject result;// The json result from web services

        // Simulando comunicacion
        result = new JSONObject();
        try {
            result.put("action", "sign_up"); // or action : sign_in
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //End

        try {
            if (result.getString("action").equals("sign_up")) {
                switch (kind) {
                    case FACEBOOK: {
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,verified");
                        toBeRequested = SOCIAL.FACEBOOK;
                        mFacebookLoginManager.newMeRequest(parameters, this);
                        break;
                    }
                    case TWITTER: {
                        AccountService mTwitterAcc = Twitter.getApiClient(twitterSession).getAccountService();
                        mTwitterAcc.verifyCredentials(true, true, new Callback<User>() {
                            @Override
                            public void success(Result<User> result) {
                                String imageUrl = result.data.profileImageUrl;
                                String userName = result.data.name;
                                //TODO aqui se puede obtener algonos otros datos
                            }

                            @Override
                            public void failure(TwitterException e) {
                                setError(e.getMessage());
                            }
                        });

                        // Preguntar por EMAIL permisos
                        TwitterAuthClient mAuthClient = new TwitterAuthClient();
                        mAuthClient.requestEmail(twitterSession, new Callback<String>() {
                            @Override
                            public void success(Result<String> result) {
                                registerUser(result.data);
                            }

                            @Override
                            public void failure(TwitterException e) {
                                setError(e.getMessage());
                            }
                        });
                        break;
                    }
                    case GOOGLE: {
                        try {
                            //TODO ARREGLAR ESTO
                            if (mGoogleLoginManager.getCurrentPerson() != null) {
                                registerUser(mGoogleLoginManager.getEmail());
                            } else {
                                setError("Google Api no pudo encontrar una cuenta activa");
                            }
                        } catch (Exception e) {
                            setError(e.getMessage());
                        }
                        break;
                    }
                }
            } else if (result.getString("action").equals("sign_in")) {
                //TODO aqui para autentificar y obtener recursos
            } else {
                mListener.onResponseListener(RESPONSE.FAIL_WRONG_CREDENTIALS);
            }
        } catch (JSONException e) {
            setError(e.getMessage());
        }
    }

    public void logOut(){
        if(mFacebookLoginManager.isLogedIn())
            mFacebookLoginManager.logOut();
        if(mGoogleLoginManager.isLogged())
            mGoogleLoginManager.logOut();
    }

    private void registerUser(String email) {                // Registrar un usuario nuevo en la tabla
        data.putString("email", email);                      // Se pone el email
        if(mListener != null)
            mListener.onResponseListener(RESPONSE.SUCCESS_NEW_USER);
    }

    private void setError(String error) {
        data.putString("error", error);
        mListener.onResponseListener(RESPONSE.FAIL_ERROR);
    }

    public interface OnResponseListener {
        void onResponseListener(RESPONSE response);
    }

    public boolean isLogedIn(SOCIAL kind) {
        return mFacebookLoginManager.isLogedIn() || mGoogleLoginManager.isLogged();
    }

    //TODO Mejorar esto para que solo sea para el tipo de servicio que lo pidio, Facebook, Goolge o Twitter
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (toBeRequested){
            case USERAUTH:
                break;
            case FACEBOOK:
                mFacebookLoginManager.onActivityResult(requestCode,resultCode,data);
                break;
            case GOOGLE:
                mGoogleLoginManager.onActivityResult(requestCode,resultCode,data);
                break;
            case TWITTER:
                break;
            case NOTHING:
                break;
        }
        toBeRequested = SOCIAL.NOTHING;
    }

}
