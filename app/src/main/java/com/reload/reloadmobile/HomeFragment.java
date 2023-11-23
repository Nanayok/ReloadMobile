package com.reload.reloadmobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.reload.reloadmobile.Activity.AirtimeFragment;
import com.reload.reloadmobile.Activity.ElectricityFragment;
import com.reload.reloadmobile.Adapter.CardAdapter;
import com.reload.reloadmobile.Adapter.ProductAdapter;
import com.reload.reloadmobile.Model.CardItem;
import com.reload.reloadmobile.Model.Product;
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
    private RecyclerView recyclerView, recyclerViewTopMerchants;
    private CardAdapter cardAdapter;
    private ProductAdapter productAdapter;
    TextView textViewAllMerchants;
    String topMerchants, allMerchants, allAdverts;
    HashMap<String,String> url_maps = new HashMap<String, String>();

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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Perform any additional setup or view binding here



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



        // Sample card items
        List<CardItem> cardItems = new ArrayList<>();
        cardItems.add(new CardItem("1234 5678 9012 3456", "Nana Yaw", "528"));
        cardItems.add(new CardItem("9876 5432 1098 7654", "Jane Smith", "224"));
        cardItems.add(new CardItem("7644 2543 5202 1174", "Mike Arms", "324"));

        List<Product> productItems = new ArrayList<>();
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

        productAdapter = new ProductAdapter(requireContext(), productItems);
        recyclerViewTopMerchants.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTopMerchants.setAdapter(productAdapter);

        // Set the active position to the first card initially
        //cardAdapter.setActivePosition(0);

        textViewAllMerchants = view.findViewById(R.id.textview_all_merchants);
        textViewAllMerchants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent( getContext(), AllMerchantsActivity.class);
                intent.putExtra("EXTRA_MESSAGE_ALL_MERCHANTS",allMerchants);
                startActivity(intent);

            }
        });

        try{
            getAllMerchants();

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

    private void getAllMerchants() throws JSONException {

//        System.out.println("Email"+email);
//        System.out.println("Password"+password);

//        hud = KProgressHUD.create(SplashActivity.this)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setLabel("Please wait")
//                .setDetailsLabel("Loading")
//                .setCancellable(true)
//                .setAnimationSpeed(2)
//                .setDimAmount(0.5f)
//                .show();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        //try {
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

//            paramObject.put(Constants.KEY_EMAIL,email);
//            paramObject.put(Constants.KEY_PASSWORD,password);


//            Log.d("Tag_message_body", paramObject.toString());


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
//                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
//                    intent.putExtra("EXTRA_MESSAGE",responseBody);
//                    startActivity(intent);

//                    finish();

//                        try {
//                            JSONObject jsonObject = new JSONObject(responseBody);
//
//                            //String error = jsonObject.getString(Constants.KEY_ERROR);
//                            String message = jsonObject.getString("message");
//                            JSONObject jobjectUser = jsonObject.getJSONObject("user");
////                            String id = jobjectUser.getString("id");
////                            String email = jobjectUser.getString("email");
////                            String fullname = jobjectUser.getString("fullname");
////                            String password = jobjectUser.getString("password");
////                            String phone = jobjectUser.getString("phone");
////                            String address = jobjectUser.getString("address");
////                            String lastLogin = jobjectUser.getString("lastLogin");
////                            String dateJoined = jobjectUser.getString("dateJoined");
////                            String isActive = jobjectUser.getString("isActive");
////                            String changePassword = jobjectUser.getString("changePassword");
//
////                            JSONObject jobjectBiller = jsonObject.getJSONObject("billerId");
////                            String billerId = jobjectBiller.getString("id");
////                            String billerAlias = jobjectBiller.getString("alias");
////                            String billername = jobjectBiller.getString("billername");
//
//
//
//                            Toast.makeText(SplashActivity.this, message, Toast.LENGTH_LONG).show();
//
//
//                            Intent i = new Intent(SplashActivity.this, DashboardActivity.class);
//                            startActivity(i);
//
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }



                }else{
                    Toast.makeText(getContext(),"Getting all merchants failed",Toast.LENGTH_SHORT).show();

                }



            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

//    private void getAllAdverts() throws JSONException {
//
//
//        ApiService apiService = ApiClient.getClient().create(ApiService.class);
//
//        //try {
//        JSONObject paramObject = new JSONObject();
//
//        Call<String> userCall = apiService.getAllAdverts();
//
//        String requestUrl = userCall.request().url().toString();
//        Log.d("Request_URL All Adverts", requestUrl);
//
//        // Log the request headers
//        Headers requestHeaders = userCall.request().headers();
//        for (String name : requestHeaders.names()) {
//            Log.d("Request_Header", name + ": " + requestHeaders.get(name));
//        }
//
//        userCall.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//
//                //hud.dismiss();
//                System.out.println("ResponseCode"+response.code());
//
//                if (response.code() == 200){
//                    //Toast.makeText(MainActivity.this,response.body(), Toast.LENGTH_LONG).show();
//                    Log.d("Tag: Response All Merchants", response.toString());
//                    Log.d("Tag: Response Body All Merchants", response.body());
//                    System.out.println("Response Body All Merchants"+response.body());
//
//                    allAdverts  = response.body();
//                    System.out.println("allAdverts"+allAdverts);
//
//                    try {
//                        JSONArray jsonArrayAdverts = new JSONArray(allAdverts);
//                        System.out.println("jsonArrayAdverts"+jsonArrayAdverts);
//
//                        // Iterate through the array
//                        for (int i = 0; i < jsonArrayAdverts.length(); i++) {
//
//                            JSONObject jsonObject = jsonArrayAdverts.optJSONObject(i);
//
//                            // Check if the object is null
//                            if (jsonObject != null) {
//
//                                String id = jsonObject.optString("id");
//                                String imageUrl = jsonObject.optString("imageUrl");
////
//                                System.out.println("imageUrl: " + imageUrl);
//                                //System.out.println();
//
//
//                                //productItems.add(new Product(id, logourl, productname, description, pdtCategoryId, billerCode, processor));
//                                url_maps.put(id, imageUrl);
//
//
//
//                            }
//
//
//                        }
//
//
////                        for (String company : url_maps.keySet()) {
////                            System.out.println("Company_adverts: " + company);
////                            System.out.println("URL_adverts: " + url_maps.get(company));
////                        }
//
//
//
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }else{
//                    Toast.makeText(getContext(),"Getting all merchants failed",Toast.LENGTH_SHORT).show();
//
//                }
//
//
//
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
//
//
//
//    }






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
