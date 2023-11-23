package com.reload.reloadmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.reload.reloadmobile.Activity.AirtimeFragment;
import com.reload.reloadmobile.Activity.ElectricityFragment;
import com.reload.reloadmobile.Fragment.CableTvFragment;
import com.reload.reloadmobile.Sessions.SessionManager;
import com.reload.reloadmobile.network.ApiClient;
import com.reload.reloadmobile.network.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    BottomNavigationView bottomNavigationView;

    String topMerchants, electricityMerchants, airtime, cableTv, allAdverts;
    SessionManager session;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    TextView profileName, profileEmail;
    ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar); // Replace with your actual Toolbar ID
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        navigationView = findViewById(R.id.navigation_drawer);
        View headerView = navigationView.getHeaderView(0); // 0 is the index of the header layout

// Access the views within the header layout
        profileName = headerView.findViewById(R.id.profile_name_nav);
        profileEmail = headerView.findViewById(R.id.profile_email_nav);
        profileImage = headerView.findViewById(R.id.image_header);


        //       TextView profileName = findViewById(R.id.profile_name_nav);
//        profileEmail = findViewById(R.id.profile_email_nav);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();

            String token = acct.getIdToken();
            System.out.println("token from google"+token);

            profileName.setText(personName);
            profileEmail.setText(personEmail);
            //Glide.with(this).load(acct.getPhotoUrl()).into(profileImage);

        }


        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //start
        // Make sure to call setSupportActionBar() and obtain the action bar instance
        //setSupportActionBar(toolbar);  // Replace 'toolbar' with your actual Toolbar instance

        // Enable the "up" button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //end

        Intent intent = getIntent();
        topMerchants = intent.getStringExtra("EXTRA_MESSAGE_TOP_MERCHANT");
        System.out.println("TopMerchants"+topMerchants);

        allAdverts = intent.getStringExtra("EXTRA_MESSAGE_ALL_ADVERTS");
        System.out.println("Alladverts"+allAdverts);

        navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.nav_item_home) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new HomeFragment())
                            .commit();
                    drawerLayout.closeDrawers();
                    return true;

                } else if (itemId == R.id.nav_item_profile) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ProfileFragment())
                            .commit();
                    drawerLayout.closeDrawers();
                    return true;
                }else if (itemId == R.id.nav_item_settings) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new SettingsFragment())
                            .commit();
                    drawerLayout.closeDrawers();
                    return true;
                }
//                else if (itemId == R.id.airtime) {
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fragment_container, new AirtimeFragment())
//                            .commit();
//                    drawerLayout.closeDrawers();
//                    return true;
//                }else if (itemId == R.id.electricity) {
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fragment_container, new ElectricityFragment())
//                            .commit();
//                    drawerLayout.closeDrawers();
//                    return true;
//                }
                else if (itemId == R.id.nav_item_logout) {

                    gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            //finish();
                            //startActivity(new Intent(SecondActivity.this, MainActivity.class));
                            //startActivity(new Intent(DashboardActivity.this, MainActivity.class));

                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new HomeFragment())
                                    .commit();
                            drawerLayout.closeDrawers();


                        }
                    });

                    return true;
                }
                else {
                    return false;
                }
            }
        });

        // Set the home fragment as the default fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();

        // Highlight the home item in the navigation drawer
        navigationView.setCheckedItem(R.id.nav_item_home);

//        bottomNavigationView
//                = findViewById(R.id.bottom_navigation);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home:
                                                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new HomeFragment())
                            .commit();
                    drawerLayout.closeDrawers();
                    return true;


                                //break;
                            case R.id.airtime:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new AirtimeFragment())
                                        .commit();
                                //drawerLayout.closeDrawers();
                                return true;


                            //break;
                            case R.id.electricity:

                                                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ElectricityFragment())
                            .commit();
                    //drawerLayout.closeDrawers();
                    return true;
                            case R.id.cable_tv:

                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new CableTvFragment())
                                        .commit();
                                //drawerLayout.closeDrawers();
                                return true;


                            //break;
//                            case R.id.action_music:
//
//                                break;
                        }
                        return false;
                    }
                });

        try{
            getElectricityMerchants();
            getAirtimeMerchants();
            getCableTvMerchants();
            //getAllAdverts();
        }catch(JSONException e){
            e.printStackTrace();
        }
        
    }

    public String getMyData() {
        return topMerchants;
    }

    public String getAllAdvertsData() {
        return allAdverts;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getElectricityMerchants() throws JSONException {

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


        Call<String> userCall = apiService.getElectricityMerchants();

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

                    electricityMerchants = response.body();

//                    String responseBody = response.body();
//                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
//                    intent.putExtra("EXTRA_MESSAGE",responseBody);
//                    startActivity(intent);
//
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
                    Toast.makeText(getBaseContext(),"Login Failed",Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        return electricityMerchants;
    }

    public String getAirtimeMerchants() throws JSONException {

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


        Call<String> userCall = apiService.getAirtimeMerchants();

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

                    airtime = response.body();

//                    String responseBody = response.body();
//                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
//                    intent.putExtra("EXTRA_MESSAGE",responseBody);
//                    startActivity(intent);
//
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
                    Toast.makeText(getBaseContext(),"Login Failed",Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        return airtime;
    }

    public String getCableTvMerchants() throws JSONException {

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        //try {
        JSONObject paramObject = new JSONObject();

        Call<String> userCall = apiService.getCableTvMerchants();

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

                    cableTv = response.body();


                }else{
                    Toast.makeText(getBaseContext(),"Login Failed",Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        return cableTv;
    }

//    public String getAllAdverts() throws JSONException {
//
//        ApiService apiService = ApiClient.getClient().create(ApiService.class);
//
//        //try {
//        JSONObject paramObject = new JSONObject();
//
//        Call<String> userCall = apiService.getAllAdverts();
//
//        String requestUrl = userCall.request().url().toString();
//        Log.d("Request_URL", requestUrl);
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
//                    Log.d("Tag: Response", response.toString());
//                    Log.d("Tag: Response Body", response.body());
//                    System.out.println("Response Body"+response.body());
//
//                    allAdverts = response.body();
//
//
//                }else{
//                    Toast.makeText(getBaseContext(),"Login Failed",Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
//
//        return allAdverts;
//    }





}