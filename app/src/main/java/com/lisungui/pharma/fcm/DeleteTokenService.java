package com.lisungui.pharma.fcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.lisungui.pharma.models.TokenRegPojo;
import com.lisungui.pharma.rest.RestClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public class DeleteTokenService extends IntentService
{
    public static final String TAG = DeleteTokenService.class.getSimpleName();

    public DeleteTokenService()
    {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        try
        {
            // Check for current token
            String originalToken = getTokenFromPrefs();
            //Log.e(TAG, "Token before deletion: " + originalToken);

            // Resets Instance ID and revokes all tokens.
            FirebaseInstanceId.getInstance().deleteInstanceId();

            // Clear current saved token
            storeRegIdInPref("");

            // Check for success of empty token
            String tokenCheck = getTokenFromPrefs();
           // Log.e(TAG, "Token deleted. Proof: " + tokenCheck);

            // Now manually call onTokenRefresh()
            //Log.e(TAG, "Getting new token"+FirebaseInstanceId.getInstance().getToken());
            FirebaseInstanceId.getInstance().getToken();
            storeRegIdInPref(FirebaseInstanceId.getInstance().getToken());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

//    private void saveTokenToPrefs(String _token)
//    {
//        // Access Shared Preferences
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = preferences.edit();
//
//        // Save to SharedPreferences
//        editor.putString("registration_id", _token);
//        editor.apply();
//    }


    private void storeRegIdInPref(String token) {
/*
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("Pharmacy", 0);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("USER_FCM_TOKEN", token);
//        editor.apply();
//        Log.e("token", token);
*/

/*
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.apply();
*/

    }


    private String getTokenFromPrefs()
    {
        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF2, MODE_PRIVATE);
        return preferences.getString("regId", "");
    }

}