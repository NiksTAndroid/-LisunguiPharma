package com.lisungui.pharma.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.models.MedDetails;
import com.lisungui.pharma.models.OrderDetailsResponce;

import java.util.ArrayList;

public class ViewOrderAdapter extends BaseAdapter {
    private static final String TAG = "ViewOrderAdapter";

    private Context context;
    private ArrayList<MedDetails> medArray;
    private LayoutInflater inflater;
    OrderDetailsResponce.OrderDetail result;

    public ViewOrderAdapter(Context context, ArrayList<MedDetails> medArray, OrderDetailsResponce.OrderDetail result) {
        this.context = context;
        this.medArray = medArray;
        this.result=result;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(medArray != null)
            return medArray.size();
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
        TextView txt_med_name, txt_qty, txt_price, txt_total;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder;

        if(view == null) {
            view = inflater.inflate(R.layout.row_view_order, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.txt_med_name = view.findViewById(R.id.txt_med_name);
            viewHolder.txt_qty = view.findViewById(R.id.txt_qty);
            viewHolder.txt_price = view.findViewById(R.id.txt_price);
            viewHolder.txt_total = view.findViewById(R.id.txt_total);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.txt_med_name.setText(medArray.get(position).getMed_name());
        Log.d(TAG, "getView: "+result.getMedName());
        Log.d(TAG, "getView:med quant "+result.getOrderQnty() );
        Log.d(TAG, "getView: medprice"+result.getMedPrice() );
        viewHolder.txt_qty.setText(context.getString(R.string.str_qty)+": "+medArray.get(position).getDetail_med_qnty());
        //viewHolder.txt_qty.setText(context.getString(R.string.str_qty)+": "+medArray.get(position).getDetail_qnty());

        viewHolder.txt_price.setText(context.getString(R.string.str_price)+": "+medArray.get(position).getDetail_med_price());
        //viewHolder.txt_price.setText(context.getString(R.string.str_price)+": "+medArray.get(position).get);
        viewHolder.txt_total.setText(String.valueOf(Integer.parseInt(medArray.get(position).getDetail_qnty()) * medArray.get(position).getDetail_med_price()));

        return view;
    }
}
