package com.lisungui.pharma.activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lisungui.pharma.R;
import com.lisungui.pharma.models.TreatAlarmModel;

import java.util.Calendar;

public class AddTreatAlarmActivity extends AppCompatActivity {

    private static final String TAG = AddTreatAlarmActivity.class.getSimpleName();
    private Bundle bundle;
    private EditText edt_medicine_name;
    private Spinner spn_day_from;
    private Spinner spn_day_to;
    //    private LinearLayout lin_time_container;
//    private LinearLayout lin_add;
    private ImageView img_alarm;
    private EditText edt_description;
    private TextView txt_save;
    private Button btn_time;
    private boolean toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_treat_alarm);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();

//        dataBaseHelper = new SQLiteDataBaseHelper(this);

        String[] day_array = getResources().getStringArray(R.array.day_array);

        ArrayAdapter<String> dayArrayAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, day_array);
        dayArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spn_day_from.setAdapter(dayArrayAdapter);

        spn_day_to.setAdapter(dayArrayAdapter);

        try {
            bundle = getIntent().getExtras();

            if (bundle.containsKey("Treatment")) {
                TreatAlarmModel treatmentModel = bundle.getParcelable("Treatment");

                int id = treatmentModel.getId();
                String med_name = treatmentModel.getMed_name();
                String from_day = treatmentModel.getFrom_day();
                String to_day = treatmentModel.getTo_day();
                String time = treatmentModel.getTime();
//                img_source = treatmentModel.getImg_source();
                String toggle1 = treatmentModel.getToggle();
                String description = treatmentModel.getDescription();

                edt_medicine_name.setText(med_name);
                edt_description.setText(description);

                if (toggle1.equals("True")) {
                    toggle = true;
                    img_alarm.setImageResource(R.drawable.toggle_on);
                } else {
                    toggle = false;
                    img_alarm.setImageResource(R.drawable.toggle_off);
                }

                btn_time.setText(time);

                String str_update = getResources().getString(R.string.str_update);
                txt_save.setText(str_update);

                spn_day_from.setSelection(Integer.parseInt(from_day));
                spn_day_to.setSelection(Integer.parseInt(to_day));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTreatAlarmActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                btn_time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long rowID = -1;

                String med_name = edt_medicine_name.getText().toString().trim();
                String med_desc = edt_description.getText().toString().trim();
                String time = btn_time.getText().toString().trim();

                String alarm_state;
                if (toggle)
                    alarm_state = "True";
                else
                    alarm_state = "False";

                if (med_name.equals("")) {
                    String str_enter_med_name = getResources().getString(R.string.str_enter_med_name);
                    Toast.makeText(AddTreatAlarmActivity.this, str_enter_med_name, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (btn_time.getText().toString().trim().equalsIgnoreCase("")) {
                    String str_plz_sel_time = getResources().getString(R.string.str_plz_sel_time);
                    Toast.makeText(AddTreatAlarmActivity.this, str_plz_sel_time, Toast.LENGTH_SHORT).show();
                    return;
                }

                TreatAlarmModel treatmentModel = new TreatAlarmModel();
                treatmentModel.setImg_source("");
                treatmentModel.setMed_name(med_name);
                treatmentModel.setDescription(med_desc);
                treatmentModel.setTime(time);
                int pos_from = spn_day_from.getSelectedItemPosition();
//                String from = dayArray.get(pos_from);

                int pos_to = spn_day_to.getSelectedItemPosition();
//                String to = dayArray.get(pos_to);

                treatmentModel.setFrom_day(String.valueOf(pos_from));
                treatmentModel.setTo_day(String.valueOf(pos_to));
                treatmentModel.setToggle(alarm_state);

                try { //Update
                    if (bundle != null) {

                        String[] col_name = new String[]{"treat_med_name", "treat_from_day", "treat_to_day",
                                "treat_med_time", "treat_alarm", "treat_med_desc"};
                        String[] values = new String[]{med_name, String.valueOf(pos_from), String.valueOf(pos_to), time, alarm_state, med_desc};

//                        rowID = dataBaseHelper.updateAlarm(id, col_name, values);

                    } else {//Save
//                        rowID = dataBaseHelper.addTreatment(treatmentModel);
                    }

                    if (rowID != -1) {
                        String str_added_success = getResources().getString(R.string.str_added_success);
                        Toast.makeText(AddTreatAlarmActivity.this, str_added_success, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        String str_some_went_wrong = getResources().getString(R.string.str_some_went_wrong);
                        Toast.makeText(AddTreatAlarmActivity.this, str_some_went_wrong, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        img_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggle) {
                    toggle = false;
                    img_alarm.setImageResource(R.drawable.toggle_off);
                } else {
                    toggle = true;
                    img_alarm.setImageResource(R.drawable.toggle_on);
                }

            }
        });

        /*spn_day_to.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        spn_day_from.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });*/

    }

    private void init() {
        edt_medicine_name = (EditText) findViewById(R.id.edt_medicine_name);
        spn_day_from = (Spinner) findViewById(R.id.spn_day_from);
        spn_day_to = (Spinner) findViewById(R.id.spn_day_to);
//        lin_time_container = (LinearLayout) findViewById(R.id.lin_time_container);
//        lin_add = (LinearLayout) findViewById(R.id.lin_add);
        img_alarm = (ImageView) findViewById(R.id.img_alarm);
        edt_description = (EditText) findViewById(R.id.edt_description);
        txt_save = (TextView) findViewById(R.id.txt_save);

        btn_time = (Button) findViewById(R.id.btn_time);

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
