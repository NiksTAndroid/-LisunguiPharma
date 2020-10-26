package com.lisungui.pharma.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.lisungui.pharma.R;
import com.lisungui.pharma.activities.CallHelpActivity;
import com.lisungui.pharma.activities.MainActivity;
import com.lisungui.pharma.adapter.RecycleCityListAdapter;
import com.lisungui.pharma.adapter.RecycleCountryListAdapter;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.CityPojo;
import com.lisungui.pharma.models.CityPojoArray;
import com.lisungui.pharma.models.CountriesPojo;
import com.lisungui.pharma.models.CountryPojoArray;
import com.lisungui.pharma.rest.RestClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CityFragment extends Fragment {


    private static final String TAG = "CityFragment";
    RecyclerView recycle_cityList;
    RecycleCityListAdapter cityListAdapter;
    ArrayList<CityPojoArray> CityList=new ArrayList<>();
    Context nContext;
    private ProgressDialog pDialog;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int lastID = 0;
    private SQLiteDataBaseHelper dataBaseHelper;
    private FragmentManager fragmentManager;
    private Activity currentActivity;

    String countryid;


    public CityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        nContext = context;
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        lastID = 0;
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
        menu.findItem(R.id.action_refresh).setVisible(false);
        menu.findItem(R.id.menu_item_save).setVisible(false);
        menu.findItem(R.id.menu_item_delete).setVisible(false);

        menu.findItem(R.id.call_help_item).setVisible(true);

//        inflater.inflate(R.menu.menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.call_help_item) {

            Intent intent1 = new Intent(nContext, CallHelpActivity.class);
            startActivity(intent1);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        fragmentManager=((MainActivity)getActivity()).getSupportFragmentManager();
        currentActivity=((MainActivity)getActivity());
        dataBaseHelper = new SQLiteDataBaseHelper(getActivity());

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.str_loading));
        pDialog.setCancelable(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_city, container, false);
        recycle_cityList=view.findViewById(R.id.recycle_cityList);
        countryid=  getArguments().getString("CountryId");
        Log.d(TAG, "onCreateView: "+countryid);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycle_cityList.setLayoutManager(layoutManager);

        setAdapter(getContext(),fragmentManager,currentActivity,CityList);
        getcitylist();
        return view;
    }
    void setAdapter(Context context,FragmentManager fragmentManager,Activity currentActivity,ArrayList<CityPojoArray> cityList){
        cityListAdapter=new RecycleCityListAdapter(context,fragmentManager,currentActivity,cityList,countryid);

        recycle_cityList.setAdapter(cityListAdapter);

        cityListAdapter.notifyDataSetChanged();
    }

    private void getcitylist() {
        showProgressDialog();
        RestClient.RestApiInterface service = RestClient.getClient();
        Call<CityPojo> call = service.getCities(countryid);
        call.enqueue(new Callback<CityPojo>() {
            @Override
            public void onResponse(Call<CityPojo> call, Response<CityPojo> response) {
                if(response.isSuccessful()){
                    hideProgressDialog();
                    CityPojo cityPojo = response.body();
                    Log.d(TAG, "onResponse: "+response.body().getCity());
                    ArrayList<CityPojoArray> citylist = new ArrayList<>();
                    if (cityPojo.getCity()!=null) {
                        citylist.addAll(cityPojo.getCity());
                    }
                    setAdapter(getContext(),fragmentManager,currentActivity,citylist);



                }
            }

            @Override
            public void onFailure(Call<CityPojo> call, Throwable t) {

            }
        });
    }
}