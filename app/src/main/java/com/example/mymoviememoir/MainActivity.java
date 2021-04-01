package com.example.mymoviememoir;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mymoviememoir.networkconnection.NetworkConnection;

public class MainActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    TextView tvRegister, tvDashboard;
    Button btnLogin;
    NetworkConnection networkConnection = null;

    final String base_url =
            "http://192.168.1.123:8080/MemoirMovie/webresources/memoir.credentials/findByEmailAndPassword";


    DatePicker picker;
    TextView tvwdob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkConnection = new NetworkConnection();

        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        tvDashboard = (TextView) findViewById(R.id.tv_dashboard);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = etEmail.getText().toString();
                String Password = etPassword.getText().toString();

                LoginUser loginUser = new LoginUser();
                loginUser.execute(Email, Password);

            }
        });


    }

    private class LoginUser extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {
            String email = strings[0];
            String password = strings[1];
            String result = "";
            String message = "success";
            String error = "fail";
            try {
                result = networkConnection.findByEmailAndPassword(email, password);
                if (result.equalsIgnoreCase("success")) {
                    result = message;
                } else {
                    result = error;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("fail")) {
                TextView resultTextView = findViewById(R.id.tv_login);
                resultTextView.setText("Invalid email or password");
            } else if (result.equalsIgnoreCase("success")) {
                TextView resultTextView = findViewById(R.id.tv_login);
                resultTextView.setText("Login Successfully");

                Intent i = new Intent(MainActivity.this, DashboardActivity.class);
                String email = etEmail.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                i.putExtras (bundle);
                startActivity(i);

            }
        }
    }
}
