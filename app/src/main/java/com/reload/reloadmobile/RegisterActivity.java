package com.reload.reloadmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.reload.reloadmobile.Sessions.SessionManager;
import com.reload.reloadmobile.Utilities.Constants;
import com.reload.reloadmobile.network.ApiClient;
import com.reload.reloadmobile.network.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    CountryCodePicker ccp;
    //TextView textViewPassword, textViewRegister;
    EditText editTextEmail, editTextPassword, editTextFullName, editTextPhoneNumber;
    Button buttonRegister;
    String email, password, fullName, phoneNumber, countryCode;
    KProgressHUD hud;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbarRegister);
        setSupportActionBar(toolbar);

        // Enable the back arrow (up button)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the title for the activity (optional)
        getSupportActionBar().setTitle("");

        // Session Manager
        session = new SessionManager(getApplicationContext());

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        countryCode = ccp.getSelectedCountryCode();

        editTextPassword = findViewById(R.id.edittext_password_register);
        ImageView imageViewPasswordVisibility = findViewById(R.id.imageViewPasswordVisibility);
        imageViewPasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputType = editTextPassword.getInputType();

                if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Change input type to password
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imageViewPasswordVisibility.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                } else {
                    // Change input type to visible password
                    editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    imageViewPasswordVisibility.setImageResource(R.drawable.ic_baseline_visibility_24);
                }

                // Move the cursor to the end of the text
                editTextPassword.setSelection(editTextPassword.getText().length());
            }
        });

        editTextEmail = findViewById(R.id.edittext_email_register);
        editTextPassword = findViewById(R.id.edittext_password_register);
        editTextFullName = findViewById(R.id.edittext_fullname_register);
        editTextPhoneNumber = findViewById(R.id.edittext_phonenumber_register);

        buttonRegister = findViewById(R.id.signup_continue_button);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("register button clicked");
                validateInput();
                //Toast.makeText(getApplicationContext(),"Register buttton clicked",Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void validateInput () {


        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        fullName = editTextFullName.getText().toString().trim();
        phoneNumber = editTextPhoneNumber.getText().toString().trim();

        System.out.println("Email"+email);
        System.out.println("password"+password);
        System.out.println("fullName"+fullName);
        System.out.println("phoneNumber"+phoneNumber);

        if (!fullName.isEmpty()) {

        if (!email.isEmpty() || isValidEmail(email)) {
            // Email is valid, process it further (e.g., send it to the server)

            if (!phoneNumber.isEmpty()) {
//                Toast.makeText(getBaseContext(),"Country code selected"+countryCode,Toast.LENGTH_SHORT).show();


//            if (!password.isEmpty() && isValidPassword(password)) {
            if (!password.isEmpty()) {
                // Password is valid, process it further (e.g., save it securely)
                // Your code here...

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {

                    try{
                        registerUser();
                    }catch(JSONException e){
                        e.printStackTrace();
                    }

                } else {

                    Toast.makeText(getBaseContext(),"No network connection available.",Toast.LENGTH_SHORT).show();

                }

            } else {
                // Password is invalid, show an error message to the user
                Toast.makeText(this, "Invalid password. Please follow the password criteria.", Toast.LENGTH_LONG).show();
            }

        } else {
            // Email is invalid, show an error message to the user
            Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
        }


        } else {
            // Email is invalid, show an error message to the user
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
        }

    } else {
        // Email is invalid, show an error message to the user
        Toast.makeText(this, "Invalid Fullname", Toast.LENGTH_SHORT).show();
    }

    }

    private void registerUser() throws JSONException {



//        System.out.println("Email"+email);
//        System.out.println("Password"+password);

        hud = KProgressHUD.create(RegisterActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Loading")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        try {
            JSONObject paramObject = new JSONObject();

//            paramObject.put(Constants.KEY_LOGIN_CODE, loginCode+loginCode2);
//            //paramObject.put(Constants.KEY_IMEI, imei);
//            retailerCode = loginCode+loginCode2;
//
//            session.createLoginSessionRetailerCode(retailerCode);
//
//            Log.d("retailerCode", retailerCode);
////            Log.d("password", password);
////            Log.d("email", email);

            paramObject.put(Constants.KEY_EMAIL,email);
            paramObject.put(Constants.KEY_PASSWORD,password);
            paramObject.put(Constants.KEY_FULL_NAME,fullName);
            paramObject.put(Constants.KEY_PHONE,countryCode+phoneNumber);


            Log.d("Tag_message_body", paramObject.toString());


            Call<String> userCall = apiService.registerUser(paramObject.toString());

            String requestUrl = userCall.request().url().toString();
            Log.d("Request_URL Register", requestUrl);

            // Log the request headers
            Headers requestHeaders = userCall.request().headers();
            for (String name : requestHeaders.names()) {
                Log.d("Request_Header Register", name + ": " + requestHeaders.get(name));
            }

            userCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    hud.dismiss();
                    System.out.println("ResponseCode"+response.code());

                    if (response.code() == 200){
                        //Toast.makeText(MainActivity.this,response.body(), Toast.LENGTH_LONG).show();
                        Log.d("Tag: Response", response.toString());
                        Log.d("Tag: Response Body", response.body());
                        System.out.println("Response Body"+response.body());

                        String responseBody = response.body();

                        try {
                            JSONObject jsonObject = new JSONObject(responseBody);

                            //String error = jsonObject.getString(Constants.KEY_ERROR);
                            String message = jsonObject.getString("message");
                            JSONObject jobjectUser = jsonObject.getJSONObject("user");
                            String id = jobjectUser.getString("id");
                            String email = jobjectUser.getString("email");
                            String fullname = jobjectUser.getString("fullname");
                            String password = jobjectUser.getString("password");
                            String phone = jobjectUser.getString("phone");
                            String address = jobjectUser.getString("address");
                            String lastLogin = jobjectUser.getString("lastLogin");
                            String dateJoined = jobjectUser.getString("dateJoined");
                            String isActive = jobjectUser.getString("isActive");
                            String changePassword = jobjectUser.getString("changePassword");

//                            JSONObject jobjectBiller = jsonObject.getJSONObject("billerId");
//                            String billerId = jobjectBiller.getString("id");
//                            String billerAlias = jobjectBiller.getString("alias");
//                            String billername = jobjectBiller.getString("billername");



                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();

                            if(!email.isEmpty() && !phone.isEmpty()){

//                                session.createLoginSession(email,fullname, phone);
                                //session.createLoginSession(email,fullname);


                                Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();

                            }else{

                                Toast.makeText(getBaseContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                            }





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }else{
                        Toast.makeText(getBaseContext(),"Login Failed",Toast.LENGTH_SHORT).show();

                    }



                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the back arrow click event
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Go back to the previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to validate an email address
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Method to validate a password
    private boolean isValidPassword(String password) {
        // Define the criteria for a valid password
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";

        // Check if the password matches the pattern
        return password.matches(passwordPattern);
    }
}