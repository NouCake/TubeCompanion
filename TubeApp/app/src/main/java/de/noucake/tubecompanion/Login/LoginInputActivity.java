package de.noucake.tubecompanion.Login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import de.noucake.tubecompanion.Data.DataLoader;
import de.noucake.tubecompanion.R;
import de.noucake.tubecompanion.TubeCompanion;
import de.noucake.tubecompanion.TubeHandler;

/**
 * This Activity is for the sole purpose of grabbing the login information.
 */
public class LoginInputActivity extends AppCompatActivity {

    private TextView register;
    private EditText inputUsername;
    private EditText inputPassword;
    private Button btnLogin;
    private CheckBox remember;

    private LoginInputHandler handler;
    private TubeCompanion main;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("TubeCompanion-D", "LoginActivity started");

        main = TubeCompanion.getInstance(this);
        handler = new LoginInputHandler(this);
        username = "";
        password = "";

        findViewContent();
        setListener();
        fillFields();
    }

    private void findViewContent(){
        register = findViewById(R.id.login_register);
        inputUsername = findViewById(R.id.login_input_username);
        inputPassword = findViewById(R.id.login_input_password);
        btnLogin = findViewById(R.id.login_input_button);
        remember = findViewById(R.id.login_input_remember);
    }

    private void setListener(){
        register.setOnClickListener(handler);
        btnLogin.setOnClickListener(handler);
    }

    private void fillFields(){
        String[] data = DataLoader.loadLoginData(this);
        if(data != null){
            username = data[0];
            password = data[1];
        }

        inputUsername.setText(username);
        inputPassword.setText(password);
        remember.setChecked(true);
    }

    public void onLoginClicked(){
        username = inputUsername.getText().toString();
        password = inputPassword.getText().toString();

        if(remember.isChecked()){
            DataLoader.saveLoginData(this, username, password, true);
        }
        setFormEnabled(false);

        main.getHandler().sendEmptyMessage(TubeHandler.LOGIN_INPUT_READY);
    }

    private void setFormEnabled(boolean enabled){
        inputUsername.setEnabled(enabled);
        inputPassword.setEnabled(enabled);
        remember.setEnabled(enabled);
        //TODO
        //btnLogin.setEnabled(enabled);
    }

    public void showLogin(){
        setFormEnabled(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public boolean isRememberChecked(){
        return remember.isChecked();
    }

    public String[] getLoginData(){
        String[] data = {username, password};
        return data;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TubeCompanion-D", "LoginActivity stopped");
    }
}
