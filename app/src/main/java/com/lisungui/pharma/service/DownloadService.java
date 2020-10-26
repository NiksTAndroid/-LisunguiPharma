package com.lisungui.pharma.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.DownloadOrderDetailsModel;
import com.lisungui.pharma.models.ListDownloadOrderDetails;
import com.lisungui.pharma.models.OrderDetails;
import com.lisungui.pharma.models.OrderIDs;
import com.lisungui.pharma.rest.RestClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class DownloadService extends IntentService {

    private static final String TAG = DownloadService.class.getSimpleName();
    private SQLiteDataBaseHelper dataBaseHelper;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownloadService(String name) {
        super(name);
    }

    public DownloadService() {
        super(DownloadService.class.getSimpleName());
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        dataBaseHelper = new SQLiteDataBaseHelper(this);

        try {
            ArrayList<OrderDetails> orderDetailses = dataBaseHelper.getAllOrders();
            if(orderDetailses.size() > 0) {

                ArrayList<Integer> order_ids = dataBaseHelper.getOrderID();
                if(order_ids.size() > 0) {
                    getStatus(order_ids);
                }

            } else {
                getData();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getStatus(ArrayList<Integer> order_ids) {
        Gson converter = new Gson();

        OrderIDs orderIDs = new OrderIDs();
        orderIDs.setOrder_id(order_ids);
        Log.d(TAG, "84 json : "+converter.toJson(orderIDs));

        SharedPreferences pref = getSharedPreferences("Pharmacy", 0);

        RestClient.RestApiInterface service = RestClient.getClient();
        Call<ListDownloadOrderDetails> call = service.getStatusData(pref.getString("USER_ID", ""), converter.toJson(orderIDs));
        call.enqueue(new Callback<ListDownloadOrderDetails>() {
            @Override
            public void onResponse(Call<ListDownloadOrderDetails> call, retrofit2.Response<ListDownloadOrderDetails> response) {

                if (response.isSuccessful()) {

                    ListDownloadOrderDetails orderDetails = response.body();
                    Log.d(TAG, "Status : "+orderDetails.getStatus());
                    if (orderDetails.getStatus() == 1) {

                        String[] col_order = new String[] {
                                SQLiteDataBaseHelper.COLUMN_ORDER_UPDATE_DATE,
                                SQLiteDataBaseHelper.COLUMN_ORDER_TRACK_STATUS
                        };

                        ArrayList<DownloadOrderDetailsModel> orderArrayList = orderDetails.getOrder_details();
                        Log.d(TAG, "orderArrayList size : "+orderArrayList.size());

                        for(int i = 0; i < orderArrayList.size(); i++) {

                            String[] val_order = new String[] {
                                    orderArrayList.get(i).getOrder_update_date(),
                                    orderArrayList.get(i).getOrder_track_status()
                            };

                            Log.d(TAG, "Update Date : "+orderArrayList.get(i).getOrder_update_date());

                            String selection = String.format(SQLiteDataBaseHelper.COLUMN_ORDER_SERVER_ID+ "='%s'", orderArrayList.get(i).getOrder_server_id());
                            Log.d(TAG, "Selection : "+selection);

                            long local_order_id = dataBaseHelper.insertOrUpdate(SQLiteDataBaseHelper.ORDER_TABLE, selection, col_order, val_order);
                            Log.d(TAG, "119 local_order_id : "+local_order_id);
                        }

                    } else {
                        Log.d(TAG, "No Records Found");
                    }

                } else {
                    Log.d(TAG, "Response Failed");
                }
            }

            @Override
            public void onFailure(Call<ListDownloadOrderDetails> call, Throwable t) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    private void getData() {
        SharedPreferences pref = getSharedPreferences("Pharmacy", 0);

        RestClient.RestApiInterface service = RestClient.getClient();
        Call<ListDownloadOrderDetails> call = service.getOrderData(pref.getString("USER_ID", ""));
        call.enqueue(new Callback<ListDownloadOrderDetails>() {
            @Override
            public void onResponse(Call<ListDownloadOrderDetails> call, retrofit2.Response<ListDownloadOrderDetails> response) {

                if (response.isSuccessful()) {

                    ListDownloadOrderDetails orderDetails = response.body();
                    Log.d(TAG, "Status : "+orderDetails.getStatus());
                    if (orderDetails.getStatus() == 1) {

                        String[] col_order = new String[] {
                                SQLiteDataBaseHelper.COLUMN_ORDER_SERVER_ID,
                                SQLiteDataBaseHelper.COLUMN_ORDER_QNTY,
                                SQLiteDataBaseHelper.COLUMN_ORDER_TOTAL_PRICE,
                                SQLiteDataBaseHelper.COLUMN_ORDER_DATE,
                                SQLiteDataBaseHelper.COLUMN_ORDER_UPDATE_DATE,
                                SQLiteDataBaseHelper.COLUMN_ORDER_TRACK_STATUS
                        };

                        String[] col_order_details = new String[] {
                                SQLiteDataBaseHelper.COLUMN_DETAIL_ORDER_ID,
                                SQLiteDataBaseHelper.COLUMN_DETAIL_FK_ORDER_ID,
                                SQLiteDataBaseHelper.COLUMN_DETAIL_MED_ID,
                                SQLiteDataBaseHelper.COLUMN_ORDER_MED_NAME,
                                SQLiteDataBaseHelper.COLUMN_DETAIL_MED_PRICE,
                                SQLiteDataBaseHelper.COLUMN_DETAIL_PRICE,
                                SQLiteDataBaseHelper.COLUMN_DETAIL_MED_QNTY,
                                SQLiteDataBaseHelper.COLUMN_DETAIL_QNTY,

                        };

                        ArrayList<DownloadOrderDetailsModel> orderArrayList = orderDetails.getOrder_details();
                        Log.d(TAG, "orderArrayList size : "+orderArrayList.size());

                        for(int i = 0; i < orderArrayList.size(); i++) {

                            String[] val_order = new String[]{
                                    String.valueOf(orderArrayList.get(i).getOrder_server_id()),
                                    String.valueOf(orderArrayList.get(i).getOrder_qnty()),
                                    String.valueOf(orderArrayList.get(i).getOrder_total_price()),
                                    orderArrayList.get(i).getOrder_date(),
                                    orderArrayList.get(i).getOrder_update_date(),
                                    orderArrayList.get(i).getOrder_track_status()
                            };

                            String selection = String.format(SQLiteDataBaseHelper.COLUMN_ORDER_SERVER_ID + "='%s'", orderArrayList.get(i).getOrder_server_id());

                            long local_order_id = dataBaseHelper.insertOrUpdate(SQLiteDataBaseHelper.ORDER_TABLE, selection, col_order, val_order);
                            Log.d(TAG, "104 local_order_id : " + local_order_id);
                            ArrayList<DownloadOrderDetailsModel.OrderDataModel> medArrayList = orderArrayList.get(i).getOrder_data();
                            //Log.d(TAG, "106 Med List medArrayList size : "+medArrayList.size());

                            if (medArrayList != null) {

                                for (int j = 0; j < medArrayList.size(); j++) {

                                    String[] val_order_details = new String[]{
                                            String.valueOf(local_order_id),
                                            String.valueOf(orderArrayList.get(i).getOrder_server_id()),
                                            String.valueOf(medArrayList.get(j).getDetail_med_id()),
                                            medArrayList.get(j).getMed_name(),
                                            String.valueOf(medArrayList.get(j).getDetail_med_price()),
                                            String.valueOf(medArrayList.get(j).getDetail_med_price()),
                                            String.valueOf(medArrayList.get(j).getDetail_med_qnty()),
                                            String.valueOf(medArrayList.get(j).getDetail_med_qnty())
                                    };

//                                String select = String.format(COLUMN_DETAIL_ORDER_ID+ "='%s'", local_order_id);
                                    long local_order_detail_id = dataBaseHelper.insertOrUpdate(SQLiteDataBaseHelper.ORDER_DETAILS_TABLE, null, col_order_details, val_order_details);
                                    Log.d(TAG, "119 local_order_detail_id : " + local_order_detail_id);
                                }
                            }
                        }

                    } else {
                        Log.d(TAG, "No Records Found");
                    }

                } else {
                    Log.d(TAG, "Response Failed");
                }
            }

            @Override
            public void onFailure(Call<ListDownloadOrderDetails> call, Throwable t) {
                Log.d(TAG, "onFailure");
            }
        });


    }
}