package com.lisungui.pharma.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lisungui.pharma.R;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.MedDetails;
import com.lisungui.pharma.models.PaymentData;
import com.lisungui.pharma.models.PlaceOrderPojo;
import com.lisungui.pharma.models.TempOrderPojo;
import com.lisungui.pharma.models.UploadOrders;
import com.lisungui.pharma.models.UserPojo;
import com.lisungui.pharma.rest.RestClient;
import com.lisungui.pharma.utility.CommonUtility;
import com.lisungui.pharma.utility.PrefManager;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StripePaymentActivity extends AppCompatActivity {

    private static final String TAG = "StripePaymentActivity";
    private Spanned titles;
    Context nContext;
    private EditText edtxt_Emailid;
//    private EditText edtxt_card_Number;
//    private EditText edtxt_card_Expiry;
//    private EditText edtxt_card_CVV;
    private Button btn_Done_credit_card_dialog;


    private SQLiteDataBaseHelper dataBaseHelper;
   // PrefManager prefManager;


    Activity nActivity;
    private OkHttpClient httpClient = new OkHttpClient();
    private Stripe stripe;
    //String stripeApiKey="sk_test_51GxDnQCQukwBvStGvlBTeqXYBSLJIN8Zkx6Gm8xQj6Feh27FzaGPpjkgsTY6YEYHVVi1W1Dq7PoSmMs6Bv1rhZQI00VV4S8EzS";

    //String stripePublishKey="pk_test_51GxDnQCQukwBvStGZPWrhUYitcxINjf9TagBAfReaaGL2xsJ6iPXM1ZwEmHJ8jGGqnojQhokvDbhhsYUa5weyKNK00NW3aAnjm";
    String stripeApiKey="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";
    String stripePublishKey="pk_live_51GxDnQCQukwBvStGqBECbDiZugu1rZkbHC5KWYZ1SlOasJHtYBbn2SePDUz6mBkq7BQdthjlWTyk9kHPHNanLkyO00cKGIufSJ";
    Card card;
    CardInputWidget cardInputWidget;
    Bundle bundle;
    Double amount;
    String productName;
    private ProgressDialog pDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String pharmacyId;
    String user_id;
    String txt_del_address;
    String countryid;
    String cityID;
    int quantity;
     String uploadOrders;
    Gson converter = new Gson();
    private UserPojo userPojo;
    private ArrayList<TempOrderPojo> tempOrderPojos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_payment);
        nContext=StripePaymentActivity.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dataBaseHelper = new SQLiteDataBaseHelper(this);
        userPojo = dataBaseHelper.getUser();
        //uploadOrders = new UploadOrders();
        bundle=getIntent().getExtras();
        if(bundle!=null){
            String amountString=bundle.getString("Ammount");
            if(amountString.contains(",")){
                amountString=amountString.replace(",",".");
            }
            amount=Double.parseDouble(amountString);
            //amount=bundle.getDouble("Ammount");
            Log.d(TAG, "bundle: amount"+amount);
            productName=bundle.getString("product_name");
            Log.d(TAG, "bundle: productName"+productName);
            pharmacyId=bundle.getString("pharmacyId");
            user_id=bundle.getString("user_id");
            txt_del_address=bundle.getString("txt_del_address");
            countryid=bundle.getString("countryid");
            cityID=bundle.getString("cityID");
            quantity=bundle.getInt("quantity");
            uploadOrders= bundle.getString("uploadOrders");
            //tempOrderPojos= (ArrayList<TempOrderPojo>) bundle.getSerializable("tempOrderPojos");
            Log.d(TAG, "bundle:c pharmacyid "+pharmacyId);
            Log.d(TAG, "bundle:c user_id "+user_id);
            Log.d(TAG, "bundle:c txt_del_address"+txt_del_address);
            Log.d(TAG, "bundle:c meddetails"+converter.toJson(uploadOrders));
            Log.d(TAG, "bundle:c countryid"+countryid);
            Log.d(TAG, "bundle:c cityid"+cityID);

        }
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.str_loading));
        pDialog.setCancelable(false);


        if (Locale.getDefault().getLanguage().equals("en")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                titles = Html.fromHtml("<font color=\"#000000\">" + "Order " + "</font>" + "<font color=\"#729619\">" + "Details" + "</font>", Html.FROM_HTML_MODE_LEGACY);
            } else {
                titles = (Html.fromHtml("<font color=\"#000000\">" + "Order " + "</font>" + "<font color=\"#729619\">" + "Details" + "</font>"));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                titles = Html.fromHtml("<font color=\"#000000\">" + getString(R.string.str_order_details1) + " " + "</font>" + "<font color=\"#729619\">" + getString(R.string.str_order_details2) + "</font>", Html.FROM_HTML_MODE_LEGACY);
            } else {
                titles = (Html.fromHtml("<font color=\"#000000\">" + getString(R.string.str_order_details1) + " " + "</font>" + "<font color=\"#729619\">" + getString(R.string.str_order_details2) + "</font>"));
            }
        }
        initWidgets();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(nContext, "Please complete the payment", Toast.LENGTH_SHORT).show();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Toast.makeText(nContext, "Please complete the payment to place order", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void initWidgets() {

        edtxt_Emailid=findViewById(R.id.edtxt_Emailid);
//        edtxt_card_Number=findViewById(R.id.edtxt_card_Number);
//        edtxt_card_Expiry=findViewById(R.id.edtxt_card_Expiry);
//        edtxt_card_CVV=findViewById(R.id.edtxt_card_CVV);
        cardInputWidget = findViewById(R.id.cardInputWidget);
        btn_Done_credit_card_dialog=findViewById(R.id.btn_Done_credit_card);
        btn_Done_credit_card_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtxt_Emailid.getText().toString().isEmpty() && edtxt_Emailid.getText().toString().trim().matches(emailPattern)) {
                    showProgressDialog();
                    stripePay(edtxt_Emailid.getText().toString().trim());
                }else{
                    Toast.makeText(nContext, "Please enter valid emailid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void stripePay(final String userEmailid){

       /* if (!edtxt_card_Number.getText().toString().isEmpty() || edtxt_card_Number.getText().length() == 16) {
            cardInputWidget.setCardNumber(edtxt_card_Number.getText().toString());
        } else {
            Toast.makeText(nContext, "Please enter valid card number", Toast.LENGTH_SHORT).show();
        }
        if (!edtxt_card_Expiry.getText().toString().isEmpty() || edtxt_card_Number.getText().length() < 5) {
            Log.d(TAG, "stripePay: "+edtxt_card_Expiry.getText());
            String[] ExpirySplit = edtxt_card_Expiry.getText().toString().split("/");
            cardInputWidget.setExpiryDate(Integer.parseInt(ExpirySplit[0]), Integer.parseInt(ExpirySplit[1]));
        } else {
            Toast.makeText(nContext, "Please enter valid card Expiry date", Toast.LENGTH_SHORT).show();
        }
        if (!edtxt_card_CVV.getText().toString().isEmpty() || edtxt_card_CVV.getText().length() == 3) {
            cardInputWidget.setCvcCode(edtxt_card_CVV.getText().toString());
        } else {
            Toast.makeText(nContext, "Please enter valid CVC", Toast.LENGTH_SHORT).show();
        }*/

        if(cardInputWidget.getCard()!=null){
            card=cardInputWidget.getCard();
        }
        stripe=new Stripe(nContext,stripePublishKey);
        stripe.createToken(card, new ApiResultCallback<Token>() {
            @Override
            public void onSuccess(Token token) {
                RestClient.RestApiInterface service=RestClient.getClient();
                retrofit2.Call<PaymentData> call=service.doPayment(productName,userEmailid,
                        token.getId(),amount
                );
                call.enqueue(new Callback<PaymentData>() {
                    @Override
                    public void onResponse(Call<PaymentData> call, Response<PaymentData> response) {
                        Log.d(TAG, "onResponse: "+response.body().getStripeToken());
                        Log.d(TAG, "onResponse: "+response.code());
                        if(response.isSuccessful()) {
                            hideProgressDialog();
                            placeOrder();
                            final Dialog dialog = new Dialog(nContext, R.style.CustomAlertDialog);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setContentView(R.layout.dialog_place_order_success);
                            TextView txt_ok = dialog.findViewById(R.id.txt_ok);
                            txt_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(nContext, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                            dialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PaymentData> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getMessage());

                    }
                });
            }

            @Override
            public void onError(@NotNull Exception e) {
                Log.d(TAG, "onError: "+e.getMessage());

            }
        });
    }

    private void placeOrder() {
        RestClient.RestApiInterface service = RestClient.getClient();
        Call<PlaceOrderPojo> call = service.placeCountryOrderData(user_id,
                uploadOrders,
                amount, quantity,
                txt_del_address,
                "0.0", "0.0","1",countryid,cityID);
        call.enqueue(new Callback<PlaceOrderPojo>() {
            @Override
            public void onResponse(Call<PlaceOrderPojo> call, retrofit2.Response<PlaceOrderPojo> response) {
                hideProgressDialog();

                if (response.isSuccessful()) {
                    tempOrderPojos.clear();
                    tempOrderPojos=dataBaseHelper.getMyCartDetails();
                    Log.d(TAG, "onResponse: tempOrderPojos"+tempOrderPojos.get(0));
                    PlaceOrderPojo placeOrderPOJO = response.body();
                    Log.d(TAG, "onResponse : " + response.body());

                    if (placeOrderPOJO.getStatus() == 1) {
                        int server_order_id = placeOrderPOJO.getOrder_id();
                        String order_date = placeOrderPOJO.getDate();
                        Log.d(TAG, "230 server_order_id : " + server_order_id);

                        String[] column = new String[]{
                                SQLiteDataBaseHelper.COLUMN_ORDER_SERVER_ID,
                                SQLiteDataBaseHelper.COLUMN_ORDER_QNTY,
                                SQLiteDataBaseHelper.COLUMN_ORDER_TOTAL_PRICE,
                                SQLiteDataBaseHelper.COLUMN_ORDER_DATE,
                                SQLiteDataBaseHelper.COLUMN_ORDER_TRACK_STATUS
                        };

                        String[] values = new String[]{
                                String.valueOf(server_order_id),
                                String.valueOf(quantity),
                                String.valueOf(amount),
                                order_date,
                                getString(R.string.str_received)
                        };

                        try {
                            long rowId = dataBaseHelper.insertOrUpdate(SQLiteDataBaseHelper.ORDER_TABLE, null, column, values);
                            Log.d(TAG, "247 rowId : " + rowId);

                            String[] col = new String[]{
                                    SQLiteDataBaseHelper.COLUMN_DETAIL_ORDER_ID,
                                    SQLiteDataBaseHelper.COLUMN_DETAIL_FK_ORDER_ID,
                                    SQLiteDataBaseHelper.COLUMN_DETAIL_MED_ID,
                                    SQLiteDataBaseHelper.COLUMN_ORDER_MED_NAME,
                                    SQLiteDataBaseHelper.COLUMN_DETAIL_MED_PRICE,
                                    SQLiteDataBaseHelper.COLUMN_DETAIL_QNTY,
                                    SQLiteDataBaseHelper.COLUMN_DETAIL_MED_QNTY,
                                    SQLiteDataBaseHelper.COLUMN_DETAIL_PRICE

                            };

                            for (int i = 0; i < tempOrderPojos.size(); i++) {
                                Log.d(TAG, "placeorder update qty : " + tempOrderPojos.get(i).getTemp_qty());
                                String[] val = new String[]{
                                        String.valueOf(rowId),
                                        String.valueOf(server_order_id),
                                        String.valueOf(tempOrderPojos.get(i).getTemp_order_med_id()),
                                        String.valueOf(tempOrderPojos.get(i).getTemp_order_med_name()),
                                        String.valueOf(tempOrderPojos.get(i).getTemp_order_med_price()),
                                        String.valueOf(tempOrderPojos.get(i).getTemp_qty()),
                                        String.valueOf(tempOrderPojos.get(i).getTemp_qty()),
                                        String.valueOf(tempOrderPojos.get(i).getTemp_order_med_price())
                                };

                                long rowId1 = dataBaseHelper.insertOrUpdate(SQLiteDataBaseHelper.ORDER_DETAILS_TABLE, null, col, val);

                            }

                            dataBaseHelper.deleteAll(SQLiteDataBaseHelper.TEMP_ORDER_TABLE);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //pharmacyid "+pharmacyid);
                        //user_id "+user_id);
                        //txt_del_address"+txt_del_address.getText().toString());
                        //meddetails"+converter.toJson(uploadOrders));
                        //amount"+total_amount);
                        //countryid"+countryid);
                        /*Intent intent = new Intent(CountryPlaceOrderActivity.this, StripePaymentActivity.class);
                        intent.putExtra("pharmacyId",pharmacyid);
                        intent.putExtra("user_id",user_id);
                        intent.putExtra("txt_del_address",txt_del_address.getText().toString());
                        intent.putExtra("countryid",countryid);
                        intent.putExtra("quantity",tempOrderPojos.size());
                        intent.putExtra("uploadOrders", converter.toJson(uploadOrders));
                        intent.putExtra("Ammount",total_amount+1.5);

                        intent.putExtra("product_name",productNames+"");
                        startActivity(intent);*/
                        final Dialog dialog = new Dialog(StripePaymentActivity.this, R.style.CustomAlertDialog);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setContentView(R.layout.dialog_place_order_success);
                        TextView txt_ok = dialog.findViewById(R.id.txt_ok);
                        txt_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Intent intent = new Intent(StripePaymentActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                        if(!isFinishing()) {
                            dialog.show();
                        }


                    } else {
//                        String str_invalid_uname = getResources().getString(R.string.str_invalid_uname);
                        Toast.makeText(StripePaymentActivity.this, getString(R.string.str_try_again), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    String str_check_int = getResources().getString(R.string.str_check_int);
                    Toast.makeText(StripePaymentActivity.this, str_check_int, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlaceOrderPojo> call, Throwable t) {
                hideProgressDialog();
                Log.d(TAG, "onFailure");
                String str_check_int = getResources().getString(R.string.str_check_int);
                Toast.makeText(StripePaymentActivity.this, str_check_int, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }




}