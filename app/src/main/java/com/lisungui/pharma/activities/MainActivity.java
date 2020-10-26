package com.lisungui.pharma.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.lisungui.pharma.FirebaseChating.ChattingFragment;
import com.lisungui.pharma.FirebaseChating.InboxFragment;
import com.lisungui.pharma.R;
import com.lisungui.pharma.alarm.AlarmServiceBroadcastReciever;
import com.lisungui.pharma.constant.StringConstant;
import com.lisungui.pharma.fcm.Config;
import com.lisungui.pharma.fcm.DeleteTokenService;
import com.lisungui.pharma.fcm.NotificationUtils;
import com.lisungui.pharma.fragment.HealthTipsFragment;
import com.lisungui.pharma.fragment.MyAccountFragment;
import com.lisungui.pharma.fragment.NewHomeFragment;
import com.lisungui.pharma.fragment.NotificationFragment;
import com.lisungui.pharma.fragment.CountriesPriceFragment;
import com.lisungui.pharma.fragment.PriceFragment;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.AdvertisePojo;
import com.lisungui.pharma.rest.RestClient;
import com.lisungui.pharma.service.DownloadService;
import com.lisungui.pharma.utility.CommonUtility;
import com.lisungui.pharma.utility.PrefManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

import static com.lisungui.pharma.helper.SQLiteDataBaseHelper.ORDER_TABLE;
import static com.lisungui.pharma.helper.SQLiteDataBaseHelper.USER_TABLE;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FragmentTransaction transaction;
    private String title;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    BannerSlider bannerSlider;
    List<Banner> banners = new ArrayList<>();
    private ImageView bannerImageView;
    private RelativeLayout bannerRelativeLayout;
    private BottomNavigationView bottomNavigation;
    private boolean resumedActivity = false;
    private Intent intentResume;
    private SQLiteDataBaseHelper dataBaseHelper;
    private DatabaseReference firebase;
    private String fcmToken;

    /*
     * flanez dans les rayons de notre = Stroll the rays of our
     * un nouveau besoin = a new need
     * infos perso = personal information
     * ma ligne = my line
     * factures = bills
     * ma conso = my conso
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fcmToken = FirebaseInstanceId.getInstance().getToken();
        if (CommonUtility.isInternetON(this)) {
            PrefManager.setFCM(this, fcmToken);
        }
        Intent mathAlarmServiceIntent = new Intent(this, AlarmServiceBroadcastReciever.class);
        sendBroadcast(mathAlarmServiceIntent, null);
        bannerSlider = (BannerSlider) findViewById(R.id.banner_slider1);
        bannerImageView = (ImageView) findViewById(R.id.banner_imageview);
        bannerRelativeLayout = (RelativeLayout) findViewById(R.id.banner_rl);
        fragmentManager = getSupportFragmentManager();
        getAdvertise();
        //SQLiteDatabase db=dataBaseHelper.getReadableDatabase();
        //dataBaseHelper.DropTables(db);
        //Run the Service
        Intent serviceIntent = new Intent(this, DownloadService.class);
        startService(serviceIntent);
        updateMenu();



        itemCart();

        //show default home screen
        bannerRelativeLayout.setVisibility(View.VISIBLE);
        bannerSlider.setVisibility(View.VISIBLE);
        fragment = new NewHomeFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();
        getSupportActionBar().setTitle(R.string.str_home);

        //on intent received only in oncreate
        Intent intent = getIntent();
        onNewIntent(intent);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_home:
                        //fragment = new HomeFragment();
                        fragment = new NewHomeFragment();
                        title = getString(R.string.str_home);
                        bannerSlider.setVisibility(View.VISIBLE);
                        bannerRelativeLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.action_price:
                        fragment = new PriceFragment();
                        title = getString(R.string.str_check_med);
                        bannerSlider.setVisibility(View.GONE);
                        bannerRelativeLayout.setVisibility(View.GONE);
                        break;

                    case R.id.action_place:
                        fragment = new NotificationFragment();
                        //fragment = new NewNotificationFragment();
                        title = getString(R.string.str_notification);
                        bannerSlider.setVisibility(View.GONE);
                        bannerRelativeLayout.setVisibility(View.GONE);
                        break;

                    case R.id.action_account:
                        fragment = new MyAccountFragment();
                        title = getString(R.string.str_profile);
                        bannerSlider.setVisibility(View.GONE);
                        bannerRelativeLayout.setVisibility(View.GONE);
                        break;

                    case R.id.action_inbox:
                        fragment = new InboxFragment();
                        title = getString(R.string.str_inbox);
                        bannerSlider.setVisibility(View.GONE);
                        bannerRelativeLayout.setVisibility(View.GONE);
                        break;
                }

                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                getSupportActionBar().setTitle(title);
                return true;

            }
        });


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                }
                else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
//                    Log.d(TAG, "Push Notification");
//                    String message = intent.getStringExtra("message");
//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }

            }
        };

        displayFirebaseRegId();

        //add banner using image url
        //add banner using resource drawable
        //banners.add(new DrawableBanner(R.drawable.add_one));

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @UiThread
    private void updateMenu() {

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        if (bottomNavigation != null) {
            bottomNavigation.inflateMenu(R.menu.bottom_menu);

        }
        bottomNavigation.getMenu().findItem(R.id.action_home).setChecked(true);
        if (PrefManager.getUserType(this).equals("4")) {


            bottomNavigation.getMenu().findItem(R.id.action_account).setVisible(false);
        } else {
            bottomNavigation.getMenu().findItem(R.id.action_account).setVisible(true);

        }
        if (!PrefManager.getUserType(this).equals("1")) {
            bottomNavigation.getMenu().findItem(R.id.action_inbox).setVisible(true);
        } else {
            bottomNavigation.getMenu().findItem(R.id.action_inbox).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                showDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getAdvertise() {

        RestClient.RestApiInterface service = RestClient.getClient();
        Call<AdvertisePojo> call = service.getAdvertise();
        call.enqueue(new Callback<AdvertisePojo>() {
            @Override
            public void onResponse(Call<AdvertisePojo> call, Response<AdvertisePojo> response) {

                Log.d(TAG, "onResponse : " + response.toString());

                if (response.isSuccessful()) {

                    bannerImageView.setVisibility(View.GONE);

                    AdvertisePojo listLoc = response.body();


                    for (int i = 0; i < listLoc.getAdvertise().size(); i++) {
                        //banners.add(new RemoteBanner("http://lisunguipharma.com/application/webroot/images/profile/" + listLoc.getAdvertise().get(i).adv_img));
                        banners.add(new RemoteBanner(StringConstant.PIC_URL + listLoc.getAdvertise().get(i).adv_img));
                        //banners.add(new RemoteBanner(StringConstant.BASE_URL + listLoc.getAdvertise().get(i).adv_img));

                        //StringConstant.BASE_URL
                        //Log.e("url-","http://lisunguipharma.com/application/webroot/images/profile/"+listLoc.getAdvertise().get(i).adv_img);
                        //Log.e("url-","https://lisunguipharma.com/application/webroot/images/profile/"+listLoc.getAdvertise().get(i).adv_img);
                        Log.e("url-", StringConstant.PIC_URL + listLoc.getAdvertise().get(i).adv_img);

                    }

                    bannerSlider.setBanners(banners);
                    bannerSlider.onAnimateIndicatorsChange();
                    bannerSlider.setInterval(3000);


/*for (int i = 0; i < longArrayList.size(); i++) {
                        if(i==0) { ////FOR ANIMATING THE CAMERA FOCUS FIRST TIME ON THE GOOGLE MAP
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(longArrayList.get(i).getPharm_lat(), longArrayList.get(i).getPharm_lng())).zoom(15).build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                        mCurrLocationMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(longArrayList.get(i).getPharm_lat(), longArrayList.get(i).getPharm_lng())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                .title(longArrayList.get(i).getPharm_name()));
                    }*/

                } else {
                    bannerRelativeLayout.setVisibility(View.VISIBLE);
                    bannerImageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AdvertisePojo> call, Throwable t) {
                Log.d(TAG, "onFailure");
                bannerRelativeLayout.setVisibility(View.VISIBLE);
                bannerImageView.setVisibility(View.VISIBLE);

            }

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        CommonUtility.updateUser(this, true, "0", true);

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));


        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        resumedActivity = true;
        //Toast.makeText(MainActivity.this,"OnNewIntent", Toast.LENGTH_SHORT).show();
        setIntent(intent);
        if (intent.hasExtra("tip_message") && resumedActivity) {
            //fragment = new NotificationFragment();
            bannerRelativeLayout.setVisibility(View.GONE);

            bannerSlider.setVisibility(View.GONE);
            fragment = new HealthTipsFragment();
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, fragment).commit();
            getSupportActionBar().setTitle(getString(R.string.health_tips));
        }
        else if (intent.hasExtra("NOTIFICATION") && resumedActivity) {
            bannerRelativeLayout.setVisibility(View.GONE);

            bannerSlider.setVisibility(View.GONE);
            fragment = new NotificationFragment();
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, fragment).commit();
            getSupportActionBar().setTitle(getString(R.string.str_notification));

        }
        else if (intent.hasExtra("MED_NOTIFICATION") && resumedActivity) {


            SharedPreferences pref = getSharedPreferences("Pharmacy", 0);
            String user_id = pref.getString("USER_ID", "");
            String user_type_id = pref.getString("USERTYPEID", "");

            if (user_type_id.equals("1")) {
                bannerRelativeLayout.setVisibility(View.VISIBLE);
                bannerSlider.setVisibility(View.VISIBLE);
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                getSupportActionBar().setTitle(R.string.str_home);
            } else if (user_type_id.equals("2")) {
                Intent promointent = new Intent(MainActivity.this, PromotionActivity.class);
                startActivity(promointent);

            }

        }
        else if (intent.hasExtra("prom_noti_msg") && resumedActivity) {
            //fragment = new NotificationFragment();
            fragment = new NewHomeFragment();
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, fragment).commit();
            getSupportActionBar().setTitle(R.string.str_home);
        }
        else if (intent.hasExtra("CHAT")) {
            if (ChattingFragment.active){return;}
            fragment = new InboxFragment();
            bannerSlider.setVisibility(View.GONE);
            bannerRelativeLayout.setVisibility(View.GONE);
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, fragment).commit();
            getSupportActionBar().setTitle(R.string.inbox);
        }

        resumedActivity = false;
        intentResume = null;

    }

    private void displayFirebaseRegId() {
        Intent i = new Intent(this, DeleteTokenService.class);
        startService(i);
    }


    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_home_add);

        LinearLayout lin_monitor = dialog.findViewById(R.id.lin_monitor);
        LinearLayout lin_tracking = dialog.findViewById(R.id.lin_tracking);
        LinearLayout lin_sharing = dialog.findViewById(R.id.lin_sharing);
        LinearLayout lin_about_us = dialog.findViewById(R.id.lin_about_us);
        LinearLayout lin_rating = dialog.findViewById(R.id.lin_rating);
        LinearLayout lin_sign_out = dialog.findViewById(R.id.lin_sign_out);

        LinearLayout lin_promotionlist = dialog.findViewById(R.id.lin_promotionlist);

        lin_monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                Intent intent = new Intent(MainActivity.this, MonitoringActivity.class);
                startActivity(intent);
            }
        });

        lin_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                Intent intent = new Intent(MainActivity.this, OrderTackingActivity.class);
                startActivity(intent);
            }
        });

        lin_sharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Telecharger Gratuitement votre application Lisungui vous permettant de trouver les pharmacies de garde et de geo-localiser les pharmacies les plus proches en cliquant sur ce lien: https://play.google.com/store/apps/details?id=com.lisungui.pharma");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        lin_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });

        lin_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });


        lin_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //todo: here sing out need to change firebase database
                CommonUtility.updateUser(MainActivity.this, false, "" + new Date().getTime(), false);
                dialog.dismiss();

                SharedPreferences pref = getSharedPreferences("Pharmacy", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("LoggedIn", false);

                editor.clear();
                editor.commit();

                //editor.apply();

                dataBaseHelper = new SQLiteDataBaseHelper(MainActivity.this);
                SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
                db.execSQL("delete from " + ORDER_TABLE);
                db.execSQL("delete from " + USER_TABLE);//USER_TABLE

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        SharedPreferences pref = getSharedPreferences("Pharmacy", 0);
        String user_id = pref.getString("USER_ID", "");
        String user_type_id = pref.getString("USERTYPEID", "");

        if (user_type_id.equals("1"))
            lin_promotionlist.setVisibility(View.GONE);
        else if (user_type_id.equals("2"))
            lin_promotionlist.setVisibility(View.VISIBLE);

        lin_promotionlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent intent = new Intent(MainActivity.this, PromotionActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {


        fragment = new NewHomeFragment();
        title = getString(R.string.str_home);
        bannerSlider.setVisibility(View.VISIBLE);

        bannerRelativeLayout.setVisibility(View.VISIBLE);

        transaction = fragmentManager.beginTransaction();
        //transaction.addToBackStack("NewHomeFragment");
        transaction.replace(R.id.main_container, fragment, "NewHomeFragment").commit();

        getSupportActionBar().setTitle(R.string.str_home);

        bottomNavigation.getMenu().findItem(R.id.action_home).setChecked(true);
        if (PrefManager.getUserType(this).equals("4")) {
            bottomNavigation.getMenu().findItem(R.id.action_account).setVisible(false);
        } else {
            bottomNavigation.getMenu().findItem(R.id.action_account).setVisible(true);

        }

        NewHomeFragment fragment1 = new NewHomeFragment();
        fragment1 = (NewHomeFragment) getSupportFragmentManager().findFragmentByTag("NewHomeFragment");

        if (fragment1 != null && fragment1.isAdded()) {
            Log.d(TAG, "onFragmentAdded" + fragment1.isAdded());
            String str_exit = getResources().getString(R.string.str_exit);
            String str_yes = getResources().getString(R.string.str_yes);
            String str_no = getResources().getString(R.string.str_no);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(str_exit);
            builder.setPositiveButton(str_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.setNegativeButton(str_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }


    }

}


/*
name:admin
password:Lisungui@P2465
*/
