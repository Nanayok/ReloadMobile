package com.reload.reloadmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.reload.reloadmobile.Sessions.SessionManager;
import com.reload.reloadmobile.Utilities.Constants;
import com.reload.reloadmobile.network.ApiClient;
import com.reload.reloadmobile.network.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

     TextView textViewForgotPassword, textViewRegister;
     EditText editTextEmail, editTextPassword;
     Button buttonLogin;
     String email, password;
     KProgressHUD hud;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Session Manager
        session = new SessionManager(getApplicationContext());


        editTextPassword = findViewById(R.id.edittext_password_login);
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

        editTextEmail = findViewById(R.id.edittext_email_login);
        editTextPassword = findViewById(R.id.edittext_password_login);



        buttonLogin = findViewById(R.id.login_Button);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("login button clicked");
                validateInput();
                //Toast.makeText(getApplicationContext(),"Login buttton clicked",Toast.LENGTH_SHORT).show();

            }
        });


        textViewForgotPassword = findViewById(R.id.textview_forgot_password);
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);

            }
        });

        textViewRegister = findViewById(R.id.textview_register);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });


    }

    public void validateInput () {

//        email = editTextEmail.getText().toString().trim();
//        password = editTextPassword.getText().toString().trim();

        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        System.out.println("Email"+email);
        System.out.println("password"+password);

        if (!email.isEmpty() || isValidEmail(email)) {
            // Email is valid, process it further (e.g., send it to the server)

//            if (!password.isEmpty() && isValidPassword(password)) {
            if (!password.isEmpty()) {
                // Password is valid, process it further (e.g., save it securely)
                // Your code here...

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {

                    try{
                        loginUser();
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
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
        }

    }

    private void loginUser() throws JSONException {

//        System.out.println("Email"+email);
//        System.out.println("Password"+password);

        hud = KProgressHUD.create(LoginActivity.this)
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


            Log.d("Tag_message_body", paramObject.toString());


            Call<String> userCall = apiService.loginUser(paramObject.toString());

            String requestUrl = userCall.request().url().toString();
            Log.d("Request_URL", requestUrl);

            // Log the request headers
            Headers requestHeaders = userCall.request().headers();
            for (String name : requestHeaders.names()) {
                Log.d("Request_Header", name + ": " + requestHeaders.get(name));
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



                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();

                            if(!email.isEmpty() && !phone.isEmpty()){

//                                session.createLoginSession(email,fullname, phone);
                                //session.createLoginSession(email,fullname);



                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
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