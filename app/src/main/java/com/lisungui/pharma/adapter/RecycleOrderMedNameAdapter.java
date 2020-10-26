package com.lisungui.pharma.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.activities.ViewOrderActivity;
import com.lisungui.pharma.models.OrderDetails;

import java.util.ArrayList;

public class RecycleOrderMedNameAdapter extends RecyclerView.Adapter<RecycleOrderMedNameAdapter.ViewHolder> {

    private static final String TAG = "RecycleOrderListAdapter";
    private Context nContext;
    private LayoutInflater inflater;
    private ArrayList<String> Mednames;

    public RecycleOrderMedNameAdapter(Context context, ArrayList<String> Mednames) {
        this.nContext = context;
        this.Mednames = Mednames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nContext).inflate(R.layout.row_med_order_details,
                parent, false);
        return new RecycleOrderMedNameAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,final int i) {
        viewHolder.txt_med_name.setText(Mednames.get(i));

    }

    @Override
    public int getItemCount() {
        return Mednames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_med_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_med_name = itemView.findViewById(R.id.txt_med_name);

        }
    }
}
