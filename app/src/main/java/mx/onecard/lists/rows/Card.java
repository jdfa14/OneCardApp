package mx.onecard.lists.rows;

import mx.onecard.onecardapp.R;

/**
 * Created by OneCardAdmon on 09/07/2015.
 * Encapsula la información de una tarjeta
 */
public class Card {
    private String product;         // Despensa
    private String cardNumber;         // 5236
    private int imageResourceId;    // ID del recurso drawable
    private double balance;

    public Card(String cardNumber, int product) {
        this.cardNumber = cardNumber;
        setProduct(product);
    }

    private void setProduct(int product) {
        switch (product) {
            case 0:
                this.product = "Despensa";
                imageResourceId = R.drawable.despensa;
                break;
            case 1:
                this.product = "Viaticos y gastos";
                imageResourceId = R.drawable.viaticos;
                break;
            case 2:
                this.product = "Gasolina";
                imageResourceId = R.drawable.gasolina;
                break;
            default:
                this.product = "Nuevo Producto";
                imageResourceId = R.drawable.gasolina;
                break;
        }
    }

    public String getCardNumber() {
        return "****-****-****-" + cardNumber; // ****-****-****-5236
    }

    public String getProduct() {
        return product;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }
}
