package mx.onecard.parse;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mx.onecard.lists.items.Card;
import mx.onecard.lists.items.NotificationItem;
import mx.onecard.lists.items.Transaction;

/**
 * Created by OneCardAdmon on 17/07/2015.
 * Clase que ayudara a parsear desde un JSON a su objeto correspondiente
 */

public class JSONParser {
    private static final String TAG = JSONParser.class.getSimpleName();

    /**
     * @param cardJson Json con los elementos para crear una tarjeta entera, transacciones, numeros etc.
     * @return regresa el objeto tarjeta en caso de tener todo bien construido
     */
    public static Card parseCard(JSONObject cardJson) {
        Card card;
        JSONObject transactions;
        try {
            if (!cardJson.has("card")
                    || !cardJson.has("balance")
                    || !cardJson.has("product")
                    || !cardJson.has("transactions")) { // Si no tiene todos los elementos necesarios
                Log.e(TAG, "JSONCard malformed");
                return null;
            }
            transactions = cardJson.getJSONObject("transactions");
            if (!transactions.has("type1")
                    || !transactions.has("type2")
                    || !transactions.has("type3")){
                Log.e(TAG, "JSONObject transactions of JSONCard malformed");
                return null;
            }

            card = new Card(cardJson.getString("card"), cardJson.getString("product"), cardJson.getDouble("balance"));
            card.updateTransactions(
                    parseTransactionSet(transactions.getJSONArray("type1")),
                    parseTransactionSet(transactions.getJSONArray("type2")),
                    parseTransactionSet(transactions.getJSONArray("type3"))
            );

        } catch (JSONException e) {
            Log.e(TAG, "Error on getting elements from JSONCard: " + e.getMessage());
            card = null;
        }

        return card;
    }

    /**
     * @param transactionSetArray JSONArray con tdos los objetos de tipo transaccion0
     * @return un ArrayList<Transaction> con los objetos de transaction que se encontraban en el set
     */
    public static ArrayList<Transaction> parseTransactionSet(JSONArray transactionSetArray) {
        ArrayList<Transaction> transactionSet = new ArrayList<Transaction>();
        try {
            for (int i = 0; i < transactionSetArray.length(); i++) {
                Transaction aux = parseTransaction(transactionSetArray.getJSONObject(i));
                if (aux != null) {// La transaccion debe estar bien construida
                    transactionSet.add(aux);
                } else {
                    Log.e(TAG, "A transaction has been ignored: " + i);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error en obtener objeto de transaccion");
        }
        return transactionSet;
    }

    /**
     * @param transactionJSON Objeto JSON con los elementos que obtiene una transaccion
     * @return Objeto Transaction con los elementos parseados en caso de que el JSON este correcto
     */
    public static Transaction parseTransaction(JSONObject transactionJSON) {
        Transaction transaction;

        if (!transactionJSON.has("note")
                || !transactionJSON.has("merchant")
                || !transactionJSON.has("dtApplied")
                || !transactionJSON.has("transactionAmount")
                || !transactionJSON.has("availableAmount")) {
            Log.e(TAG, "JSONTransaction malformed");
            return null;// Si una transaccion no esta completa return null
        }
        try {// Crear una nueva transaccion con todos los parametros
            transaction = new Transaction(
                    transactionJSON.getString("note"),
                    transactionJSON.getString("merchant"),
                    transactionJSON.getString("dtApplied"),
                    transactionJSON.getDouble("transactionAmount"),
                    transactionJSON.getDouble("availableAmount"));
        } catch (JSONException e) {
            transaction = null;
            Log.e(TAG, "Error con creating a new transaction: " + e.getMessage());
        }
        return transaction;
    }

    public static ArrayList<NotificationItem> parseNotificationSet(JSONArray notificationSetArray){
        ArrayList<NotificationItem> transactionSet = new ArrayList<NotificationItem>();
        try {
            for (int i = 0; i < notificationSetArray.length(); i++) {
                NotificationItem aux = parseNotification(notificationSetArray.getJSONObject(i));
                if (aux != null) {// La transaccion debe estar bien construida
                    transactionSet.add(aux);
                } else {
                    Log.e(TAG, "A transaction has been ignored: " + i);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error en obtener objeto de transaccion");
        }
        return transactionSet;
    }

    public static NotificationItem parseNotification(JSONObject notificationJSON){
        final String fieldTitle = "title";
        final String fieldDescription = "description";
        final String fieldType = "type";
        final String fieldId = "id";

        NotificationItem notificationItem = null;
        if(!notificationJSON.has(fieldTitle)
                || !notificationJSON.has(fieldDescription)
                || !notificationJSON.has(fieldId)){
            Log.e(TAG,"JSONNotification malformed");
            return null;
        }
        try {
            int type = (notificationJSON.has(fieldType)) ? notificationJSON.getInt(fieldType) : 0;

            notificationItem = new NotificationItem(type,
                    notificationJSON.getInt(fieldId),
                    notificationJSON.getString(fieldTitle),
                    notificationJSON.getString(fieldDescription)
            );
        } catch (JSONException e) {
            Log.e(TAG,e.getMessage());
        }
        return notificationItem;
    }
}
