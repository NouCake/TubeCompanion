package de.noucake.tubecompanion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText inputUsername;
    private EditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUsername = findViewById(R.id.login_input_username);
        inputPassword = findViewById(R.id.login_input_password);

        inputUsername.clearFocus();
        inputUsername.getText().clear();

    }
}
