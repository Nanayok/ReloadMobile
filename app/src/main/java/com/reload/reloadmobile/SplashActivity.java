package com.reload.reloadmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.reload.reloadmobile.Sessions.SessionManager;
import com.reload.reloadmobile.Utilities.Constants;
import com.reload.reloadmobile.network.ApiClient;
import com.reload.reloadmobile.network.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import me.wangyuwei.particleview.ParticleView;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    /** Duration of wait **/
    private static int SPLASH_DISPLAY_LENGTH = 6000;
    //private static int SPLASH_DISPLAY_LENGTH = 50000;
    private ParticleView mParticleView;

    String allAdverts, topMerchants, accountNo;

    KProgressHUD hud;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        session = new SessionManager(getApplicationContext());

        mParticleView = (ParticleView) findViewById(R.id.particle_logo);

        mParticleView.startAnim();

        SystemClock.sleep(200); //ms


        SystemClock.sleep(200); //ms

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
//                Intent i = new Intent(SplashActivity.this, MainActivity.class);
//                startActivity(i);

//                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
//                startActivity(i);

//                Intent i = new Intent(SplashActivity.this, RegisterActivity.class);
//                startActivity(i);

                try{
                    getTopMerchants();
                }catch(JSONException e){
                    e.printStackTrace();
                }

//                Intent i = new Intent(SplashActivity.this, DashboardActivity.class);
//                startActivity(i);

                // close this activity

                //finish();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void getTopMerchants() throws JSONException {

                HashMap<String,String> userDetails = session.getUserDetails();
        accountNo = userDetails.get(Constants.KEY_CUSTOMER_ID);

        System.out.println("accountNo"+accountNo);

        if(accountNo == null || accountNo.trim().isEmpty())
            accountNo = "0";



        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        //try {
            JSONObject paramObject = new JSONObject();

            Call<String> userCall = apiService.getTopMerchants(accountNo);

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

                    //hud.dismiss();
                    System.out.println("ResponseCode"+response.code());

                    if (response.code() == 200){
                        //Toast.makeText(MainActivity.this,response.body(), Toast.LENGTH_LONG).show();
                        Log.d("Tag: Response", response.toString());
                        Log.d("Tag: Response Body", response.body());
                        System.out.println("Response Body"+response.body());

                        topMerchants = response.body();

//                        String responseBody = response.body();
//                        Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
//                        intent.putExtra("EXTRA_MESSAGE",responseBody);
//                        startActivity(intent);


                                try{
            getAllAdverts();
            //getAllAdverts();
        }catch(JSONException e){
            e.printStackTrace();
        }


                        //finish();


                    }else{
                        Toast.makeText(getBaseContext(),"Login Failed",Toast.LENGTH_SHORT).show();

                    }



                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });

    }

    private void getAllAdverts() throws JSONException {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        //try {
        JSONObject paramObject = new JSONObject();

        Call<String> userCall = apiService.getAllAdverts();

        String requestUrl = userCall.request().url().toString();
        Log.d("Request_URL All Adverts", requestUrl);

        // Log the request headers
        Headers requestHeaders = userCall.request().headers();
        for (String name : requestHeaders.names()) {
            Log.d("Request_Header", name + ": " + requestHeaders.get(name));
        }

        userCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                //hud.dismiss();
                System.out.println("ResponseCode"+response.code());

                if (response.code() == 200){
                    //Toast.makeText(MainActivity.this,response.body(), Toast.LENGTH_LONG).show();
                    Log.d("Tag: Response All Merchants", response.toString());
                    Log.d("Tag: Response Body All Merchants", response.body());
                    System.out.println("Response Body All Merchants"+response.body());

                    allAdverts  = response.body();
                    System.out.println("allAdverts"+allAdverts);

                   // String responseBody = response.body();
//                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
//                    intent.putExtra("EXTRA_MESSAGE_TOP_MERCHANT",topMerchants);
//                    intent.putExtra("EXTRA_MESSAGE_ALL_ADVERTS",allAdverts);
//                    startActivity(intent);


                    try{
                        getAccount();
                    }catch(JSONException e){
                        e.printStackTrace();
                    }



                }else{
                    //Toast.makeText(SplashActivity.this,"Getting all merchants failed",Toast.LENGTH_SHORT).show();

                }



            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });



    }

    private void getAccount() throws JSONException {

        HashMap<String,String> userDetails = session.getUserDetails();
        accountNo = userDetails.get(Constants.KEY_CUSTOMER_ID);

        System.out.println("accountNo"+accountNo);

        if(accountNo == null || accountNo.trim().isEmpty())
            accountNo = "0";

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        //try {
        JSONObject paramObject = new JSONObject();

        Call<String> userCall = apiService.getWalletBalance(accountNo);

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

                //hud.dismiss();
                System.out.println("ResponseCode"+response.code());

                if (response.code() == 200){
                    //Toast.makeText(MainActivity.this,response.body(), Toast.LENGTH_LONG).show();
                    Log.d("Tag: Response", response.toString());
                    Log.d("Tag: Response Body", response.body());
                    System.out.println("Response Body"+response.body());

                    String responseBody = response.body();


                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);

                        String currentBalance = jsonObject.getJSONObject("data").getString("currentBalance");

                        Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                        intent.putExtra("EXTRA_MESSAGE_TOP_MERCHANT",topMerchants);
                        intent.putExtra("EXTRA_MESSAGE_ALL_ADVERTS",allAdverts);
                        intent.putExtra("EXTRA_MESSAGE_BALANCE", currentBalance);
                        startActivity(intent);
                        finish();



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

    }




}