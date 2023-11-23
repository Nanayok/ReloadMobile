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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class PasswordResetActivity extends AppCompatActivity {

    EditText editTextNewPassword, editTextConfirmNewPassword;
    Button buttonPasswordReset;
    String phoneNumber, newPassword, confirmNewPassword;
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        Toolbar toolbar = findViewById(R.id.toolbarResetPassword);
        setSupportActionBar(toolbar);

        // Enable the back arrow (up button)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the title for the activity (optional)
        getSupportActionBar().setTitle("");

        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("EXTRA_MESSAGE");
        System.out.println("Phone Number"+phoneNumber);

        editTextNewPassword = findViewById(R.id.edittext_new_password);
        ImageView imageViewPasswordVisibility = findViewById(R.id.imageViewPasswordVisibility);
        imageViewPasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputType = editTextNewPassword.getInputType();

                if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Change input type to password
                    editTextNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imageViewPasswordVisibility.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                } else {
                    // Change input type to visible password
                    editTextNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    imageViewPasswordVisibility.setImageResource(R.drawable.ic_baseline_visibility_24);
                }

                // Move the cursor to the end of the text
                editTextNewPassword.setSelection(editTextNewPassword.getText().length());
            }
        });

        editTextConfirmNewPassword = findViewById(R.id.edittext_confirm_new_password);
        ImageView imageViewPasswordVisibility2 = findViewById(R.id.imageViewPasswordVisibility2);
        imageViewPasswordVisibility2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputType = editTextConfirmNewPassword.getInputType();

                if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Change input type to password
                    editTextConfirmNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imageViewPasswordVisibility2.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                } else {
                    // Change input type to visible password
                    editTextConfirmNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    imageViewPasswordVisibility2.setImageResource(R.drawable.ic_baseline_visibility_24);
                }

                // Move the cursor to the end of the text
                editTextConfirmNewPassword.setSelection(editTextConfirmNewPassword.getText().length());
            }
        });

        buttonPasswordReset = findViewById(R.id.password_reset_Button);
        buttonPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("reset password button clicked");
                validateInput();
                //Toast.makeText(getApplicationContext(),"Register buttton clicked",Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void validateInput () {

        newPassword = editTextNewPassword.getText().toString().trim();
        confirmNewPassword = editTextConfirmNewPassword.getText().toString().trim();


        System.out.println("newPassword"+newPassword);
        System.out.println("confirmNewPassword"+confirmNewPassword);



        if (!newPassword.isEmpty() && !confirmNewPassword.isEmpty()) {

            if (newPassword.equals(confirmNewPassword)) {


                ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {

                try{
                    resetPassword();
                }catch(JSONException e){
                    e.printStackTrace();
                }

            } else {

                Toast.makeText(getBaseContext(),"No network connection available.",Toast.LENGTH_SHORT).show();

            }

        } else {
            // Email is invalid, show an error message to the user
            Toast.makeText(this, "Password dont match", Toast.LENGTH_SHORT).show();
        }


        } else {
            // Email is invalid, show an error message to the user
            Toast.makeText(this, "Password or new Password cannot be empty", Toast.LENGTH_SHORT).show();
        }

    }

    private void resetPassword() throws JSONException {

        hud = KProgressHUD.create(PasswordResetActivity.this)
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
            paramObject.put(Constants.KEY_PASSWORD,newPassword);


            Log.d("Tag_message_body", paramObject.toString());


            Call<String> userCall = apiService.resetPasswordUser(paramObject.toString());

            String requestUrl = userCall.request().url().toString();
            Log.d("Request_URL reset password", requestUrl);

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

                            Toast.makeText(PasswordResetActivity.this, message, Toast.LENGTH_LONG).show();

                            if(!message.isEmpty()){

                                //session.createLoginSession(email,fullname, phone);

                                System.out.println("PhoneNumber in Forgot Password"+phoneNumber);


                                Intent intent = new Intent(PasswordResetActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();

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