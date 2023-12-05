package com.reload.reloadmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

public class WalletWebViewActivity extends AppCompatActivity {

    private WebView webView;
    String amount, personEmail, personName, currency, productId, productDescription, customerAccount, transReference;

    SessionManager session;

    KProgressHUD hud;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_web_view);

        session = new SessionManager(getApplicationContext());

        webView = findViewById(R.id.walletWebView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Add JavaScript interface
        webView.addJavascriptInterface(new WalletWebViewActivity.WebAppInterface(), "AndroidInterface");

        // Load your HTML file or website URL
        //webView.loadUrl("file:///android_asset/index.html");
        //webView.loadUrl("file:///android_asset/flutter.html");
        webView.loadUrl("https://reload.ng/flutter/");
        //webView.loadUrl("http://localhost:80/flutter.html");


        // Set a WebViewClient to handle redirects and load new pages within the WebView
        webView.setWebViewClient(new WebViewClient());

        Intent intent = getIntent();
        amount = intent.getStringExtra("EXTRA_MESSAGE_AMOUNT");
        personEmail = intent.getStringExtra("EXTRA_MESSAGE_EMAIL");
        personName = intent.getStringExtra("EXTRA_MESSAGE_NAME");
        currency = intent.getStringExtra("EXTRA_MESSAGE_CURRENCY");
        transReference = intent.getStringExtra("EXTRA_MESSAGE_TRANS_REF");

        System.out.println("WebView data:"+amount+","+personEmail+","+personName+","+currency);

    }

    public class WebAppInterface {

//        @JavascriptInterface
//        public void sendDataToServer(String amt, String email, String name, String curr) {
//            // Implement your code to send data to the server here
//            // You can use libraries like Retrofit or AsyncTask for HTTP requests
//            // Example: sendDataToServerAsync(data1, data2);
//
//
//        }


        @JavascriptInterface
        public String sendData() {
            // Implement your code to send data to the server here
            // You can use libraries like Retrofit or AsyncTask for HTTP requests
            // Example: sendDataToServerAsync(data1, data2);
            System.out.println("sendData:"+amount+","+personEmail+","+personName+","+currency);
            return amount+","+personEmail+","+personName+","+currency;

        }

        @JavascriptInterface
        public void sendCallBackData() {
            // Implement your code to send data to the server here
            // You can use libraries like Retrofit or AsyncTask for HTTP requests
            // Example: sendDataToServerAsync(data1, data2);
            //System.out.println("sendData:"+amount+","+personEmail+","+personName+","+currency);

//            finish();

            try{
                callFundWallet(personEmail, personName);
            }catch(JSONException e){
                e.printStackTrace();
            }
            finish();


        }
    }

    private void callFundWallet(String email, String customerName) throws JSONException {

//        System.out.println("Email"+email);
//        System.out.println("Password"+password);

//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
//        if(acct!=null){
//            //String personName = acct.getDisplayName();
//             email = acct.getEmail();
//
//        }
//        HashMap<String,String> userDetails = session.getUserDetails();
//        email = userDetails.get(SessionManager.KEY_EMAIL);
//        fullName = userDetails.get(SessionManager.KEY_FULL_NAME);
//        System.out.println("FullName"+fullName);
//        System.out.println("Email"+email);


        hud = KProgressHUD.create(this)
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


            paramObject.put(Constants.KEY_PRODUCT_AMOUNT, amount);
            paramObject.put(Constants.KEY_PRODUCT_PAYMENT_METHOD,"billpayflutter");
            paramObject.put(Constants.KEY_TRANSACTION_REF, transReference);
//            paramObject.put(Constants.KEY_PRODUCT_EMAIL, email);
//            paramObject.put(Constants.KEY_CUSTOMER_ID, customerAccount);
//            paramObject.put(Constants.KEY_CUSTOMER_NAME, customerName);

            Log.d("Tag_message_body", paramObject.toString());

            Call<String> userCall = apiService.callPaymentIntent(paramObject.toString());

            String requestUrl = userCall.request().url().toString();
            Log.d("Request_URL Payment", requestUrl);

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
                        Log.d("Tag: Response Payment", response.toString());
                        Log.d("Tag: Response Body Payment", response.body());
                        System.out.println("Response Body Payment"+response.body());

                        String responseBody = response.body();

                        try {
                            JSONObject jsonObject = new JSONObject(responseBody);

                            String accountNumber = jsonObject.getJSONObject("account").getString("accountNo");
                            String transRef = jsonObject.getString("transRef");
                            System.out.println("Trans Ref"+transRef);
                            System.out.println("accountNumber in webview"+accountNumber);


                            session.createLoginSession(accountNumber);

//                            try{
//                                finalizePayment(transRef);
//                            }catch(JSONException e){
//                                e.printStackTrace();
//                            }

//
                            Intent intent = new Intent(WalletWebViewActivity.this, PaymentConfirmation.class);
                            intent.putExtra("EXTRA_TRANSACTION_REF", transRef);
                            intent.putExtra("EXTRA_AMOUNT", amount);
                            startActivity(intent);
//                            finish();

                            Toast.makeText(WalletWebViewActivity.this, "Payment Successful", Toast.LENGTH_LONG).show();

//                            Intent intent = new Intent(context, PaymentActivity.class);
//                            context.startActivity(intent);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }else{
                        Toast.makeText(WalletWebViewActivity.this,"Payment Failed",Toast.LENGTH_SHORT).show();

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

    private void finalizePayment(String transRef) throws JSONException {

//        HashMap<String,String> userDetails = session.getUserDetails();
//        email = userDetails.get(SessionManager.KEY_EMAIL);
//        fullName = userDetails.get(SessionManager.KEY_FULL_NAME);
//        System.out.println("FullName"+fullName);
//        System.out.println("Email"+email);
//
//        amount = editTextAmount.getText().toString().trim();
//        phoneNumber = editTextPhoneNumber.getText().toString().trim();

        hud = KProgressHUD.create(this)
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


            paramObject.put(Constants.KEY_TRANSACTION_REF, transRef);


            Log.d("Tag_message_body", paramObject.toString());

            Call<String> userCall = apiService.finalizePayment(paramObject.toString());

            String requestUrl = userCall.request().url().toString();
            Log.d("Request_URL Finalize Payment", requestUrl);

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
                        Log.d("Tag: Response Finalize Payment", response.toString());
                        Log.d("Tag: Response Body Finalize Payment", response.body());
                        System.out.println("Response Body F Payment"+response.body());

                        String responseBody = response.body();

                        try {
                            JSONObject jsonObject = new JSONObject(responseBody);

                            //String paymentResponse = jsonObject.getString(Constants.KEY_PAYMENT_RESPONSE);

//                            finish();
                            //Toast.makeText(WebViewActivity.this, paymentResponse, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(WalletWebViewActivity.this, PaymentConfirmation.class);
                            intent.putExtra("EXTRA_TRANSACTION_REF", transRef);
                            intent.putExtra("EXTRA_AMOUNT", amount);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }else{
                        Toast.makeText(WalletWebViewActivity.this,"Finalize Payment Failed",Toast.LENGTH_SHORT).show();
                        finish();

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