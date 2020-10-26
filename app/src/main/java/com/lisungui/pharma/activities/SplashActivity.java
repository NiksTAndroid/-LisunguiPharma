package com.lisungui.pharma.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.lisungui.pharma.R;
import com.lisungui.pharma.rest.RestClient;
import com.lisungui.pharma.utility.PrefManager;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = getIntent();
        if (intent.hasExtra("GPS_CALL")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, 2000);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Locale.getDefault().getLanguage().equals("en")) {
            if (PrefManager.getUserType(this).equals("4")) {
                setPharmaLang("en");
            } else setUserLang("en");
        } else {
            if (PrefManager.getUserType(this).equals("4")) {
                setPharmaLang("fr");
            } else setUserLang("fr");
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            showSettingsAlert();
        } else {
            final SharedPreferences pref = getSharedPreferences("Pharmacy", 0);
//            final SQLiteDataBaseHelper dataBaseHelper = new SQLiteDataBaseHelper(this);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

/*
                    if (dataBaseHelper.getUserCount() > 0) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
*/

                    if (pref.getBoolean("LoggedIn", false)) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }

                    finish();
                }
            }, 2000);
        }

    }

    private void setPharmaLang(String fr) {
        RestClient.getClient().setLanguagePharmacy(PrefManager.getUserID(this), fr)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.v("RESPONCE", response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
    }


    private void setUserLang(String ln) {
        RestClient.getClient().setLanguageUser(PrefManager.getUserID(this), ln)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.v("RESPONCE", response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
    }

    public void showSettingsAlert() {

        String str_gps_turned_off = getResources().getString(R.string.str_gps_turned_off);
        String str_gps_not_enabled = getResources().getString(R.string.str_gps_not_enabled);
        String str_cancel = getResources().getString(R.string.str_cancel);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle(str_gps_turned_off);

        // Setting Dialog Message
        alertDialog.setMessage(str_gps_not_enabled);

        // On pressing Settings button
        alertDialog.setPositiveButton(getString(R.string.str_settings), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton(str_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
