package com.lisungui.pharma.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.caller.IMethodCaller;
import com.lisungui.pharma.models.TempOrderPojo;

import java.util.ArrayList;

/**
 * Created by khrishi on 8/1/17.
 */
public class OrderListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;

    private IMethodCaller iMethodCaller;
    private ArrayList<TempOrderPojo> tempOrderPojos;

    public OrderListAdapter(Context context, ArrayList<TempOrderPojo> tempOrderPojos, IMethodCaller iMethodCaller) {
        this.iMethodCaller = iMethodCaller;
        this.context = context;
        this.tempOrderPojos = tempOrderPojos;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        if(tempOrderPojos != null)
            return tempOrderPojos.size();

        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView mTMedName, mTprice, mTQty, mTTotal, mTRemove;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;

        if(view == null) {
            view = inflater.inflate(R.layout.row_order_list, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.mTMedName = view.findViewById(R.id.txt_med_name);
            viewHolder.mTprice = view.findViewById(R.id.txt_price);
            viewHolder.mTQty = view.findViewById(R.id.txt_qty);
            viewHolder.mTTotal = view.findViewById(R.id.txt_total);
            viewHolder.mTRemove = view.findViewById(R.id.txt_remove);


            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }



/*
        viewHolder.mTMedName.setText(tempOrderPojos.get(position).getTemp_order_med_name());
        viewHolder.mTprice.setText("xaf "+tempOrderPojos.get(position).getTemp_order_med_price());
        viewHolder.mTQty.setText(context.getString(R.string.str_qty)+" "+tempOrderPojos.get(position).getTemp_qty());
        viewHolder.mTTotal.setText("xaf "+tempOrderPojos.get(position).getTemp_order_total_price());
*/

        viewHolder.mTMedName.setText(tempOrderPojos.get(position).getTemp_order_med_name());
        viewHolder.mTprice.setText(String.valueOf(tempOrderPojos.get(position).getTemp_order_med_price()));
        viewHolder.mTQty.setText(context.getString(R.string.str_qty)+" "+tempOrderPojos.get(position).getTemp_qty());
        viewHolder.mTTotal.setText(String.valueOf(tempOrderPojos.get(position).getTemp_order_total_price()));

        viewHolder.mTRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iMethodCaller.removeCart(position);
            }
        });

        return view;
    }
}
