package com.lisungui.pharma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.models.HealthTipDataDetails;

import java.util.List;

public class HealthTipsNotificationAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<HealthTipDataDetails> healthTipDataDetailsList;

    public HealthTipsNotificationAdapter(Context context, List<HealthTipDataDetails> healthTipDataDetailsList)
    {
        this.context = context;
        inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.healthTipDataDetailsList = healthTipDataDetailsList;
    }

    @Override
    public int getCount() {
        //return 0;
        return healthTipDataDetailsList.size();
    }

    @Override
    public Object getItem(int i) {
        //return null;
        return healthTipDataDetailsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        //return 0;
        return i;
    }

    static class ViewHolder {
        TextView txt_order_id, txt_date, txt_msg;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        HealthTipsNotificationAdapter.ViewHolder viewHolder;

        //view = null;


        if(view == null) {
            view = inflater.inflate(R.layout.row_notification, viewGroup, false);
            viewHolder = new HealthTipsNotificationAdapter.ViewHolder();

            viewHolder.txt_order_id = view.findViewById(R.id.txt_order_id);
            viewHolder.txt_date = view.findViewById(R.id.txt_date);
            viewHolder.txt_msg = view.findViewById(R.id.txt_msg);

            view.setTag(viewHolder);
        } else {
            viewHolder = (HealthTipsNotificationAdapter.ViewHolder) view.getTag();
        }

        viewHolder.txt_order_id.setText(String.valueOf(healthTipDataDetailsList.get(i).getTipId()));
        viewHolder.txt_date.setText(String.valueOf(healthTipDataDetailsList.get(i).getTipDate()));
        viewHolder.txt_msg.setText(String.valueOf(healthTipDataDetailsList.get(i).getTipMessage()));

        //return null;
        return view;
    }
}
