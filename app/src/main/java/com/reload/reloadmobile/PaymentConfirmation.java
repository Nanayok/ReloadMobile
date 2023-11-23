package com.reload.reloadmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reload.reloadmobile.Sessions.SessionManager;

import java.util.HashMap;

public class PaymentConfirmation extends AppCompatActivity {

    String amount, transactionRef, email;

    TextView textViewPaymentAmount, textViewTransactionRef, textViewEmail;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        HashMap<String,String> userDetails = session.getUserDetails();
        email = userDetails.get(SessionManager.KEY_EMAIL);

        Intent intent = getIntent();
        amount = intent.getStringExtra("EXTRA_AMOUNT");
        transactionRef = intent.getStringExtra("EXTRA_TRANSACTION_REF");

        textViewPaymentAmount = findViewById(R.id.textview_payment_amount);
        textViewTransactionRef = findViewById(R.id.textview_transactionRef);

        textViewPaymentAmount.setText(amount);
        textViewTransactionRef.setText(transactionRef);

        textViewEmail = findViewById(R.id.textview_email);
        textViewEmail.setText(email);

        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout_done);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}