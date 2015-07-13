package mx.onecard.input;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by OneCardAdmon on 10/07/2015.
 * Ayudara a dar formato a diferente strings
 */
public class Formatter {

    /**
     * Limpia un numro con formato en string, p.e. $250.20 => 250.20 o 78-58#.25 => 7858.25
     * @param formattedNumber '$ 52.69' o '81-75-452'
     * @return '52.69' o '8175452'
     */
    public static String cleanNumberFormat(String formattedNumber) {
        char[] stringCharArray = formattedNumber.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char ch : stringCharArray) {
            if (Character.isDigit(ch) || ch == '.') {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     *
     * @param currency 0.0
     * @return $0.00
     */
    public static String toStringCurrency(double currency){
        return NumberFormat.getCurrencyInstance(new Locale("es","MX")).format(currency); // Son pesos
    }

    /**
     *
     * @param creditCard 5555444433332222
     * @param separator - o ' '
     * @return 5555-4444-3333-2222 o 5555 4444 3333 2222
     */
    public static String toStringCard(String creditCard, String separator){
        String fixedCreditCard = cleanNumberFormat(creditCard).replace(".", ""); // remove format and doubles

        char[] numbers = fixedCreditCard.toCharArray();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numbers.length; i++) {
            if (i % 4 == 0 && i > 0) {
                sb.append(separator);
            }
            sb.append(numbers[i]);
        }
        return sb.toString();
    }

}
