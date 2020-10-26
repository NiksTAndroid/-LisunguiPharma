package com.lisungui.pharma.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lisungui.pharma.R;
import com.lisungui.pharma.adapter.MedPromNotificationAdapter;
import com.lisungui.pharma.adapter.NotificationAdapter;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.MedNotificationData;
import com.lisungui.pharma.models.MedNotificationDetails;
import com.lisungui.pharma.models.Notification;
import com.lisungui.pharma.models.PromotionNotificationDetails;
import com.lisungui.pharma.rest.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromotionActivity extends AppCompatActivity {

    private Context context;
    private String user_id;
    private SharedPreferences pref;

    private ProgressDialog pDialog;

    private TextView txt_empty;
    private ListView list_pharma;
    private TextView txt_title;
    private NotificationAdapter adapter;
    private ArrayList<Notification> notifications = new ArrayList<>();
    private SQLiteDataBaseHelper dataBaseHelper;
    private Spanned titles;
    private List<PromotionNotificationDetails> promotionList;

    private List<MedNotificationDetails> medNotificationDetailsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.setTitle("Promotion");

        context = PromotionActivity.this;

        init();

        pref = getSharedPreferences("Pharmacy", 0);
        user_id = pref.getString("USER_ID", "");

        if(Locale.getDefault().getLanguage().equals("en")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                titles = Html.fromHtml("<font color=\"#000000\">" + " Promotion " + "</font>" + "<font color=\"#729619\">" + "List" + "</font>", Html.FROM_HTML_MODE_LEGACY);
            } else {
                titles = (Html.fromHtml("<font color=\"#000000\">" + " Promotion " + "</font>" + "<font color=\"#729619\">" + "List" + "</font>"));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                titles = Html.fromHtml("<font color=\"#000000\">" + getString(R.string.promotion_list)+" " + "</font>" + "<font color=\"#729619\">" + getString(R.string.str_not_list2) + "</font>", Html.FROM_HTML_MODE_LEGACY);
            } else {
                titles = (Html.fromHtml("<font color=\"#000000\">" + getString(R.string.promotion_list) +" "+ "</font>" + "<font color=\"#729619\">" + getString(R.string.str_not_list2) + "</font>"));
            }
        }

        txt_title.setText(titles);

        //promotionList = new ArrayList<>(Arrays.asList("Promotion medicine 1", "Promotion medicine 2", "Promotion medicine 3", "Promotion medicine 4", "Promotion medicine 5", "Promotion medicine 6", "Promotion medicine 7", "Promotion medicine 8"));
//        promotionList = new ArrayList<>();
//        PromotionListAdapter promotionListAdapter = new PromotionListAdapter(context, promotionList);
//        list_pharma.setAdapter(promotionListAdapter);
//        promotionListAdapter.notifyDataSetChanged();

        promotionList = new ArrayList<>();

        medNotificationDetailsList = new ArrayList<>();

        pDialog = new ProgressDialog(context);
        pDialog.setMessage(getString(R.string.str_loading));

        //pDialog.setCancelable(false);
        showProgressDialog();
        //display promotion notification list
        //displayPromotionNotifications();
        displayMedPromotionNotifications();

        list_pharma.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PromotionActivity.this, PromotionDetailsActivity.class);
//                intent.putExtra("promotionId", promotionList.get(position).getPromNotiId());
//                intent.putExtra("promotionDate", promotionList.get(position).getPromNotiDate());
//                intent.putExtra("promotionMsg", promotionList.get(position).getPromNotiMsg());
//                intent.putExtra("promotionImg", promotionList.get(position).getPromNotiImg());

                intent.putExtra("medPromotionId", medNotificationDetailsList.get(position).getNotifiId());
                intent.putExtra("medPromotionDate", medNotificationDetailsList.get(position).getNotifiDate());
                intent.putExtra("medPromotionMsg", medNotificationDetailsList.get(position).getNotifiMessage());
                intent.putExtra("medPromotionImg", medNotificationDetailsList.get(position).getNotifiImg());

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                //return true;
                break;
//            default:
//                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setListAdapter() {
        adapter = new NotificationAdapter(PromotionActivity.this, notifications);
        list_pharma.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void init() {
        txt_empty = (TextView)findViewById(R.id.txt_empty);
        list_pharma = (ListView)findViewById(R.id.list_pharma);
        txt_title = (TextView)findViewById(R.id.txt_title);
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
    public void onBackPressed() {
        //super.onBackPressed();
        this.finish();
    }

    private void displayMedPromotionNotifications()
    {
        RestClient.RestApiInterface service = RestClient.getClient();
        Call<MedNotificationData> call = service.getMedPromotionNotifications(user_id);
        call.enqueue(new Callback<MedNotificationData>() {
            @Override
            public void onResponse(Call<MedNotificationData> call, Response<MedNotificationData> response) {
                hideProgressDialog();
                if(response.isSuccessful())
                {
                    if(response.body().getStatus() == 1) {
                        medNotificationDetailsList = response.body().getNotification();

                        MedPromNotificationAdapter medPromNotificationAdapter = null;

                        if (!medNotificationDetailsList.isEmpty()) {
                            medPromNotificationAdapter = new MedPromNotificationAdapter(context, medNotificationDetailsList);
                            list_pharma.setAdapter(medPromNotificationAdapter);
                            medPromNotificationAdapter.notifyDataSetChanged();
                        }
                    }
                    else if(response.body().getStatus() == 0)
                    {
                        String str_check_int = getResources().getString(R.string.data_not_available);
                        Toast.makeText(context, str_check_int, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MedNotificationData> call, Throwable t) {
                hideProgressDialog();
                //Log.d(TAG, "onFailure");
                String str_check_int = getResources().getString(R.string.str_check_int);
                Toast.makeText(context, str_check_int, Toast.LENGTH_SHORT).show();
            }
        });
    }

/*
    private void displayPromotionNotifications()
    {
        RestClient.RestApiInterface service = RestClient.getClient();
        Call<PromotionNotificationData> call = service.getPromotionNotifications();
        call.enqueue(new Callback<PromotionNotificationData>() {
            @Override
            public void onResponse(Call<PromotionNotificationData> call, Response<PromotionNotificationData> response) {
                hideProgressDialog();
                if(response.isSuccessful())
                {
                    if(response.body().getStatus() == 1) {
                        promotionList = response.body().getPharmNotification();
                        PromotionListAdapter promotionListAdapter = null;
                        if (!promotionList.isEmpty()) {
                            promotionListAdapter = new PromotionListAdapter(context, promotionList);
                            list_pharma.setAdapter(promotionListAdapter);
                            promotionListAdapter.notifyDataSetChanged();
                        }
                    }
                    else if(response.body().getStatus() == 0)
                    {
                        String str_check_int = getResources().getString(R.string.data_not_available);
                        Toast.makeText(context, str_check_int, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PromotionNotificationData> call, Throwable t) {
                hideProgressDialog();
                //Log.d(TAG, "onFailure");
                String str_check_int = getResources().getString(R.string.str_check_int);
                Toast.makeText(context, str_check_int, Toast.LENGTH_SHORT).show();
            }
        });
    }
*/

}
