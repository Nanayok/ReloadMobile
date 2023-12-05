package com.reload.reloadmobile.network;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    //@Headers("Content-Type: application/json")

    //Login
    @Headers({
            "Content-Type: application/json",
            "merchantKey: 099035353"
    })
    @POST("billercustomer/mobile/login")
    Call<String> loginUser(@Body String body);

    //Register
    @Headers({
            "Content-Type: application/json",
            "merchantKey: 099035353"
    })
    @POST("billercustomer/mobile/reg")
    Call<String> registerUser(@Body String body);


    //Forgot Password
    @Headers({
            "Content-Type: application/json",
            "merchantKey: 099035353"
    })
    @POST("billercustomer/mobile/forgotpassword")
    Call<String> forgotPasswordUser(@Body String body);

    //otp verify
    @Headers({
            "Content-Type: application/json",
            "merchantKey: 099035353"
    })
    @POST("billercustomer/mobile/verify")
    Call<String> verifyOtpUser(@Body String body);

    //Reset Password
    @Headers({
            "Content-Type: application/json",
            "merchantKey: 099035353"
    })
    @POST("billercustomer/mobile/resetpassword")
    Call<String> resetPasswordUser(@Body String body);

    //Get Top Merchants
    @Headers({
            "Content-Type: application/json",
            "merchantKey: 099035353"
    })

    @GET("transaction/mobile/ranking/{accountNo}")
    Call<String> getTopMerchants(@Path("accountNo") String accountNo);


    //Get All Merchants
    @Headers({
            "Content-Type: application/json",
            "merchantKey: 099035353"
    })

    @GET("product/mobile/lst")
    Call<String> getAllMerchants();

    //Get All Adverts
    @Headers({
            "Content-Type: application/json",
            "merchantKey: 099035353"
    })

    @GET("banner")
    Call<String> getAllAdverts();

    //Verify Account
    @Headers({
            "Content-Type: application/json",
            "merchantKey: 099035353"
    })
    @POST("product/merchant/mobile/customer-lookup")
    Call<String> verifyAccount(@Body String body);

    //Payment Intent
    @Headers({
            "Content-Type: application/json",
            "merchantKey: 099035353"
    })
    @POST("transaction/payment/mobile/intent")
    Call<String> callPaymentIntent(@Body String body);

    //Finalize Payment
    @Headers({
            "Content-Type: application/json",
            "merchantKey: 099035353"
    })
    @POST("transaction/payment/finalize")
    Call<String> finalizePayment(@Body String body);

    //Get slug name
    @Headers({
            "Content-Type: application/json",
            "merchantKey: 099035353"
    })
    @GET("coralpay/biller/slug/{billerCode}")
    Call<String> getSlugName(@Path("billerCode") String billerCode);

    //Get Electricity Merchants
    @Headers({
            "Content-Type: application/json",
            "merchantKey: 099035353"
    })

    @GET("product/28")
    Call<String> getElectricityMerchants();

    //Get Airtime Merchants
    @Headers({
            "Content-Type: application/json",
            "merchantKey: 099035353"
    })

    @GET("product/22")
    Call<String> getAirtimeMerchants();

    //Get Cable Tv Merchants
    @Headers({
            "Content-Type: application/json",
            "merchantKey: 099035353"
    })

    @GET("product/24")
    Call<String> getCableTvMerchants();

    //Get Account
    @Headers({
            "Content-Type: application/json",
            "merchantKey: 099035353"
    })

    @GET("wallettrans/balance/{accountNo}")
    Call<String> getWalletBalance(@Path("accountNo") String accountNo);

    //Get All Transactions
    @Headers({
            "Content-Type: application/json",
            "merchantKey: 099035353"
    })

    @GET("transaction/mobile/{accountNo}")
    Call<String> getAllTransactions(@Path("accountNo") String accountNo);

    //Initialize wallet funding
    @Headers({
            "Content-Type: application/json",
            "merchantKey: 099035353"
    })

    @POST("wallettrans/data/funding")
    Call<String> initializeWalletFunding(@Body String body);



}
