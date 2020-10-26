package com.lisungui.pharma.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lisungui.pharma.GPSTracking.GPSTracker;
import com.lisungui.pharma.R;
import com.lisungui.pharma.adapter.OrderListAdapter;
import com.lisungui.pharma.caller.IMethodCaller;
import com.lisungui.pharma.constant.StringConstant;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.Address;
import com.lisungui.pharma.models.MedDetails;
import com.lisungui.pharma.models.PlaceOrderPojo;
import com.lisungui.pharma.models.TempOrderPojo;
import com.lisungui.pharma.models.UploadOrders;
import com.lisungui.pharma.models.UserPojo;
import com.lisungui.pharma.rest.RestClient;
import com.lisungui.pharma.utility.CommonUtility;
import com.lisungui.pharma.utility.ImageFilePath;
import com.lisungui.pharma.utility.PrefManager;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.lisungui.pharma.utility.ImageFilePath.nopath;


public class CountryPlaceOrderActivity extends AppCompatActivity implements IMethodCaller {

    private static final String TAG = CountryPlaceOrderActivity.class.getSimpleName();
    private static final int CHOOSE_PHOTO_FROM_GALLARY = 123;
    private String user_id;
    private SharedPreferences pref;
    private UploadOrders uploadOrders;
    private ListView list_place_order;
    private TextView txt_order_total;
    private TextView txt_place_order;
    private TextView txt_order_details;
    private TextView txt_empty_cart;
    private TextView txt_commamountshow;
    private TextView txt_mediamountshow;
    private SQLiteDataBaseHelper dataBaseHelper;
    private boolean capturePrescription = false;
    private ArrayList<TempOrderPojo> tempOrderPojos = new ArrayList<>();
    private double total_amount;
    private String productNames="";
    private ProgressDialog pDialog;
    private Spanned titles;
    private UserPojo userPojo;
    private TextView btn_address;
    private TextView txt_del_address;
    private ImageView img_edit;
    private RelativeLayout rel_add;
    String pharmacyid;
    String countryid;
    String cityID;
    Bundle nBundle;
    double totalAmountShow;
    private Context context;
    private static final int CAPTURE_PHOTO_FROM_CAMERA = 151;
    private int READ_STORAGE_PERMISSION_REQUEST_CODE = 51;
    private int WRITE_STORAGE_PERMISSION_REQUEST_CODE = 52;
    private String capturedPhotoFilePath = "";


    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int REQUEST_CODE_PERMISSION = 2;
    private ImageView prescriptionPhotoImage;

    GPSTracker gps;
    private double latitude;
    private double longitude;
    private boolean isPrescriptionOrder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries_place_order);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        pref = getSharedPreferences("Pharmacy", 0);
        SharedPreferences.Editor editor = pref.edit();
        if(pref.getString("New","")==null) {
            editor.putString("New", "NewUser");
            editor.apply();
            Log.d(TAG, "onCreate: "+"new user");

        }
        else{
            editor.putString("New", "OldUser");
            editor.apply();
        }

        context = CountryPlaceOrderActivity.this;
        prescriptionPhotoImage = (ImageView) findViewById(R.id.prescphoto_imageview);
        // prescriptionPhotoImage.setVisibility(View.GONE);

        user_id = pref.getString("USER_ID", "");
        if (user_id.equals("")) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        nBundle=getIntent().getExtras();
        if(nBundle!=null){
            countryid=nBundle.getString("countryid");
            cityID=nBundle.getString("cityId");
            Log.d(TAG, "onCreate:placeorder  "+countryid);
            Log.d(TAG, "onCreate:placeorder  "+cityID);
        }

        init();
        prescriptionPhotoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPrescriptionOrder) {
                    selectImageDialog();
                }
            }
        });
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

        txt_order_details.setText(titles);

        uploadOrders = new UploadOrders();
        userPojo = dataBaseHelper.getUser();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.str_loading));
        pDialog.setCancelable(false);


        if (userPojo.getUser_address()==null||userPojo.getUser_address().isEmpty()) {
            rel_add.setVisibility(View.GONE);
            btn_address.setVisibility(View.VISIBLE);
            btn_address.setText(context.getString(R.string.str_ent_add));
        } else {
            rel_add.setVisibility(View.VISIBLE);
            txt_del_address.setText(userPojo.getUser_address());
        }

        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressDialog(userPojo.getUser_address());
            }
        });

        btn_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressDialog(userPojo.getUser_address());
            }
        });

        setTotal();
        txt_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CommonUtility.isInternetON(CountryPlaceOrderActivity.this)) {
                    String str_check_int = getResources().getString(R.string.str_check_int);
                    Toast.makeText(CountryPlaceOrderActivity.this, str_check_int, Toast.LENGTH_SHORT).show();
                } else if (userPojo.getUser_address()==null||userPojo.getUser_address().isEmpty()) {
                    Toast.makeText(CountryPlaceOrderActivity.this, getString(R.string.str_enter_address), Toast.LENGTH_SHORT).show();
                } else {

                    if (tempOrderPojos.size() > 0 && !capturePrescription)// && !capturePrescription
                    {
                        ArrayList<MedDetails> medDetails = new ArrayList<>();
                        for (int i = 0; i < tempOrderPojos.size(); i++) {
                            MedDetails medDetails1 = new MedDetails();
                            medDetails1.setDetail_med_id(tempOrderPojos.get(i).getTemp_order_med_id());
                            medDetails1.setDetail_med_price(tempOrderPojos.get(i).getTemp_order_med_price());

                            medDetails1.setMed_name(tempOrderPojos.get(i).getTemp_order_med_name());

                            medDetails1.setDetail_qnty(tempOrderPojos.get(i).getTemp_qty() + "");
                            medDetails1.setDetail_med_qnty(tempOrderPojos.get(i).getTemp_qty());

                            medDetails.add(medDetails1);
                        }

                        uploadOrders.setOrder_details(medDetails);
                        Log.d(TAG, "onClick: "+uploadOrders.getOrder_details());

                        placeOrder();
                    } else if (capturePrescription) {
                        placePrescriptionOrder();
                        //Toast.makeText(PlaceOrderActivity.this, getString(R.string.str_add_med), Toast.LENGTH_SHORT).show();
                    } else {
                        //placePrescriptionOrder();
                        Toast.makeText(CountryPlaceOrderActivity.this, getString(R.string.str_add_med), Toast.LENGTH_SHORT).show();
                    }
                    //placeOrder();
                }
            }
        });

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //to receive direct intent from home screen to open camera

        Intent intent = getIntent();
        if (intent != null && null != intent.getStringExtra("camera_intent") &&
                intent.getStringExtra("camera_intent").equals("CAMERA")) {
            if (tempOrderPojos.isEmpty()) {
                isPrescriptionOrder = true;
                prescriptionPhotoImage.setVisibility(View.VISIBLE);
                list_place_order.setVisibility(View.GONE);
                selectImageDialog();
            } else {
                finish();
                Toast.makeText(CountryPlaceOrderActivity.this, getString(R.string.already_have_an_order_of_other_medicine), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void placePrescriptionOrder() {

        capturePrescription = false;

        double latitude = 0.0;
        double longitude = 0.0;
        // check if GPS enabled

        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        this.latitude = latitude;
        this.longitude = longitude;


        showProgressDialog();

        //to compress and to show captured prescriprion photo image
        File imageFile =null;
        File compressedImage =  new File(capturedPhotoFilePath);
        if (!capturedPhotoFilePath.equalsIgnoreCase("")) {
            imageFile = new File(capturedPhotoFilePath);
            try {
                compressedImage = new Compressor(this).compressToFile(imageFile);
            } catch (IOException e) {
                e.printStackTrace();
                compressedImage =imageFile;
            }
        }

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), compressedImage);

        MultipartBody.Part image_fileToUpload = MultipartBody.Part.createFormData("order_pres_img", imageFile.getName(), requestBody);

       RequestBody user_Id = RequestBody.create(MediaType.parse("text/plain"), user_id);
        RequestBody uploadOrdersData = RequestBody.create(MediaType.parse("text/plain"), "");
        final RequestBody totalAmount = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody orderQuantity = RequestBody.create(MediaType.parse("text/plain"), "1"); //20.9288 74.7602
        RequestBody pharmacyId = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody addressText = RequestBody.create(MediaType.parse("text/plain"), txt_del_address.getText().toString());
        RequestBody userlatitude = RequestBody.create(MediaType.parse("text/plain"), new Double(latitude).toString());
        RequestBody userlongitude = RequestBody.create(MediaType.parse("text/plain"), new Double(longitude).toString());

        RestClient.RestApiInterface service = RestClient.getClient();


        Call<PlaceOrderPojo> call = service.placePrescriptionOrderData(user_Id, uploadOrdersData,
                totalAmount, orderQuantity,
                pharmacyId, addressText, userlatitude, userlongitude, image_fileToUpload);

        call.enqueue(new Callback<PlaceOrderPojo>() {
            @Override
            public void onResponse(Call<PlaceOrderPojo> call, retrofit2.Response<PlaceOrderPojo> response) {
                hideProgressDialog();

                if (response.isSuccessful()) {


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
                                String.valueOf(tempOrderPojos.size()),
                                String.valueOf(total_amount),
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

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(CountryPlaceOrderActivity.this, StripePaymentActivity.class);

                        intent.putExtra("Ammount",total_amount+1.5);
                        startActivity(intent);

                        /*final Dialog dialog = new Dialog(PlaceOrderActivity.this, R.style.CustomAlertDialog);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setContentView(R.layout.dialog_place_order_success);
                        TextView txt_ok = dialog.findViewById(R.id.txt_ok);
                        txt_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                txt_empty_cart.setVisibility(View.VISIBLE);
                                list_place_order.setVisibility(View.VISIBLE);
                                prescriptionPhotoImage.setVisibility(View.GONE);
                                dialog.dismiss();
                                Intent intent = new Intent(PlaceOrderActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });

                        dialog.show();*/

                    } else {
                        Toast.makeText(CountryPlaceOrderActivity.this, getString(R.string.str_try_again), Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    String str_check_int = getResources().getString(R.string.str_check_int);
                    Toast.makeText(CountryPlaceOrderActivity.this, str_check_int, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PlaceOrderPojo> call, Throwable t) {
                hideProgressDialog();
                prescriptionPhotoImage.setImageDrawable(null);
                selectImageDialog();
                Log.d(TAG, "onFailure " + t.getMessage());
                String str_check_int = getResources().getString(R.string.str_check_int);
                Toast.makeText(CountryPlaceOrderActivity.this, str_check_int, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }

        });
    }

    private void selectImageDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.image_chooser_dialog);
        ///    dialog.setCancelable(false);
        dialog.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dispatchTakePictureIntent();

            }
        });
        dialog.findViewById(R.id.gallary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dispatchChoosePictureIntent();

            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gps = new GPSTracker(CountryPlaceOrderActivity.this);
        gps.getLocation();
    }

    private void updateDelAddress(final String address) {
        RestClient.RestApiInterface service = RestClient.getClient();
        Call<Address> call = service.updateAddress(String.valueOf(userPojo.getUser_server_id()), address);
        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, retrofit2.Response<Address> response) {

                hideProgressDialog();

                if (response.isSuccessful()) {

                    Address signUpPojo = response.body();

                    if (signUpPojo.getStatus() == 1) {

                        userPojo.setUser_address(address);
                        rel_add.setVisibility(View.VISIBLE);
                        txt_del_address.setText(address);
                        btn_address.setVisibility(View.GONE);

                        String[] col = new String[]{
                                SQLiteDataBaseHelper.COLUMN_USER_NAME,
                                SQLiteDataBaseHelper.COLUMN_USER_GENDER,
                                SQLiteDataBaseHelper.COLUMN_USER_MB_NO,
                                SQLiteDataBaseHelper.COLUMN_USER_EMAIL_ID,
                                SQLiteDataBaseHelper.COLUMN_USER_ADDRESS
                        };

                        String[] val = new String[]{
                                userPojo.getUser_name(),
                                userPojo.getUser_gender(),
                                userPojo.getUser_mb_no(),
                                userPojo.getUser_email_id(),
                                address
                        };

                        String selection = String.format(SQLiteDataBaseHelper.COLUMN_USER_SERVER_ID + "='%s'", userPojo.getUser_id());
                        long user_id = dataBaseHelper.insertOrUpdate(SQLiteDataBaseHelper.USER_TABLE, selection, col, val);

                        if (user_id != 0) {
                            Toast.makeText(CountryPlaceOrderActivity.this, getString(R.string.str_saved), Toast.LENGTH_SHORT).show();
                            txt_del_address.setText(address);
                        }

                    } else if (signUpPojo.getStatus() == 2) {
                        Toast.makeText(CountryPlaceOrderActivity.this, signUpPojo.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        String str_check_int = getResources().getString(R.string.str_check_int);
                        Toast.makeText(CountryPlaceOrderActivity.this, str_check_int, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    String str_check_int = getResources().getString(R.string.str_check_int);
                    Toast.makeText(CountryPlaceOrderActivity.this, str_check_int, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Address> call, Throwable t) {
                hideProgressDialog();
                String str_check_int = getResources().getString(R.string.str_check_int);
                Toast.makeText(CountryPlaceOrderActivity.this, str_check_int, Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void setTotal() {

        tempOrderPojos.clear();

        tempOrderPojos = dataBaseHelper.getMyCartDetails();
        if (tempOrderPojos.size()!=0){
            PrefManager.setOrderType(context, StringConstant.CountryOrder);
        }else{
            PrefManager.setOrderType(context, "");
        }


        total_amount = 0.0;


        if (tempOrderPojos.size() > 0) {
            pharmacyid = tempOrderPojos.get(0).getTemp_pharma_id();
            Log.d(TAG, "setTotal: "+tempOrderPojos.get(0).getTemp_pharma_id());
            txt_empty_cart.setVisibility(View.GONE);
            list_place_order.setVisibility(View.VISIBLE);
            productNames = tempOrderPojos.get(0).getTemp_order_med_name();

                if(tempOrderPojos.size()<=1) {
                    total_amount = tempOrderPojos.get(0).getTemp_order_total_price();
                    productNames = tempOrderPojos.get(0).getTemp_order_med_name();
                }else{
                    for (int i = 0; i < tempOrderPojos.size(); i++) {
                    total_amount = total_amount + tempOrderPojos.get(i).getTemp_order_total_price();
                    productNames= productNames+","+tempOrderPojos.get(0).getTemp_order_med_name();
                }
            }

            //txt_order_total.setText(getString(R.string.str_total_order) + ": xaf " + String.valueOf(total_amount));
            double medAmountDisplay=total_amount;
                double commAmountDisplay= 1000;

            double totalAmount=medAmountDisplay+commAmountDisplay;
            totalAmountShow=totalAmount/656;
            String tAmtdis=getString(R.string.str_total_order)+": "+String.format("%.2f",totalAmountShow)+"€";
            Log.d(TAG, "setTotal: "+tAmtdis);
            if(tAmtdis.contains(",")){
                tAmtdis=tAmtdis.replace(",",".");
            }
            txt_order_total.setText(tAmtdis);
            String cAmtdis=commAmountDisplay+" FCFA";
            String mAmtdis=medAmountDisplay+" FCFA";
            txt_commamountshow.setText(cAmtdis);
            txt_mediamountshow.setText(mAmtdis);

        } else {
            txt_empty_cart.setVisibility(View.VISIBLE);
            list_place_order.setVisibility(View.GONE);
            //txt_order_total.setText(getString(R.string.str_total_order) + ": xaf 0");
            txt_order_total.setText(getString(R.string.str_total_order) + ": ");
            txt_commamountshow.setText("0€");
            txt_mediamountshow.setText(0+"€");
        }

        setListAdapter();
    }

    private void showAddressDialog(String address) {
        final Dialog dialog = new Dialog(CountryPlaceOrderActivity.this, R.style.CustomAlertDialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_delivery_address);
        final EditText edt_del_address = dialog.findViewById(R.id.edt_del_address);
        TextView txt_submit = dialog.findViewById(R.id.txt_submit);

        edt_del_address.setText(address);

        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                if (edt_del_address.getText().toString().trim().isEmpty()) {
                    edt_del_address.setError(getString(R.string.error_field_required));
                } else {
                    showProgressDialog();
                    updateDelAddress(edt_del_address.getText().toString().trim());
                }

            }
        });

        dialog.show();
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    //if (it) above method:checkPermissionForReadExtertalStorage(), return false then call below method, it will show a dialog for permission
    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean checkPermissionForWriteExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForWriteExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_refresh).setVisible(false);
        menu.findItem(R.id.menu_item_save).setVisible(false);
        menu.findItem(R.id.menu_item_delete).setVisible(false);
        menu.findItem(R.id.action_cart).setVisible(false);
        menu.findItem(R.id.text_message_item).setVisible(false);
        menu.findItem(R.id.photo_camera_item).setVisible(false);
        menu.findItem(R.id.call_help_item).setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
            case R.id.photo_camera_item:
                if (!checkPermissionForWriteExtertalStorage()) {
                    try {
                        requestPermissionForWriteExtertalStorage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                selectImageDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void dispatchChoosePictureIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), CHOOSE_PHOTO_FROM_GALLARY);
  }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.lisungui.pharma.provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAPTURE_PHOTO_FROM_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        //to store images directly Android directories projects folder works and put path="." in filepath_providers xml
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        //to store images directly in Pictures folder works and put path="." in filepath_providers xml
        File storageDir = new File("" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
    File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        capturedPhotoFilePath = image.getAbsolutePath();
      return image;
    }

    File imageFile = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_PHOTO_FROM_CAMERA && resultCode == RESULT_OK) {
            capturePrescription = true;
            File compressedImage = null;

            if (!capturedPhotoFilePath.equalsIgnoreCase("") && capturePrescription) {
                prescriptionPhotoImage.setVisibility(View.VISIBLE);
                txt_empty_cart.setVisibility(View.GONE);
                list_place_order.setVisibility(View.GONE);

                imageFile = new File(capturedPhotoFilePath);

                try {
                    compressedImage = new Compressor(this)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .setQuality(50)
                            .compressToFile(imageFile);
                    prescriptionPhotoImage.setImageURI(Uri.fromFile(compressedImage));
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

        }
        else if (requestCode == CHOOSE_PHOTO_FROM_GALLARY && resultCode == RESULT_OK) {
            capturePrescription = true;

            prescriptionPhotoImage.setVisibility(View.VISIBLE);
            txt_empty_cart.setVisibility(View.GONE);
            list_place_order.setVisibility(View.GONE);

            String realPath = ImageFilePath.getPath(CountryPlaceOrderActivity.this, data.getData());

            capturedPhotoFilePath = realPath;
            if (realPath.equals(nopath)) {
                Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show();
                prescriptionPhotoImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_frame));
                return;
            } else {
                imageFile = new File(realPath);
                File compressedImage = null;
                try {
                    compressedImage = new Compressor(this)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .setQuality(50)
                            .compressToFile(imageFile);
                    prescriptionPhotoImage.setImageURI(Uri.fromFile(compressedImage));

                } catch (IOException e) {
                    e.printStackTrace();
                    prescriptionPhotoImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_frame));

                }
            }
        }
        else {
            capturePrescription = false;
            selectImageDialog();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setListAdapter() {
        OrderListAdapter orderListAdapter = new OrderListAdapter(this, tempOrderPojos, this);
        list_place_order.setAdapter(orderListAdapter);
        orderListAdapter.notifyDataSetChanged();
    }

    private void init() {

        dataBaseHelper = new SQLiteDataBaseHelper(this);

        btn_address = (TextView) findViewById(R.id.btn_address);
        txt_del_address = (TextView) findViewById(R.id.txt_del_address);

        img_edit = (ImageView) findViewById(R.id.img_edit);
        rel_add = (RelativeLayout) findViewById(R.id.rel_add);

        txt_empty_cart = (TextView) findViewById(R.id.txt_empty_cart);
        list_place_order = (ListView) findViewById(R.id.list_place_order);
        txt_order_details = (TextView) findViewById(R.id.txt_order_details);
        txt_place_order = (TextView) findViewById(R.id.txt_place_order);
        txt_order_total = (TextView) findViewById(R.id.txt_order_total);
        txt_commamountshow=findViewById(R.id.txt_commamountshow);
        txt_mediamountshow=findViewById(R.id.txt_mediamountshow);
    }

    private void placeOrder() {

        final Gson converter = new Gson();
        //showProgressDialog();
        String pharma_id;
        //Log.d(TAG, "placeOrder:c pharmacyid "+pharmacyid);
        //Log.d(TAG, "placeOrder:c user_id "+user_id);
        //Log.d(TAG, "placeOrder:c txt_del_address"+txt_del_address.getText().toString());
        //Log.d(TAG, "placeOrder:c meddetails"+converter.toJson(uploadOrders));
        //Log.d(TAG, "placeOrder:c amount"+total_amount);
        //Log.d(TAG, "placeOrder:c countryid"+countryid);
        if(pharmacyid==null){
            pharma_id="123";
        }
        else {
            pharma_id=pharmacyid;
        }
        Log.d(TAG, "placeOrder:c pharmacyid "+pharma_id);

        Intent intent = new Intent(CountryPlaceOrderActivity.this, StripePaymentActivity.class);
        intent.putExtra("pharmacyId",pharmacyid);
        intent.putExtra("user_id",user_id);
        intent.putExtra("txt_del_address",txt_del_address.getText().toString());
        intent.putExtra("countryid",countryid);
        intent.putExtra("cityID",cityID);
        intent.putExtra("quantity",tempOrderPojos.size());
        intent.putExtra("uploadOrders", converter.toJson(uploadOrders));
        DecimalFormat precision = new DecimalFormat("###.00");
        Log.d(TAG, "totalAmountShow: "+precision.format(totalAmountShow));
        intent.putExtra("Ammount",precision.format(totalAmountShow));
        //intent.putExtra("Ammount",totalAmountShow);

        intent.putExtra("product_name",productNames+"");
        startActivity(intent);
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
    public void removeCart(int pos) {
        int order_id = tempOrderPojos.get(pos).getTemp_order_id();

        String selection = String.format(SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_ID + "='%s'", order_id);

        try {
            dataBaseHelper.deleteRows(SQLiteDataBaseHelper.TEMP_ORDER_TABLE, selection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTotal();
    }


}