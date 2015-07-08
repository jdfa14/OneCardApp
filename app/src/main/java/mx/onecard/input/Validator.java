package mx.onecard.input;

import android.graphics.drawable.Drawable;
import android.util.Patterns;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jesus David on 03/07/2015.
 * Esta clase simplemente validará si un editText cumple con los requisitos
 */
public class Validator {
    /**
     * {@PASSWORD_PATTERN} Borrar o agregar las condiciones necesarias
     * (			        # Start of group
     * (?=.*\d)		#   must contains one digit from 0-9
     * (?=.*[a-z])		#   must contains one lowercase characters
     * (?=.*[A-Z])		#   must contains one uppercase characters
     * (?=.*[!@#$%])	#   must contains one special symbols in the list "!@#$%"
     * .		        #   match anything with previous condition checking
     * {6,20}	        #   length at least 6 characters and maximum of 20
     * )			        # End of group
     */


    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%]).{6,20})";

    private static final String CREDIT_MASTER_CARD_PATTERN = "^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14})$";
    private static final String CREDIT_CARNET_ONECARD = "^(5062|5063)[0-9]{12}$";
    private static final String GIFT_CARD_ONECARD = "^(6046)[0-9]{12}$";

    public enum CARD_TYPE {
        UNACCEPTED_CARD,
        CARNET_CARD,
        MASTER_CARD,
        GIF_CARD
    }

    public static boolean isValidURL(String url){
        return Patterns.WEB_URL.matcher(url).matches();
    }

    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isValidCard(String card_number) {
        return getCardType(card_number) != CARD_TYPE.UNACCEPTED_CARD;
    }

    public static CARD_TYPE getCardType(String card_number) {
        Matcher matcher;
        if (Pattern.compile(CREDIT_CARNET_ONECARD).matcher(card_number).matches()) {
            return CARD_TYPE.CARNET_CARD;
        }
        if (Pattern.compile(CREDIT_MASTER_CARD_PATTERN).matcher(card_number).matches()) {
            return CARD_TYPE.MASTER_CARD;
        }
        if (Pattern.compile(GIFT_CARD_ONECARD).matcher(card_number).matches()) {
            return CARD_TYPE.GIF_CARD;
        }
        return CARD_TYPE.UNACCEPTED_CARD;
    }

    public static class EditTextValidator {
        public static boolean validateEmail(EditText email, String errorString) {
            if (!isValidEmail(email.getText().toString())) {
                email.setError(errorString);
                return false;
            }
            return true;
        }

        public static boolean validateEmail(EditText email, String errorString, Drawable imgIcon) {
            if (!isValidEmail(email.getText().toString())) {
                email.setError(errorString, imgIcon);
                return false;
            }
            return true;
        }

        public static boolean validatePassword(EditText password, String errorString) {
            if (!isValidPassword(password.getText().toString())) {
                password.setError(errorString);
                return false;
            }
            return true;
        }

        public static boolean validatePassword(EditText password, String errorString, Drawable imgIcon) {
            if (!isValidPassword(password.getText().toString())) {
                password.setError(errorString, imgIcon);
                return false;
            }
            return true;
        }

        public static boolean validateMatchPasswords(EditText password, EditText passwordConfirmation, String errorString) {
            if (!password.getText().toString().equals(passwordConfirmation.getText().toString())) {
                passwordConfirmation.setError(errorString);
                return false;
            }
            return true;
        }

        public static boolean validateMatchPasswords(EditText password, EditText passwordConfirmation, String errorString, Drawable imgIcon) {
            if (!password.getText().toString().equals(passwordConfirmation.getText().toString())) {
                passwordConfirmation.setError(errorString, imgIcon);
                return false;
            }
            return true;
        }

        public static boolean validateCardNumber(EditText card, String errorString) {
            if (!isValidCard(card.getText().toString())) {
                card.setError(errorString);
                return false;
            }
            return true;
        }

        public static boolean validateCardNumber(EditText card, String errorString, Drawable imgIcon) {
            if (!isValidCard(card.getText().toString())) {
                card.setError(errorString, imgIcon);
                return false;
            }
            return true;
        }
    }
}
