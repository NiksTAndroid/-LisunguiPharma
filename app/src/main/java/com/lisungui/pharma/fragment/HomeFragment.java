package com.lisungui.pharma.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.lisungui.pharma.R;
import com.lisungui.pharma.activities.PharmacyDetailsActivity;
import com.lisungui.pharma.adapter.PharmacyListAdapter;
import com.lisungui.pharma.models.LatLongPojo;
import com.lisungui.pharma.models.ListLocations;
import com.lisungui.pharma.rest.RestClient;
import com.lisungui.pharma.utility.CommonUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;

public class HomeFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private ProgressDialog pDialog;

    private double latitude;
    private double longitude;
    private double altitude;

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            msg.arg1 = 0;
            handler.sendMessage(msg);
        }
    };

    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private TextView txt_empty;
    private ListView list_pharma;
    private ArrayList<LatLongPojo> latLongs = new ArrayList<>();

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String ing_value = "2";

            if (message.arg1 == 0) {
                if (CommonUtility.isInternetON(mContext))
                    sendLocation(mCurrentLocation);
                else
                    saveGpsOffline(mCurrentLocation);
            }
            handler.postDelayed(run, Integer.parseInt(ing_value) * 1000);

            return false;
        }
    });

    private Spanned titles;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        pref = mContext.getSharedPreferences("Pharmacy", 0);
        editor = pref.edit();
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pharmacy_list, container, false);

        list_pharma = view.findViewById(R.id.list_pharma);

        txt_empty = view.findViewById(R.id.txt_empty);
        TextView txt_title = view.findViewById(R.id.txt_title);

        if (Locale.getDefault().getLanguage().equals("en")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                titles = Html.fromHtml("<font color=\"#000000\">" + "Pharmacy " + "</font>" + "<font color=\"#729619\">" + "List" + "</font>", Html.FROM_HTML_MODE_LEGACY);
            } else {
                titles = (Html.fromHtml("<font color=\"#000000\">" + "Pharmacy " + "</font>" + "<font color=\"#729619\">" + "List" + "</font>"));
            }



        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                titles = Html.fromHtml("<font color=\"#000000\">" + getString(R.string.str_pharm_list1) + " " + "</font>" + "<font color=\"#729619\">" + getString(R.string.str_pharm_list2) + "</font>", Html.FROM_HTML_MODE_LEGACY);
            } else {
                titles = (Html.fromHtml("<font color=\"#000000\">" + getString(R.string.str_pharm_list1) + " " + "</font>" + "<font color=\"#729619\">" + getString(R.string.str_pharm_list2) + "</font>"));
            }
                }


        txt_title.setText(titles);
        String str_loading = getResources().getString(R.string.str_loading);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(str_loading);
        pDialog.setCancelable(false);

        setListViewAdapter();

        list_pharma.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                LatLongPojo latLong = latLongs.get(i);
                Intent intent = new Intent(getActivity(), PharmacyDetailsActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("CurrentPharmacy", latLong);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });

        return view;
    }



    private void setListViewAdapter() {

        Log.d(TAG, "153");
        PharmacyListAdapter adapter = new PharmacyListAdapter(getActivity(), latLongs);
        list_pharma.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getNearLocations(double lat, double lng) {
        showProgressDialog();

        RestClient.RestApiInterface service = RestClient.getClient();
        Call<ListLocations> call = service.getNearLocations("" + lat, "" + lng);
        call.enqueue(new Callback<ListLocations>() {
            @Override
            public void onResponse(Call<ListLocations> call, Response<ListLocations> response) {

                hideProgressDialog();

                disConnect();

                Log.d(TAG, "onResponse : " + response);

                if (response.isSuccessful()) {

                    ListLocations listLoc = response.body();

                    if (listLoc.getStatus() == 1) {
                        txt_empty.setVisibility(View.GONE);
                        latLongs = listLoc.getPharmacy();

                        Log.d(TAG, "Response_1:" + response.body().toString());
                        setListViewAdapter();

                        Log.d(TAG, "Response Array : " + latLongs.toString());
                    } else {
                        txt_empty.setVisibility(View.VISIBLE);
//                        Toast.makeText(PharmacyListActivity.this, "No List Available", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    //String str_check_int = getResources().getString(R.string.str_check_int);
                    String str_check_int = getActivity().getResources().getString(R.string.data_not_available);
                    Toast.makeText(getActivity(), str_check_int, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListLocations> call, Throwable t) {
                hideProgressDialog();
                Log.d(TAG, "onFailure");
                disConnect();
                String str_check_int = getActivity().getResources().getString(R.string.str_check_int);
                Toast.makeText(getActivity(), str_check_int, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), "Something went wrong!!!", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_refresh).setVisible(true);
        menu.findItem(R.id.menu_item_save).setVisible(false);
        menu.findItem(R.id.menu_item_delete).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {

            if (!CommonUtility.isInternetON(mContext)) {
                String str_check_int = getResources().getString(R.string.str_check_int);
                Toast.makeText(mContext, str_check_int, Toast.LENGTH_LONG).show();
            } else {
                getGps();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LocationManager manager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            if (!CommonUtility.isInternetON(mContext)) {
                String str_check_int = getResources().getString(R.string.str_check_int);
                Toast.makeText(mContext, str_check_int, Toast.LENGTH_LONG).show();
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    askMultiplePermission(1);
                } else {

                    if (!pref.getString("lat", "").isEmpty() && !pref.getString("lng", "").isEmpty()) {
                        Log.d(TAG, "Lat : " + pref.getString("lat", ""));
                        latitude = Double.parseDouble(pref.getString("lat", ""));
                        longitude = Double.parseDouble(pref.getString("lng", ""));
                        getNearLocations(latitude, longitude);
                    } else {
                        getGps();
                    }
                }
            }

        } else {
            String str_gps_turned_off = getResources().getString(R.string.str_gps_turned_off);
            Toast.makeText(mContext, str_gps_turned_off, Toast.LENGTH_SHORT).show();
        }

    }

    private void getGps() {
        if (CommonUtility.checkPlayServices(mContext)) {
            showProgressDialog();
            buildGoogleApiClient(mContext);
        } else {
            Toast.makeText(mContext, "Not Updated with Googple Play Services", Toast.LENGTH_LONG).show();
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(mContext, permission);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            return ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission);
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Light_Dialog_Alert)
                .setMessage(message)
                .setPositiveButton(getString(R.string.str_ok), okListener)
                .setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }

    private void askMultiplePermission(final int request_code) {

        List<String> permissionsNeeded = new ArrayList<>();
        final List<String> permissionsList = new ArrayList<>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (request_code == 1) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            1);
                                }

                            }
                        });
                return;
            }
            if (request_code == 1) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), 1);
            }
            return;
        }

        if (request_code == 1) {
            getGps();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        Map<String, Integer> perms = new HashMap<String, Integer>();
        // Initial
        perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
        // Fill with results
        for (int i = 0; i < permissions.length; i++)
            perms.put(permissions[i], grantResults[i]);
        // Check for ACCESS_FINE_LOCATION
        if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // All Permissions Granted
            getGps();
        } else {
            // Permission Denied
            String str_per_deny = getResources().getString(R.string.str_per_deny);
            Toast.makeText(mContext, str_per_deny, Toast.LENGTH_SHORT).show();
        }
    }

    private synchronized void buildGoogleApiClient(Context context) {

        long interval = 1000 * Integer.parseInt("10");
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(interval); // Update location every second
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mGoogleApiClient.isConnected()) {
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                handler.postDelayed(run, Integer.parseInt("2") * 1000);
            } catch (SecurityException e) {
                String str_allow_per = getResources().getString(R.string.str_allow_per);
                Toast.makeText(mContext, str_allow_per, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void disConnect() {
        hideProgressDialog();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        if (handler != null)
            handler.removeCallbacks(run);
    }

    @Override
    public void onConnectionSuspended(int i) {
        hideProgressDialog();
        Toast.makeText(mContext, "onConnectionSuspended", Toast.LENGTH_LONG).show();
        switch (i) {
            case 1:
                Toast.makeText(mContext, "service NA", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(mContext, "service NA", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
    }

    private void sendLocation(Location mCurrentLocation) {

        if (mCurrentLocation != null) {
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();
            altitude = mCurrentLocation.getAltitude();

            editor.putString("lat", "" + mCurrentLocation.getLatitude());
            editor.putString("lng", "" + mCurrentLocation.getLongitude());
            editor.apply();
            getNearLocations(latitude, longitude);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(mContext, connectionResult.toString(), Toast.LENGTH_LONG).show();
        disConnect();
    }

    private void saveGpsOffline(Location myLocation) {
        if (myLocation != null) {
            editor.putString("lat", "" + myLocation.getLatitude());
            editor.putString("lng", "" + myLocation.getLongitude());
            editor.commit();
        }
    }
}
