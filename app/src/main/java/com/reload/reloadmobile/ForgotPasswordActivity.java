package com.reload.reloadmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText editTextPhoneNumber;
    Button buttonForgotPassword;
    String phoneNumber, countryCode;
    KProgressHUD hud;
    SessionManager session;
    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Toolbar toolbarForgotPassword = findViewById(R.id.toolbarForgotPassword);
        setSupportActionBar(toolbarForgotPassword);

        // Enable the back arrow (up button)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the title for the activity (optional)
        getSupportActionBar().setTitle("");

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        countryCode = ccp.getSelectedCountryCode();

        editTextPhoneNumber = findViewById(R.id.edittext_phonenumber_forgotpassword);

        buttonForgotPassword = findViewById(R.id.forgot_password_Button);
        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("forgot password button clicked");
                validateInput();
                //Toast.makeText(getApplicationContext(),"forgot password button clicked",Toast.LENGTH_SHORT).show();

            }
        });
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

    public void validateInput () {

        phoneNumber = editTextPhoneNumber.getText().toString().trim();

        System.out.println("phoneNumber"+phoneNumber);


                if (!phoneNumber.isEmpty()) {
//                Toast.makeText(getBaseContext(),"Country code selected"+countryCode,Toast.LENGTH_SHORT).show();

                        ConnectivityManager connMgr = (ConnectivityManager)
                                getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {

                            try{
                                forgotPassword();
                            }catch(JSONException e){
                                e.printStackTrace();
                            }

                        } else {

                            Toast.makeText(getBaseContext(),"No network connection available.",Toast.LENGTH_SHORT).show();

                        }



                } else {
                    // Email is invalid, show an error message to the user
                    Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }

    }

    private void forgotPassword() throws JSONException {

//        System.out.println("Email"+email);
//        System.out.println("Password"+password);

        hud = KProgressHUD.create(ForgotPasswordActivity.this)
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

//            paramObject.put(Constants.KEY_PHONE_NUMBER,countryCode+phoneNumber);
            paramObject.put(Constants.KEY_PHONE_NUMBER,phoneNumber);


            Log.d("Tag_message_body", paramObject.toString());


            Call<String> userCall = apiService.forgotPasswordUser(paramObject.toString());

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
//                            JSONObject jobjectUser = jsonObject.getJSONObject("user");
//                            String id = jobjectUser.getString("id");
//                            String email = jobjectUser.getString("email");
//                            String fullname = jobjectUser.getString("fullname");
//                            String password = jobjectUser.getString("password");
//                            String phone = jobjectUser.getString("phone");
//                            String address = jobjectUser.getString("address");
//                            String lastLogin = jobjectUser.getString("lastLogin");
//                            String dateJoined = jobjectUser.getString("dateJoined");
//                            String isActive = jobjectUser.getString("isActive");
//                            String changePassword = jobjectUser.getString("changePassword");

//                            JSONObject jobjectBiller = jsonObject.getJSONObject("billerId");
//                            String billerId = jobjectBiller.getString("id");
//                            String billerAlias = jobjectBiller.getString("alias");
//                            String billername = jobjectBiller.getString("billername");



                            Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_LONG).show();

                            if(!message.isEmpty()){

                                //session.createLoginSession(email,fullname, phone);

                                System.out.println("PhoneNumber in Forgot Password"+phoneNumber);


                                Intent intent = new Intent(ForgotPasswordActivity.this, OTPActivity.class);
                                intent.putExtra("EXTRA_MESSAGE",phoneNumber);
                                startActivity(intent);
                                //finish();

                            }else{

                                Toast.makeText(getBaseContext(),"Forgot Password Failed",Toast.LENGTH_SHORT).show();
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


}