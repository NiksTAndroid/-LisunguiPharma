package com.lisungui.pharma.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.fragment.CityFragment;
import com.lisungui.pharma.fragment.CountriesPriceFragment;
import com.lisungui.pharma.models.CountryPojoArray;

import java.util.ArrayList;

public class RecycleCountryListAdapter extends RecyclerView.Adapter<RecycleCountryListAdapter.ViewHolder> {

    private Context nContext;
    private ArrayList<CountryPojoArray> CountryList;
    private FragmentTransaction transaction;
    private Fragment fragment;
    private String title;
    private FragmentManager fragmentManager;
    Activity currentActivity;

    public RecycleCountryListAdapter(Context ncontext,FragmentManager fragmentManager, Activity currentActivity, ArrayList<CountryPojoArray> countryList) {
        this.nContext = ncontext;
        this.fragmentManager = fragmentManager;
        this.currentActivity = currentActivity;
        CountryList = countryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(nContext).inflate(R.layout.recycle_contrieslist,
                viewGroup, false);
        return new RecycleCountryListAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final CountryPojoArray pojoarray=CountryList.get(i);
        viewHolder.txt_CountryName.setText(pojoarray.getConName());
        viewHolder.CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment=new CityFragment();
                Bundle args = new Bundle();
                args.putString("CountryId", pojoarray.getConId());
                fragment.setArguments(args);
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).addToBackStack(null).commit();
                //((MainActivity)getActivity()).getSupportActionBar().setTitle(title);
            }
        });

    }

    @Override
    public int getItemCount() {
        return CountryList.size();
    }


    ///View Holder sub-class
    public class ViewHolder  extends RecyclerView.ViewHolder  {
        CardView CardView;
        TextView txt_CountryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CardView=itemView.findViewById(R.id.CardView);
            txt_CountryName=itemView.findViewById(R.id.txt_CountryName);


        }
    }
}


