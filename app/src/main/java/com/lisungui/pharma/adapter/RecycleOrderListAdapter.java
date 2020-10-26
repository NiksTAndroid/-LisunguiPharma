package com.lisungui.pharma.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.activities.ViewOrderActivity;
import com.lisungui.pharma.models.OrderDetails;

import java.util.ArrayList;

public class RecycleOrderListAdapter extends RecyclerView.Adapter<RecycleOrderListAdapter.ViewHolder> {

    private static final String TAG = "RecycleOrderListAdapter";
    private Context nContext;
    private LayoutInflater inflater;
    private ArrayList<OrderDetails> orderDetailsArrayList;

    public RecycleOrderListAdapter(Context context, ArrayList<OrderDetails> orderDetailsArrayList) {
        this.nContext = context;
        this.orderDetailsArrayList = orderDetailsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nContext).inflate(R.layout.row_order_details_list,
                parent, false);
        return new RecycleOrderListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,final int i) {
        if (nContext.getString(R.string.lang).contentEquals("English")) {
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
        ArrayList<String> mednames=new ArrayList<>();

        for (int j = 0; j < orderDetailsArrayList.get(i).getOrder_data().size(); j++) {
            Log.d(TAG, "getView: i value"+i);
            Log.d(TAG, "getView: size value"+orderDetailsArrayList.get(i).getOrder_data().size());
            Log.d(TAG, "getView: medicine name value"+orderDetailsArrayList.get(i).getOrder_data().get(j).getMed_name());
            LayoutInflater layoutInflater = (LayoutInflater) nContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mednames.add(orderDetailsArrayList.get(i).getOrder_data().get(j).getMed_name());
        }
        RecycleOrderMedNameAdapter medNameAdapter=new RecycleOrderMedNameAdapter(nContext,mednames);
        viewHolder.recycle_order_items.setLayoutManager(new LinearLayoutManager(nContext));
        viewHolder.recycle_order_items.setAdapter(medNameAdapter);
        viewHolder.txt_order_no.setText(nContext.getString(R.string.str_order_no) + ": " + orderDetailsArrayList.get(i).getOrder_server_id());
        viewHolder.txt_order_date.setText(nContext.getString(R.string.str_placed_on) + ": " + orderDetailsArrayList.get(i).getOrder_date());
        viewHolder.txt_deliverd_date.setText(orderDetailsArrayList.get(i).getOrder_update_date());
        String str = nContext.getString(R.string.str_total_amt_product) + " " + orderDetailsArrayList.get(i).getOrder_total_price()
                + "/" + orderDetailsArrayList.get(i).getOrder_qnty();//+" "+context.getString(R.string.str_item)

        viewHolder.txt_total_amount.setText(str);

        viewHolder.txt_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrderDetails orderDetails = orderDetailsArrayList.get(i);
                Log.d(TAG, "107 Order Data Size : " + orderDetails.getOrder_data().size());
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("VIEW_ORDER", orderDetails);
                Intent intent = new Intent(nContext, ViewOrderActivity.class);
                intent.putExtras(mBundle);
                nContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderDetailsArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_order_status, txt_order_no, txt_order_date, txt_total_amount;
        RecyclerView recycle_order_items;
        TextView txt_deliverd_on, txt_deliverd_date, txt_view_details;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_order_status = itemView.findViewById(R.id.txt_order_status);
            txt_order_no = itemView.findViewById(R.id.txt_order_no);
            txt_order_date = itemView.findViewById(R.id.txt_order_date);
            txt_total_amount = itemView.findViewById(R.id.txt_total_amount);
            txt_deliverd_on = itemView.findViewById(R.id.txt_deliverd_on);
            txt_deliverd_date = itemView.findViewById(R.id.txt_deliverd_date);
            txt_view_details = itemView.findViewById(R.id.txt_view_details);
//          r.txt_total = (TextView) view.findViewById(R.id.txt_total);
//          r.txt_remove = (TextView) view.findViewById(R.id.txt_remove);
            recycle_order_items = itemView.findViewById(R.id.recycle_order_items);
        }
    }
}
