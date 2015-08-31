package mx.onecard.parse;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by OneCardAdmon on 24/08/2015.
 */
public class Preferences {
    public static final String USER = "USER_PREFERENCES";

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue, String PREF_FILE_NAME){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String loadFromPreferences(Context context, String preferenceName, String defaultValue, String PREF_FILE_NAME){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,defaultValue);
    }
}
