package mx.onecard.onecardapp;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by OneCardAdmon on 20/08/2015.
 */
public class ElementWarningLayout extends RelativeLayout {
    public enum TYPE{
        BY_TIME
    }

    public ElementWarningLayout(Context context) {
        super(context);
    }

    public ElementWarningLayout(Context context, TYPE type) {
        super(context);
    }

    private TextView getStyledTextView(Context context){
        return new TextView(context);
    }
}
