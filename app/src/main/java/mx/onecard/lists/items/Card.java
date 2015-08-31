package mx.onecard.lists.items;

import java.util.ArrayList;

import mx.onecard.input.Formatter;
import mx.onecard.onecardapp.ApplicationOneCard;
import mx.onecard.onecardapp.R;

/**
 * Created by OneCardAdmon on 09/07/2015.
 * Encapsula la información de una tarjeta
 */
public class Card {
    //TODO corregir estos arreglos a ser mas nucleares
    private String[] mStates;                       // Arreglo con la descripcion de los estados
    private String[] mProducts;                     // Arreglo con los productos
    private int product;                            // Despensa
    private int cardNumber;                         // 5236
    private int imageResourceId;                    // ID del recurso drawable
    private int state;                              // 1-9 (activa, robada y asi)
    private double balance;
    private ArrayList<Transaction> type1;           // TOP 10
    private ArrayList<Transaction> type2;           // This Week   TODO NOT IMPLEMENTED
    private ArrayList<Transaction> type3;           // This Month  TODO NOT IMPLEMENTED

    public Card(int cardNumber, int product, double balance, int state) {
        this.cardNumber = cardNumber;
        this.balance = balance;
        this.product = product;
        this.state = state;
        setProductImage();
        mStates = ApplicationOneCard.getContext().getResources().getStringArray(R.array.states);                       // Arreglo con la descripcion de los estados
        mProducts = ApplicationOneCard.getContext().getResources().getStringArray(R.array.products);
    }

    private void setProductImage() {
        switch (product){
            case 14:
                imageResourceId = R.drawable.img_card_pantry;
                break;
            case 7 :
                imageResourceId = R.drawable.img_card_gasoline;
                break;
            case 9 :
                imageResourceId = R.drawable.img_card_diem;
                break;
            default:
                imageResourceId = R.drawable.img_card_diem_mastercard;
                break;
        }
    }

    public void update(Card card){
        this.balance = card.balance;
        this.state = card.state;
        this.type1 = card.type1;
        this.type2 = card.type2;
        this.type3 = card.type3;
    }

    public int getCardDigits(){ return cardNumber;}

    public String getCardNumber() {
        return "****-" + cardNumber; // ****-5236
    }

    public String getCardNumberMedium(){
        return "****-****-" + cardNumber;
    }

    public String getCardNumberLong(){
        return "****-****-****-" + cardNumber;
    }

    public String getProduct() {
        if(product <= 18)
            return mProducts[product];
        return mProducts[0];
    }

    public int getProductNumber(){
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

    public boolean isActive(){
        return state == 2 || state == 3;
    }

    public void swapActivation(){
        if(isActive()){
            state =  4; // Suspendida
        }else{
            state = 2;
        }
    }

    public String getState(){
        if(state > 0 && state <= 9){ // Estados existentes hasta la fecha actual 17/08/2015
            return mStates[state];
        }
        return mStates[0]; // Estado desconocido
    }
}
