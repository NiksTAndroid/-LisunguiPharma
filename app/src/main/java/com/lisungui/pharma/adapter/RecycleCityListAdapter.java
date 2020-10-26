package com.lisungui.pharma.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.fragment.CountriesPriceFragment;
import com.lisungui.pharma.models.CityPojoArray;
import com.lisungui.pharma.models.CountryPojoArray;

import java.util.ArrayList;

public class RecycleCityListAdapter extends RecyclerView.Adapter<RecycleCityListAdapter.ViewHolder> {

    private static final String TAG = "RecycleCityListAdapter";
    private Context nContext;
    private ArrayList<CityPojoArray> CityList;
    private FragmentTransaction transaction;
    private Fragment fragment;
    private String title;
    private String countryID;
    private FragmentManager fragmentManager;
    Activity currentActivity;

    public RecycleCityListAdapter(Context ncontext,FragmentManager fragmentManager, Activity currentActivity, ArrayList<CityPojoArray> CityList,String countryID) {
        this.nContext = ncontext;
        this.fragmentManager = fragmentManager;
        this.currentActivity = currentActivity;
        this.countryID = countryID;
        this.CityList = CityList;
    }

    @NonNull
    @Override
    public RecycleCityListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(nContext).inflate(R.layout.recycle_contrieslist,
                viewGroup, false);
        return new RecycleCityListAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecycleCityListAdapter.ViewHolder viewHolder, int i) {
        final CityPojoArray pojoarray=CityList.get(i);
        viewHolder.txt_CountryName.setText(pojoarray.getCityName());
        viewHolder.CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment=new CountriesPriceFragment();
                Log.d(TAG, "onClick: "+pojoarray.getCityId());
                Log.d(TAG, "onClick: "+countryID);
                Bundle args = new Bundle();
                args.putString("CityId", pojoarray.getCityId());
                args.putString("CountryId", countryID);
                fragment.setArguments(args);
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).addToBackStack(null).commit();
                //((MainActivity)getActivity()).getSupportActionBar().setTitle(title);
            }
        });

    }

    @Override
    public int getItemCount() {
        return CityList.size();
    }


    ///View Holder sub-class
    public class ViewHolder  extends RecyclerView.ViewHolder  {
        androidx.cardview.widget.CardView CardView;
        TextView txt_CountryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CardView=itemView.findViewById(R.id.CardView);
            txt_CountryName=itemView.findViewById(R.id.txt_CountryName);


        }
    }
}