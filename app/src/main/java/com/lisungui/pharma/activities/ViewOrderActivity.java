package com.lisungui.pharma.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.adapter.OrderDetailsAdapter;
import com.lisungui.pharma.adapter.ViewOrderAdapter;
import com.lisungui.pharma.adapter.ViewOrdersDetailAdapter;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.MedDetails;

import com.lisungui.pharma.models.OrderDetails;
import com.lisungui.pharma.models.OrderDetailsResponce;
import com.lisungui.pharma.rest.RestClient;
import com.lisungui.pharma.utility.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewOrderActivity extends AppCompatActivity {

    private static final String TAG = ViewOrderActivity.class.getSimpleName();

    private Bundle bundle;
    private OrderDetails orderDetails;
    private ArrayList<MedDetails> medDetailsArrayList;

    private ListView list_items;
    private TextView txt_order_id, txt_order_status, txt_order_date, txt_order_qty, txt_order_total;

    //    private String order_update_date;

    private ViewOrderAdapter orderAdapter;
    private SQLiteDataBaseHelper dataBaseHelper;
    OrderDetailsAdapter orderDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dataBaseHelper = new SQLiteDataBaseHelper(this);
        init();
        try {
            bundle = getIntent().getExtras();
            if (bundle != null) {
                if (bundle.containsKey("VIEW_ORDER")) {
                    orderDetails = bundle.getParcelable("VIEW_ORDER");
                    int order_server_id = orderDetails.getOrder_server_id();
                    int order_qnty = orderDetails.getOrder_qnty();
                    double order_total_price = orderDetails.getOrder_total_price();
                    String order_date = orderDetails.getOrder_date();
                    String order_track_status = orderDetails.getOrder_track_status();
                    medDetailsArrayList = orderDetails.getOrder_data();


                    txt_order_id.setText(getString(R.string.str_order_no) + ": " + order_server_id);

                    if (getApplicationContext().getString(R.string.lang).contentEquals("English")) {
                        txt_order_status.setText(order_track_status);
                    } else {
                        if (order_track_status.contentEquals("Received")) {
                            txt_order_status.setText("Reçue");
                        } else if (order_track_status.contentEquals("On The Way")) {
                            txt_order_status.setText("En chemin");
                        } else if (order_track_status.contentEquals("Delivered")) {
                            txt_order_status.setText("Livré");
                        }
                    }


                    txt_order_date.setText(getString(R.string.str_placed_on) + ": " + order_date);
                    txt_order_qty.setText(getString(R.string.str_order_qty) + ": " + order_qnty);
                    txt_order_total.setText(getString(R.string.str_tot_order_price) + ": " + order_total_price+"€");

                    //setOrderAdapter();

                    getOrderDetails(order_server_id);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getOrderDetails(int order_server_id) {
        Log.d(TAG, "getOrderDetails: "+order_server_id+" userid : "+PrefManager.getUserID(this));
        RestClient.getClient().getOrderDetail(order_server_id, PrefManager.getUserID(this)).enqueue(new Callback<OrderDetailsResponce>() {
            @Override
            public void onResponse(Call<OrderDetailsResponce> call, Response<OrderDetailsResponce> response) {
                OrderDetailsResponce data = response.body();
                Log.d(TAG, "onResponse: "+data);
                OrderDetailsResponce.OrderDetail result=null;

                        if(data.getOrderDetails()!=null){
                            result =   data.getOrderDetails().get(0);
                        }
                Log.d(TAG, "onResponse: "+result.getOrderTrackStatus());
                Log.d(TAG, "onResponse: "+result.getOrderPharmaData());
                Log.d(TAG, "onResponse: "+result.getOrderPharmaDesc());
                if (result.getOrderTrackStatus()!=null && result.getOrderTrackStatus().equals("Pending")&&result.getOrderPharmaDesc()!=null) {
                    List<OrderDetailsResponce.OrderPharmaDatum> responses = result.getOrderPharmaData();
                    List<OrderDetailsResponce.OrderPharmaDesc> descriptionList = result.getOrderPharmaDesc();

                    ViewOrdersDetailAdapter orderAdapter = new ViewOrdersDetailAdapter(ViewOrderActivity.this, responses, descriptionList,result,dataBaseHelper);

                    list_items.setAdapter(orderAdapter);
                    orderAdapter.notifyDataSetChanged();
                }else{
                    setOrderAdapter(result);
                }

            }

            @Override
            public void onFailure(Call<OrderDetailsResponce> call, Throwable t) {

            }
        });

    }

    private void init() {
        list_items = (ListView) findViewById(R.id.list_items);
        txt_order_id = (TextView) findViewById(R.id.txt_order_id);
        txt_order_status = (TextView) findViewById(R.id.txt_order_status);
        txt_order_date = (TextView) findViewById(R.id.txt_order_date);
        txt_order_qty = (TextView) findViewById(R.id.txt_order_qty);
        txt_order_total = (TextView) findViewById(R.id.txt_order_total);
    }

    private void setOrderAdapter(OrderDetailsResponce.OrderDetail result) {
        orderAdapter = new ViewOrderAdapter(this, medDetailsArrayList,result);
        list_items.setAdapter(orderAdapter);
        orderAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
