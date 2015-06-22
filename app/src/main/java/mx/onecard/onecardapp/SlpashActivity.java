package mx.onecard.onecardapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;


public class SlpashActivity extends Activity {
    private boolean dataLoaded = false;
    private boolean dataLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slpash);

        checkData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkData();
    }

    private void checkData(){
        if(!dataLoaded) {
            if (!dataLoading){
                new LoadDataTask().execute();
                dataLoading = true;
            }
        }
        else {
            nextActivity();
        }
    }

    private class LoadDataTask extends AsyncTask <Void,Integer,Void>
    {
        /*
        @Override
        protected void onPreExecute() {
            /* En caso de progresDialog
            mProgressDialog = new ProgressDialog(SlpashActivity.this);              // Creando un nuevo ProcessDialog para cuestiones visuales
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);      // El tipo de progressDialog a mostrar (barra horizontal)
            mProgressDialog.setTitle("Cargando...");                                // Label Titulo de la ProgressBar
            mProgressDialog.setMessage("Conectando con el servicio...");            // Comentario de la progress bar
            mProgressDialog.setCancelable(false);                                   // No se puede cancelar dando click en el boton back
            mProgressDialog.setIndeterminate(false);                                // Hay progreso, no es indeterminado
            mProgressDialog.setMax(100);                                            // Numero Maximo de progreso 0 - 100
            mProgressDialog.setProgress(0);                                         // Progreso inicial en 0
            mProgressDialog.show();                                                 // Se muestra el dialogo

        }
        */

        @Override
        protected Void doInBackground(Void... params) {
            try
            {
                synchronized (this)                     // Obtrenemos el token del thread actual
                {
                    //Aqui es un codigo temporal, que solo simula cargar datos
                    int counter = 0;
                    while(counter <= 4)
                    {
                        this.wait(850);
                        counter++;
                        publishProgress(counter*25);
                    }
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // Aqui solo pasamos el dato a la barra de progreso o elementos visuales
            //mProgressDialog.setProgress(values[0]);
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
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        //Deshabilitamos el backPress :3
    }
}
