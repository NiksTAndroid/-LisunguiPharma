package com.lisungui.pharma.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lisungui.pharma.FirebaseChating.ChattingActivity;
import com.lisungui.pharma.R;
import com.lisungui.pharma.models.LatLongPojo;
import com.lisungui.pharma.models.UserFirebaseModel;
import com.lisungui.pharma.utility.CommonUtility;
import com.lisungui.pharma.utility.PrefManager;

import java.util.Locale;

public class PharmacyDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = PharmacyDetailsActivity.class.getSimpleName();

    private TextView txt_pharm_name, txt_address, txt_distance, txt_phone, txt_timing, txt_night, txt_insurance;

    private String pharm_name;
    private double pharm_lat;
    private double pharm_lng;
    private String pharm_insurance;
    private RelativeLayout chat_Layout;
    private String PHARMACYID = "123";
    private DatabaseReference userDatabase;
    private UserFirebaseModel database;
    private ValueEventListener changeValueEvent;
    private boolean isactive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();

        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle.containsKey("CurrentPharmacy")) {
                LatLongPojo latLong = (LatLongPojo) bundle.getSerializable("CurrentPharmacy");

                pharm_name = latLong.getPharmName();
                pharm_lat = Double.parseDouble(latLong.getPharmLat());
                PHARMACYID = latLong.getPharmId();
                userDatabase = CommonUtility.getUserDatabase("4_"+PHARMACYID);
                chat_Layout.setVisibility(View.GONE);

                changeValueEvent = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        database = dataSnapshot.getValue(UserFirebaseModel.class);
                        if (database != null) {
                            isactive = database.isActive();
                            if (!PrefManager.getUserType(PharmacyDetailsActivity.this)
                                    .equals("1") && isactive) {
                                findViewById(R.id.start_chatting).setVisibility(View.VISIBLE);
                            } else {
                                findViewById(R.id.start_chatting).setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                userDatabase.addValueEventListener(changeValueEvent);

                pharm_lng = Double.parseDouble(latLong.getPharmLng());
                String pharm_mb_no = latLong.getPharmMbNo();
                double pharm_distance = Double.parseDouble(latLong.getPharmDistance());
                String pharm_address = latLong.getPharmAddress();
                String pharm_night = latLong.getPharmNight();
                String pharm_s_day = latLong.getPharmSDay();
                String pharm_e_day = latLong.getPharmEDay();
                String pharm_s_time = latLong.getPharmSTime();
                String pharm_e_time = latLong.getPharmETime();

                pharm_insurance = latLong.getPharmInsurance();

                Log.d(TAG, "pharma name : " + pharm_name);
                Log.d(TAG, "lat : " + pharm_lat);
                Log.d(TAG, "lng : " + pharm_lng);

                txt_pharm_name.setText(pharm_name);
                txt_address.setText(pharm_address);
                txt_distance.setText(pharm_distance + " KM");
                txt_phone.setText(pharm_mb_no);

                txt_insurance.setText(pharm_insurance);

                if (Locale.getDefault().getLanguage().equals("en")) {
                    txt_night.setText(pharm_night);

                    if (!pharm_s_day.isEmpty())
                        txt_timing.setText(pharm_s_day + " " + getString(R.string.str_to) + " " + pharm_e_day + " (" + pharm_s_time + " " + getString(R.string.str_to) + " " + pharm_e_time + ")");

                } else {

                    if (pharm_night.equalsIgnoreCase("Yes")) {
                        txt_night.setText(getString(R.string.str_yes));
                    } else {
                        txt_night.setText(getString(R.string.str_no));
                    }

                    if (!pharm_s_day.isEmpty())
                        txt_timing.setText(getDay(pharm_s_day) + " " + getString(R.string.str_to) + " " + getDay(pharm_e_day) + " (" + pharm_s_time + " " + getString(R.string.str_to) + " " + pharm_e_time + ")");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private String getDay(String day) {

        switch (day) {
            case "Dimanche":
            case "Sunday":
                day = getString(R.string.str_sunday);
                break;

            case "Lundi":
            case "Monday":
                day = getString(R.string.str_monday);
                break;

            case "Mardi":
            case "Tuesday":
                day = getString(R.string.str_tuesday);
                break;

            case "Mercredi":
            case "Wednesday":
                day = getString(R.string.str_wednesday);
                break;

            case "Jeudi":
            case "Thursday":
                day = getString(R.string.str_thursday);
                break;

            case "Vendredi":
            case "Friday":
                day = getString(R.string.str_friday);
                break;

            case "Samedi":
            case "Saturday":
                day = getString(R.string.str_saturday);
                break;

            default:
                day = getString(R.string.str_monday);
                break;

        }

        return day;
    }

    private void init() {

        txt_pharm_name = (TextView) findViewById(R.id.txt_pharm_name);
        txt_address = (TextView) findViewById(R.id.txt_address);
        txt_distance = (TextView) findViewById(R.id.txt_distance);
        txt_phone = (TextView) findViewById(R.id.txt_phone);
        txt_timing = (TextView) findViewById(R.id.txt_timing);
        txt_night = findViewById(R.id.txt_night);
        chat_Layout = findViewById(R.id.start_chatting);
        txt_insurance = (TextView) findViewById(R.id.txt_insurance);//txt_insurance

        chat_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PharmacyDetailsActivity.this, ChattingActivity.class)
                        .putExtra("PHARMACYID", PHARMACYID)
                        .putExtra("PHARMACYNAME", pharm_name)
                        .putExtra("TYPE", "4");
                startActivity(intent);


            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        userDatabase.removeEventListener(changeValueEvent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng latLng = new LatLng(pharm_lat, pharm_lng);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(pharm_name);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
