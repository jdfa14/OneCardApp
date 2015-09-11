package mx.onecard.DIalogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.lang.reflect.Field;

import mx.onecard.input.Validator;
import mx.onecard.onecardapp.R;
import mx.onecard.parse.User;

public class AddCardDialogFragment extends DialogFragment implements User.OnUpdate, DatePicker.OnDateChangedListener {
    private EditText mCardEditText;
    private DatePicker mDatePicker;

    String title = "Title";
    String message = "Message Body";
    int positiveBtn = R.string.dialog_default_positive_button;
    int negativeBtn = R.string.dialog_default_negative_button;

    public AddCardDialogFragment() {
        // empty constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_add_card, null);

        builder.setView(R.layout.dialog_add_card)
                //        .setMessage(message)
                .setPositiveButton(positiveBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Validator.EditTextValidator.validateCardNumber(mCardEditText, getResources().getString(R.string.reg_error_card));

                    }
                })
                .setNegativeButton(negativeBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                mDatePicker = (DatePicker) alertDialog.findViewById(R.id.dialog_addcard_expiration_datepicker);
                mCardEditText = (EditText) alertDialog.findViewById(R.id.dialog_addcard_cardnumber_editext);
                prepareDateSpinner(mDatePicker);
            }
        });

        return alertDialog;
    }

    private void prepareDateSpinner(DatePicker datePicker) {
        int year    = datePicker.getYear();
        int month   = datePicker.getMonth();
        int day     = datePicker.getDayOfMonth();
        datePicker.init(year,month,day,this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int id = Resources.getSystem().getIdentifier("day","id","android");
            View v = datePicker.findViewById(id);
            if(v != null){
                v.setVisibility(View.GONE);
            }
        } else {
            Field f[] = datePicker.getClass().getDeclaredFields();
            try {
                for (Field field : f) {
                    if (field.getName().equals("mDayPicker") || field.getName().equals("mDaySpinner")) {
                        field.setAccessible(true);
                        Object dayPicker = new Object();
                        dayPicker = field.get(datePicker);
                        ((View) dayPicker).setVisibility(View.GONE);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPreUpdate() {
        // do nothing
    }

    @Override
    public void onPostUpdate(int response, String data) {
        this.dismiss();
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }
}
