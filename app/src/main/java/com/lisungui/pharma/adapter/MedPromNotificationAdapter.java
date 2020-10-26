package com.lisungui.pharma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.models.MedNotificationDetails;

import java.util.List;

public class MedPromNotificationAdapter extends BaseAdapter {

    private Context context;
    private List<MedNotificationDetails> medNotificationDetailsList;
    private LayoutInflater inflater;


    public MedPromNotificationAdapter(Context context, List<MedNotificationDetails> medNotificationDetailsList)
    {
        this.context = context;
        this.medNotificationDetailsList = medNotificationDetailsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        //return 0;
        return medNotificationDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        //return null;
        return medNotificationDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        //return 0;
        return position;
    }

    class ViewHolder {
        TextView txt_order_id, txt_date, txt_msg;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder;

        if(view == null) {
            view = inflater.inflate(R.layout.row_notification, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.txt_order_id = view.findViewById(R.id.txt_order_id);
            viewHolder.txt_date = view.findViewById(R.id.txt_date);
            viewHolder.txt_msg = view.findViewById(R.id.txt_msg);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.txt_order_id.setText(medNotificationDetailsList.get(position).getNotifiId());
        viewHolder.txt_date.setText(medNotificationDetailsList.get(position).getNotifiDate());
        viewHolder.txt_msg.setText(medNotificationDetailsList.get(position).getNotifiMessage());

        //return null;
        return view;
    }
}
