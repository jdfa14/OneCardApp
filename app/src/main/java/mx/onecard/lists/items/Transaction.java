package mx.onecard.lists.items;

import mx.onecard.input.Formatter;

/**
 * Created by OneCardAdmon on 17/07/2015.
 * Clase que encapsula los datos de una transaccion y facilita su impresion de dineroses
 */
public class Transaction {
    public String note;                // Tipo de transaccion que se realizo, Abono, compra, cancelacion etc
    public String description;         // El nombre del comercio en el que se uso
    public String dateApplied;         // Cuando fue aplicada la transaccion
    private double transactionAmount;   // El monto de la transaccion
    private double avaliableAmount;     // El saldo restante

    //TODO tambien puede recibir un json con todos esos atributos
    public Transaction(String note, String description, String dateApplied, double transactionAmount, double avaliableAmount) {
        this.note = note;
        this.description = description;
        this.transactionAmount = transactionAmount;
        this.dateApplied = dateApplied;
        this.avaliableAmount = avaliableAmount;
    }

    public String getTransactionAmount() {
        return Formatter.toStringCurrency(transactionAmount);
    }

    public String getAvaliableAmount() {
        return Formatter.toStringCurrency(avaliableAmount);
    }
}
