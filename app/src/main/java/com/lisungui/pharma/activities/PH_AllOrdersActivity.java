package com.lisungui.pharma.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.Toast;

import com.lisungui.pharma.R;
import com.lisungui.pharma.adapter.PharmacyOrdersListAdapter;
import com.lisungui.pharma.models.PharmacyOrdersResponseModel;
import com.lisungui.pharma.rest.RestClient;
import com.lisungui.pharma.utility.PrefManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PH_AllOrdersActivity extends AppCompatActivity {

    private static final String TAG = "PH_AllOrdersActivity";

    private ArrayList<PharmacyOrdersResponseModel.OrderDetail> ordersList = new ArrayList<>();
    private RecyclerView rv_orders_list;
    private PharmacyOrdersListAdapter listAdapter;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ph_activity_all_orders);
        setTitle(getString(R.string.Orders));
        rv_orders_list = findViewById(R.id.rv_orders_list);
        listAdapter = new PharmacyOrdersListAdapter(this, ordersList);
        rv_orders_list.setLayoutManager(new LinearLayoutManager(this));
        rv_orders_list.setAdapter(listAdapter);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.str_loading));
        pDialog.setCancelable(false);
        showProgressDialog();

    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void loadData() {
        Log.d(TAG, "loadData: pharma id"+PrefManager.getUserID(this));

        RestClient.getClient()
                .getPharmacyOrdersList(PrefManager.getUserID(this))
                .enqueue(new Callback<PharmacyOrdersResponseModel>() {
                    @Override
                    public void onResponse(Call<PharmacyOrdersResponseModel> call,
                                           Response<PharmacyOrdersResponseModel> response) {
                        hideProgressDialog();
                        if (response.body() != null) {
                            if (response.body().getStatus() == 1) {
                                ordersList.clear();
                                ordersList.addAll(response.body().getOrderDetails());
                                Collections.sort(ordersList, new Comparator<PharmacyOrdersResponseModel.OrderDetail>() {
                                    @Override
                                    public int compare(PharmacyOrdersResponseModel.OrderDetail lhs, PharmacyOrdersResponseModel.OrderDetail rhs) {
                                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                                        return Integer.parseInt(lhs.getOrderServerId()) > Integer.parseInt(rhs.getOrderServerId()) ? -1 :
                                                (Integer.parseInt(lhs.getOrderServerId()) < Integer.parseInt(rhs.getOrderServerId()) ) ? 1 : 0;
                                    }
                                });
                                listAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(PH_AllOrdersActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PharmacyOrdersResponseModel> call, Throwable t) {
                        hideProgressDialog();
                        Toast.makeText(PH_AllOrdersActivity.this, "Something wrong "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: "+t.getMessage());
                        t.printStackTrace();

                    }
                });
    }

}
