package com.androidprojects.pablo.healthscanner;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;


import org.apache.http.NameValuePair;
import org.json.JSONException;

import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Calendar;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.security.keystore.KeyProperties;
import android.security.keystore.KeyProtection;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import com.chilkatsoft.*;

/**
 * Created by pablo on 17/3/2017.
 */


public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {



    private String url_register = "http://192.168.1.5:3000/insert";
    private String url_find_user = "http://192.168.1.5:3000/find_user";

    private NodeServer ns = new NodeServer();

    private EditText mUsernameView, mFirstnameView, mLastnameView;
    private EditText mPasswordView, mPasswordConfirmView;
    private EditText mEmailView, mDateOfBirth;
    private Date dob;
    private RadioGroup mradioGenderGroup;
    private RadioButton mradioGenderButton;
    private Boolean gender;
    // ...
    String[] formatStrings = {"dd/MM/yyyy", "dd-MM-yyyy"};
    // ...
    private View mProgressView;
    private EditText mCityView;
    private View mRegisterFormView;
    private RegisterUser register_op = null;

    int PLACE_PICKER_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.ar_username);
        mFirstnameView = (EditText) findViewById(R.id.ar_firstname);
        mLastnameView = (EditText) findViewById(R.id.ar_lastname);

        mPasswordView = (EditText) findViewById(R.id.ar_password);
        mPasswordConfirmView = (EditText) findViewById(R.id.ar_password_confirm);
        mEmailView = (EditText) findViewById(R.id.ar_email);
        mCityView = (EditText) findViewById(R.id.ar_city);

        mCityView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    try {

                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                        startActivityForResult(builder.build(RegisterActivity.this), PLACE_PICKER_REQUEST);

                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }

                } else {
                }
            }
        });

        mradioGenderGroup = (RadioGroup) findViewById(R.id.ar_gender);



        mDateOfBirth = (EditText) findViewById(R.id.ar_date_of_birth);

        Button mRegisterButton = (Button) findViewById(R.id.ar_register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }

        });

        mRegisterFormView = findViewById(R.id.ar_register_form);
        mProgressView = findViewById(R.id.ar_register_progress);
    }


    private void attemptRegister() {

        if (register_op != null) {
            return;
        }
        String invalidData = "";


        if (mradioGenderGroup.getCheckedRadioButtonId() == -1 || mCityView.getText().length()==0 || mUsernameView.getText().length()==0 || mPasswordView.getText().length()==0 || mDateOfBirth.getText().length()==0 ||  mFirstnameView.getText().length()==0 || mLastnameView.getText().length()==0)
            invalidData += "All fields are necessary!\n";
        else {
            if (mPasswordView.getText().toString().length() < 6) {
                invalidData += "Password length should be at least 6 characters\n";
                mPasswordView.setText("");
                mPasswordConfirmView.setText("");
            } else if (!mPasswordView.getText().toString().equals(mPasswordConfirmView.getText().toString())) {
                invalidData = invalidData + "The passwords do not match\n";
                mPasswordView.setText("");
                mPasswordConfirmView.setText("");
            }
            if (!mEmailView.getText().toString().contains("@")) {
                invalidData = invalidData + "Invalid email\n";
                mEmailView.setText("");
            }
            //DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); // Make sure user insert date into edittext in this format.

            dob = tryParse(mDateOfBirth.getText().toString());
            if (dob == null) {
                invalidData = invalidData + "Invalid date\n";
                mDateOfBirth.setText("");
            } else {                        //never coming here because of tryParse!

                Calendar cal = Calendar.getInstance();
                cal.setLenient(false);
                cal.setTime(dob);
                try {
                    cal.getTime();
                } catch (Exception e) {
                    invalidData += "Invalid Date\n";
                    // System.out.println("Invalid date\n");
                }
            }

        }
        if (invalidData != "")
            Toast.makeText(getApplicationContext(), invalidData.substring(0, invalidData.length() - 1), Toast.LENGTH_LONG).show();

        else{
            int radio_id = mradioGenderGroup.getCheckedRadioButtonId();
            if (radio_id == R.id.ar_male)
                gender = true;
            else
                gender = false;




            register_op= new RegisterUser(mFirstnameView.getText().toString(),mLastnameView.getText().toString(),mUsernameView.getText().toString(),mPasswordView.getText().toString(),mCityView.getText().toString(),dob,gender);
           register_op.execute((Void) null);
        }

    }


    Date tryParse(String dateString) {

        for (String formatString : formatStrings)
        {
            try {
                DateFormat df = new SimpleDateFormat(formatString);
                df.setLenient(false);
                return df.parse(dateString);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public class RegisterUser extends AsyncTask<Void, Void, String> {


        private final String username,password, city, firstname, lastname;
        private final Date dob;
        private final Boolean gender;

        RegisterUser(String mfirstname, String mlastname, String musername, String mpassword, String mcity,  Date mdob, Boolean mgender) {
            username = musername;
            password = mpassword;
            city = mcity;
            firstname = mfirstname;
            lastname = mlastname;
            dob = mdob;
            gender = mgender;
        }


        @Override
        protected String doInBackground(Void... params) {


            String result = "";
            try {

                //Create data to send to server
                JSONObject dataToSend = new JSONObject();
           //     dataToSend.put("username", username);
                //       dataToSend.put("password", password);

           //     result = ns.postData(url_find_user, dataToSend);
           //     if (result == null) {
                    dataToSend = new JSONObject();
                    dataToSend.put("firstname", firstname);
                    dataToSend.put("lastname", lastname);
                    dataToSend.put("username", username);
                    dataToSend.put("password", password);
                    dataToSend.put("city",city);
                    dataToSend.put("dob",dob);
                  //  dataToSend.toString().getBytes();


                    result = ns.postData(url_register, dataToSend);
            //    }


            } catch (IOException ex) {
                return "Network error !";
            } catch (JSONException ex) {
                return "Data Invalid !";
            }

            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            mCityView.setText(result + "!");
        }


        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, RegisterActivity.this);
                //   String toastMsg = String.format("Place: %s", place.getName());
                //   Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                mCityView.setText(place.getAddress());
                mCityView.clearFocus();

            }
        }
    }

    static {
        System.loadLibrary("chilkat");
    }

}