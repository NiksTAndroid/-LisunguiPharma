package com.lisungui.pharma.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.lisungui.pharma.models.TokenRegPojo;
import com.lisungui.pharma.rest.RestClient;
import com.lisungui.pharma.utility.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by administrator on 19/1/17.
 */

public class TokenRegisterService extends IntentService {

    private static final String TAG = TokenRegisterService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public TokenRegisterService() {
        super(TokenRegisterService.class.getSimpleName());
    }

    public TokenRegisterService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "onHandleIntent");

        SharedPreferences pref = getSharedPreferences("Pharmacy", 0);

        String gcm_key = intent.getExtras().getString("gcm_key");

        RestClient.RestApiInterface service = RestClient.getClient();
        Call<TokenRegPojo> call;
        if (PrefManager.getUserType(getApplicationContext()).equals("4")) {
            call = service.updateToken(pref.getString("USER_ID", ""), gcm_key);
        } else {
            call = service.updatePharmacyToken(pref.getString("USER_ID", ""), gcm_key);
        }

        call.enqueue(new Callback<TokenRegPojo>() {
            @Override
            public void onResponse(Call<TokenRegPojo> call, retrofit2.Response<TokenRegPojo> response) {

                if (response.isSuccessful()) {

                    TokenRegPojo tokenRegPojo = response.body();

                    if (tokenRegPojo.getStatus() == 1) {
                        Log.d(TAG, "Status Updated");
                    } else {
                        Log.d(TAG, "Status " + tokenRegPojo.getStatus());
                    }

                } else {
                    Log.d(TAG, "Response Failed");
                }
            }

            @Override
            public void onFailure(Call<TokenRegPojo> call, Throwable t) {
                Log.d(TAG, "onFailure");
            }
        });

    }
}
