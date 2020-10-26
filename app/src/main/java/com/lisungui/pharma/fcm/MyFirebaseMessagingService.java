package com.lisungui.pharma.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lisungui.pharma.R;
import com.lisungui.pharma.activities.MainActivity;
import com.lisungui.pharma.activities.PH_ActivityOrderDetails;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.TokenRegPojo;
import com.lisungui.pharma.rest.RestClient;
import com.lisungui.pharma.utility.CommonUtility;
import com.lisungui.pharma.utility.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private static final String CHANNEL_ID = "111";
    private Call<TokenRegPojo> call;
    private NotificationUtils notificationUtils;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
//        sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        Log.d(TAG, "token_" + "\t" + refreshedToken);

        sendFCMToServer(refreshedToken);
    }
    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.apply();
    }

    private void sendFCMToServer(String refreshedToken) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Pharmacy", 0);
        String regId = pref.getString("USER_FCM_TOKEN", "");

        Log.d(TAG, "token_regId" + "\t" + regId);

        RestClient.RestApiInterface service = RestClient.getClient();
        if (PrefManager.getUserType(getApplicationContext()).equals("4")) {
            call = service.updatePharmacyToken(pref.getString("USER_ID", ""), refreshedToken);
        } else {
            call = service.updateToken(pref.getString("USER_ID", ""), refreshedToken);
        }
        CommonUtility.updateUser(this, false, "0", true);


        call.enqueue(new Callback<TokenRegPojo>() {
            @Override
            public void onResponse(Call<TokenRegPojo> call, retrofit2.Response<TokenRegPojo> response) {

                if (response.isSuccessful()) {

                    TokenRegPojo tokenRegPojo = response.body();

                    if (tokenRegPojo.getStatus() == 1) {
                        Log.d(TAG, "Status_Updated");
                    } else {
                        Log.d(TAG, "Status\t" + tokenRegPojo.getStatus());
                    }

                } else {
                    Log.d(TAG, "Response_Failed");
                }
            }

            @Override
            public void onFailure(Call<TokenRegPojo> call, Throwable t) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String usertype = PrefManager.getUserType(this);
        if (usertype.equals("4")) {

            if (remoteMessage.getData() != null) {

                Map<String, String> data = remoteMessage.getData();

                createNotificationChannel();

                if (checkIsChattingNotification(data)) {
                    return;
                }


                Intent intent = new Intent(this, PH_ActivityOrderDetails.class)
                        .putExtra("OID", data.get("order_id"));

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(getString(R.string.newPrescriptionorder))
                        .setContentText(data.get("message"))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(Config.NOTIFICATION_ID, notification);
            }

        } else {
            Map<String, String> data = remoteMessage.getData();

            createNotificationChannel();

            if (checkIsChattingNotification(data)) {
                return;
            }

            SQLiteDataBaseHelper dataBaseHelper = new SQLiteDataBaseHelper(this);

            Log.e(TAG, "getData: " + remoteMessage.getData());

            if (dataBaseHelper.getUserCount() > 0) {
                Log.e(TAG, "From: " + remoteMessage.getFrom());

                // Check if message contains a notification payload.
                if (remoteMessage.getNotification() != null) {
                    Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                    handleNotification(remoteMessage.getNotification().getBody());
                }

                // Check if message contains a data payload.
                if (remoteMessage.getData().size() > 0) {
                    Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

                    try {
                        if (remoteMessage.getData().containsKey("notifi_message")
                                || remoteMessage.getData().containsKey("tip_message")
                                || remoteMessage.getData().containsKey("prom_noti_msg")) {
                            handleNotificationData(remoteMessage);
                        } else {
                               JSONObject json = new JSONObject(remoteMessage.getData());
                            handleDataMessage(json);
                        }

                    }
                    catch (Exception e) {
                        Log.e(TAG, "Exception: " + e.getMessage());
                    }

                }

            }
        }
    }

    private boolean checkIsChattingNotification(Map<String, String> data) {

        if (data.containsKey("type")) {
            Intent intent = new Intent(this, MainActivity.class)
                    .putExtra("CHAT", data.get("userid"));

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle(getResources().getString(R.string.msg) +" "+ data.get("username"))
                    .setContentText(data.get("message"))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(Config.NOTIFICATION_ID, notification);
            return true;
        } else {
            return false;
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this)
                    .sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
            Intent resultIntent = new Intent(this, MainActivity.class);
            resultIntent.putExtra("NOTIFICATION", "NOTIFICATION");
            // check for image attachment
            showNotificationMessage(this, message, message, "", resultIntent);
            NotificationUtils notificationUtils = new NotificationUtils(this);
            notificationUtils.playNotificationSound();
        }
    }

    private void handleDataMessage(JSONObject json) {
                try {
                      String message = "";
            String finalmess = "";
            if (getApplicationContext().getString(R.string.lang).contentEquals("English")) {
                finalmess = json.getString("message");
            } else {
                if (json.getString("message").contains("has been Ready In Pharmacy")) {
                    message = json.getString("message").replace("has been Ready In Pharmacy",
                            "Prête en pharmacie");
                    finalmess = message.replace("OrderId", "Cmnde N°");
                } else if (json.getString("message").contains("has been On The Way")) {
                    message = json.getString("message").replace("has been On The Way",
                            "est sur le chemin");
                    finalmess = message.replace("OrderId", "Cmnde N°");
                } else if (json.getString("message").contains("has been Received")) {
                    message = json.getString("message").replace("has been Received",
                            "a été reçue");
                    finalmess = message.replace("OrderId", "Cmnde N°");
                } else finalmess = json.getString("message");
            }


            String order_date = json.getString("order_date");
            int order_id = json.getInt("order_id");


            String[] col = new String[]{
                    SQLiteDataBaseHelper.COLUMN_NOT_ORDER_ID,
                    SQLiteDataBaseHelper.COLUMN_NOT_MESSAGE,
                    SQLiteDataBaseHelper.COLUMN_NOT_DATE
            };

            String[] val = new String[]{
                    String.valueOf(order_id),
                    finalmess,
                    order_date
            };

            try {
                SQLiteDataBaseHelper dataBaseHelper = new SQLiteDataBaseHelper(this);
                long i = dataBaseHelper.insertOrUpdate(SQLiteDataBaseHelper.NOTIFICATION_TABLE, null, col, val);
                Log.d(TAG, "Id : " + i);
            } catch (Exception e) {
                e.printStackTrace();
            }


            String imageUrl = "";

//            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + finalmess);

            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.putExtra("NOTIFICATION", "NOTIFICATION");

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), "", finalmess, "", resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), "", finalmess, "", resultIntent, imageUrl);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void handleNotificationData(RemoteMessage remoteMessage) {
        String imageUrl = "";

        String dataMessage = "";
        String dataTime = "";
        String dataId = "";
        String dataStatus = "";
        String dataTitle = "";

        Log.e(TAG, "containsKey: " + remoteMessage.getData().containsKey("notifi_message") + "\t" + remoteMessage.getData().get("notifi_message"));
        //dataMessage = remoteMessage.getData().get("notifi_message");

        if (remoteMessage.getData().containsKey("notifi_message")) {
            //dataTitle = "Medicine Promotion";
            dataTitle = getResources().getString(R.string.medicine_promotion);
            dataMessage = remoteMessage.getData().get("notifi_message");
            dataTime = remoteMessage.getData().get("notifi_date");
            dataStatus = remoteMessage.getData().get("notifi_status");
            dataId = remoteMessage.getData().get("notifi_user_id");
        } else if (remoteMessage.getData().containsKey("tip_message")) {
            //dataTitle = "Health Tips";
            dataTitle = getResources().getString(R.string.health_tips);
            dataMessage = remoteMessage.getData().get("tip_message");//{tip_date=2019-06-25 03:41:23, tip_id=9, tip_message=user health tip testing, tip_status=1}
            dataTime = remoteMessage.getData().get("tip_date");
            dataStatus = remoteMessage.getData().get("tip_status");
            dataId = remoteMessage.getData().get("tip_id");
        } else if (remoteMessage.getData().containsKey("prom_noti_msg")) {
            dataTitle = "Promotion";
            dataMessage = remoteMessage.getData().get("prom_noti_msg");
            dataTime = remoteMessage.getData().get("prom_noti_date");
            dataStatus = remoteMessage.getData().get("prom_noti_status");
            dataId = remoteMessage.getData().get("prom_noti_id");
        }

        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        //Intent resultIntent = new Intent(Config.PUSH_NOTIFICATION);

        if (remoteMessage.getData().containsKey("tip_message")) {
            resultIntent.putExtra("tip_message", "tip_message");
        } else if (remoteMessage.getData().containsKey("notifi_message"))
            resultIntent.putExtra("MED_NOTIFICATION", "MED_NOTIFICATION");
        else if (remoteMessage.getData().containsKey("prom_noti_msg"))
            resultIntent.putExtra("prom_noti_msg", "prom_noti_msg");

        //LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);

        // check for image attachment
        if (TextUtils.isEmpty(imageUrl)) {
            //showNotificationMessage(getApplicationContext(), "", dataMessage, "", resultIntent);
            showNotificationMessage(getApplicationContext(), dataTitle, dataMessage, "", resultIntent);
        } else {
            // image is present, show notification with image
            //showNotificationMessageWithBigImage(getApplicationContext(), "", dataMessage, "", resultIntent, imageUrl);
            showNotificationMessageWithBigImage(getApplicationContext(), dataTitle, dataMessage, "", resultIntent, imageUrl);
        }
    }//Hello, we apologize for the notifications tests we are doing right now. We will not have any longer. The Lisungui Pharma team!

    /**
     * Showing notification with text only
     */

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */

    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}