package com.lisungui.pharma.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.adapter.OrderDetailsAdapter;
import com.lisungui.pharma.adapter.RecycleOrderListAdapter;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.OrderDetails;

import java.util.ArrayList;

public class OrderTackingActivity extends AppCompatActivity {

    private static final String TAG = OrderTackingActivity.class.getSimpleName();

    private SQLiteDataBaseHelper dataBaseHelper;
    private RecyclerView list_order_details;
    private RecyclerView recycle_OrderList;
    ArrayList<OrderDetails> orderDetailsArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tacking);

        Log.d(TAG, "OrderTackingActivity");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();

        setListAdapter();
    }

    private void setListAdapter() {
        orderDetailsArrayList.clear();
        orderDetailsArrayList = dataBaseHelper.getAllOrders();
        //Log.d(TAG, "setListAdapter: "+dataBaseHelper.getAllOrders().get(0).getOrder_data().get(0).getMed_name());

        /*for (int i = 0; i <orderDetailsArrayList.size(); i++) {
            Log.d(TAG, i+" Order Data Size : "+orderDetailsArrayList.get(i).getOrder_data().get(0).getMed_name());
            Log.d(TAG, i+" Order Data Size : "+orderDetailsArrayList.get(i).getOrder_data().get(0).getDetail_fk_order_id());
            Log.d(TAG, i+" Order Data Size : "+orderDetailsArrayList.get(i).getOrder_data().get(0).getDetail_id());
            Log.d(TAG, i+" Order Data Size : "+orderDetailsArrayList.get(i).getOrder_data().get(0).getDetail_med_id());
            Log.d(TAG, i+" Order Data Size : "+orderDetailsArrayList.get(i).getOrder_data().get(0).getDetail_med_qnty());
            Log.d(TAG, i+" Order Data Size : "+orderDetailsArrayList.get(i).getOrder_data().get(0).getDetail_med_price());
        }*/

        Log.d(TAG, "43 Order Size : "+ orderDetailsArrayList.size());

        /*OrderDetailsAdapter orderDetailsAdapter = new OrderDetailsAdapter(this, orderDetailsArrayList);
        list_order_details.setAdapter(orderDetailsAdapter);
        orderDetailsAdapter.notifyDataSetChanged();*/
        RecycleOrderListAdapter orderAdapter=new RecycleOrderListAdapter(this,orderDetailsArrayList);
        list_order_details.setAdapter(orderAdapter);
        orderAdapter.notifyDataSetChanged();
    }

    private void init() {
        dataBaseHelper = new SQLiteDataBaseHelper(this);
        list_order_details = (RecyclerView) findViewById(R.id.list_order_details);
        list_order_details.setLayoutManager(new LinearLayoutManager(OrderTackingActivity.this));
        //recycle_OrderList=(RecyclerView) findViewById(R.id.recycle_OrderList);
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