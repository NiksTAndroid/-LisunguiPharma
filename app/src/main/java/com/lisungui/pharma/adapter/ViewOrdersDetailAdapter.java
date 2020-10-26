package com.lisungui.pharma.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lisungui.pharma.R;
import com.lisungui.pharma.activities.ViewOrderActivity;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.OrderDetailsResponce;
import com.lisungui.pharma.models.SignUpPojo;
import com.lisungui.pharma.rest.RestClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewOrdersDetailAdapter extends BaseAdapter {

    private final List<OrderDetailsResponce.OrderPharmaDatum> responses;
    private final List<OrderDetailsResponce.OrderPharmaDesc> descriptionList;
    private final LayoutInflater inflater;
    private SQLiteDataBaseHelper dataBaseHelper;
    private OrderDetailsResponce.OrderDetail result;
   // ViewOrderAdapter viewOrderAdapter;
    private Activity context;

    public ViewOrdersDetailAdapter(ViewOrderActivity context, List<OrderDetailsResponce.OrderPharmaDatum> responses, List<OrderDetailsResponce.OrderPharmaDesc> descriptionList, OrderDetailsResponce.OrderDetail result, SQLiteDataBaseHelper dataBaseHelper) {
        this.context = context;
        this.responses = responses;
        this.descriptionList = descriptionList;
        this.result = result;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.dataBaseHelper = dataBaseHelper;
    }

    @Override
    public int getCount() {
        return responses.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.row_view_accepted_pharmas, viewGroup, false);
            viewHolder = new ViewHolder();

            viewHolder.parma_name = view.findViewById(R.id.parma_name);
            viewHolder.tv_pharmaAddress = view.findViewById(R.id.tv_pharmaAddress);
            viewHolder.tv_mobile = view.findViewById(R.id.tv_mobile);
            viewHolder.tv_acceptButton = view.findViewById(R.id.tv_acceptButton);
            viewHolder.tv_description = view.findViewById(R.id.tv_description);

            view.setTag(viewHolder);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        viewHolder.parma_name.setText(responses.get(i).getPharmName());
        viewHolder.tv_pharmaAddress.setText(responses.get(i).getPharmAddress());
        viewHolder.tv_mobile.setText(Html.fromHtml("<b>Mobile</b> <br>" + responses.get(i).getPharmMbNo()));
        viewHolder.tv_description.setText(String.valueOf(descriptionList.get(i).getDesc()));
        viewHolder.tv_acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo accept order
                StringBuffer sd = new StringBuffer();
                for (int j = 0; j < responses.size(); j++) {
                    if (j != i) {
                        sd.append(responses.get(j).getPharmId());
                        if (j != responses.size() - 1) {
                            sd.append(",");
                        }
                    }
                }

                RestClient.getClient().getOrderAccept(result.getOrderServerId(), responses.get(i).getPharmId(), sd.toString()).enqueue(new Callback<SignUpPojo>() {
                    @Override
                    public void onResponse(Call<SignUpPojo> call, Response<SignUpPojo> response) {
//                        ordersDetailAdapter.notifyDataSetChanged();

                        updateStatus(result.getOrderServerId());

                        Toast.makeText(context, "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        context.finish();
                        //viewOrderAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure(Call<SignUpPojo> call, Throwable t) {
                        Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        return view;
    }

    public void updateStatus(String orderid) {

        String[] column = new String[]{
                SQLiteDataBaseHelper.COLUMN_ORDER_SERVER_ID,
                SQLiteDataBaseHelper.COLUMN_ORDER_TRACK_STATUS
        };
        String[] values = new String[]{
                String.valueOf(result.getOrderServerId()),
                context.getString(R.string.str_received)
        };

        dataBaseHelper.insertOrUpdate(SQLiteDataBaseHelper.ORDER_TABLE, null, column, values);
    }

    static class ViewHolder {
        TextView parma_name, tv_pharmaAddress, tv_mobile, tv_acceptButton, tv_description;
    }
}
