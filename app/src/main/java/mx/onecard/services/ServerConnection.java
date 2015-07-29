package mx.onecard.services;

import android.content.Context;
import android.util.Log;

import com.savagelook.android.UrlJsonAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by JesusDavid on 7/3/2015.
 * Clase para invokar una actividad asincrona que se contactara con el servidor
 */
public class ServerConnection extends UrlJsonAsyncTask {
    private static final String TAG = "ServerConnection";
    public static final String GET = "GET";
    public static final String POST = "POST";
    private OnServerResponseListener onServerResponseListener;

    /**
     * Constructor
     *
     * @param context contexto para mostrar la barra de espera e imprimir mensaje de error
     */
    public ServerConnection(Context context, OnServerResponseListener mListener) {
        super(context);
        setOnServerResponseListener(mListener);
    }

    /**
     * @param urls SERVICE URL, Request Method, Params
     * @return JSONOject
     */
    @Override
    protected JSONObject doInBackground(String... urls) {
        if (urls.length != 1)
            return null;
        return tryConnection(urls[0]);
    }

    private JSONObject tryConnection(String url) {
        JSONObject json = new JSONObject();
        JSONObject headers = new JSONObject();
        HttpURLConnection mConnection = null;
        Map<String, List<String>> map;
        int retries = getRetryCount();
        try {
            try {
                mConnection = (HttpURLConnection) new URL(url).openConnection();

                mConnection.setRequestMethod(getRequestMethod());
                mConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                mConnection.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));
                mConnection.setUseCaches(false);
                mConnection.setDoInput(true);
                mConnection.setDoOutput(true);
                mConnection.setConnectTimeout(getTimeoutConnect());
                mConnection.setReadTimeout(getTimeoutRead());

                //send request
                OutputStream os = mConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(parameters);
                writer.flush();
                writer.close();
                os.close();

                mConnection.connect();

                json.put("response_code", mConnection.getResponseCode());

                //get response (Body)
                if (mConnection.getErrorStream() != null) {
                    InputStream is = mConnection.getErrorStream();
                    mConnection.getResponseCode();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuffer serverBody = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        serverBody.append(line);
                        serverBody.append('\r');
                    }
                    rd.close();
                    json.put("body", serverBody);
                } else if (mConnection.getInputStream() != null) {
                    InputStream is = mConnection.getInputStream();
                    mConnection.getResponseCode();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuffer serverBody = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        serverBody.append(line);
                        serverBody.append('\r');
                    }
                    rd.close();
                    json.put("body", serverBody);
                }

                // get response (headers)
                map = mConnection.getHeaderFields();
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    if (entry.getKey() != null)
                        headers.put(entry.getKey(), entry.getValue());
                }

                json.put("response_message", mConnection.getResponseMessage());
                json.put("headers", headers);
                json.put("content_type", mConnection.getContentType());

                mConnection.disconnect();

            } catch (SocketTimeoutException e) {// Error de timeout
                if (retries > 0) {
                    retries--;
                    setRetryCount(retries);
                    json = tryConnection(url);
                } else {
                    json.put("response_code", 408);
                    json.put("body", e.getMessage());
                }
            } catch (IOException e) {// Error de escritura del buffer reader
                if (retries > 0) {
                    retries--;
                    setRetryCount(retries);
                    json = tryConnection(url);
                } else {
                    Log.e(TAG, e.getMessage());
                    return null;
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        return json;
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        onServerResponseListener.onServerResponse(json);
        super.onPostExecute(json);
    }

    /**
     * Este metodo DEBE ser implementado para poder tener respuesta del servidor
     *
     * @param onServerResponseListener Clase que implementa la interfaz
     */

    public void setOnServerResponseListener(OnServerResponseListener onServerResponseListener) {
        this.onServerResponseListener = onServerResponseListener;
    }

    public interface OnServerResponseListener {
        void onServerResponse(JSONObject serverResponse);
    }
}