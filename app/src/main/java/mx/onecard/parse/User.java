package mx.onecard.parse;

import android.content.Context;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import mx.onecard.lists.item.Card;
import mx.onecard.services.ServerConnection;

/**
 * Created by OneCardAdmon on 20/07/2015.
 * Clase que manejara las actualizaciones de los datos del usuario
 */

//TODO incompleto
public class User implements ServerConnection.OnServerResponseListener {
    private String name;
    private String token;
    private ArrayList<Card> cards;
    private OnUpdate mListener;
    private DateTime lastRequestTime;

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
        cards = new ArrayList<Card>();
        lastRequestTime = new DateTime().minusDays(1);
        Update();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void Update(Context context, OnUpdate mListener) {
        this.mListener = mListener;

        if (!Update()) {
            mListener.onPostUpdate();
            this.mListener = null;
        } else {
            // TODO hacer algo aqui si se pudo actualizar (barra de progreso)
            mListener.onPostUpdate();
            this.mListener = null;
        }
    }

    private synchronized boolean Update() {
        DateTime requestTime = new DateTime();
        Interval interval;
        interval = new Interval(lastRequestTime, requestTime);

        if(mListener != null){
            mListener.onPreUpdate();
        }
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

            wait(3000);                     // TODO prueba
        } catch (InterruptedException e) {  // TODO prueba
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public interface OnUpdate {
        void onPreUpdate();

        void onPostUpdate();
    }

    @Override
    public void onServerResponse(JSONObject serverResponse) {
        //Todo. aqui debe cargar las transacciones y un parser del sistema
        if (mListener != null) {
            mListener.onPostUpdate();
            mListener = null;
        }
    }
}
