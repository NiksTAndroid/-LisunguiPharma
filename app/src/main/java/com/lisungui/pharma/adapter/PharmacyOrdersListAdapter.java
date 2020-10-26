package com.lisungui.pharma.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.activities.PH_ActivityOrderDetails;
import com.lisungui.pharma.models.PharmacyOrdersResponseModel;

import java.util.ArrayList;

public class PharmacyOrdersListAdapter extends RecyclerView.Adapter<PharmacyOrdersListAdapter.ViewHolder> {

    private Context context;

    public PharmacyOrdersListAdapter(Context context, ArrayList<PharmacyOrdersResponseModel.OrderDetail> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    ArrayList<PharmacyOrdersResponseModel.OrderDetail> dataList;

    @NonNull
    @Override
    public PharmacyOrdersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_orders, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacyOrdersListAdapter.ViewHolder h, int i) {

        final PharmacyOrdersResponseModel.OrderDetail data = dataList.get(i);
        h.tv_userName.setText(data.getOrder_username());
        if (data.getOrderTrackStatus().equalsIgnoreCase("Pending")) {
            h.tv_status.setText(context.getString(R.string.str_pending));
        } else if (data.getOrderTrackStatus().equalsIgnoreCase("Received")) {
            h.tv_status.setText(context.getString(R.string.str_received));
        } else if (data.getOrderTrackStatus().equalsIgnoreCase("On The Way")) {
            h.tv_status.setText(context.getString(R.string.str_on_the_way));
        } else if (data.getOrderTrackStatus().equalsIgnoreCase("Ready In Pharmacy")) {
            h.tv_status.setText(context.getString(R.string.reddyinpharmacy));
        } else {
            h.tv_status.setText(data.getOrderTrackStatus());
        }
        h.tv_orderID.setText(Html.fromHtml("<font color=\"#000000\"><b>OID:</b> </font>" + data.getOrderServerId()));
        h.tv_orderDate.setText(Html.fromHtml("<font color=\"#000000\"><b>Date:</b> </font>" + data.getOrderDate()));
        h.tv_ammount.setText(Html.fromHtml("<font color=\"#000000\"><b>Amt:</b> </font>" + data.getOrderTotalPrice()));

        h.mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, PH_ActivityOrderDetails.class)
                        .putExtra("OID", data.getOrderServerId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_orderID, tv_ammount, tv_orderDate, tv_userName, tv_status;
        LinearLayout mainlayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_orderID = itemView.findViewById(R.id.tv_orderID);
            mainlayout = itemView.findViewById(R.id.mainlayout);
            tv_ammount = itemView.findViewById(R.id.tv_ammount);
            tv_orderDate = itemView.findViewById(R.id.tv_orderDate);
            tv_userName = itemView.findViewById(R.id.tv_userName);
            tv_status = itemView.findViewById(R.id.tv_status);

        }
    }
}
