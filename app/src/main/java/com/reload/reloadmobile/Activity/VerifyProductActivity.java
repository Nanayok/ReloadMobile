package com.reload.reloadmobile.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.reload.reloadmobile.Model.SlugData;
import com.reload.reloadmobile.PaymentActivity;
import com.reload.reloadmobile.PaymentConfirmation;
import com.reload.reloadmobile.R;
import com.reload.reloadmobile.Sessions.SessionManager;
import com.reload.reloadmobile.Utilities.Constants;
import com.reload.reloadmobile.WebViewActivity;
import com.reload.reloadmobile.network.ApiClient;
import com.reload.reloadmobile.network.ApiService;
import com.vinaygaba.creditcardview.CreditCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyProductActivity extends AppCompatActivity {

    TextView textViewAccountName, textViewAccountCode;
    EditText editTextAccountNumber, editTextAmount, editTextAccountCvv;
    Button buttonPayment;

    String productId, slugData, accountNumber, personName, slug, selectedTextSpinnerSlugName, firstWordName, secondWordSlug, productDescription, personEmail, amount, billerCode, accountCvv;
    KProgressHUD hud;

    private Spinner spinnerSlugNames;
    ArrayAdapter<String> spinnerAdapterSlugNames;

    SessionManager session;

    JSONArray names = null;
    ArrayList<String> list = new ArrayList<String>();
    int RC_SIGN_IN = 1000;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
//
//    Gson gson = new Gson();
//    Type packageListType = new TypeToken<List<SlugData>>() {}.getType();

    //CreditCardView creditCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_product);

        Toolbar toolbarForgotPassword = findViewById(R.id.toolbarVerifyProduct);
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


        spinnerSlugNames = (Spinner)findViewById(R.id.spinnerSlugNames);

        Intent intent = getIntent();
        slugData = intent.getStringExtra("EXTRA_SLUG_DATA");
        productId = intent.getStringExtra("EXTRA_PRODUCT_ID");
        billerCode = intent.getStringExtra("EXTRA_BILLER_CODE");
        productDescription = intent.getStringExtra("EXTRA_PRODUCT_DESCRIPTION");
        System.out.println("slugData"+slugData);
        System.out.println("productId"+productId);

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(slugData);
            names = jsonObj.getJSONArray("responseData");
            for(int i=0;i<names.length();i++) {

                JSONObject c = names.getJSONObject(i);
                //name = c.getString("name");
                slug = c.getString("slug");

//                                JSONObject c = peoples.getJSONObject(i);
                // String  categoryName = jsonObj.getString(KEY_CATEGORY);
                //list = jsonObj.getString(KEY_CATEGORY);
                //Toast.makeText(AddAnEventActivity.this,serviceName, Toast.LENGTH_SHORT).show();

//                list.add(name);
//                list.add(name+" ("+slug+")");
                list.add(slug);
            }

            spinnerAdapterSlugNames = new ArrayAdapter<String>(VerifyProductActivity.this, android.R.layout.simple_list_item_1, list);


            // Specify the layout to use when the list of choices appears
            spinnerAdapterSlugNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Apply the adapter to the spinner
            spinnerSlugNames.setAdapter(spinnerAdapterSlugNames);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Set up an OnItemSelectedListener
        spinnerSlugNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected text
                selectedTextSpinnerSlugName = (String) parentView.getItemAtPosition(position);

                // Now you can use the selectedText as needed
                // For example, you might want to display it, pass it to a method, etc.
                Toast.makeText(getApplicationContext(), "Selected: " + selectedTextSpinnerSlugName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here if not needed
            }
        });


        editTextAccountNumber = findViewById(R.id.edittext_account_number);
//        accountNumber = editTextAccountNumber.getText().toString().trim();

        editTextAmount = findViewById(R.id.edittext_amount);
//        amount = editTextAmount.getText().toString().trim();

        buttonPayment = findViewById(R.id.button_payment);
        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                accountNumber = editTextAccountNumber.getText().toString().trim();
                //if(session.isLoggedIn()){

                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(VerifyProductActivity.this);
                if(acct!=null){
                     personName = acct.getDisplayName();
                     personEmail = acct.getEmail();

                    //String token = acct.getIdToken();
                    //System.out.println("token from google"+token);

                    Toast.makeText(VerifyProductActivity.this, "Already logged in", Toast.LENGTH_LONG).show();

                    try{
                        //callPaymentIntent(acct.getEmail());
                        verifyAccount(productId, selectedTextSpinnerSlugName, billerCode, accountNumber, personName, personEmail);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }

//                    try {
//
//                    accountNumber = editTextAccountNumber.getText().toString().trim();
//                    verifyAccount(productId, selectedTextSpinnerSlugName, billerCode, accountNumber);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }




            }else{

                    Toast.makeText(VerifyProductActivity.this, "Not logged in", Toast.LENGTH_LONG).show();


                    Intent signInIntent = gsc.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);

                }
            }
        });

//        JSONObject jsonObject;
//
//        try {
//             jsonObject = new JSONObject(slugData);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//
//        List<SlugData> packages = gson.fromJson(String.valueOf(jsonObject), packageListType);
//        // Assuming you have retrieved the list of packages
//        //List<Package> packages = getPackageList(); // Replace with your actual method
//
//// Extract names from packages
//        List<String> packageNames = new ArrayList<>();
//        for (SlugData pkg : packages) {
//            packageNames.add(pkg.getName());
//        }
//
//// Populate the Spinner
//        Spinner spinner = findViewById(R.id.spinnerSlugNames);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, packageNames);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);

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

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
            //navigateToSecondActivity();
            //System.out.println("Account from google:"+account.getIdToken());

//            try{
//                callPaymentIntent(account.getEmail());
//            }catch(JSONException e){
//                e.printStackTrace();
//            }

            try{
                //callPaymentIntent(acct.getEmail());
               // verifyAccount(productId, selectedTextSpinnerSlugName, billerCode, accountNumber);
                verifyAccount(productId, selectedTextSpinnerSlugName, billerCode, accountNumber, personName, personEmail);
            }catch(JSONException e){
                e.printStackTrace();
            }


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(VerifyProductActivity.this, "Google login failed", Toast.LENGTH_LONG).show();


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


    private void verifyAccount(String productId, String productName, String billerCode, String acctNumber, String personName, String personEmail) throws JSONException {

//        System.out.println("Email"+email);
//        System.out.println("Password"+password);
        amount = editTextAmount.getText().toString().trim();

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

            paramObject.put(Constants.KEY_ACCOUNT_NUMBER,acctNumber);
            paramObject.put(Constants.KEY_BILLER_CODE,billerCode);
            paramObject.put(Constants.KEY_PRODUCT_NAME,productName);
            paramObject.put(Constants.KEY_PRODUCT_ID,productId);

//            paramObject.put(Constants.KEY_ACCOUNT_NUMBER,"04171176334");
//            paramObject.put(Constants.KEY_BILLER_CODE,"EKEDC");
//            paramObject.put(Constants.KEY_PRODUCT_NAME,"EKEDC_PREPAID");
//            paramObject.put(Constants.KEY_PRODUCT_ID,"35");

            Log.d("Tag_message_body", paramObject.toString());

            Call<String> userCall = apiService.verifyAccount(paramObject.toString());

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
                        Log.d("Tag: Response Verify", response.toString());
                        Log.d("Tag: Response Body Verify", response.body());
                        System.out.println("Response Body Verify"+response.body());

                        String responseBody = response.body();

//                                        try{
//                    callPaymentIntent(acctNumber);
//                }catch(JSONException e){
//                    e.printStackTrace();
//                }

                        try{
                            callPaymentIntent(personEmail, personName);
                        }catch(JSONException e){
                            e.printStackTrace();
                        }



//                        Intent intent = new Intent(VerifyProductActivity.this, WebViewActivity.class);
//                        intent.putExtra("EXTRA_MESSAGE_AMOUNT",amount);
//                        intent.putExtra("EXTRA_MESSAGE_EMAIL",personEmail);
//                        intent.putExtra("EXTRA_MESSAGE_NAME",personName);
//                        intent.putExtra("EXTRA_MESSAGE_CURRENCY","NGN");
//                        intent.putExtra("EXTRA_MESSAGE_PRODUCT_ID",productId);
//                        intent.putExtra("EXTRA_MESSAGE_PRODUCT_DESC",productDescription);
//                        intent.putExtra("EXTRA_MESSAGE_CUSTOMER_ACCT",accountNumber);
//                        startActivity(intent);



//                        try {
//                            JSONObject jsonObject = new JSONObject(responseBody);
//
//                            //String error = jsonObject.getString(Constants.KEY_ERROR);
////                            String message = jsonObject.getString("message");
////                            JSONObject jobjectUser = jsonObject.getJSONObject("user");
////                            String id = jobjectUser.getString("id");
////                            String email = jobjectUser.getString("email");
////                            String fullname = jobjectUser.getString("fullname");
////                            String password = jobjectUser.getString("password");
//
//                            Toast.makeText(VerifyProductActivity.this, "Verification successful", Toast.LENGTH_LONG).show();
//
////                            Intent intent = new Intent(VerifyProductActivity.this, PaymentActivity.class);
////                            startActivity(intent);
//
////                            try{
////                                callPaymentIntent(acctNumber);
////                            }catch(JSONException e){
////                                e.printStackTrace();
////                            }
//
//                            PaystackSdk.initialize(getApplicationContext());
//
//                            // This sets up the card and check for validity
//                            // This is a test card from paystack
////        String cardNumber = "4084084084084081";
////                            String cardNumber = creditCardView.getCardNumber();
////                            String expiryDate = creditCardView.getExpiryDate();
////
////                            String firstTwoChars = expiryDate.substring(0, 2);
////                            int firstTwoInt = Integer.parseInt(firstTwoChars);
////
////                            // Get the last two characters and convert to int
////                            String lastTwoChars = expiryDate.substring(2);
////                            int lastTwoInt = Integer.parseInt(lastTwoChars);
//
//
////                            int expiryMonth = 11; //any month in the future
////                            int expiryYear = 18; // any year in the future. '2018' would work also!
////                            int expiryMonth = firstTwoInt; //any month in the future
////                            int expiryYear = lastTwoInt; // any year in the future. '2018' would work also!
////                            String cvv = "408";  // cvv of the test card
//
//                            accountCvv = editTextAccountCvv.getText().toString().trim();
//
//
//                            Card card = new Card(cardNumber, expiryMonth, expiryYear, accountCvv);
//                            if (card.isValid()) {
//                                // charge card
//                                performCharge(card);
//                            } else {
//                                //do something
//                                finish();
//                                Toast.makeText(VerifyProductActivity.this, "Card Details Not Valid", Toast.LENGTH_LONG).show();
//
//                            }
//
//
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }



                    }else{
                        Toast.makeText(VerifyProductActivity.this,"Verification Failed",Toast.LENGTH_SHORT).show();

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

//    public void performCharge(Card card){
//        //create a Charge object
//        Charge charge = new Charge();
//        charge.setCard(card); //sets the card to charge
//
//        PaystackSdk.chargeCard(VerifyProductActivity.this, charge, new Paystack.TransactionCallback() {
//            @Override
//            public void onSuccess(Transaction transaction) {
//                // This is called only after transaction is deemed successful.
//                // Retrieve the transaction, and send its reference to your server
//                // for verification.
//
////                try{
////                    callPaymentIntent();
////                }catch(JSONException e){
////                    e.printStackTrace();
////                }
//
//                accountNumber = editTextAccountNumber.getText().toString().trim();
//
//                                            try{
//                                callPaymentIntent(accountNumber);
//                            }catch(JSONException e){
//                                e.printStackTrace();
//                            }
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





//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }
//    }

//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
//            //updateUI(account);
//            //navigateToSecondActivity();
//            System.out.println("Account:"+account);
//            email = account.getEmail();
//            fullname = account.getGivenName()+""+account.getFamilyName();
//            session.createLoginSession(email,fullname);
//
//            try {
//
//                int startIndex = selectedTextSpinnerSlugName.indexOf('(');
//                int endIndex = selectedTextSpinnerSlugName.indexOf(')');
//                if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
//                    // Extract the substrings
//                    firstWordName = selectedTextSpinnerSlugName.substring(0, startIndex).trim();      // "man"
//                    secondWordSlug = selectedTextSpinnerSlugName.substring(startIndex + 1, endIndex).trim(); // "boy"
//
//                    System.out.println("First Word Name: " + firstWordName);
//                    System.out.println("Second Word Slug: " + secondWordSlug);
//                } else {
//                    // Handle the case where '(' and ')' are not properly formatted
//                    System.out.println("Input does not match the expected format.");
//                }
//
//
//                verifyAccount(productId, firstWordName, secondWordSlug, accountNumber);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            Toast.makeText(VerifyProductActivity.this,"Google Signin Failed",Toast.LENGTH_SHORT).show();
//
//            //updateUI(null);
//            //navigateToSecondActivity();
//        }
//    }


//    private void callPaymentIntent(String acctNumber) throws JSONException {
//
////        System.out.println("Email"+email);
////        System.out.println("Password"+password);
//
////        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
////        if(acct!=null){
////            //String personName = acct.getDisplayName();
////             email = acct.getEmail();
////
////        }
////        HashMap<String,String> userDetails = session.getUserDetails();
////        email = userDetails.get(SessionManager.KEY_EMAIL);
//
//        amount = editTextAmount.getText().toString().trim();
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
////            paramObject.put(Constants.KEY_ACCOUNT_NUMBER,"04171176334");
////            paramObject.put(Constants.KEY_BILLER_CODE,"EKEDC");
////            paramObject.put(Constants.KEY_PRODUCT_NAME,"EKEDC_PREPAID");
////            paramObject.put(Constants.KEY_PRODUCT_ID,"35");
//
//            paramObject.put(Constants.KEY_PRODUCT_AMOUNT, amount);
//            paramObject.put(Constants.KEY_PRODUCT_DESCRIPTION, productDescription);
//            paramObject.put(Constants.KEY_PRODUCT_PAYMENT_METHOD,"billpayflutter");
//            paramObject.put(Constants.KEY_PRODUCT_ID, productId);
//            paramObject.put(Constants.KEY_PRODUCT_EMAIL, personEmail);
//            paramObject.put(Constants.KEY_CUSTOMER_ID, acctNumber);
//
//            Log.d("Tag_message_body", paramObject.toString());
//
//            Call<String> userCall = apiService.callPaymentIntent(paramObject.toString());
//
//            String requestUrl = userCall.request().url().toString();
//            Log.d("Request_URL Register", requestUrl);
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
//                        Log.d("Tag: Response Verify", response.toString());
//                        Log.d("Tag: Response Body Verify", response.body());
//                        System.out.println("Response Body Verify"+response.body());
//
//                        String responseBody = response.body();
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(responseBody);
//                            //String transRef = jsonObject.getString(Constants.KEY_TRANSACTION_REF);
//
//                            //String error = jsonObject.getString(Constants.KEY_ERROR);
////                            String message = jsonObject.getString("message");
////                            JSONObject jobjectUser = jsonObject.getJSONObject("user");
////                            String id = jobjectUser.getString("id");
////                            String email = jobjectUser.getString("email");
////                            String fullname = jobjectUser.getString("fullname");
////                            String password = jobjectUser.getString("password");
//
////                            try{
////                                finalizePayment(transRef);
////                            }catch(JSONException e){
////                                e.printStackTrace();
////                            }
//
//                            finish();
//                            // Toast.makeText(PaymentActivity.this, paymentResponse, Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(VerifyProductActivity.this, PaymentConfirmation.class);
//                            intent.putExtra("EXTRA_TRANSACTION_REF", "bp32311080004376");
//                            intent.putExtra("EXTRA_AMOUNT", amount);
//                            startActivity(intent);
//
//                            Toast.makeText(VerifyProductActivity.this, "Payment Successful", Toast.LENGTH_LONG).show();
//
////                            Intent intent = new Intent(context, PaymentActivity.class);
////                            context.startActivity(intent);
//
//
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//
//                    }else{
//                        Toast.makeText(VerifyProductActivity.this,"Payment Failed",Toast.LENGTH_SHORT).show();
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
//                            Toast.makeText(VerifyProductActivity.this, paymentResponse, Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(VerifyProductActivity.this, PaymentConfirmation.class);
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
//                        Toast.makeText(VerifyProductActivity.this,"Finalize Payment Failed",Toast.LENGTH_SHORT).show();
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

    private void callPaymentIntent(String email, String customerName) throws JSONException {

        amount = editTextAmount.getText().toString().trim();
        //phoneNumber = edittext_account_number.getText().toString().trim();
        accountNumber = editTextAccountNumber.getText().toString().trim();

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
            paramObject.put(Constants.KEY_CUSTOMER_ID, accountNumber);
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

                            Intent intent = new Intent(VerifyProductActivity.this, WebViewActivity.class);
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
                        Toast.makeText(VerifyProductActivity.this,"Payment Failed",Toast.LENGTH_SHORT).show();

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