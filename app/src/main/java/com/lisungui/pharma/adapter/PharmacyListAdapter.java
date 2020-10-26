package com.lisungui.pharma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.models.LatLongPojo;

import java.util.ArrayList;

/**
 * Created by khrishi on 17/12/16.
 */
public class PharmacyListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<LatLongPojo> latLongs;

    public PharmacyListAdapter(Context context, ArrayList<LatLongPojo> latLongs) {
        this.context = context;
        this.latLongs = latLongs;
        //inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (latLongs != null)
            return latLongs.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    static class ViewHolder {
        TextView txt_distance, txt_rating, txt_pharm_name;
        ImageView img_night;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        final ViewHolder viewHolder;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = inflater.inflate(R.layout.row_pharmacy_list, viewGroup, false);
            viewHolder = new ViewHolder();

            viewHolder.txt_distance = view.findViewById(R.id.txt_distance);
            viewHolder.txt_rating = view.findViewById(R.id.txt_rating);
            viewHolder.txt_pharm_name = view.findViewById(R.id.txt_pharm_name);
            viewHolder.img_night = view.findViewById(R.id.img_night);

            if (context.getResources().getString(R.string.lang).contentEquals("English")) {
                viewHolder.img_night.setImageResource(R.drawable.open_eng);
            } else {
                viewHolder.img_night.setImageResource(R.drawable.open_fre);

            }

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.txt_pharm_name.setText(latLongs.get(i).getPharmName());
        viewHolder.txt_distance.setText(latLongs.get(i).getPharmDistance() + " KM");

        if (latLongs.get(i).getPharmNight().equals("Yes"))
            viewHolder.img_night.setVisibility(View.VISIBLE);
        else
            viewHolder.img_night.setVisibility(View.GONE);

//        viewHolder.txt_pharm_name.setText(latLongs.get(i).getPharm_name());

/*
        if (latLongs.get(i).getPharm_24().equals("1"))
            viewHolder.img_night.setVisibility(View.VISIBLE);
        else
            viewHolder.img_night.setVisibility(View.GONE);
*/

        return view;
    }
}
