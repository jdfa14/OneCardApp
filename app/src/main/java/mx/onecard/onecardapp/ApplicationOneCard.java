package mx.onecard.onecardapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by OneCardAdmon on 18/08/2015.
 *
 */
public class ApplicationOneCard extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
