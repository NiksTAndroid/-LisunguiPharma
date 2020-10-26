package com.lisungui.pharma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.fragment.NotificationFragment;
import com.lisungui.pharma.models.PlaceOrderNotificationDetails;

import java.util.List;

public class PlaceOrderNotificationAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<PlaceOrderNotificationDetails> placeOrderNotificationDetailsList;
    private NotificationFragment notificationFragment;

    public PlaceOrderNotificationAdapter(Context context, List<PlaceOrderNotificationDetails> placeOrderNotificationDetailsList, NotificationFragment notificationFragment)
    {
        this.context = context;
        inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.placeOrderNotificationDetailsList = placeOrderNotificationDetailsList;
        this.notificationFragment = notificationFragment;
    }

    @Override
    public int getCount() {
        //return 0;
        return placeOrderNotificationDetailsList.size();
    }

    @Override
    public Object getItem(int i) {
        //return null;
        return placeOrderNotificationDetailsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        //return 0;
        return i;
    }

    static class ViewHolder {
        TextView txt_order_id, txt_date, txt_msg, txt_contact, message_txt;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        PlaceOrderNotificationAdapter.ViewHolder viewHolder;

        //view = null;

        if(view == null) {
            view = inflater.inflate(R.layout.placedorder_notification_list_item, viewGroup, false);
            viewHolder = new PlaceOrderNotificationAdapter.ViewHolder();

            viewHolder.txt_order_id = view.findViewById(R.id.txt_order_id);
            viewHolder.txt_date = view.findViewById(R.id.txt_date);
            viewHolder.txt_msg = view.findViewById(R.id.txt_msg);
            viewHolder.txt_contact = view.findViewById(R.id.contact_txt);
            viewHolder.message_txt = view.findViewById(R.id.message_txt);

            view.setTag(viewHolder);
        } else {
            viewHolder = (PlaceOrderNotificationAdapter.ViewHolder) view.getTag();
        }


            viewHolder.txt_order_id.setText(String.valueOf(placeOrderNotificationDetailsList.get(i).getOrnotOrderId()));
            viewHolder.txt_date.setText(String.valueOf(placeOrderNotificationDetailsList.get(i).getOrnotDate()));
            //viewHolder.txt_msg.setText(String.valueOf(placeOrderNotificationDetailsList.get(i).getOrnotMessage()));
            viewHolder.txt_contact.setText(String.valueOf(placeOrderNotificationDetailsList.get(i).getOrnotMbNo()));

            if(placeOrderNotificationDetailsList.get(i).getOrnotOrderDesc().equals("")||placeOrderNotificationDetailsList.get(i).getOrnotOrderDesc().isEmpty())
                viewHolder.message_txt.setVisibility(View.GONE);
            else {
                viewHolder.message_txt.setText(String.valueOf(placeOrderNotificationDetailsList.get(i).getOrnotOrderDesc()));

                viewHolder.message_txt.setVisibility(View.VISIBLE);
            }
            String message="";
        String finalmess="";
        if(context.getString(R.string.lang).contentEquals("English"))
        {
            finalmess = placeOrderNotificationDetailsList.get(i).getOrnotMessage();
        }
        else
        {
            if (!placeOrderNotificationDetailsList.get(i).getFrench_msg().isEmpty()){
                finalmess=placeOrderNotificationDetailsList.get(i).getFrench_msg();
            }else{
            if(placeOrderNotificationDetailsList.get(i).getOrnotMessage().contains("has been Ready In Pharmacy"))
            {
                message=placeOrderNotificationDetailsList.get(i).getOrnotMessage().replace("has been Ready In Pharmacy","Prête en pharmacie");
                finalmess=message.replace("OrderId","Cmnde N°");
            }
            else if(placeOrderNotificationDetailsList.get(i).getOrnotMessage().contains("has been On The Way"))
            {
                message=placeOrderNotificationDetailsList.get(i).getOrnotMessage().replace("has been On The Way","est sur le chemin");
                finalmess=message.replace("OrderId","Cmnde N°");
            }
            else if(placeOrderNotificationDetailsList.get(i).getOrnotMessage().contains("has been Received"))
            {
                message=placeOrderNotificationDetailsList.get(i).getOrnotMessage().replace("has been Received","a été reçue");
                finalmess=message.replace("OrderId","Cmnde N°");
            }else {
                finalmess = placeOrderNotificationDetailsList.get(i).getOrnotMessage();
            }
            }
        }

        viewHolder.txt_msg.setText(finalmess);
        viewHolder.txt_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationFragment.makePhoneCall(String.valueOf(placeOrderNotificationDetailsList.get(i).getOrnotMbNo()));
            }
        });

        //return null;
        return view;
    }
}
