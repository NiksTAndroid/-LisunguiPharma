package com.lisungui.pharma.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lisungui.pharma.R;
import com.lisungui.pharma.activities.MainActivity;
import com.lisungui.pharma.adapter.NotificationAdapter;
import com.lisungui.pharma.adapter.PH_PharmacyNotificationsAdapter;
import com.lisungui.pharma.adapter.PlaceOrderNotificationAdapter;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.Notification;
import com.lisungui.pharma.models.PharmacyNotificationsResponse;
import com.lisungui.pharma.models.PlaceOrderNotificationData;
import com.lisungui.pharma.models.PlaceOrderNotificationDetails;
import com.lisungui.pharma.rest.RestClient;
import com.lisungui.pharma.utility.PrefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CALL_PHONE;

public class NotificationFragment extends Fragment {

    private TextView txt_empty;
    private ListView list_pharma;
    private TextView txt_title;
    private NotificationAdapter adapter;
    private ArrayList<Notification> notifications = new ArrayList<>();
    private SQLiteDataBaseHelper dataBaseHelper;
    private Spanned titles;

    private ProgressDialog pDialog;
    private Context context;
    private String user_id;
    private SharedPreferences pref;

    private PlaceOrderNotificationAdapter placeOrderNotificationAdapter;
    private List<PlaceOrderNotificationDetails> placeOrderNotificationDetailsList;
    private List<PharmacyNotificationsResponse.Notification> pharamcyNotificationsList = new ArrayList<>();
    private PH_PharmacyNotificationsAdapter pharmaAdapter;

    public NotificationFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to make visible/invisible the icons/logos in menu on actionbar
        setHasOptionsMenu(true);

        dataBaseHelper = new SQLiteDataBaseHelper(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pharmacy_list, container, false);
        init(view);

        pref = ((MainActivity) getActivity()).getSharedPreferences("Pharmacy", 0);
        user_id = pref.getString("USER_ID", "");

        placeOrderNotificationDetailsList = new ArrayList<>();
        pharmaAdapter = new PH_PharmacyNotificationsAdapter(getActivity(), pharamcyNotificationsList);

        if (Locale.getDefault().getLanguage().equals("en")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                titles = Html.fromHtml("<font color=\"#000000\">" + "Notification " + "</font>" + "<font color=\"#729619\">" + "List" + "</font>", Html.FROM_HTML_MODE_LEGACY);
            } else {
                titles = (Html.fromHtml("<font color=\"#000000\">" + "Notification " + "</font>" + "<font color=\"#729619\">" + "List" + "</font>"));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                titles = Html.fromHtml("<font color=\"#000000\">" + getString(R.string.str_not_list1) + " " + "</font>" + "<font color=\"#729619\">" + getString(R.string.str_not_list2) + "</font>", Html.FROM_HTML_MODE_LEGACY);
            } else {
                titles = (Html.fromHtml("<font color=\"#000000\">" + getString(R.string.str_not_list1) + " " + "</font>" + "<font color=\"#729619\">" + getString(R.string.str_not_list2) + "</font>"));
            }
        }

        txt_title.setText(titles);


        return view;
    }

    private void getFromDB() {
        notifications = dataBaseHelper.getAllNotification();
        dataBaseHelper.closeDB();
    }

    private void setListAdapter() {
        adapter = new NotificationAdapter(getActivity(), notifications);
        list_pharma.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void init(View view) {
        txt_empty = view.findViewById(R.id.txt_empty);
        list_pharma = view.findViewById(R.id.list_pharma);
        txt_title = view.findViewById(R.id.txt_title);
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgressDialog();
        if (PrefManager.getUserType(getActivity()).equals("4")) {
            getPharmacyNotification();
        } else {
            displayPlaceOrderNotification(user_id);
        }
    }

    private void getPharmacyNotification() {

        RestClient.RestApiInterface service = RestClient.getClient();
        Call<PharmacyNotificationsResponse> call = service.getPharmaNotifications(user_id);
        call.enqueue(new Callback<PharmacyNotificationsResponse>() {
            @Override
            public void onResponse(Call<PharmacyNotificationsResponse> call, Response<PharmacyNotificationsResponse> response) {

                hideProgressDialog();
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Something wrong", Toast.LENGTH_SHORT).show();

                    return;
                }
                if (response.body().getStatus() == 0) {
                    String str_check_int = getResources().getString(R.string.data_not_available);
                    Toast.makeText(getContext(), str_check_int, Toast.LENGTH_SHORT).show();

                    return;
                }
                pharamcyNotificationsList.clear();
                pharamcyNotificationsList.addAll(response.body().getNotification());
                list_pharma.setAdapter(pharmaAdapter);
                list_pharma.smoothScrollToPosition(pharamcyNotificationsList.size());
                pharmaAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<PharmacyNotificationsResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(getString(R.string.str_loading));
        pDialog.setCancelable(false);
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void makePhoneCall(String contactNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        //callIntent.setData(Uri.parse("tel:"+8802177690));//change the number
        callIntent.setData(Uri.parse("tel:" + contactNumber));//change the number
        if (ActivityCompat.checkSelfPermission((MainActivity) getActivity(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            //return;
            startActivity(callIntent);
        } else {
            requestPermissions(new String[]{CALL_PHONE}, 1);
        }
        //startActivity(callIntent);
    }

    private void displayPlaceOrderNotification(String user_id) {

        RestClient.RestApiInterface service = RestClient.getClient();
        Call<PlaceOrderNotificationData> call = service.getPlaceOrderNotificationsList(user_id);

        call.enqueue(new Callback<PlaceOrderNotificationData>() {
            @Override
            public void onResponse(Call<PlaceOrderNotificationData> call, Response<PlaceOrderNotificationData> response) {
                hideProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
                        placeOrderNotificationDetailsList = response.body().getOrderNotification();
                        if (placeOrderNotificationDetailsList == null || placeOrderNotificationDetailsList.isEmpty())
                            return;

                        PlaceOrderNotificationAdapter placeOrderNotificationAdapter = new PlaceOrderNotificationAdapter(context, placeOrderNotificationDetailsList, NotificationFragment.this);
                        list_pharma.setAdapter(placeOrderNotificationAdapter);
                        list_pharma.smoothScrollToPosition(placeOrderNotificationDetailsList.size());
                        placeOrderNotificationAdapter.notifyDataSetChanged();
                    } else {
                        String str_check_int = getResources().getString(R.string.data_not_available);
                        Toast.makeText(getContext(), str_check_int, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PlaceOrderNotificationData> call, Throwable t) {
                hideProgressDialog();
                //Log.d(TAG, "onFailure");
                String str_check_int = getResources().getString(R.string.str_check_int);
                Toast.makeText(context, str_check_int, Toast.LENGTH_SHORT).show();

            }
        });

    }


}
