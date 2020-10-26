package com.lisungui.pharma.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.activities.ViewOrderActivity;
import com.lisungui.pharma.models.MedDetails;
import com.lisungui.pharma.models.OrderDetails;

import java.util.ArrayList;

public class OrderDetailsAdapter //extends BaseAdapter
{

    /*private static final String TAG = OrderDetailsAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<OrderDetails> orderDetailsArrayList;
    private ArrayList<MedDetails> medDetailsArrayList=new ArrayList<>();

    public OrderDetailsAdapter(Context context, ArrayList<OrderDetails> orderDetailsArrayList) {
        this.context = context;
        this.orderDetailsArrayList = orderDetailsArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (orderDetailsArrayList != null)
            return orderDetailsArrayList.size();

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
        TextView txt_order_status, txt_order_no, txt_order_date, txt_total_amount;
        LinearLayout lin_order_items;
        TextView txt_deliverd_on, txt_deliverd_date, txt_view_details;
//        TextView txt_remove, txt_total;
    }

    int k=0;
    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        final ViewHolder viewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.row_order_details_list, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.txt_order_status = view.findViewById(R.id.txt_order_status);
            viewHolder.txt_order_no = view.findViewById(R.id.txt_order_no);
            viewHolder.txt_order_date = view.findViewById(R.id.txt_order_date);
            viewHolder.txt_total_amount = view.findViewById(R.id.txt_total_amount);
            viewHolder.txt_deliverd_on = view.findViewById(R.id.txt_deliverd_on);
            viewHolder.txt_deliverd_date = view.findViewById(R.id.txt_deliverd_date);
            viewHolder.txt_view_details = view.findViewById(R.id.txt_view_details);
//            viewHolder.txt_total = (TextView) view.findViewById(R.id.txt_total);
//            viewHolder.txt_remove = (TextView) view.findViewById(R.id.txt_remove);
            viewHolder.lin_order_items = view.findViewById(R.id.lin_order_items);
            *//*for(int k=0;k<orderDetailsArrayList.size();k++){
                Log.d(TAG, "getView: medicine name "+orderDetailsArrayList.get(k).getOrder_data().get(0).getMed_name());
                Log.d(TAG, "getView: medicine name "+orderDetailsArrayList.get(k).getOrder_server_id());

            }*//*



            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

//        Log.d(TAG , "Track Status : "+orderDetailsArrayList.get(i).getOrder_track_status());

        if (context.getString(R.string.lang).contentEquals("English")) {
            viewHolder.txt_order_status.setText(orderDetailsArrayList.get(i).getOrder_track_status());
        } else {
            if (orderDetailsArrayList.get(i).getOrder_track_status().contentEquals("Received")) {
                viewHolder.txt_order_status.setText("Reçue");
            } else if (orderDetailsArrayList.get(i).getOrder_track_status().contentEquals("On The Way")) {
                viewHolder.txt_order_status.setText("En chemin");
            }
            else if (orderDetailsArrayList.get(i).getOrder_track_status().contentEquals("Delivered")) {
                viewHolder.txt_order_status.setText("Livré");
            }
        }


        //viewHolder.lin_order_items.removeAllViews();

            for (int j = 0; j < orderDetailsArrayList.get(k).getOrder_data().size(); j++) {
                Log.d(TAG, "getView: i value"+i);
                Log.d(TAG, "getView: size value"+orderDetailsArrayList.get(k).getOrder_data().size());
                Log.d(TAG, "getView: medicine name value"+orderDetailsArrayList.get(k).getOrder_data().get(j).getMed_name());
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View addView = layoutInflater.inflate(R.layout.row_med_order_details, null);
                TextView tMedName = addView.findViewById(R.id.txt_med_name);
                tMedName.setText(orderDetailsArrayList.get(k).getOrder_data().get(j).getMed_name());
                viewHolder.lin_order_items.addView(addView);
            }
        if (k<orderDetailsArrayList.size()){
            Log.d(TAG, "getView: Kvalue "+k);
            k++;
        }


//        Log.d(TAG , "Server ID : "+orderDetailsArrayList.get(i).getOrder_server_id());
        viewHolder.txt_order_no.setText(context.getString(R.string.str_order_no) + ": " + orderDetailsArrayList.get(i).getOrder_server_id());
        viewHolder.txt_order_date.setText(context.getString(R.string.str_placed_on) + ": " + orderDetailsArrayList.get(i).getOrder_date());
        viewHolder.txt_deliverd_date.setText(orderDetailsArrayList.get(i).getOrder_update_date());
//        String str = context.getString(R.string.str_total_amt_product) + " xaf " + orderDetailsArrayList.get(i).getOrder_total_price()
//                + "/" + orderDetailsArrayList.get(i).getOrder_qnty();//+" "+context.getString(R.string.str_item)

        String str = context.getString(R.string.str_total_amt_product) + " " + orderDetailsArrayList.get(i).getOrder_total_price()
                + "/" + orderDetailsArrayList.get(i).getOrder_qnty();//+" "+context.getString(R.string.str_item)

        viewHolder.txt_total_amount.setText(str);

        viewHolder.txt_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrderDetails orderDetails = orderDetailsArrayList.get(i);
                Log.d(TAG, "107 Order Data Size : " + orderDetails.getOrder_data().size());

                Bundle mBundle = new Bundle();
                mBundle.putParcelable("VIEW_ORDER", orderDetails);

                Intent intent = new Intent(context, ViewOrderActivity.class);
                intent.putExtras(mBundle);
                context.startActivity(intent);
            }
        });

        return view;
    }*/
}
