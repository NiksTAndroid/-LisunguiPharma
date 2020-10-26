package com.lisungui.pharma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.models.PromotionNotificationDetails;

import java.util.List;

public class PromotionListAdapter extends BaseAdapter
{
    private Context context;
    private List<PromotionNotificationDetails> promotionList;
    private LayoutInflater inflater;

    public PromotionListAdapter(Context context, List<PromotionNotificationDetails> promotionList)
    {
        this.context = context;
        this.promotionList = promotionList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        //return 0;
        return promotionList.size();
    }

    @Override
    public Object getItem(int position) {
        //return null;
        return promotionList.get(position);
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

        viewHolder.txt_order_id.setText(promotionList.get(position).getPromNotiId());
        viewHolder.txt_date.setText(promotionList.get(position).getPromNotiDate());
        viewHolder.txt_msg.setText(promotionList.get(position).getPromNotiMsg());

        //return null;
        return view;
    }
}
