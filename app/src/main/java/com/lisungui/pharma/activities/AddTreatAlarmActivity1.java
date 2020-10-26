package com.lisungui.pharma.activities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.lisungui.pharma.R;
import com.lisungui.pharma.adapter.AlarmAdapter;
import com.lisungui.pharma.alarm.AlarmServiceBroadcastReciever;
import com.lisungui.pharma.alarm.StaticWakeLock;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.Alarm;
import com.lisungui.pharma.models.TreatmentModel;
import com.lisungui.pharma.preferences.AlarmPreferencesActivity;

import java.util.ArrayList;

public class AddTreatAlarmActivity1 extends AppCompatActivity {

    private static final String TAG = AddTreatAlarmActivity1.class.getSimpleName();
    private FloatingActionButton fab;
    private TreatmentModel treatmentModel;
    private Bundle bundle;
    private SQLiteDataBaseHelper dataBaseHelper;
    private AlarmAdapter alarmAdapter;
    private EditText edt_med_name;
    private EditText edt_desc;
    private ListView list_medicine;
    private int treat_id = -1;
    //    private String img_source;
    private ArrayList<Alarm> alarmArrayList = new ArrayList<>();

    private Vibrator vibrator;
    private Alarm alarm;
    private boolean alarmActive;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_treat_alarm1);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();

        dataBaseHelper = new SQLiteDataBaseHelper(this);

        setAlarmAdapter();

        try {
            bundle = getIntent().getExtras();

            if (bundle != null) {
                String med_name;
                String description;
                if (bundle.containsKey("Treatment")) {
                    treatmentModel = (TreatmentModel) bundle.getSerializable("Treatment");

                    treat_id = treatmentModel.getId();
                    med_name = treatmentModel.getMed_name();
//                    img_source = treatmentModel.getImg_source();
                    description = treatmentModel.getDescription();
                    alarmArrayList = treatmentModel.getArrayList();
                    edt_med_name.setText(med_name);
                    edt_desc.setText(description);

                } else if (bundle.containsKey("alarm")) {

                    ArrayList<Alarm> alarms = dataBaseHelper.getAll("");
                    String alm = bundle.getString("alarm");

                    for (Alarm alarm : alarms) {
                        String is = "" + alarm.getTreat_id();
                        if (is.equals(alm)) {
                            this.alarm = alarm;
                        }
                    }
                    String selection = " WHERE " + alarm.getTreat_id();

                    ArrayList<TreatmentModel> treatmentModels = dataBaseHelper.getAllTreatAlarm(selection);
                    treatmentModel = treatmentModels.get(0);
                    treat_id = treatmentModel.getId();
                    med_name = treatmentModel.getMed_name();
//                    img_source = treatmentModel.getImg_source();
                    description = treatmentModel.getDescription();
                    alarmArrayList = treatmentModel.getArrayList();
                    Log.d(TAG, "Hrishi79 alarm size : " + alarmArrayList.size());
                    edt_med_name.setText(med_name);
                    edt_desc.setText(description);

                    Toast.makeText(this, getString(R.string.str_alarm_time), Toast.LENGTH_SHORT).show();

                    TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

                    PhoneStateListener phoneStateListener = new PhoneStateListener() {
                        @Override
                        public void onCallStateChanged(int state, String incomingNumber) {
                            switch (state) {
                                case TelephonyManager.CALL_STATE_RINGING:
                                    Log.d(getClass().getSimpleName(), "Incoming call: "
                                            + incomingNumber);
                                    try {
                                        mediaPlayer.pause();
                                    } catch (IllegalStateException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case TelephonyManager.CALL_STATE_IDLE:
                                    Log.d(getClass().getSimpleName(), "Call State Idle");
                                    try {
                                        mediaPlayer.start();
                                    } catch (IllegalStateException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                            super.onCallStateChanged(state, incomingNumber);
                        }
                    };

                    telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

                    startAlarm();
                }
            } else Toast.makeText(this, "Bundle Empty", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Exception" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddTreatAlarmActivity1.this, AlarmPreferencesActivity.class);
//                intent.putExtra("alarm", ala)
                startActivityForResult(intent, 2);
            }
        });

        list_medicine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AddTreatAlarmActivity1.this, AlarmPreferencesActivity.class);
                intent.putExtra("alarm", alarmArrayList.get(i));
                startActivityForResult(intent, 3);
            }
        });

        setAlarmAdapter();
    }

    private void startAlarm() {

        if (alarm.getAlarmTonePath() != "") {
            mediaPlayer = new MediaPlayer();
            if (alarm.getVibrate()) {
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                long[] pattern = {1000, 200, 200, 200};
                vibrator.vibrate(pattern, 0);
            }
            try {
                mediaPlayer.setVolume(1.0f, 1.0f);
                mediaPlayer.setDataSource(this,
                        Uri.parse(alarm.getAlarmTonePath()));
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.start();

            } catch (Exception e) {
                mediaPlayer.release();
                alarmActive = false;
            }
        }

    }

    @Override
    public void onBackPressed() {

        if (bundle != null && bundle.containsKey("alarm")) {
            if (!alarmActive)
                super.onBackPressed();

            alarmActive = false;
            if (vibrator != null)
                vibrator.cancel();
            try {
                mediaPlayer.stop();
            } catch (IllegalStateException ise) {
                ise.printStackTrace();
            }
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.finish();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bundle != null && bundle.containsKey("alarm"))
            StaticWakeLock.lockOff(this);
    }

    @Override
    protected void onDestroy() {

        if (bundle != null && bundle.containsKey("alarm")) {
            try {
                if (vibrator != null)
                    vibrator.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mediaPlayer.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bundle != null && bundle.containsKey("alarm"))
            alarmActive = true;
    }

    private void setAlarmAdapter() {
        alarmAdapter = new AlarmAdapter(this, alarmArrayList);
        list_medicine.setAdapter(alarmAdapter);
        alarmAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2 || resultCode == 3 || resultCode == 4) {
            if (requestCode == 2) {          //new
                Alarm alarm = (Alarm) data.getExtras().getSerializable("alarm_set");
                Log.d(TAG, "120 alarm : " + alarm.getAlarmName());
                if (alarm != null)
                    alarmArrayList.add(alarm);

                setAlarmAdapter();
            } else if (requestCode == 3) {   //update
                Alarm alarm = (Alarm) data.getExtras().getSerializable("alarm_set");
                int id = alarm.getId();

                int pos = -1;
                for (int i = 0; i < alarmArrayList.size(); i++) {
                    if (id == alarmArrayList.get(i).getId())
                        pos = i;
                }

                if (pos != -1) {
                    alarmArrayList.set(pos, alarm);
                }

                setAlarmAdapter();
            } else if (requestCode == 4) {       //delete
                Alarm alarm = (Alarm) data.getExtras().getSerializable("alarm_set");
                int id = alarm.getId();

                int pos = -1;
                for (int i = 0; i < alarmArrayList.size(); i++) {
                    if (id == alarmArrayList.get(i).getId())
                        pos = i;
                }

                if (pos != -1) {
                    alarmArrayList.remove(pos);
                }

                setAlarmAdapter();
            }
        }
    }

    private void init() {
        edt_med_name = (EditText) findViewById(R.id.edt_med_name);
        edt_desc = (EditText) findViewById(R.id.edt_desc);
        list_medicine = (ListView) findViewById(R.id.list_medicine);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm_menu, menu);//Menu Resource, Menu
        if (treat_id == -1)
            menu.findItem(R.id.menu_item_delete).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

            case R.id.menu_item_save:

                try {
                    setAlarm();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.menu_item_delete:
                try {
                    dataBaseHelper.deleteTreatAlarm(treat_id);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAlarm() {

        String med_name = edt_med_name.getText().toString().trim();
        String med_desc = edt_desc.getText().toString().trim();

        if (med_name.isEmpty()) {
            Toast.makeText(this, getString(R.string.str_enter_med), Toast.LENGTH_SHORT).show();
            return;
        }

        if (med_desc.isEmpty()) {
            Toast.makeText(this, getString(R.string.str_enter_med_desc), Toast.LENGTH_SHORT).show();
            return;
        }

        TreatmentModel treatmentModel1 = new TreatmentModel();
        if (treat_id == -1)
            treatmentModel1.setId(-1);
        else
            treatmentModel1.setId(treat_id);

        treatmentModel1.setMed_name(med_name);
        treatmentModel1.setDescription(med_desc);
        treatmentModel1.setImg_source("");
        treatmentModel1.setArrayList(alarmArrayList);
        Log.d(TAG, "299 alarmArrayList size " + alarmArrayList.size());

        try {

            long rowId = dataBaseHelper.addTreatAlarm(treatmentModel1);
            if (rowId == -1) {
                Toast.makeText(this, getString(R.string.str_try_again), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.str_alarm_set), Toast.LENGTH_SHORT).show();
                Intent mathAlarmServiceIntent = new Intent(this, AlarmServiceBroadcastReciever.class);
                sendBroadcast(mathAlarmServiceIntent, null);
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}