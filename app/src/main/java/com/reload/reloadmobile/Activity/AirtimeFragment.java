package com.reload.reloadmobile.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reload.reloadmobile.Adapter.ProductAdapter;
import com.reload.reloadmobile.DashboardActivity;
import com.reload.reloadmobile.Model.Product;
import com.reload.reloadmobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AirtimeFragment extends Fragment {

    String airtimeData;

    private RecyclerView recyclerViewAirtime;
    private ProductAdapter productAdapter;

    public AirtimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DashboardActivity activity = (DashboardActivity) getActivity();
        try {
            airtimeData = activity.getAirtimeMerchants();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        System.out.println("airtimeData"+airtimeData);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_airtime, container, false);

        // Perform any additional setup or view binding here

        recyclerViewAirtime = view.findViewById(R.id.recyclerViewAirtime);
        List<Product> productItems = new ArrayList<>();

        try {
            JSONArray jsonArrayMerchants = new JSONArray(airtimeData);
            System.out.println("jsonArrayMerchants"+jsonArrayMerchants);

            // Iterate through the array
            for (int i = 0; i < jsonArrayMerchants.length(); i++) {
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

                JSONObject jsonObject = jsonArrayMerchants.optJSONObject(i);

                // Check if the object is null
                if (jsonObject != null) {
                    // Get the values you need from the JSON object
                    //long dateCreated = jsonObject.optLong("datecreated");
                    //JSONObject product = jsonObject.optJSONObject("product");
                    System.out.println("jsonObject"+jsonObject);
                    String description = jsonObject.optString("description");
                    String billerCode = jsonObject.optString("billerCode");
                    String processor = jsonObject.optString("processor");
                    String productname = jsonObject.getString("productname");
                    String logourl = jsonObject.getString("logourl");
                    String id = jsonObject.getString("id");
                    JSONObject productCategoryId = jsonObject.optJSONObject("productcategoryId");
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
        recyclerViewAirtime.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerViewAirtime.setAdapter(productAdapter);



        return view;
    }


}
