package com.reload.reloadmobile.Sessions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.reload.reloadmobile.DashboardActivity;
import com.reload.reloadmobile.Utilities.Constants;

import java.util.HashMap;

public class SessionManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "OnwlPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // All Shared Preferences Keys
    private static final String IS_TELEMATICS_REGISTERED = "IsTelematicsRegisteredIn";

    // All Shared Preferences Keys
    private static final String IS_DIGITAL_FIRE_REGISTERED = "IsDigitalFireRegisteredIn";

    // User name (make variable public to access from outside)
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_FULL_NAME = "fullName";


    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    public static final String KEY_TELEPHONE = "phone";


    // Email address (make variable public to access from outside)
    //public static final String KEY_PASSWORD = "password";

    public static final String KEY_CUSTOMER_NAME = "customer_name";

    //    public static final String KEY_VEHICLE_NUMBER = "vehicle_number";
    public static final String KEY_VEHICLE_NUMBER = "vnum";
    //    public static final String KEY_POLICY_NUMBER = "policynumber";
    public static final String KEY_POLICY_NUMBER = "pnum";
    //    public static final String KEY_INSURED_NAME = "insured_name";
    public static final String KEY_INSURED_NAME = "name";

    public static final String KEY_RESIDENTIAL_ADDRESS_FIRE = "residential_address_fire";
    public static final String KEY_POLICY_NUMBER_FIRE = "policynumberFire";
    public static final String KEY_INSURED_NAME_FIRE = "insured_name_fire";

//    public static final String KEY_POSTAL_ADDRESS = "postal_address";
//    public static final String KEY_RESIDENTIAL_ADDRESS = "residential_address";
//    public static final String KEY_FAX = "fax";
//    public static final String KEY_DATE_OF_BIRTH = "dob";
//    public static final String KEY_NEXT_OF_KIN = "next_of_kin";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
//    public void createLoginSession(String email, String fullName, String phoneNumber){
    public void createLoginSession(String customerId){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
//        editor.putString(KEY_FIRST_NAME, firstName);
//        editor.putString(KEY_LAST_NAME, lastName);
//        editor.putString(KEY_FULL_NAME, fullName);
        editor.putString(Constants.KEY_CUSTOMER_ID, customerId);

        // Storing email in pref
        //editor.putString(KEY_EMAIL, email);

        //editor.putString(KEY_TELEPHONE, phoneNumber);

        // Storing password in pref
        //editor.putString(KEY_PASSWORD, password);

//        editor.putString(KEY_CUSTOMER_NAME,customerName);
//        editor.putString(KEY_POSTAL_ADDRESS,postalAddress);
//        editor.putString(KEY_RESIDENTIAL_ADDRESS, residentialAddress);
//        editor.putString(KEY_OCCUPATION, occupation);
//        editor.putString(KEY_TELEPHONE, telephone);
//        editor.putString(KEY_FAX, fax);
//        editor.putString(KEY_DATE_OF_BIRTH, dateOfBirth);
//        editor.putString(KEY_NEXT_OF_KIN, nextOfKin);


        // commit changes
        editor.commit();
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        //user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(Constants.KEY_CUSTOMER_ID, pref.getString(Constants.KEY_CUSTOMER_ID, null));

        // user password id
        //user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

//        user.put(KEY_FIRST_NAME, pref.getString(KEY_FIRST_NAME, null));
//        user.put(KEY_LAST_NAME, pref.getString(KEY_LAST_NAME, null));
//        user.put(KEY_FULL_NAME, pref.getString(KEY_FULL_NAME, null));
//        user.put(KEY_POSTAL_ADDRESS, pref.getString(KEY_POSTAL_ADDRESS, null));
//        user.put(KEY_RESIDENTIAL_ADDRESS, pref.getString(KEY_RESIDENTIAL_ADDRESS, null));
//        user.put(KEY_OCCUPATION, pref.getString(KEY_OCCUPATION, null));
//        user.put(KEY_TELEPHONE, pref.getString(KEY_TELEPHONE, null));
//        user.put(KEY_FAX, pref.getString(KEY_FAX, null));
//        user.put(KEY_DATE_OF_BIRTH, pref.getString(KEY_DATE_OF_BIRTH, null));
//        user.put(KEY_NEXT_OF_KIN, pref.getString(KEY_NEXT_OF_KIN, null));


        // return user
        return user;
    }




    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, DashboardActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, DashboardActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }


    /**
     * Create digital motor login session
     * */
    public void createTelematicsRegistrationSession(String vehicleNumber, String policyNumber,String insuredName){
        // Storing login value as TRUE
        editor.putBoolean(IS_TELEMATICS_REGISTERED, true);

        // Storing name in pref
        // editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_VEHICLE_NUMBER, vehicleNumber);

        // Storing password in pref
        //editor.putString(KEY_PASSWORD, password);

        editor.putString(KEY_POLICY_NUMBER,policyNumber);

        editor.putString(KEY_INSURED_NAME,insuredName);
//        editor.putString(KEY_POSTAL_ADDRESS,postalAddress);
//        editor.putString(KEY_RESIDENTIAL_ADDRESS, residentialAddress);
//        editor.putString(KEY_OCCUPATION, occupation);
//        editor.putString(KEY_TELEPHONE, telephone);
//        editor.putString(KEY_FAX, fax);
//        editor.putString(KEY_DATE_OF_BIRTH, dateOfBirth);
//        editor.putString(KEY_NEXT_OF_KIN, nextOfKin);


        // commit changes
        editor.commit();
    }


    // Get Telematics Registration State
    public boolean isTelematicsRegistered(){
        return pref.getBoolean(IS_TELEMATICS_REGISTERED, false);
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getTelematicsRegistrationDetails(){
        HashMap<String, String> registrationDetails = new HashMap<String, String>();
        // user name
        //user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        registrationDetails.put(KEY_POLICY_NUMBER, pref.getString(KEY_POLICY_NUMBER, null));

        // user password id
        //user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        registrationDetails.put(KEY_VEHICLE_NUMBER, pref.getString(KEY_VEHICLE_NUMBER, null));

        registrationDetails.put(KEY_INSURED_NAME,pref.getString(KEY_INSURED_NAME,null));
//        user.put(KEY_POSTAL_ADDRESS, pref.getString(KEY_POSTAL_ADDRESS, null));
//        user.put(KEY_RESIDENTIAL_ADDRESS, pref.getString(KEY_RESIDENTIAL_ADDRESS, null));
//        user.put(KEY_OCCUPATION, pref.getString(KEY_OCCUPATION, null));
//        user.put(KEY_TELEPHONE, pref.getString(KEY_TELEPHONE, null));
//        user.put(KEY_FAX, pref.getString(KEY_FAX, null));
//        user.put(KEY_DATE_OF_BIRTH, pref.getString(KEY_DATE_OF_BIRTH, null));
//        user.put(KEY_NEXT_OF_KIN, pref.getString(KEY_NEXT_OF_KIN, null));

        // return user
        return registrationDetails;
    }


    /**
     * Create digital fire login session
     * */
    public void createDigitalFireRegistrationSession(String residentialAddressFire, String policyNumberFire,String insuredNameFire){
        // Storing login value as TRUE
        editor.putBoolean(IS_DIGITAL_FIRE_REGISTERED, true);

        // Storing name in pref
        // editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_RESIDENTIAL_ADDRESS_FIRE, residentialAddressFire);

        // Storing password in pref
        //editor.putString(KEY_PASSWORD, password);

        editor.putString(KEY_POLICY_NUMBER_FIRE,policyNumberFire);

        editor.putString(KEY_INSURED_NAME_FIRE,insuredNameFire);
//        editor.putString(KEY_POSTAL_ADDRESS,postalAddress);
//        editor.putString(KEY_RESIDENTIAL_ADDRESS, residentialAddress);
//        editor.putString(KEY_OCCUPATION, occupation);
//        editor.putString(KEY_TELEPHONE, telephone);
//        editor.putString(KEY_FAX, fax);
//        editor.putString(KEY_DATE_OF_BIRTH, dateOfBirth);
//        editor.putString(KEY_NEXT_OF_KIN, nextOfKin);


        // commit changes
        editor.commit();
    }


    // Get Digital Fire Registration State
    public boolean isDigitalFireRegistered(){
        return pref.getBoolean(IS_DIGITAL_FIRE_REGISTERED, false);
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getDigitalFireRegistrationDetails(){
        HashMap<String, String> registrationDetailsFire = new HashMap<String, String>();
        // user name
        //user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        registrationDetailsFire.put(KEY_POLICY_NUMBER_FIRE, pref.getString(KEY_POLICY_NUMBER_FIRE, null));

        // user password id
        //user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        registrationDetailsFire.put(KEY_RESIDENTIAL_ADDRESS_FIRE, pref.getString(KEY_RESIDENTIAL_ADDRESS_FIRE, null));

        registrationDetailsFire.put(KEY_INSURED_NAME_FIRE,pref.getString(KEY_INSURED_NAME_FIRE,null));
//        user.put(KEY_POSTAL_ADDRESS, pref.getString(KEY_POSTAL_ADDRESS, null));
//        user.put(KEY_RESIDENTIAL_ADDRESS, pref.getString(KEY_RESIDENTIAL_ADDRESS, null));
//        user.put(KEY_OCCUPATION, pref.getString(KEY_OCCUPATION, null));
//        user.put(KEY_TELEPHONE, pref.getString(KEY_TELEPHONE, null));
//        user.put(KEY_FAX, pref.getString(KEY_FAX, null));
//        user.put(KEY_DATE_OF_BIRTH, pref.getString(KEY_DATE_OF_BIRTH, null));
//        user.put(KEY_NEXT_OF_KIN, pref.getString(KEY_NEXT_OF_KIN, null));

        // return user
        return registrationDetailsFire;
    }



}
