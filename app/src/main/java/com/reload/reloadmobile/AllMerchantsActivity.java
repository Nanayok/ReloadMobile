package com.reload.reloadmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import com.reload.reloadmobile.Adapter.ProductAdapter;
import com.reload.reloadmobile.Model.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllMerchantsActivity extends AppCompatActivity {

    String allMerchants;
    private RecyclerView recyclerViewAllMerchants;
    private ProductAdapter productAdapter;
    EditText editTextSearch;
    List<Product> productItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_merchants);

        Toolbar toolbarForgotPassword = findViewById(R.id.toolbarAllMerchants);
        setSupportActionBar(toolbarForgotPassword);

        // Enable the back arrow (up button)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the title for the activity (optional)
        getSupportActionBar().setTitle("All Merchants");

        Intent intent = getIntent();
        allMerchants = intent.getStringExtra("EXTRA_MESSAGE_ALL_MERCHANTS");
        System.out.println("allMerchants in new activity"+allMerchants);

//        List<Product> productItems = new ArrayList<>();
//        productItems.add(new Product("65", "https://blacksiliconimages.s3-us-west-2.amazonaws.com/Reload.ng/PHED.jpeg","Electricity Prepaid (phed)", "Electricity Prepaid (phed)"));
//        productItems.add(new Product("65", "https://blacksiliconimages.s3-us-west-2.amazonaws.com/Reload.ng/PHED.jpeg","Electricity Prepaid (phed)", "Electricity Prepaid (phed)"));
//        productItems.add(new Product("65", "https://blacksiliconimages.s3-us-west-2.amazonaws.com/Reload.ng/PHED.jpeg","Electricity Prepaid (phed)", "Electricity Prepaid (phed)"));


        // Parse the JSON data

        try {
            JSONArray jsonArrayAllMerchants = new JSONArray(allMerchants);
            System.out.println("jsonArrayAllMerchants"+jsonArrayAllMerchants);

            // Iterate through the array
            for (int i = 0; i < jsonArrayAllMerchants.length(); i++) {
                // Get the JSON object at the current index
                //JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Get the values you need from the JSON object
                //long dateCreated = jsonArray.getJSONObject(i).getLong("datecreated");
//                JSONObject productId = jsonArray.getJSONObject(i).getJSONObject("productId");
//                String description = productId.getString("description");
//                String productname = productId.getString("productname");
//                String id = productId.getString("id");
//
//                // Print or process the values as needed
//                //System.out.println("Date Created: " + dateCreated);
//                System.out.println("Description: " + description);
//                System.out.println("productname: " + productname);
//                System.out.println();

                JSONObject jsonObject = jsonArrayAllMerchants.optJSONObject(i);

                // Check if the object is null
                if (jsonObject != null) {
                    // Get the values you need from the JSON object
                    //long dateCreated = jsonObject.optLong("datecreated");
                    JSONObject product = jsonObject.optJSONObject("productId");
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

        recyclerViewAllMerchants = findViewById(R.id.recyclerViewAllMerchants);
        editTextSearch = (EditText) findViewById(R.id.editTextSearch);

//        productAdapter = new ProductAdapter(this, productItems);
//        recyclerViewAllMerchants.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        recyclerViewAllMerchants.setAdapter(productAdapter);

        productAdapter = new ProductAdapter(this, productItems);
        recyclerViewAllMerchants.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewAllMerchants.setAdapter(productAdapter);

//        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
//        recyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,1);
//        recyclerView.setLayoutManager(layoutManager);

        //adding a TextChangedListener
        //to call a method whenever there is some change on the EditText
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });
    }


    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<Product> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (Product s : productItems) {
            //if the existing elements contains the search input
            if (s.getProductname().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        productAdapter.filterList(filterdNames);
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
}