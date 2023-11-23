package com.reload.reloadmobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ProductBottomSheetFragment extends BottomSheetDialogFragment {

    public ProductBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_bottom_sheet, container, false);

        // Initialize UI components and set up behavior here

        return view;
    }

}
