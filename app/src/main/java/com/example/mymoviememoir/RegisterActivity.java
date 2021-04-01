package com.example.mymoviememoir;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymoviememoir.networkconnection.NetworkConnection;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    EditText etFname, etLname, etAddress, etPostcode, etEmail;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button btnRegister;

    DatePicker picker;
    Button btnGet;
    TextView tvwDob;
    String selectedDob;

    Spinner stateSpinner;
    Button btnState;
    TextView tvwState;
    String selectedState;

    TextView tvRegister;

    NetworkConnection networkConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        networkConnection = new NetworkConnection();

        etFname = (EditText) findViewById(R.id.et_fname);
        etLname = (EditText) findViewById(R.id.et_lname);
        etAddress = (EditText) findViewById(R.id.et_address);
        etPostcode = (EditText) findViewById(R.id.et_postcode);

        etEmail = (EditText) findViewById(R.id.et_email);

        // Spinner for choosing State
        tvwState = (TextView) findViewById(R.id.tv_selectedstate);
        stateSpinner = (Spinner) findViewById(R.id.stateSpinner);
        btnState = (Button) findViewById(R.id.btn_state);
        btnState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedState = stateSpinner.getSelectedItem().toString();
                tvwState.setText(selectedState);
            }
        });

        // DatePicker for choosing DOB
        // credit: https://www.tutlane.com/tutorial/android/android-datepicker-with-examples
        tvwDob = (TextView) findViewById(R.id.tv_selecteddob);
        picker = (DatePicker) findViewById(R.id.dp_dob);
        btnGet = (Button) findViewById(R.id.btn_dob);
        // credit: https://stackoverflow.com/questions/14194109/datepicker-in-android-get-month-and-day-as-mm-dd
        btnGet.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Integer month = picker.getMonth() + 1;
                Integer day = picker.getDayOfMonth();
                selectedDob = picker.getYear() + "-" + ((month.toString().length() == 1 ? "0" + month.toString() : month.toString()))
                        + "-" + ((day.toString().length() == 1 ? "0" + day.toString() : day.toString())) + "T00:00:00+11:00";
                tvwDob.setText(selectedDob);
            }
        });


        radioGroup = (RadioGroup) findViewById(R.id.radio_sex);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                String fname = etFname.getText().toString();
                String lname = etLname.getText().toString();
                String email = etEmail.getText().toString();

                int selectedGender = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) radioGroup.findViewById(selectedGender);
                String gender = (String) radioButton.getText();

                String address = etAddress.getText().toString();
                String postcode = etPostcode.getText().toString();
                int personid = networkConnection.findMaxPersonID() + 1;

                if (String.valueOf(personid) == ("") || fname == "" ||
                        lname == "" || gender == "" || selectedDob == "" ||
                        address == "" || selectedState == "" || postcode == "") {
                    Toast.makeText(getApplicationContext(), "Not enough details to register." +
                                    "Make sure all the * fields are filled.",
                            Toast.LENGTH_LONG).show();
                } else {
                    RegisterUser registerUser = new RegisterUser();
                    registerUser.execute(Integer.toString(personid), fname, lname, gender, selectedDob, address, selectedState, postcode);
                }
            }
        });

        tvRegister = (TextView) findViewById(R.id.tv_register);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private class RegisterUser extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {
            //int personid = networkConnection.findMaxPersonID() + 1;
            //String strPersonid = String.valueOf(personid);
            //String message= "The user with id: " + personid + " was added";
            String result = "";
            EditText etEmail = findViewById(R.id.et_email);
            String email = etEmail.getText().toString();

            EditText etPassword = findViewById(R.id.et_password);
            String password = etPassword.getText().toString();

            String alreadyExists = "";
            try {
                alreadyExists = networkConnection.findByEmail(email);
            } catch (Exception e) {
                e.printStackTrace();
            }

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date signUpDate = new Date();

            int creid = networkConnection.findMaxCreID() + 1;
            if (alreadyExists.equalsIgnoreCase("fail")) {
                try {
                    String hasEnoughDetails = networkConnection.addPerson(strings);
                    if (hasEnoughDetails.startsWith("<!DOCTYPE")) {
                        result = "missing";
                    } else {
                        try {
                            int personid = networkConnection.findMaxPersonID();
                            //String person = networkConnection.addPerson(strings);
                            String passhash = networkConnection.getMd5(password);
                            String id = String.valueOf(creid);
                            String strSignUpDate = df.format(signUpDate) + "T00:00:00+11:00";
                            /*char ch='"';
                                String strPersonId = "{\"personid\":" + personid + "}";*/
                            String[] details = {id, email, passhash, strSignUpDate, String.valueOf(personid)};

                            networkConnection.addCredentials(details);
                            result = "fail";

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                /*try {
                    int personid = networkConnection.findMaxPersonID();
                    //String person = networkConnection.addPerson(strings);
                    String passhash = networkConnection.getMd5(password);
                    String id = String.valueOf(creid);
                    String strSignUpDate = df.format(signUpDate) + "T00:00:00+11:00";
                    *//*char ch='"';
                    String strPersonId = "{\"personid\":" + personid + "}";*//*
                    String[] details = {id, email, passhash, strSignUpDate, String.valueOf(personid)};

                    networkConnection.addCredentials(details);
                    result = "fail";

                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            } else if (alreadyExists.equalsIgnoreCase("success")) {
                result = "success";
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("fail")) {
                TextView resultTextView = findViewById(R.id.tv_register);
                resultTextView.setText("Register Successfully. Click here to login.");
                /*Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
                finish();*/
            } else if (result.equalsIgnoreCase("success")) {
                TextView resultTextView = findViewById(R.id.tv_register);
                resultTextView.setText("Email already exists! Please try again");
            } else if (result.equalsIgnoreCase("missing")) {
                TextView resultTextView = findViewById(R.id.tv_register);
                resultTextView.setText("Missing required details. Please try again.");
            }

            /*if (result.equalsIgnoreCase("success")) {
                TextView resultTextView = findViewById(R.id.tv_register);
                resultTextView.setText("User already exists!");
            } else if (result.equalsIgnoreCase("fail")){
                TextView resultTextView = findViewById(R.id.tv_register);
                resultTextView.setText("Register Successfully");
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }*/
        }
    }

}