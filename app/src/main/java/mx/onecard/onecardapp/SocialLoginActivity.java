package mx.onecard.onecardapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.ArrayList;
import java.util.List;

import mx.onecard.input.Validator;
import mx.onecard.parse.User;
import mx.onecard.socialnetworks.SocialNetworkSessionHandler;


public class SocialLoginActivity extends AppCompatActivity implements
        SocialNetworkSessionHandler.OnResponseListener,
        View.OnClickListener {

    private final int REQUEST_LOGIN = 1250;

    //Twitter stuff
    private TwitterLoginButton mTwitterLoginBtn;

    // Handler for login
    private SocialNetworkSessionHandler mSocialNetworkSessionHandler;

    private static final String TAG = "SocialLoginActivity";

    private EditText emailTextBox;
    private EditText passwordTextBox;

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
        emailTextBox = (EditText) findViewById(R.id.login_email_textbox);
        passwordTextBox = (EditText) findViewById(R.id.login_password_textbox);

        // Instancia de SessionHandler
        mSocialNetworkSessionHandler = SocialNetworkSessionHandler.getInstance(this);
        mSocialNetworkSessionHandler.setListener(this);

        //asignando click listeners
        findViewById(R.id.login_auth_button).setOnClickListener(this);
        findViewById(R.id.login_auth_register_button).setOnClickListener(this);

        findViewById(R.id.facebook_login_button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> permissions = new ArrayList<>();
                permissions.add("public_profile");
                permissions.add("email");
                mSocialNetworkSessionHandler.loginWithFacebook(SocialLoginActivity.this, permissions);
            }
        });

        findViewById(R.id.google_login_button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocialNetworkSessionHandler.loginWithGooglePlus();
            }
        });
        //findViewById(R.id.twitter_login_button2).setOnClickListener(v -> mSocialNetworkSessionHandler.loginWithTwitter());

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_CANCELED) {
                mSocialNetworkSessionHandler.logOut();
            } else if (data != null) {
                //TODO aqui mandar llamar a inicio de sesion por usuario y password EN data
                String email = data.getStringExtra("email");
                String password = data.getStringExtra("password");
                login(email, password);
            }
            return;
        }
        mSocialNetworkSessionHandler.onActivityResult(requestCode, resultCode, data);
        mTwitterLoginBtn.onActivityResult(requestCode, resultCode, data);
    }

    //ACTIVITY FUNCTIONS
    protected void login(String email, String password) {
        //TODO Comunicación con el servidor. Se invoca metodo y se
        if (true) { // IF se valida el inicio de session
            accessGranted("David", "T0K3N");// TODO Crear usuario con su verdader nombre y token
        }
    }

    protected void accessGranted(String name, String token) {
        User.setNewUser(name,token); // Instanciamos que el usuario exista
        Intent intent = new Intent(this, NavDrawActivity2.class);
        startActivity(intent);
        finish(); // Sale de queue
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_auth_button:
                if (Validator.EditTextValidator.validateEmail(emailTextBox, getResources().getString(R.string.reg_error_email))) {
                    String email = emailTextBox.getText().toString();
                    String password = passwordTextBox.getText().toString();
                    login(email, password);
                }
                break;
            case R.id.login_auth_register_button:

                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN);
                break;
        }
    }

}
