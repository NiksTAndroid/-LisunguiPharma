package com.lisungui.pharma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.models.Notification;

import java.util.ArrayList;

/**
 * Created by pavan on 29/1/17.
 */

public class NotificationAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Notification> notifications;

    public NotificationAdapter(Context context,ArrayList<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(notifications != null)
            return notifications.size();
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
        TextView txt_order_id, txt_date, txt_msg;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if(view == null) {
            view = inflater.inflate(R.layout.row_notification, viewGroup, false);
            viewHolder = new ViewHolder();

            viewHolder.txt_order_id = view.findViewById(R.id.txt_order_id);
            viewHolder.txt_date = view.findViewById(R.id.txt_date);
            viewHolder.txt_msg = view.findViewById(R.id.txt_msg);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.txt_order_id.setText(String.valueOf(notifications.get(i).getNot_order_id()));
        viewHolder.txt_date.setText(String.valueOf(notifications.get(i).getNot_date()));
        viewHolder.txt_msg.setText(String.valueOf(notifications.get(i).getNot_msg()));

        return view;
    }
}
