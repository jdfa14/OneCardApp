package mx.onecard.onecardapp;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
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

/**
 * Created by OneCardAdmon on 30/06/2015.
 * Manejará el inicio de sesion y obtendrá los parametros necesarios
 * para validar losd atos
 */
public class LoginSessionHandler {
    private static LoginSessionHandler instance = new LoginSessionHandler();    // Instancia para SINGLETON
    private static ResponseListener mListener;
    private AccessToken fbAccesToken;                                    // Access token de facebook
    private TwitterSession twitterSession;                               // Sesion de twitter
    private GoogleApiClient googleApiClient;                             // Api de Google para iniciar sesion
    private STATE signed = STATE.LOG_OFF;                                // Estado en que se encuentra el usuario
    private Bundle data;

    public enum SOCIAL {
        NONE,               // email and psw
        FACEBOOK,           // facebook login
        GOOGLE,             // google login
        TWITTER             //
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

    private enum STATE {             // Manejara los posibles estados de la sesion
        LOG_OFF,                    // Estado inicial cuando no se esta haciendo nada
        LOG_FAIL,                   // Estado intermedio que pasara a ser LOG OFF una vez leido
        LOGIN_IN,                   // Estado intermedio cunado se activo un proceso asincrono para iniciar sesion
        LOGGED_IN                   // Estado cuando fue confirmado que se inicio la sesion en el proceso asincrono
    }

    public static LoginSessionHandler getInstance(Activity activity) {
        setListener((ResponseListener)activity);
        return instance;
    }

    public static void setListener(ResponseListener mListener){
        LoginSessionHandler.mListener = mListener;
    }

    protected LoginSessionHandler() {
        // Constructor vacio necesario para instanciar el objeto
    }

    public boolean login(String email, String password) {
        if (true) {// validate with web service
            data.putString("email", email);
            data.putString("password", password);
            //TODO aqui se inicia sesion y se guarda en el dispositivo
        }
        return true;
    }

    public void login(AccessToken fbAccesToken) {
        this.fbAccesToken = fbAccesToken;
        login(SOCIAL.FACEBOOK);
    }

    public void login(TwitterSession twitterSession) {
        this.twitterSession = twitterSession;
        login(SOCIAL.TWITTER);
    }

    public void login(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
        login(SOCIAL.GOOGLE);
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
                        GraphRequest request = GraphRequest.newMeRequest(
                                fbAccesToken,
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                                        try {
                                            registerUser(jsonObject.getString("email"));
                                        } catch (JSONException e) {
                                            setError(e.getMessage());
                                        }
                                    }
                                }
                        );
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,verified");
                        request.setParameters(parameters);
                        request.executeAsync();
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
                            if (Plus.PeopleApi.getCurrentPerson(googleApiClient) != null) {
                                /**Person currentPerson = Plus.PeopleApi.getCurrentPerson(googleApiClient);
                                 * String personName = currentPerson.getDisplayName();
                                 * String personPhotoUrl = currentPerson.getImage().getUrl();
                                 * String personGooglePlusProfile = currentPerson.getUrl();
                                 * String email = Plus.AccountApi.getAccountName(googleApiClient);
                                 */
                                registerUser(Plus.AccountApi.getAccountName(googleApiClient));
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
                loadData();
            } else {
                mListener.responseListener(RESPONSE.FAIL_WRONG_CREDENTIALS);
            }
        } catch (JSONException e) {
            setError(e.getMessage());
        }
    }



    // TODO completar estas funciones que debe cargar los numeros de tarjeta, nombre del cliente y cosas similares de servicios externos
    private void loadData() {

    }

    private void registerUser(String email) {                // Registrar un usuario nuevo en la tabla
        data.putString("email", email);                      // Se pone el email
        mListener.responseListener(RESPONSE.SUCCESS_NEW_USER);
    }

    private void setError(String error) {
        data.putString("error", error);
        mListener.responseListener(RESPONSE.FAIL_ERROR);
    }

    public interface ResponseListener{
        void responseListener(RESPONSE response);
    }
}
