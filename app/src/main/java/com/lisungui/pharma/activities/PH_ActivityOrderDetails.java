package com.lisungui.pharma.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lisungui.pharma.R;
import com.lisungui.pharma.models.OrderUpdateReponseModel;
import com.lisungui.pharma.models.PharmaOrderDetails;
import com.lisungui.pharma.rest.RestClient;
import com.lisungui.pharma.utility.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PH_ActivityOrderDetails extends AppCompatActivity {
    private static final String TAG = "PH_ActivityOrderDetails";
    private Spinner sttaus_spinner;
    String[] status = {"Change Status", "Received", "On The Way", "Ready In Pharmacy"};
    String[] statusfr = {"Changer le statut", "Reçu", "En route", "Prêt en pharmacie"};
    ImageView iv_prescription;
    Activity context;
    String prescription_image = null;
    String pharmID;
    String orderID;
    private Button bt_submit, bt_accept, bt_close;
    private TextView tv_medicineName, tv_price, tv_Quantity, tv_totalPrice, tv_ContactNo, tv_date, tv_orderStatus, tv_totalItem, tv_TotalAmmount, tv_username, tv_userStatus, tv_address, tv_purchaseDateTime;
    private LinearLayout linlay_AddViews;
    private EditText et_massage;
    private ProgressDialog pDialog;
    PharmaOrderDetails.OrderDetail detailData;
    PharmaOrderDetails.OrderDetail data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ph_activty_order_details);
        context = PH_ActivityOrderDetails.this;
        Bundle bundle = getIntent().getExtras();
        pharmID = PrefManager.getUserID(context);
        if (bundle != null) {
            orderID = bundle.getString("OID");
        }

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.str_loading));
        pDialog.setCancelable(false);

        findViewById(R.id.mainlayout).setVisibility(View.GONE);
        setTitle("Order ID: #" + orderID);
        initViews();

        if (context.getString(R.string.lang).contentEquals("English")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.simple_list_item_1, status);
            sttaus_spinner.setAdapter(adapter);
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.simple_list_item_1, statusfr);
            sttaus_spinner.setAdapter(adapter);
        }
        getOrderDetails();
    }

    private void getOrderDetails() {
        pDialog.show();

        RestClient.getClient().getPharmOrderDetail(pharmID, orderID)
                .enqueue(new Callback<PharmaOrderDetails>() {
                    @Override
                    public void onResponse(Call<PharmaOrderDetails> call,
                                           Response<PharmaOrderDetails> response) {
                        pDialog.dismiss();

                        PharmaOrderDetails result = response.body();
                        if (result == null) {
                            return;
                        }
                        if (result.getStatus() == 1) {
                            findViewById(R.id.mainlayout).setVisibility(View.VISIBLE);
                            Log.d(TAG, "onResponse: "+result.getOrderDetails().size());
                            detailData = result.getOrderDetails().get(0);
                            if (result.getOrderDetails().size()==1 && detailData.getOrderData()!=null) {

                                for (int i=0;i<detailData.getOrderData().size();i++) {

                                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View addView = layoutInflater.inflate(R.layout.pharmamed_details_addview, null);
                                    tv_medicineName=addView.findViewById(R.id.tv_medicineName);
                                    tv_Quantity=addView.findViewById(R.id.tv_Quantity);
                                    tv_price=addView.findViewById(R.id.tv_price);
                                    tv_medicineName.setText(detailData.getOrderData().get(i).getMedName());
                                    tv_Quantity.setText(String.valueOf(detailData.getOrderData().get(i).getDetailMedQnty()));
                                    tv_price.setText(String.valueOf(detailData.getOrderData().get(i).getDetailMedPrice()));
                                    linlay_AddViews.addView(addView);
                                }

                            }
                            setText(tv_totalPrice,String.valueOf(detailData.getOrderTotalPrice()));
                            data=result.getOrderDetails().get(0);
                            if (data.getOrderTrackStatus()!=null) {
                                if (data.getOrderTrackStatus().equalsIgnoreCase("Pending")) {
                                    setText(tv_orderStatus, getResources().getString(R.string.str_pending));
                                } else if (data.getOrderTrackStatus().equalsIgnoreCase("Received")) {
                                    setText(tv_orderStatus, getResources().getString(R.string.str_received));
                                } else if (data.getOrderTrackStatus().equalsIgnoreCase("On The Way")) {
                                    setText(tv_orderStatus, getResources().getString(R.string.str_on_the_way));
                                } else if (data.getOrderTrackStatus().equalsIgnoreCase("Ready In Pharmacy")) {
                                    setText(tv_orderStatus, getResources().getString(R.string.reddyinpharmacy));
                                } else {
                                    setText(tv_orderStatus, data.getOrderTrackStatus());
                                }
                            }
                            setText(tv_date, data.getOrderUpdateDate());
                            setText(tv_username, data.getOrderUsername());
                            setText(tv_ContactNo, data.getOrderContact());
                            setText(tv_TotalAmmount, data.getOrderTotalPrice());
                            setText(tv_purchaseDateTime, data.getOrderDate());
                            setText(tv_address, data.getOrderAddress());
                            prescription_image = data.getOrderPresImg();
                            boolean contains = false;
                            for (int i = 0; i < data.getOrderPharmaData().size(); i++) {
                                PharmaOrderDetails.OrderDatum order = data.getOrderPharmaData().get(i);
                                if (order.getPharmId().equals(pharmID)) {
                                    contains = true;
                                }
                            }

                            if (data.getOrderTrackStatus().equalsIgnoreCase("Pending")) {
                                findViewById(R.id.rl_accept).setVisibility(View.VISIBLE);
                                findViewById(R.id.ll_changeStatus).setVisibility(View.GONE);
                            } else {
                                findViewById(R.id.rl_accept).setVisibility(View.GONE);
                                findViewById(R.id.ll_changeStatus).setVisibility(View.VISIBLE);
                            }

                            if (contains && data.getOrderTrackStatus().equalsIgnoreCase("Pending")) {
                                findViewById(R.id.rl_accept).setVisibility(View.GONE);
                                findViewById(R.id.ll_changeStatus).setVisibility(View.GONE);
                            } else if (contains && !data.getOrderTrackStatus().equalsIgnoreCase("Pending")) {
                                findViewById(R.id.rl_accept).setVisibility(View.GONE);
                                findViewById(R.id.ll_changeStatus).setVisibility(View.VISIBLE);
                            }
                            if (prescription_image == null || prescription_image.isEmpty()) {
                                findViewById(R.id.ll_prescription).setVisibility(View.GONE);
                            } else {
                                Glide.with(context).load(prescription_image).into(iv_prescription);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<PharmaOrderDetails> call, Throwable t) {
                        pDialog.dismiss();

                    }
                });
    }

    private void setText(TextView tv_medicineName, String medName) {
        if (medName == null || medName.trim().isEmpty() || medName.equals("null"))
            tv_medicineName.setText(getResources().getString(R.string.notAwailable));
        else tv_medicineName.setText(medName);
    }

    private void initViews() {
        sttaus_spinner = findViewById(R.id.spinner);
        iv_prescription = findViewById(R.id.iv_prescription);
        bt_submit = findViewById(R.id.bt_submit);
        bt_accept = findViewById(R.id.bt_accept);
        bt_close = findViewById(R.id.bt_close);

        //tv_medicineName = findViewById(R.id.tv_medicineName);
        //tv_price = findViewById(R.id.tv_price);
        //tv_Quantity = findViewById(R.id.tv_Quantity);
        tv_totalPrice = findViewById(R.id.tv_totalPrice);
        tv_orderStatus = findViewById(R.id.tv_orderStatus);
        tv_date = findViewById(R.id.tv_date);
        tv_username = findViewById(R.id.tv_username);
        tv_ContactNo = findViewById(R.id.tv_ContactNo);
        tv_totalItem = findViewById(R.id.tv_totalItem);
        tv_TotalAmmount = findViewById(R.id.tv_TotalAmmount);
        tv_purchaseDateTime = findViewById(R.id.tv_purchaseDateTime);
        tv_userStatus = findViewById(R.id.tv_userStatus);
        tv_address = findViewById(R.id.tv_address);
        et_massage = findViewById(R.id.et_massage);
        linlay_AddViews = findViewById(R.id.linlay_AddViews);

        iv_prescription = findViewById(R.id.iv_prescription);
        iv_prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prescription_image != null) {
                    startActivity(new Intent(context, FullScreenImage.class).putExtra("imagebitmap", prescription_image));
                }
            }
        });
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.show();
                if (sttaus_spinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(context, "Please Select the order status", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } else {

                    RestClient.getClient().updatePharmOrderStatus(orderID, pharmID, status[sttaus_spinner.getSelectedItemPosition()]).enqueue(new Callback<OrderUpdateReponseModel>() {
                        @Override
                        public void onResponse(Call<OrderUpdateReponseModel> call,
                                               Response<OrderUpdateReponseModel> response) {
                            assert response.body() != null;
                            Toast.makeText(context, "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            finish();
                            pDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<OrderUpdateReponseModel> call, Throwable t) {
                            Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        }
                    });
                }
            }
        });
        bt_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.show();
                RestClient.getClient().AcceptPharmOrder(orderID, pharmID, "Accept", et_massage.getText().toString().trim(), status[1]).enqueue(new Callback<OrderUpdateReponseModel>() {
                    @Override
                    public void onResponse(Call<OrderUpdateReponseModel> call, Response<OrderUpdateReponseModel> response) {
                        assert response.body() != null;
                        Toast.makeText(context, "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        finish();
                        pDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<OrderUpdateReponseModel> call, Throwable t) {
                        Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                });
            }
        });
    }

}
