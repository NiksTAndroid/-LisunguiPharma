package com.lisungui.pharma.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.adapter.MonitoringAdapter;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.TreatmentModel;

import java.util.ArrayList;
import java.util.Locale;

public class MonitoringActivity extends AppCompatActivity {

    private static final String TAG = MonitoringActivity.class.getSimpleName();

    private ListView list_treat;
    private ImageButton btn_add;
    private SQLiteDataBaseHelper dataBaseHelper;
    private ArrayList<TreatmentModel> treatmentModels = new ArrayList<>();
    private TextView txt_title;
    private Spanned titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_treatment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();

        if(Locale.getDefault().getLanguage().equals("en")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                titles = Html.fromHtml("<font color=\"#000000\">" + "Treatment " + "</font>" + "<font color=\"#729619\">" + "List" + "</font>", Html.FROM_HTML_MODE_LEGACY);
            } else {
                titles = (Html.fromHtml("<font color=\"#000000\">" + "Treatment " + "</font>" + "<font color=\"#729619\">" + "List" + "</font>"));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                titles = Html.fromHtml("<font color=\"#000000\">" + getString(R.string.str_treat_list1)+" " + "</font>" + "<font color=\"#729619\">" + getString(R.string.str_treat_list2) + "</font>", Html.FROM_HTML_MODE_LEGACY);
            } else {
                titles = (Html.fromHtml("<font color=\"#000000\">" + getString(R.string.str_treat_list1)+" " + "</font>" + "<font color=\"#729619\">" + getString(R.string.str_treat_list2) + "</font>"));
            }
        }


        txt_title.setText(titles);

        dataBaseHelper = new SQLiteDataBaseHelper(this);

        /*list_treat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TreatmentModel treatmentModel = treatmentModels.get(position);
                Log.d(TAG, "Alarm List Size : "+treatmentModels.get(position).getArrayList().size());
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("Treatment", treatmentModel);

                Intent intent = new Intent(MonitoringActivity.this, AddTreatAlarmActivity1.class);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });*/

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MonitoringActivity.this, AddTreatAlarmActivity1.class);
                startActivity(intent);
            }
        });

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

    private void setListAdapter() {

        treatmentModels.clear();
        treatmentModels = dataBaseHelper.getAllTreatAlarm("");
        dataBaseHelper.closeDB();

        MonitoringAdapter adapter = new MonitoringAdapter(this, treatmentModels);
        list_treat.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListAdapter();
    }

    private void init() {

        txt_title = (TextView) findViewById(R.id.txt_title);

        list_treat = (ListView) findViewById(R.id.list_treat);
        btn_add = (ImageButton) findViewById(R.id.btn_add);
    }
}