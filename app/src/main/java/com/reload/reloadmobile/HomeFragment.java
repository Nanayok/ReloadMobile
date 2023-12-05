package com.reload.reloadmobile;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.reload.reloadmobile.Activity.AirtimeFragment;
import com.reload.reloadmobile.Activity.ElectricityFragment;
import com.reload.reloadmobile.Adapter.CardAdapter;
import com.reload.reloadmobile.Adapter.ProductAdapter;
import com.reload.reloadmobile.Adapter.TransactionAdapter;
import com.reload.reloadmobile.Model.CardItem;
import com.reload.reloadmobile.Model.Product;
import com.reload.reloadmobile.Model.Transaction;
import com.reload.reloadmobile.Sessions.SessionManager;
import com.reload.reloadmobile.Utilities.Constants;
import com.reload.reloadmobile.network.ApiClient;
import com.reload.reloadmobile.network.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private SliderLayout mDemoSlider;
    private RecyclerView recyclerView, recyclerViewTopMerchants, recyclerViewTransactions;
    private CardAdapter cardAdapter;
    private ProductAdapter productAdapter;

    private TransactionAdapter transactionAdapter;
    TextView textViewAllMerchants, textViewWalletBalance;
    EditText editTextTopupAmount;
    String topMerchants, allMerchants, allAdverts, walletBalance, accountNo, topupAmount, personEmail, personName;
    RelativeLayout relativeLayoutTopUp;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    int RC_SIGN_IN = 1000;

    KProgressHUD hud;

    HashMap<String,String> url_maps = new HashMap<String, String>();

    SessionManager session;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        DashboardActivity activity = (DashboardActivity) getActivity();
         topMerchants = activity.getMyData();
        System.out.println("topMerchants"+topMerchants);

        allAdverts = activity.getAllAdvertsData();
        System.out.println("AllAdverts"+allAdverts);

        walletBalance = activity.getWalletBalance();
        System.out.println("walletBalance"+walletBalance);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Perform any additional setup or view binding here

        session = new SessionManager(getContext());

        mDemoSlider = (SliderLayout)view.findViewById(R.id.slider);

                    try {
                        JSONArray jsonArrayAdverts = new JSONArray(allAdverts);
                        System.out.println("jsonArrayAdverts"+jsonArrayAdverts);

                        // Iterate through the array
                        for (int i = 0; i < jsonArrayAdverts.length(); i++) {

                            JSONObject jsonObject = jsonArrayAdverts.optJSONObject(i);

                            // Check if the object is null
                            if (jsonObject != null) {

                                String id = jsonObject.optString("id");
                                String imageUrl = jsonObject.optString("imageUrl");
//
                                System.out.println("imageUrl: " + imageUrl);
                                //System.out.println();


                                //productItems.add(new Product(id, logourl, productname, description, pdtCategoryId, billerCode, processor));
                                url_maps.put(id, imageUrl);



                            }


                        }


                        for (String company : url_maps.keySet()) {
                            System.out.println("Company_adverts: " + company);
                            System.out.println("URL_adverts: " + url_maps.get(company));
                        }

                        for(String name : url_maps.keySet()){
                            TextSliderView textSliderView = new TextSliderView(getActivity());
                            // initialize a SliderLayout
                            textSliderView
                                    .description(name)
                                    .image(url_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                    .setOnSliderClickListener(this);

                            //add your extra information
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra",name);

                            mDemoSlider.addSlider(textSliderView);
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }



//        HashMap<String,String> url_maps = new HashMap<String, String>();
//        url_maps.put("BEDC", "https://www.reload.ng/reloadng/static/media/1.77747a6326784ed83a58.jpeg");
//        url_maps.put("DSTV", "https://www.reload.ng/reloadng/static/media/3.83f9f4f4c051de70f71f.jpeg");
//        url_maps.put("EKED", "https://www.reload.ng/reloadng/static/media/6.dfdf853b8122028a6115.jpeg");
//        url_maps.put("", "https://blacksiliconimages.s3.us-west-2.amazonaws.com/Reload.ng/GoTV.jpeg");
//


//        for(String name : url_maps.keySet()){
//            TextSliderView textSliderView = new TextSliderView(getActivity());
//            // initialize a SliderLayout
//            textSliderView
//                    .description(name)
//                    .image(url_maps.get(name))
//                    .setScaleType(BaseSliderView.ScaleType.Fit)
//                    .setOnSliderClickListener(this);
//
//            //add your extra information
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle()
//                    .putString("extra",name);
//
//            mDemoSlider.addSlider(textSliderView);
//        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        //mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(5000);
        mDemoSlider.addOnPageChangeListener(this);

        //recyclerView = view.findViewById(R.id.recyclerView);
        recyclerViewTopMerchants = view.findViewById(R.id.recyclerViewTopMerchants);
        recyclerViewTransactions = view.findViewById(R.id.recyclerView_Transactions);




        // Sample card items
        List<CardItem> cardItems = new ArrayList<>();
        cardItems.add(new CardItem("1234 5678 9012 3456", "Nana Yaw", "528"));
        cardItems.add(new CardItem("9876 5432 1098 7654", "Jane Smith", "224"));
        cardItems.add(new CardItem("7644 2543 5202 1174", "Mike Arms", "324"));

        List<Product> productItems = new ArrayList<>();
        List<Transaction> transactionItems = new ArrayList<>();
//        productItems.add(new Product("65", "https://blacksiliconimages.s3-us-west-2.amazonaws.com/Reload.ng/PHED.jpeg","Electricity Prepaid (phed)", "Electricity Prepaid (phed)"));
//        productItems.add(new Product("65", "https://blacksiliconimages.s3-us-west-2.amazonaws.com/Reload.ng/PHED.jpeg","Electricity Prepaid (phed)", "Electricity Prepaid (phed)"));
//        productItems.add(new Product("65", "https://blacksiliconimages.s3-us-west-2.amazonaws.com/Reload.ng/PHED.jpeg","Electricity Prepaid (phed)", "Electricity Prepaid (phed)"));


        // Parse the JSON data

        try {
            JSONArray jsonArrayMerchants = new JSONArray(topMerchants);
            System.out.println("jsonArrayMerchants"+jsonArrayMerchants);

            // Iterate through the array
            for (int i = 0; i < jsonArrayMerchants.length(); i++) {

                JSONObject jsonObject = jsonArrayMerchants.optJSONObject(i);

                // Check if the object is null
                if (jsonObject != null) {
                    // Get the values you need from the JSON object
                    //long dateCreated = jsonObject.optLong("datecreated");
                    JSONObject product = jsonObject.optJSONObject("product");
                    System.out.println("product"+product);
                    String description = product.optString("description");
                    String billerCode = product.optString("billerCode");
                    String processor = product.optString("processor");
                    String productname = product.getString("productname");
                    String logourl = product.getString("logourl");
                    String id = product.getString("id");
                    JSONObject productCategoryId = product.optJSONObject("productcategoryId");
                    int pdtCategoryId = productCategoryId.getInt("id");


                    // Print or process the values as needed
                    //System.out.println("Date Created: " + dateCreated);
                    System.out.println("Description: " + description);
                    System.out.println("Biller Code: " + billerCode);
                    System.out.println();

                    productItems.add(new Product(id, logourl, productname, description, pdtCategoryId, billerCode, processor));
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArrayMerchants = new JSONArray(topMerchants);
            System.out.println("jsonArrayMerchants"+jsonArrayMerchants);

            // Iterate through the array
            for (int i = 0; i < 3; i++) {

                JSONObject jsonObject = jsonArrayMerchants.optJSONObject(i);

                // Check if the object is null
                if (jsonObject != null) {
                    // Get the values you need from the JSON object
                    //long dateCreated = jsonObject.optLong("datecreated");
                    JSONObject product = jsonObject.optJSONObject("product");
                    System.out.println("product"+product);
                    String description = product.optString("description");
                    String billerCode = product.optString("billerCode");
                    String processor = product.optString("processor");
                    String productname = product.getString("productname");
                    String logourl = product.getString("logourl");
                    String id = product.getString("id");
                    JSONObject productCategoryId = product.optJSONObject("productcategoryId");
                    int pdtCategoryId = productCategoryId.getInt("id");


                    // Print or process the values as needed
                    //System.out.println("Date Created: " + dateCreated);
                    System.out.println("Description: " + description);
                    System.out.println("Biller Code: " + billerCode);
                    System.out.println();

                    transactionItems.add(new Transaction(id, logourl, productname, description, pdtCategoryId, billerCode, processor));
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        productAdapter = new ProductAdapter(requireContext(), productItems);
        transactionAdapter = new TransactionAdapter(requireContext(), transactionItems);

        recyclerViewTopMerchants.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTopMerchants.setAdapter(productAdapter);

        recyclerViewTransactions.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewTransactions.setAdapter(transactionAdapter);

        // Set the active position to the first card initially
        //cardAdapter.setActivePosition(0);

        textViewWalletBalance = view.findViewById(R.id.textViewAmount);
        textViewWalletBalance.setText(walletBalance);

        textViewAllMerchants = view.findViewById(R.id.textview_all_merchants);
        textViewAllMerchants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent( getContext(), AllMerchantsActivity.class);
                intent.putExtra("EXTRA_MESSAGE_ALL_MERCHANTS",allMerchants);
                startActivity(intent);

            }
        });

        relativeLayoutTopUp = view.findViewById(R.id.relativeLayout_topup);
        relativeLayoutTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
                bottomSheetDialog.setContentView(R.layout.fragment_topup_bottom_sheet);

                 editTextTopupAmount = bottomSheetDialog.findViewById(R.id.edittext_topup_amount);
//                 topupAmount = editTextTopupAmount.getText().toString().trim();

                RelativeLayout relativeLayoutTopupBalance = bottomSheetDialog.findViewById(R.id.relativeLayout_topup_button_sheet);
                relativeLayoutTopupBalance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



//                        try{
//                            initializeFundWallet(topupAmount);
//                        }catch(JSONException e){
//                            e.printStackTrace();
//                        }

                        topupAmount = editTextTopupAmount.getText().toString().trim();

                        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
                        if(acct!=null){
                            personName = acct.getDisplayName();
                            personEmail = acct.getEmail();

                        try{
                            initializeFundWallet(personEmail, topupAmount);
                        }catch(JSONException e){
                            e.printStackTrace();
                        }

//                            Intent intent = new Intent(PaymentActivity.this, WebViewActivity.class);
//                            intent.putExtra("EXTRA_MESSAGE_AMOUNT",amount);
//                            intent.putExtra("EXTRA_MESSAGE_EMAIL",personEmail);
//                            intent.putExtra("EXTRA_MESSAGE_NAME",personName);
//                            intent.putExtra("EXTRA_MESSAGE_CURRENCY","NGN");
//                            intent.putExtra("EXTRA_MESSAGE_PRODUCT_ID",productId);
//                            intent.putExtra("EXTRA_MESSAGE_PRODUCT_DESC",productDescription);
//                            intent.putExtra("EXTRA_MESSAGE_CUSTOMER_ACCT",customerAccount);
//                            startActivity(intent);
//
//                            finish();


                        }else{

                            //Toast.makeText(PaymentActivity.this, "Not logged in", Toast.LENGTH_LONG).show();

                            Intent signInIntent = gsc.getSignInIntent();
                            startActivityForResult(signInIntent, RC_SIGN_IN);

                        }




                    }
                });

                bottomSheetDialog.show();
            }
        });

        try{
            getAllMerchants();

        }catch(JSONException e){
            e.printStackTrace();
        }

        try{
            getAllTransactions();

        }catch(JSONException e){
            e.printStackTrace();
        }

//        bottomNavigationView
//                = view.findViewById(R.id.bottomNavigationView);
//
//        bottomNavigationView
//                .setOnNavigationItemSelectedListener(this);
//        bottomNavigationView.setSelectedItemId(R.id.person);

        return view;
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
            personEmail = account.getEmail();
            topupAmount = editTextTopupAmount.getText().toString().trim();

//                                try{
//                        callPaymentIntent(account.getEmail(), account.getDisplayName());
//                    }catch(JSONException e){
//                        e.printStackTrace();
//                    }

//            Intent intent = new Intent( getContext(), WebViewActivity.class);
//            intent.putExtra("EXTRA_MESSAGE_AMOUNT",topupAmount);
//            intent.putExtra("EXTRA_MESSAGE_EMAIL",personEmail);
//            intent.putExtra("EXTRA_MESSAGE_NAME",personName);
//            intent.putExtra("EXTRA_MESSAGE_CURRENCY","NGN");
//            startActivity(intent);

            try{
                initializeFundWallet(personEmail, topupAmount);
            }catch(JSONException e){
                e.printStackTrace();
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

            Toast.makeText(getContext(), "Google login failed", Toast.LENGTH_LONG).show();


            //updateUI(null);
            //navigateToSecondActivity();
        }
    }

    private void getAllMerchants() throws JSONException {

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        //try {
        JSONObject paramObject = new JSONObject();

        Call<String> userCall = apiService.getAllMerchants();

        String requestUrl = userCall.request().url().toString();
        Log.d("Request_URL All Merchants", requestUrl);

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

                    allMerchants  = response.body();


                }else{
                    Toast.makeText(getContext(),"Getting all merchants failed",Toast.LENGTH_SHORT).show();

                }



            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private void getAllTransactions() throws JSONException {

        HashMap<String,String> userDetails = session.getUserDetails();
        accountNo = userDetails.get(Constants.KEY_CUSTOMER_ID);

        System.out.println("accountNo"+accountNo);

        if(accountNo == null || accountNo.trim().isEmpty())
            accountNo = "0";

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        //try {
        JSONObject paramObject = new JSONObject();

        Call<String> userCall = apiService.getAllTransactions(accountNo);

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

                        //String currentBalance = jsonObject.getJSONObject("data").getString("currentBalance");





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else{
                    Toast.makeText(getContext(),"Login Failed",Toast.LENGTH_SHORT).show();

                }



            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private void initializeFundWallet(String email, String amount) throws JSONException {

        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Loading")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        //try {
        JSONObject paramObject = new JSONObject();

        paramObject.put(Constants.KEY_PRODUCT_AMOUNT,amount);
        paramObject.put(Constants.KEY_EMAIL,email);


        Log.d("Tag_message_body", paramObject.toString());


        //Call<String> userCall = apiService.loginUser(paramObject.toString());

        Call<String> userCall = apiService.initializeWalletFunding(paramObject.toString());

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

                        JSONObject data = jsonObject.getJSONObject("data");
                        String reference = data.getString("reference");
                        String amount = data.getString("amount");

                        System.out.println("reference"+reference);
                        System.out.println("amount"+amount);




                        hud.dismiss();

                        Intent intent = new Intent( getContext(), WalletWebViewActivity.class);
                        intent.putExtra("EXTRA_MESSAGE_AMOUNT",amount);
                        intent.putExtra("EXTRA_MESSAGE_EMAIL",personEmail);
                        intent.putExtra("EXTRA_MESSAGE_NAME",personName);
                        intent.putExtra("EXTRA_MESSAGE_CURRENCY","NGN");
                        intent.putExtra("EXTRA_MESSAGE_TRANS_REF",reference);

                        startActivity(intent);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else{
                    Toast.makeText(getContext(),"Login Failed",Toast.LENGTH_SHORT).show();

                }



            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }



    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }



    @Override
    public void onSliderClick(BaseSliderView slider) {
        // Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }


    //@Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    //@Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    //@Override
    public void onPageScrollStateChanged(int state) {}



}
