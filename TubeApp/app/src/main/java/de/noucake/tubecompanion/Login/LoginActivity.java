package de.noucake.tubecompanion.Login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import de.noucake.tubecompanion.R;

public class LoginActivity extends AppCompatActivity {

    private EditText inputUsername;
    private EditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUsername = findViewById(R.id.login_input_username);
        inputPassword = findViewById(R.id.login_input_password);

        inputUsername.clearFocus();

    }
}
