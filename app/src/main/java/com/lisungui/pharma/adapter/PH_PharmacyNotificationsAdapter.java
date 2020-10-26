package com.lisungui.pharma.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.activities.PH_ActivityOrderDetails;
import com.lisungui.pharma.models.PharmacyNotificationsResponse;

import java.util.List;


public class PH_PharmacyNotificationsAdapter extends BaseAdapter {
    private Context context;
    private final List<PharmacyNotificationsResponse.Notification> datalist;
    private LayoutInflater inflater;


    public PH_PharmacyNotificationsAdapter(Context context, List<PharmacyNotificationsResponse.Notification> pharamcyNotificationsList) {
        this.context = context;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.datalist = pharamcyNotificationsList;
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int i) {
        return datalist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    static class ViewHolder {
        TextView txt_order_id, txt_date, txt_msg, txt_contact, message_txt;
        LinearLayout mainLayout;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        PH_PharmacyNotificationsAdapter.ViewHolder viewHolder;

        final PharmacyNotificationsResponse.Notification data = datalist.get(i);
        if (view == null) {
            view = inflater.inflate(R.layout.placedorder_notification_list_item, viewGroup, false);
            viewHolder = new PH_PharmacyNotificationsAdapter.ViewHolder();

            viewHolder.txt_order_id = view.findViewById(R.id.txt_order_id);
            viewHolder.mainLayout = view.findViewById(R.id.mainLayout);
            viewHolder.txt_date = view.findViewById(R.id.txt_date);
            viewHolder.txt_msg = view.findViewById(R.id.txt_msg);
            view.setTag(viewHolder);
        } else {
            viewHolder = (PH_PharmacyNotificationsAdapter.ViewHolder) view.getTag();
        }

        viewHolder.txt_date.setText(data.getPharNotiDate());
        viewHolder.txt_order_id.setText(data.getPharOrderId());

        if (context.getString(R.string.lang).equals("English")) {
            viewHolder.txt_msg.setText(data.getPharNotiMsg());
        } else {
            String lang = data.getFrench_msg();
            if (lang==null||lang.isEmpty()) {
                viewHolder.txt_msg.setText(data.getPharNotiMsg());
            } else viewHolder.txt_msg.setText(lang);
        }
        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, PH_ActivityOrderDetails.class).putExtra("OID", data.getPharOrderId()));
            }
        });

        return view;
    }
}
