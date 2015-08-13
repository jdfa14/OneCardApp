package mx.onecard.parse;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


import mx.onecard.lists.items.Card;
import mx.onecard.lists.items.NotificationItem;
import mx.onecard.services.ServerConnection;

/**
 * Created by OneCardAdmon on 20/07/2015.
 * Clase que manejara las actualizaciones de los datos del usuario
 */

//TODO incompleto
public class User {
    private String name;
    private String token;
    private ArrayList<Card> cards;
    private ArrayList<NotificationItem> notificationItems;
    private DateTime lastRequestTime;
    private ProgressDialog progressDialog = null;

    private static User instance = null;

    public static User getActualUser() {
        return instance;
    }

    public static User setNewUser(String name, String token) {
        instance = new User(name, token);
        return instance;
    }

    private User(String name, String token) {
        this.name = name;
        this.token = token;
        cards = new ArrayList<>();
        notificationItems = new ArrayList<>();
        lastRequestTime = new DateTime().minusDays(1);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void Update(final Context context, final OnUpdate mListener) {

        AsyncTask<Void, Void, JSONObject> task = new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (mListener != null) {
                    mListener.onPreUpdate();
                }
                if (progressDialog != null) {
                    progressDialog = ProgressDialog.show(
                            context,
                            "Loading",
                            "Gattering information",
                            true,
                            true,
                            new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface arg0) {
                                }
                            }
                    );
                }
            }

            @Override
            protected JSONObject doInBackground(Void... params) {
                try {
                    synchronized (this)                     // Obtrenemos el token del thread actual
                    {
                        UpdateCards();
                        UpdateNotifications();
                        this.wait(3000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                if (mListener != null) {
                    mListener.onPostUpdate();
                }
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                progressDialog = null;
            }
        }.execute();

    }

    private synchronized boolean UpdateCards() {
        DateTime requestTime = new DateTime();
        Interval interval;
        interval = new Interval(lastRequestTime, requestTime);

        if (interval.toDuration().getStandardMinutes() < 1)
            return false;
        lastRequestTime = requestTime;


        //ServerConnection service = new ServerConnection(context, this);
        //service.setConnectionParams(5000, 5000, 2);
        //service.addParameter("Nombre","Valor");
        //service.setRequestMethod(ServerConnection.POST);
        //service.execute("URL");
        //TODO Este es temporal hasta que se hagan las pruebas y se haga una verdadera conexion al servidor
        //cards.clear(); // TODO DESCOMENTAR esto cuando no sea fase de pruebas
        try {
            cards.add(JSONParser.parseCard(new JSONObject("{\n" +
                    "      \"card\": \"5362\",\n" +
                    "      \"balance\": 17.5,\n" +
                    "      \"product\": \"Despensa\",\n" +
                    "      \"transactions\": {\n" +
                    "        \"type1\": [\n" +
                    "          {\n" +
                    "            \"note\": \"Compra\",\n" +
                    "            \"merchant\": \"HEB\",\n" +
                    "            \"dtApplied\": \"2015-07-07 4:16:32\",\n" +
                    "            \"transactionAmount\": 12.5,\n" +
                    "            \"availableAmount\": 17.5\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"note\": \"Compra\",\n" +
                    "            \"merchant\": \"Soriana\",\n" +
                    "            \"dtApplied\": \"2015-07-06 4:16:32\",\n" +
                    "            \"transactionAmount\": 10,\n" +
                    "            \"availableAmount\": 30\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"note\": \"Compra\",\n" +
                    "            \"merchant\": \"HEB\",\n" +
                    "            \"dtApplied\": \"2015-07-05 4:16:32\",\n" +
                    "            \"transactionAmount\": 60,\n" +
                    "            \"availableAmount\": 40\n" +
                    "          }\n" +
                    "        ],\n" +
                    "        \"type2\": [],\n" +
                    "        \"type3\": []\n" +
                    "      }\n" +
                    "    }")));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    private synchronized void UpdateNotifications(){
        ArrayList<NotificationItem> newArray = null;
        try {
            newArray = JSONParser.parseNotificationSet(new JSONArray("[{\"id\":1,\"title\":\"Dummy Message Title\",\"description\":\"This is a simple message\",\"type\":0},{\"id\":2,\"title\":\"Dummy Warning Title\",\"description\":\"This is a warning message\",\"type\":1},{\"id\":3,\"title\":\"Dummy News Title\",\"description\":\"This is a news message for new features\",\"type\":2},{\"id\":4,\"title\":\"Dummy Default Title\",\"description\":\"This is a message withot type specified\"},{\"id\":5,\"title\":\"Dummy Message Title\",\"description\":\"This is a simple message\",\"type\":0},{\"id\":6,\"title\":\"Dummy Warning Title\",\"description\":\"Onecard acaba de ser invadida por extraterrestres con planes malignos. Se recomienda discresion\",\"type\":1},{\"id\":7,\"title\":\"Dummy News Title\",\"description\":\"OneCard le informa que el dia de mañana todas las tarjetas seran remplazadas por nuevas tarjetas porque yolo, y se les cobrara remplazo a todos.\",\"type\":2},{\"id\":8,\"title\":\"Dummy Default Title\",\"description\":\"Ahora nos pondremos a cantar. Carmen se me perdió cadenita Con el cristo del nazareno Que tú me regalaste Carmen Que tú me regalaste Carmen Carmen por eso no voy a olvidarte Si ahora te llevo dentro Carmen Muy dentro de mi pecho A ti y al nazareno A ti y al nazareno\"}]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(newArray != null)
            notificationItems = newArray;
    }

    public ArrayList<NotificationItem> getNotificationItems() {
        return notificationItems;
    }

    public interface OnUpdate {
        void onPreUpdate();
        void onPostUpdate();
    }

}
