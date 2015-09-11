package mx.onecard.DIalogs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class CustomDatePicker extends DatePickerDialog {
    public boolean hideDay = false;
    public boolean hideMonth = false;
    public boolean hideYear = false;

    public CustomDatePicker(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }

    public void setHideElements(boolean hideDay, boolean hideMonth, boolean hideYear){
        this.hideDay = hideDay;
        this.hideMonth = hideMonth;
        this.hideYear = hideYear;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if( hideDay ) {
            int day = getContext().getResources().getIdentifier("android:id/day", null, null);
            if(day != 0){
                View view = findViewById(day);
                if(view != null){
                    view.setVisibility(View.GONE);
                }
            }
        }

        if(hideMonth) {
            int month = getContext().getResources().getIdentifier("android:id/month", null, null);
            if(month != 0){
                View view = findViewById(month);
                if(view != null){
                    view.setVisibility(View.GONE);
                }
            }
        }

        if(hideYear) {
            int year = getContext().getResources().getIdentifier("android:id/year", null, null);
            if(year != 0){
                View view = findViewById(year);
                if(view != null){
                    view.setVisibility(View.GONE);
                }
            }
        }
    }
}
