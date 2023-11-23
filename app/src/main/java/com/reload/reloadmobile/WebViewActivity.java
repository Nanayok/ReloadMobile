package com.reload.reloadmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    String amount, personEmail, personName, currency;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Add JavaScript interface
        webView.addJavascriptInterface(new WebAppInterface(), "AndroidInterface");

        // Load your HTML file or website URL
        //webView.loadUrl("file:///android_asset/index.html");
        //webView.loadUrl("file:///android_asset/flutter.html");
        webView.loadUrl("https://reload.ng/flutter/");
        //webView.loadUrl("http://localhost:80/flutter.html");


        // Set a WebViewClient to handle redirects and load new pages within the WebView
        webView.setWebViewClient(new WebViewClient());

        Intent intent = getIntent();
        amount = intent.getStringExtra("EXTRA_MESSAGE_AMOUNT");
        personEmail = intent.getStringExtra("EXTRA_MESSAGE_EMAIL");
        personName = intent.getStringExtra("EXTRA_MESSAGE_NAME");
        currency = intent.getStringExtra("EXTRA_MESSAGE_CURRENCY");
        System.out.println("WebView data:"+amount+","+personEmail+","+personName+","+currency);


    }

    // JavaScript Interface
    public class WebAppInterface {

//        @JavascriptInterface
//        public void sendDataToServer(String amt, String email, String name, String curr) {
//            // Implement your code to send data to the server here
//            // You can use libraries like Retrofit or AsyncTask for HTTP requests
//            // Example: sendDataToServerAsync(data1, data2);
//
//
//        }


        @JavascriptInterface
        public String sendData() {
            // Implement your code to send data to the server here
            // You can use libraries like Retrofit or AsyncTask for HTTP requests
            // Example: sendDataToServerAsync(data1, data2);
            System.out.println("sendData:"+amount+","+personEmail+","+personName+","+currency);

            return amount+","+personEmail+","+personName+","+currency;
        }
    }
}