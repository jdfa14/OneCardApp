package mx.onecard.onecardapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import mx.onecard.socialnetworks.SocialNetworkSessionHandler;


public class SplashActivity extends Activity {

    private boolean dataLoaded = false;
    private boolean dataLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slpash);
        new LoadDataTask(this).execute();
    }

    private class LoadDataTask extends AsyncTask <Void,Integer,Void>
    {
        public SplashActivity mSplashActivity;

        public LoadDataTask(SplashActivity mSplashActivity){
            this.mSplashActivity = mSplashActivity;
        }

        @Override
        protected Void doInBackground(Void... params) {

            try
            {
                synchronized (this)                     // Obtrenemos el token del thread actual
                {
                    //Inicializando Twitter sdk
                    TwitterAuthConfig authConfig = new TwitterAuthConfig(
                            getResources().getString(R.string.twitter_consumer_key),
                            getResources().getString(R.string.twitter_consumer_secret));
                    Fabric.with(mSplashActivity, new Twitter(authConfig));

                    //Inicializando Facebook SDK
                    FacebookSdk.sdkInitialize(getApplicationContext());
                    SocialNetworkSessionHandler.initialize(getApplicationContext(),SplashActivity.this);
                    //Aqui es un codigo temporal, que solo simula cargar datos
                    this.wait(1000);
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Aqui cerramos el progres dialog, pero si no, somplemente pasamos de activity
            // mProgressDialog.dismiss();
            // Aqui una transicion
            dataLoaded = true;
            nextActivity();
        }
    }

    private void nextActivity(){
        Intent intent = new Intent(this,SocialLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();// Se borra del stack
    }

    @Override
    public void onBackPressed() {
        //Deshabilitamos el backPress :3
    }
}
