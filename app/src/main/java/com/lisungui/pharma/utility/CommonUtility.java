package com.lisungui.pharma.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.lisungui.pharma.models.UserFirebaseModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by siddeshwar on 7/12/16.
 */
public class CommonUtility {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private final static boolean INBOX_LIVE = false;

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isInternetON(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return false;
        } else {
            return info.isConnectedOrConnecting();
        }
    }

    public static boolean checkPlayServices(Context context) {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(context,
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
            }
            return false;
        }
        return true;
    }


    public static DatabaseReference getUserDatabase(String id) {
        return FirebaseDatabase.getInstance().getReference().child("User").child(id);
    }

    public static void updateUser(Context context, boolean isonline, String lastLogin, boolean isActive) {

        String id = PrefManager.getUserID(context);
        String type = PrefManager.getUserType(context);

        if (!type.equalsIgnoreCase("1")) {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            UserFirebaseModel model = new UserFirebaseModel(
                    id,type, isonline, refreshedToken,
                    PrefManager.getUserProfilePic(context),
                    ""+getDateTime(),
                    PrefManager.getUserName(context),
                    isActive);

            CommonUtility.getUserDatabase(type+"_"+id)
                    .setValue(model);
        }
    }

    public static String getTimeAgo(long time) {

        final int SECOND_MILLIS = 1000;
        final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        final int DAY_MILLIS = 24 * HOUR_MILLIS;

        if (time < 1000000000000L) {
            time *= 1000;
        }
        long now = 0;
        //long now = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy hh:mm:ss");
        String test = sdf.format(cal.getTime());
        long millis = cal.getTimeInMillis();
        Log.e("TEST", "" + millis);

        try {
            //formatting the dateString to convert it into a Date
            Date date = sdf.parse(test);
            System.out.println("Given Time in milliseconds : " + date.getTime());

            Calendar calendar = Calendar.getInstance();
            //Setting the Calendar date and time to the given date and time
            calendar.setTime(date);
            System.out.println("Given Time in milliseconds : " + calendar.getTimeInMillis());
            now = date.getTime();


        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (time > now || time <= 0) {
            return null;
        }


        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "1min ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "1hr ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static long getDateTime() {

       return new Date().getTime();
    }

}
