package com.reload.reloadmobile;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.reload.reloadmobile.Activity.VerifyProductActivity;
import com.reload.reloadmobile.R;
import com.reload.reloadmobile.Sessions.SessionManager;
import com.reload.reloadmobile.Utilities.Constants;
import com.reload.reloadmobile.network.ApiClient;
import com.reload.reloadmobile.network.ApiService;
import com.vinaygaba.creditcardview.CreditCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    String productId, productDescription, personEmail, amount, phoneNumber, personName, accountCvv, customerAccount;
    TextView textViewProductDescription;
    Button buttonPayment;
    EditText editTextAmount, editTextAccountNumber, editTextAccountCvv;

    SessionManager session;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    int RC_SIGN_IN = 1000;

    KProgressHUD hud;

    //CreditCardView creditCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Toolbar toolbarForgotPassword = findViewById(R.id.toolbarPayment);
        setSupportActionBar(toolbarForgotPassword);

        // Enable the back arrow (up button)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the title for the activity (optional)
        getSupportActionBar().setTitle("");

//        creditCardView= (CreditCardView)findViewById(R.id.card1);
//        creditCardView.setBackgroundResource(R.drawable.cardbackground_world);


        // Session Manager
        session = new SessionManager(getApplicationContext());
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        Intent intent = getIntent();
        productId = intent.getStringExtra("EXTRA_PRODUCT_ID");
        productDescription = intent.getStringExtra("EXTRA_PRODUCT_DESCRIPTION");

        System.out.println("productId"+productId);
        System.out.println("productDescription"+productDescription);

        editTextAmount = findViewById(R.id.edittext_amount);

        editTextAccountNumber = findViewById(R.id.edittext_account_number);

        textViewProductDescription = findViewById(R.id.textview_product_desc_text);
        textViewProductDescription.setText(productDescription);

        //editTextAccountCvv = findViewById(R.id.edittext_cvv);

        buttonPayment = findViewById(R.id.button_payment);
        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(PaymentActivity.this, "Payment button clicked", Toast.LENGTH_LONG).show();

                amount = editTextAmount.getText().toString().trim();

                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(PaymentActivity.this);
                if(acct!=null){
                     personName = acct.getDisplayName();
                     personEmail = acct.getEmail();
                    customerAccount = editTextAccountNumber.getText().toString().trim();


                    try{
                        callPaymentIntent(acct.getEmail(), acct.getDisplayName());
                    }catch(JSONException e){
                        e.printStackTrace();
                    }

//                    Intent intent = new Intent(PaymentActivity.this, WebViewActivity.class);
//                    intent.putExtra("EXTRA_MESSAGE_AMOUNT",amount);
//                    intent.putExtra("EXTRA_MESSAGE_EMAIL",personEmail);
//                    intent.putExtra("EXTRA_MESSAGE_NAME",personName);
//                    intent.putExtra("EXTRA_MESSAGE_CURRENCY","NGN");
//                    intent.putExtra("EXTRA_MESSAGE_PRODUCT_ID",productId);
//                    intent.putExtra("EXTRA_MESSAGE_PRODUCT_DESC",productDescription);
//                    intent.putExtra("EXTRA_MESSAGE_CUSTOMER_ACCT",customerAccount);
//                    startActivity(intent);
//                    finish();


                }else{

                    //Toast.makeText(PaymentActivity.this, "Not logged in", Toast.LENGTH_LONG).show();

                    Intent signInIntent = gsc.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);

                }
            }
        });



//        PaystackSdk.initialize(getApplicationContext());
//
//        // This sets up the card and check for validity
//        // This is a test card from paystack
////        String cardNumber = "4084084084084081";
//        String cardNumber = creditCardView.getCardNumber();
//        //creditCardView
//
//        int expiryMonth = 11; //any month in the future
//        int expiryYear = 18; // any year in the future. '2018' would work also!
//        String cvv = "408";  // cvv of the test card
//
//        Card card = new Card(cardNumber, expiryMonth, expiryYear, cvv);
//        if (card.isValid()) {
//            // charge card
//            performCharge(card);
//        } else {
//            //do something
//        }

        //PaystackSdk.setPublicKey("pk_test_46d94e01c29197f534e456edbd41ee7a28083aa8");


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

                                try{
                        callPaymentIntent(account.getEmail(), account.getDisplayName());
                    }catch(JSONException e){
                        e.printStackTrace();
                    }

//            Intent intent = new Intent(PaymentActivity.this, WebViewActivity.class);
//            intent.putExtra("EXTRA_MESSAGE_AMOUNT",amount);
//            intent.putExtra("EXTRA_MESSAGE_EMAIL",personEmail);
//            intent.putExtra("EXTRA_MESSAGE_NAME",personName);
//            intent.putExtra("EXTRA_MESSAGE_CURRENCY","NGN");
//            startActivity(intent);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

            Toast.makeText(PaymentActivity.this, "Google login failed", Toast.LENGTH_LONG).show();


            //updateUI(null);
            //navigateToSecondActivity();
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

    // This is the subroutine you will call after creating the charge
    // adding a card and setting the access_code
//    public void performCharge(Card card){
//        //create a Charge object
//        Charge charge = new Charge();
//        charge.setCard(card); //sets the card to charge
//
//        PaystackSdk.chargeCard(PaymentActivity.this, charge, new Paystack.TransactionCallback() {
//            @Override
//            public void onSuccess(Transaction transaction) {
//                // This is called only after transaction is deemed successful.
//                // Retrieve the transaction, and send its reference to your server
//                // for verification.
//
//                                    try{
//                        callPaymentIntent();
//                    }catch(JSONException e){
//                        e.printStackTrace();
//                    }
//            }
//
//            @Override
//            public void beforeValidate(Transaction transaction) {
//                // This is called only before requesting OTP.
//                // Save reference so you may send to server. If
//                // error occurs with OTP, you should still verify on server.
//            }
//
////            @Override
////            public void showLoading(Boolean isProcessing) {
////                // This is called whenever the SDK makes network requests.
////                // Use this to display loading indicators in your application UI
////            }
//
//            @Override
//            public void onError(Throwable error, Transaction transaction) {
//                //handle error here
//            }
//
//        });
//    }

    private void callPaymentIntent(String email, String customerName) throws JSONException {

        amount = editTextAmount.getText().toString().trim();
        customerAccount = editTextAccountNumber.getText().toString().trim();

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
            paramObject.put(Constants.KEY_PRODUCT_DESCRIPTION, productDescription);
            paramObject.put(Constants.KEY_PRODUCT_PAYMENT_METHOD,"billpayflutter");
            paramObject.put(Constants.KEY_PRODUCT_ID_PAYMENT, productId);
            paramObject.put(Constants.KEY_PRODUCT_EMAIL, email);
            paramObject.put(Constants.KEY_CUSTOMER_ID, customerAccount);
            paramObject.put(Constants.KEY_CUSTOMER_NAME, customerName);

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
                            String transactionRef = jsonObject.getString("transRef");
                            session.createLoginSession(accountNumber);

                                        Intent intent = new Intent(PaymentActivity.this, WebViewActivity.class);
            intent.putExtra("EXTRA_MESSAGE_AMOUNT",amount);
            intent.putExtra("EXTRA_MESSAGE_EMAIL",personEmail);
            intent.putExtra("EXTRA_MESSAGE_NAME",personName);
            intent.putExtra("EXTRA_MESSAGE_ACCOUNT",accountNumber);
            intent.putExtra("EXTRA_MESSAGE_TRANS_REF",transactionRef);
            intent.putExtra("EXTRA_MESSAGE_CURRENCY","NGN");
            startActivity(intent);
                            finish();

                           // Toast.makeText(PaymentActivity.this, paymentResponse, Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(PaymentActivity.this, PaymentConfirmation.class);
//                            intent.putExtra("EXTRA_TRANSACTION_REF", "bp32311080004376");
//                            intent.putExtra("EXTRA_AMOUNT", amount);
//                            startActivity(intent);

                            //Toast.makeText(PaymentActivity.this, "Payment Successful", Toast.LENGTH_LONG).show();

//                            Intent intent = new Intent(context, PaymentActivity.class);
//                            context.startActivity(intent);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }else{
                        Toast.makeText(PaymentActivity.this,"Payment Failed",Toast.LENGTH_SHORT).show();

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


//    private void finalizePayment(String transRef) throws JSONException {
//
////        HashMap<String,String> userDetails = session.getUserDetails();
////        email = userDetails.get(SessionManager.KEY_EMAIL);
////        fullName = userDetails.get(SessionManager.KEY_FULL_NAME);
////        System.out.println("FullName"+fullName);
////        System.out.println("Email"+email);
////
////        amount = editTextAmount.getText().toString().trim();
////        phoneNumber = editTextPhoneNumber.getText().toString().trim();
//
//        hud = KProgressHUD.create(this)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setLabel("Please wait")
//                .setDetailsLabel("Loading")
//                .setCancellable(true)
//                .setAnimationSpeed(2)
//                .setDimAmount(0.5f)
//                .show();
//
//        ApiService apiService = ApiClient.getClient().create(ApiService.class);
//
//        try {
//            JSONObject paramObject = new JSONObject();
//
//
//            paramObject.put(Constants.KEY_TRANSACTION_REF, transRef);
//
//
//            Log.d("Tag_message_body", paramObject.toString());
//
//            Call<String> userCall = apiService.finalizePayment(paramObject.toString());
//
//            String requestUrl = userCall.request().url().toString();
//            Log.d("Request_URL Finalize Payment", requestUrl);
//
//            // Log the request headers
//            Headers requestHeaders = userCall.request().headers();
//            for (String name : requestHeaders.names()) {
//                Log.d("Request_Header Register", name + ": " + requestHeaders.get(name));
//            }
//
//            userCall.enqueue(new Callback<String>() {
//                @Override
//                public void onResponse(Call<String> call, Response<String> response) {
//
//                    hud.dismiss();
//                    System.out.println("ResponseCode"+response.code());
//
//                    if (response.code() == 200){
//                        //Toast.makeText(MainActivity.this,response.body(), Toast.LENGTH_LONG).show();
//                        Log.d("Tag: Response Finalize Payment", response.toString());
//                        Log.d("Tag: Response Body Finalize Payment", response.body());
//                        System.out.println("Response Body F Payment"+response.body());
//
//                        String responseBody = response.body();
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(responseBody);
//
//                            String paymentResponse = jsonObject.getString(Constants.KEY_PAYMENT_RESPONSE);
//
//                            finish();
//                            Toast.makeText(PaymentActivity.this, paymentResponse, Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(PaymentActivity.this, PaymentConfirmation.class);
//                            intent.putExtra("EXTRA_TRANSACTION_REF", transRef);
//                            intent.putExtra("EXTRA_AMOUNT", amount);
//                            startActivity(intent);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//
//                    }else{
//                        Toast.makeText(PaymentActivity.this,"Finalize Payment Failed",Toast.LENGTH_SHORT).show();
//
//                    }
//
//
//
//                }
//
//                @Override
//                public void onFailure(Call<String> call, Throwable t) {
//
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

}