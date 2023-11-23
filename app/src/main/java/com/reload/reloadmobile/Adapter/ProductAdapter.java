package com.reload.reloadmobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.reload.reloadmobile.Activity.VerifyProductActivity;
import com.reload.reloadmobile.ForgotPasswordActivity;
import com.reload.reloadmobile.Model.Product;
import com.reload.reloadmobile.OTPActivity;
import com.reload.reloadmobile.PaymentActivity;
import com.reload.reloadmobile.R;
import com.reload.reloadmobile.Sessions.SessionManager;
import com.reload.reloadmobile.Utilities.Constants;
import com.reload.reloadmobile.network.ApiClient;
import com.reload.reloadmobile.network.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

    private List<Product> products;
    private Context context; // Add a Context field

    KProgressHUD hud;
    String accountNumber;

    public ProductAdapter(Context context, List<Product> products)
    {
        this.context = context;
        this.products = products;

    }

//    public ProductAdapter(List<Product> products) {
//        this.products = products;
//    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);

         //OpenBottomSheet = view.findViewById(R.id.open_modal_bottom_sheet);

//        session = new SessionManager(context);
//        // Configure sign-in to request the user's ID, email address, and basic
//// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        // Build a GoogleSignInClient with the options specified by gso.
//        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
//        // Check for existing Google Sign In account, if the user is already signed in
//// the GoogleSignInAccount will be non-null.
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
//        //updateUI(account);

        return new ProductAdapter.ProductViewHolder(view);


    }

    //private int activePosition = 0;

//    public void setActivePosition(int position) {
//        activePosition = position;
//        notifyDataSetChanged();
//    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        Product productItem = products.get(position);
        // Bind cardItem data to the view holder
        holder.textViewProductName.setText(productItem.getProductname());
        //holder.textViewProductDescription.setText(productItem.getProductDescription());
        //holder.imageViewProductImage.setImageResource(productItem.getLogourl());

        // Update indicator visibility
        //holder.indicatorImageView.setVisibility(position == activePosition ? View.VISIBLE : View.INVISIBLE);

//        Glide.with(context)
//                .load(productItem.getLogourl()) // image url
//                //.placeholder(R.drawable.placeholder) // any placeholder to load at start
//                //.error(R.drawable.imagenotfound)  // any image in case of error
//                .override(200, 200) // resizing
//                .centerCrop()
//                .into(holder.imageViewProductImage);  // imageview object

        // Assuming you have an ImageView in your ViewHolder with the ID "imageView"
        //ImageView imageView = holder.imageViewProductImage;

        // Load the image using Glide
//        RequestOptions requestOptions = new RequestOptions()
//                .diskCacheStrategy(DiskCacheStrategy.ALL) // Caches the image on device
//                .centerCrop(); // Centers the image within ImageView
//
//        Glide.with(context)
//                .load(productItem.getLogourl()) // Replace with your image URL or resource
//                //.apply(requestOptions)
//                .into(holder.imageViewProductImage);

        //Picasso.with(context).load(productItem.getLogourl()).networkPolicy(NetworkPolicy.OFFLINE).resize(600, 500).into(holder.imageViewProductImage);

        Glide.with(context).load(productItem.getLogourl()).into(holder.imageViewProductImage);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        // View elements inside the card layout

        TextView textViewProductName;
        //TextView textViewProductDescription;
//        TextView textViewCardName;
        ImageView imageViewProductImage;

        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();


        //ImageView indicatorImageView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize view elements
            textViewProductName = itemView.findViewById(R.id.textview_top_merchant_name);
            //textViewProductDescription = itemView.findViewById(R.id.textview_top_merchant_description);
            imageViewProductImage = itemView.findViewById(R.id.imageView_top_merchant);

            //indicatorImageView = itemView.findViewById(R.id.indicator);
            // Other initialization

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int pos = getAdapterPosition();
                    Product card = products.get(pos);

                    //Toast.makeText(context,card.getProductname(),Toast.LENGTH_SHORT).show();
                    System.out.println("cardName"+card.getProductname());
                    System.out.println("Product Category id"+card.getProductCategoryId());

//                    context.startActivity(new Intent(context, VerifyProductActivity.class));

//                    if(session.isLoggedIn()){

                        if(card.getProductCategoryId() == 28 || card.getProductCategoryId() == 24){

                            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
                            bottomSheetDialog.setContentView(R.layout.fragment_product_bottom_sheet);

                            TextView textViewBottomSheetProductName = bottomSheetDialog.findViewById(R.id.textview_bottom_sheet_top_merchant_name);
                            textViewBottomSheetProductName.setText(card.getProductname());
                            TextView textViewBottomSheetProductDescription = bottomSheetDialog.findViewById(R.id.textview_bottom_sheet_top_merchant_description);
                            textViewBottomSheetProductDescription.setText(card.getProductDescription());
                            ImageView imageViewBottomSheet = bottomSheetDialog.findViewById(R.id.imageView_bottom_sheet_top_merchant);
                            Glide.with(context).load(card.getLogourl()).into(imageViewBottomSheet);

                            RelativeLayout relativeLayoutBuy = bottomSheetDialog.findViewById(R.id.relativeLayout_button_sheet);
                            relativeLayoutBuy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

//                                    Intent intent = new Intent(context, PaymentActivity.class);
//                                    context.startActivity(intent);

//                                    Intent intent = new Intent(context, PaymentActivity.class);
//                                    intent.putExtra("EXTRA_PRODUCT_NAME", card.getProductname());
//                                    intent.putExtra("EXTRA_PRODUCT_DESCRIPTION", card.getProductDescription());
//                                    intent.putExtra("EXTRA_PRODUCT_IMAGE", card.getLogourl());
//                                    //intent.putExtra("EXTRA_PRODUCT_NAME", card.getProductname());
//
//                                    context.startActivity(intent);

//                                    EditText editTextAccountNumber = bottomSheetDialog.findViewById(R.id.edittext_account_number);
//                                    accountNumber = editTextAccountNumber.getText().toString().trim();



//                                        try{
//                                            verifyAccount(card.getId(), card.getProductname(), card.getBillerCode(), accountNumber);
//                                        }catch(JSONException e){
//                                            e.printStackTrace();
//                                        }

                                                                            try{
                                            getSlugName(card.getId(), card.getBillerCode(), card.getProductDescription());
                                        }catch(JSONException e){
                                            e.printStackTrace();
                                        }




                                }
                            });

                            bottomSheetDialog.show();
//
                        }else{
                            Intent intent = new Intent(context, PaymentActivity.class);
                            //intent.putExtra("EXTRA_PRODUCT_NAME", card.getProductname());
                            intent.putExtra("EXTRA_PRODUCT_ID", card.getId());
                            intent.putExtra("EXTRA_PRODUCT_DESCRIPTION", card.getProductDescription());
                            context.startActivity(intent);
                        }
//
//
//                    }else{
//
//
//
//                    }



//                    OpenBottomSheet.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                        }
//                    });

//                    ProductBottomSheetFragment bottomSheetFragment = new ProductBottomSheetFragment();
//                    bottomSheetFragment.show(fragmentManager, bottomSheetFragment.getTag());




                }
            });
        }
    }

//    private void verifyAccount(String productId, String productName, String billerCode, String acctNumber) throws JSONException {
//
////        System.out.println("Email"+email);
////        System.out.println("Password"+password);
//
//        hud = KProgressHUD.create(context)
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
////            paramObject.put(Constants.KEY_ACCOUNT_NUMBER,acctNumber);
////            paramObject.put(Constants.KEY_BILLER_CODE,billerCode);
////            paramObject.put(Constants.KEY_PRODUCT_NAME,productName);
////            paramObject.put(Constants.KEY_PRODUCT_ID,productId);
//
//            paramObject.put(Constants.KEY_ACCOUNT_NUMBER,"04171176334");
//            paramObject.put(Constants.KEY_BILLER_CODE,"EKEDC");
//            paramObject.put(Constants.KEY_PRODUCT_NAME,"EKEDC_PREPAID");
//            paramObject.put(Constants.KEY_PRODUCT_ID,"35");
//
//            Log.d("Tag_message_body", paramObject.toString());
//
//            Call<String> userCall = apiService.verifyAccount(paramObject.toString());
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
//
//                            //String error = jsonObject.getString(Constants.KEY_ERROR);
////                            String message = jsonObject.getString("message");
////                            JSONObject jobjectUser = jsonObject.getJSONObject("user");
////                            String id = jobjectUser.getString("id");
////                            String email = jobjectUser.getString("email");
////                            String fullname = jobjectUser.getString("fullname");
////                            String password = jobjectUser.getString("password");
//
//                            Toast.makeText(context, "Verification successful", Toast.LENGTH_LONG).show();
//
//                            Intent intent = new Intent(context, PaymentActivity.class);
//                            context.startActivity(intent);
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
//                        Toast.makeText(context,"Verification Failed",Toast.LENGTH_SHORT).show();
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

    private void getSlugName(String productId, String billerCode, String productDescription) throws JSONException {

//        System.out.println("Email"+email);
//        System.out.println("Password"+password);

        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Loading")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        JSONObject paramObject = new JSONObject();

//            paramObject.put(Constants.KEY_ACCOUNT_NUMBER,acctNumber);
//            paramObject.put(Constants.KEY_BILLER_CODE,billerCode);
//            paramObject.put(Constants.KEY_PRODUCT_NAME,productName);
//            paramObject.put(Constants.KEY_PRODUCT_ID,productId);

//            paramObject.put(Constants.KEY_ACCOUNT_NUMBER,"04171176334");
//            paramObject.put(Constants.KEY_BILLER_CODE,"EKEDC");
//            paramObject.put(Constants.KEY_PRODUCT_NAME,"EKEDC_PREPAID");
//            paramObject.put(Constants.KEY_PRODUCT_ID,"35");
//
//            Log.d("Tag_message_body", paramObject.toString());

        Call<String> userCall = apiService.getSlugName(billerCode);

        String requestUrl = userCall.request().url().toString();
        Log.d("Request_URL Get Slug Name", requestUrl);

        // Log the request headers
        Headers requestHeaders = userCall.request().headers();
        for (String name : requestHeaders.names()) {
            Log.d("Request_Header Get Slug Name", name + ": " + requestHeaders.get(name));
        }

        userCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                hud.dismiss();
                System.out.println("ResponseCode"+response.code());

                if (response.code() == 200){
                    //Toast.makeText(MainActivity.this,response.body(), Toast.LENGTH_LONG).show();
                    Log.d("Tag: Response Name Slug", response.toString());
                    Log.d("Tag: Response Body Name Slug", response.body());
                    System.out.println("Response Body Name Slug"+response.body());

                    String responseBody = response.body();

                    Intent intent = new Intent(context, VerifyProductActivity.class);
                    intent.putExtra("EXTRA_PRODUCT_ID", productId);
                    intent.putExtra("EXTRA_BILLER_CODE", billerCode);
                    intent.putExtra("EXTRA_PRODUCT_DESCRIPTION", productDescription);
                    intent.putExtra("EXTRA_SLUG_DATA", responseBody);
                    context.startActivity(intent);

//                    try {
//                        JSONObject jsonObject = new JSONObject(responseBody);
//
//                        String error = jsonObject.getString(Constants.KEY_ERROR);
//                            String message = jsonObject.getString("message");
//                            JSONObject jobjectUser = jsonObject.getJSONObject("user");
//                            String id = jobjectUser.getString("id");
//                            String email = jobjectUser.getString("email");
//                            String fullname = jobjectUser.getString("fullname");
//                            String password = jobjectUser.getString("password");
//
//                        Toast.makeText(context, "successful", Toast.LENGTH_LONG).show();
//
//
//
//
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }



                }else{
                    Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();

                }



            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    //This method will filter the list
    //here we are passing the filtered data
    //and assigning it to the list with notifydatasetchanged method
    public void filterList(ArrayList<Product> filterdNames) {
        this.products = filterdNames;
        notifyDataSetChanged();
    }

}
