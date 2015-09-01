package mx.onecard.onecardapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import mx.onecard.input.Validator.EditTextValidator;
import mx.onecard.socialnetworks.SocialNetworkSessionHandler;


public class RegisterActivity
        extends AppCompatActivity
        implements View.OnFocusChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            if(extras.containsKey("email")) {
                ((EditText) findViewById(R.id.reg_email_textbox)).setText(extras.getString("email"));
                if (findViewById(R.id.reg_psw_textbox).requestFocus())
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }

        }else{
            if(findViewById(R.id.reg_email_textbox).requestFocus())
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        findViewById(R.id.reg_email_textbox).setOnFocusChangeListener(this);
        findViewById(R.id.reg_psw_textbox).setOnFocusChangeListener(this);
        findViewById(R.id.reg_psw_confirm_textbox).setOnFocusChangeListener(this);
        findViewById(R.id.reg_card_no_textbox).setOnFocusChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //onLostFocus de textboxes

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus)
            if (!((EditText) v).getText().toString().equals("")) {// en lostfocus y no vacio
                switch (v.getId()) {
                    case R.id.reg_email_textbox: {
                        if (EditTextValidator.validateEmail((EditText) v, getResources().getString(R.string.reg_error_email))) {
                            v.setTag(getResources().getString(R.string.reg_tag_valid));
                        } else {
                            v.setTag(getResources().getString(R.string.reg_tag_invalid));
                        }
                        break;
                    }
                    case R.id.reg_psw_textbox: {
                        if (EditTextValidator.validatePassword((EditText) v, getResources().getString(R.string.reg_error_password))) {
                            v.setTag(getResources().getString(R.string.reg_tag_valid));
                        } else {
                            v.setTag(getResources().getString(R.string.reg_tag_invalid));
                        }
                        break;
                    }

                    case R.id.reg_psw_confirm_textbox: {
                        if (EditTextValidator.validateMatchPasswords((EditText) findViewById(R.id.reg_psw_textbox), (EditText) v, getResources().getString(R.string.reg_error_password_match))) {
                            v.setTag(getResources().getString(R.string.reg_tag_valid));
                        } else {
                            v.setTag(getResources().getString(R.string.reg_tag_invalid));
                        }
                        break;
                    }
                    case R.id.reg_card_no_textbox: {
                        if (EditTextValidator.validateCardNumber((EditText) v, getResources().getString(R.string.reg_error_card))) {
                            v.setTag(getResources().getString(R.string.reg_tag_valid));
                        } else {
                            v.setTag(getResources().getString(R.string.reg_tag_invalid));
                        }
                        break;
                    }
                }
            }else{
                v.setTag(getResources().getString(R.string.reg_tag_invalid));
            }
    }

    private void logout(){
        Bundle extras = getIntent().getExtras();
        if(extras.containsKey("social")){
            switch ((SocialNetworkSessionHandler.SOCIAL)extras.get("social")){
                case FACEBOOK:{

                    break;
                }
                case GOOGLE:{
                    break;
                }
                case TWITTER:{
                    break;
                }
            }
        }
    }
}
