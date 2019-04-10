package de.noucake.tubecompanion.Login;

import android.util.Log;
import android.view.View;

import de.noucake.tubecompanion.R;

public class LoginInputHandler implements View.OnClickListener {

    private final LoginInputActivity activity;

    public LoginInputHandler(LoginInputActivity activity){
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_register:
                onRegisterClicked();
                break;
            case R.id.login_input_button:
                activity.onLoginClicked();
                break;
        }
    }

    private void onRegisterClicked(){
        Log.d("TubeCompanion-D", "Register clicked! -unimplemented yet");
    }

}
