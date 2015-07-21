package mx.onecard.lists.item;

import java.util.Date;

import mx.onecard.input.Formatter;

/**
 * Created by OneCardAdmon on 17/07/2015.
 * Clase que encapsula los datos de una transaccion y facilita su impresion de dineroses
 */
public class Transaction {
    private String note;                // Tipo de transaccion que se realizo, Abono, compra, cancelacion etc
    private String merchant;            // El nombre del comercio en el que se uso
    private String dateApplied;         // Cuando fue aplicada la transaccion
    private double transactionAmount;   // El monto de la transaccion
    private double avaliableAmount;     // El saldo restante

    //TODO tambien puede recibir un json con todos esos atributos
    public Transaction(String note, String merchant, String dateApplied, double transactionAmount, double avaliableAmount) {
        this.note = note;
        this.merchant = merchant;
        this.transactionAmount = transactionAmount;
        this.dateApplied = dateApplied;
        this.avaliableAmount = avaliableAmount;
    }

    public String getNote() {
        return note;
    }

    public String getMerchant() {
        return merchant;
    }

    public String getTransactionAmount() {
        return Formatter.toStringCurrency(transactionAmount);
    }

    public String getDateApplied() {
        return dateApplied;
    }

    public String getAvaliableAmount() {
        return Formatter.toStringCurrency(avaliableAmount);
    }
}
