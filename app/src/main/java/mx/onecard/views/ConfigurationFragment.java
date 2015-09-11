package mx.onecard.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.onecard.DIalogs.AddCardDialogFragment;
import mx.onecard.lists.items.Card;
import mx.onecard.onecardapp.R;
import mx.onecard.parse.User;


public class ConfigurationFragment extends Fragment implements CheckBox.OnClickListener {
    private static final String TAG = ConfigurationFragment.class.getSimpleName();
    private static ConfigurationFragment instance = new ConfigurationFragment();
    private User mUser;

    public static ConfigurationFragment getInstance() {
        return instance;
    }

    public ConfigurationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_configuration, container, false);


        if (savedInstanceState == null) {


            mUser = User.getActualUser();

            //Programacion de botones
            view.findViewById(R.id.frag_config_addCard_button).setOnClickListener(this);

            // Obteniendo las tarketas y agregandolas dinamicamtne;
            TableLayout tableAddStatus = (TableLayout) view.findViewById(R.id.frag_config_added_cards_tablelayout);
            TableLayout tableChangeStatus = (TableLayout) view.findViewById(R.id.frag_config_disable_cards_tablelayout);
            List<Card> cards = new ArrayList<>(mUser.mCardsMap.values());
            for(Card cardAux : cards){
                addRowCardWithStatus(cardAux, tableAddStatus,R.layout.table_row_add);
                addRowCardWithCheckbox(cardAux, tableChangeStatus,R.layout.table_row_change);
            }

        }
        return view;
    }

    private View addRowCard(Card card, TableLayout parent, int layout_resource) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newRow = inflater.inflate(layout_resource, parent, false);
        ((TextView) newRow.findViewById(R.id.row_card_textview)).setText(card.getCardNumberLong());
        ((TextView) newRow.findViewById(R.id.row_product_textview)).setText(card.getProduct());
        parent.addView(newRow);
        return newRow;
    }

    private void addRowCardWithCheckbox(Card card, TableLayout parent, int resource) {
        View newRow = addRowCard(card, parent, resource);
        CheckBox checkBox = ((CheckBox) newRow.findViewById(R.id.row_change_active_checkbox));
        checkBox.setChecked(card.isActive());
        checkBox.setId(R.id.table_checkbox);
        checkBox.setTag(R.id.checkbox_position, card.getCardDigits());
        checkBox.setTag(R.id.checkbox_state, card.isActive());
        checkBox.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG, isChecked + "");
            }
        });
    }
    private void addRowCardWithStatus(Card card, TableLayout parent, int resource){
        View newRow = addRowCard(card, parent,resource);
        ((TextView) newRow.findViewById(R.id.row_add_delete_textview)).setText(card.getState());
    }


    /**
     * Cambiar estatado de tarjeta de activa a inactiva y a activa
     *
     * @param key posicion de la tarjeta en el arreglo para obtener sus datos
     */
    public void swapCardStatus(final int key, final CheckBox checkBox) {
        synchronized (this) { // activamos o desactivamos una tarjeta a la vez
            final Card card = mUser.mCardsMap.get(key);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final User.OnUpdate listener = new User.OnUpdate() {

                @Override
                public void onPreUpdate() {
                    // Do pre stuff
                }

                @Override
                public void onPostUpdate(int responceCode, String message) {
                    // Do post Stuff
                }
            };

            DialogInterface.OnClickListener dialogOnClick = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == Dialog.BUTTON_POSITIVE) {
                        mUser.changeCardStatus(key, listener);
                        checkBox.setEnabled(false);
                    } else {
                        checkBox.setChecked(card.isActive());
                    }
                    dialog.dismiss();
                }
            };

            if (card.isActive()) {
                builder.setMessage(R.string.dialog_change_status_message_deactivating)
                        .setTitle(R.string.dialog_change_status_title_deactivating)
                        .setIcon(android.R.attr.alertDialogIcon)
                        .setPositiveButton(android.R.string.ok, dialogOnClick)
                        .setNegativeButton(android.R.string.cancel, dialogOnClick);
                builder.create().show();
            } else {
                builder.setMessage(R.string.dialog_change_status_message_activating)
                        .setTitle(R.string.dialog_change_status_title_activating)
                        .setIcon(android.R.attr.alertDialogIcon)
                        .setPositiveButton(android.R.string.ok, dialogOnClick)
                        .setNegativeButton(android.R.string.cancel, dialogOnClick);
                builder.create().show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.table_checkbox :
                if(v instanceof CheckBox){
                    swapCardStatus((int) v.getTag(R.id.checkbox_position), (CheckBox) v);
                }
                break;
            case R.id.frag_config_addCard_button :
                AddCardDialogFragment dialog = new AddCardDialogFragment();
                dialog.show(getActivity().getSupportFragmentManager(),this.getClass().getSimpleName());
                break;
        }
    }
}
