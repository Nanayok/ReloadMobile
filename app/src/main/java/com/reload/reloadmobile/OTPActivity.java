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
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.reload.reloadmobile.Utilities.Constants;
import com.reload.reloadmobile.network.ApiClient;
import com.reload.reloadmobile.network.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {

    TextView textViewPhoneNumber;
    String phoneNumber;
    EditText editTextOtp1, editTextOtp2, editTextOtp3, editTextOtp4;
    Button buttonOtp;
    String otp1, otp2, otp3, otp4;
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        Toolbar toolbarForgotPassword = findViewById(R.id.toolbarOtpPassword);
        setSupportActionBar(toolbarForgotPassword);

        // Enable the back arrow (up button)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the title for the activity (optional)
        getSupportActionBar().setTitle("");

        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("EXTRA_MESSAGE");
        System.out.println("Phone Number"+phoneNumber);

        textViewPhoneNumber = findViewById(R.id.textview_otp_phone_number);
        textViewPhoneNumber.setText(phoneNumber);

        editTextOtp1 = findViewById(R.id.edittext_otp1);
        editTextOtp2 = findViewById(R.id.edittext_otp2);
        editTextOtp3 = findViewById(R.id.edittext_otp3);
        editTextOtp4 = findViewById(R.id.edittext_otp4);

        buttonOtp = findViewById(R.id.Otp_Button);
        buttonOtp.setOnClickListener(new View.OnClickListener() {
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

        otp1 = editTextOtp1.getText().toString().trim();
        otp2 = editTextOtp2.getText().toString().trim();
        otp3 = editTextOtp3.getText().toString().trim();
        otp4 = editTextOtp4.getText().toString().trim();

        System.out.println("otp1"+otp1);
        System.out.println("otp2"+otp2);
        System.out.println("otp3"+otp3);
        System.out.println("otp4"+otp4);


        if (!otp1.isEmpty() && !otp2.isEmpty() && !otp3.isEmpty() && !otp4.isEmpty()) {
//                Toast.makeText(getBaseContext(),"Country code selected"+countryCode,Toast.LENGTH_SHORT).show();

            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {

                try{
                    OTP();
                }catch(JSONException e){
                    e.printStackTrace();
                }

            } else {

                Toast.makeText(getBaseContext(),"No network connection available.",Toast.LENGTH_SHORT).show();

            }



        } else {
            // Email is invalid, show an error message to the user
            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
        }

    }

    private void OTP() throws JSONException {

//        System.out.println("Email"+email);
//        System.out.println("Password"+password);

        hud = KProgressHUD.create(OTPActivity.this)
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
//            paramObject.put(Constants.KEY_OTP,otp1+otp2+otp3+otp4);
            paramObject.put(Constants.KEY_OTP,"123456");


            Log.d("Tag_message_body", paramObject.toString());


            Call<String> userCall = apiService.verifyOtpUser(paramObject.toString());

            String requestUrl = userCall.request().url().toString();
            Log.d("Request_URL otp verify", requestUrl);

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

                            Toast.makeText(OTPActivity.this, message, Toast.LENGTH_LONG).show();

                            if(!message.isEmpty()){

                                //session.createLoginSession(email,fullname, phone);

                                System.out.println("PhoneNumber in Forgot Password"+phoneNumber);


                                Intent intent = new Intent(OTPActivity.this, PasswordResetActivity.class);
                                intent.putExtra("EXTRA_MESSAGE",phoneNumber);
                                startActivity(intent);
                                //finish();

                            }else{

                                Toast.makeText(getBaseContext(), message,Toast.LENGTH_SHORT).show();
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