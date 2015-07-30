package mx.onecard.lists.item;

import java.util.ArrayList;

import mx.onecard.input.Formatter;
import mx.onecard.onecardapp.R;

/**
 * Created by OneCardAdmon on 09/07/2015.
 * Encapsula la información de una tarjeta
 */
public class Card {
    private String product;                         // Despensa
    private String cardNumber;                      // 5236
    private int imageResourceId;                    // ID del recurso drawable
    private double balance;
    private ArrayList<Transaction> type1;           // TOP 10
    private ArrayList<Transaction> type2;           // This Week   NOT IMPLEMENTED
    private ArrayList<Transaction> type3;           // This Month  NOT IMPLEMENTED

    public Card(String cardNumber, String product, double balance) {
        this.cardNumber = cardNumber;
        this.balance = balance;
        this.product = product;
        setProductImage();
    }

    private void setProductImage() {
        if (product.equalsIgnoreCase("despensa")) {
            imageResourceId = R.drawable.despensa;
        } else if (product.equalsIgnoreCase("Viaticos y gastos")) {
            imageResourceId = R.drawable.viaticos;
        } else if (product.equalsIgnoreCase("Gasolina")) {
            imageResourceId = R.drawable.gasolina;
        } else {
            //TODO cambiar por una tarjeta default
            imageResourceId = R.drawable.gasolina;
        }
    }

    public String getCardNumber() {
        return "****-****-****-" + cardNumber; // ****-****-****-5236
    }

    public String getProduct() {
        return product;
    }

    public String getBalance() {
        return Formatter.toStringCurrency(balance);
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public ArrayList<Transaction> getType1() {
        return type1;
    }

    public ArrayList<Transaction> getType2() {
        return type2;
    }

    public ArrayList<Transaction> getType3() {
        return type3;
    }

    public void updateTransactions(ArrayList<Transaction> topTen,ArrayList<Transaction> firstHalf,ArrayList<Transaction> secondHalf) {
        this.type1 = topTen;
        this.type2 = firstHalf;
        this.type3 = secondHalf;
    }
}
