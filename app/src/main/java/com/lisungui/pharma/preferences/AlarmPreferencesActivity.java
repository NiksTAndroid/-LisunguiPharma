/* Copyright 2014 Sheldon Neilson www.neilson.co.za
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.lisungui.pharma.preferences;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import androidx.appcompat.app.AppCompatActivity;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lisungui.pharma.R;
import com.lisungui.pharma.models.Alarm;

import java.util.Calendar;

public class AlarmPreferencesActivity extends AppCompatActivity {

//    ImageButton deleteButton;
//    TextView okButton;
//    TextView cancelButton;
    private Alarm alarm;
    private MediaPlayer mediaPlayer;

    private ListAdapter listAdapter;
    private ListView listView;
    private CountDownTimer alarmToneTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.alarm_preferences);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("alarm")) {
            setMathAlarm((Alarm) bundle.getSerializable("alarm"));
        } else {
            setMathAlarm(new Alarm());
        }
        if (bundle != null && bundle.containsKey("adapter")) {
            setListAdapter((AlarmPreferenceListAdapter) bundle.getSerializable("adapter"));
        } else {
            setListAdapter(new AlarmPreferenceListAdapter(this, getMathAlarm()));
        }

        getListView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                final AlarmPreferenceListAdapter alarmPreferenceListAdapter = (AlarmPreferenceListAdapter) getListAdapter();
                final AlarmPreference alarmPreference = (AlarmPreference) alarmPreferenceListAdapter.getItem(position);

                AlertDialog.Builder alert;
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                switch (alarmPreference.getType()) {

                    case BOOLEAN:
                        CheckedTextView checkedTextView = (CheckedTextView) v;
                        boolean checked = !checkedTextView.isChecked();
                        ((CheckedTextView) v).setChecked(checked);
                        switch (alarmPreference.getKey()) {
                            case ALARM_ACTIVE:
                                alarm.setAlarmActive(checked);
                                break;
                            case ALARM_VIBRATE:
                                alarm.setVibrate(checked);
                                if (checked) {
                                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                    vibrator.vibrate(1000);
                                }
                                break;
                        }
                        alarmPreference.setValue(checked);
                        break;

                    case STRING:

                        alert = new AlertDialog.Builder(AlarmPreferencesActivity.this);

                        alert.setTitle(alarmPreference.getTitle());
                        // alert.setMessage(message);

                        // Set an EditText view to get user input
                        final EditText input = new EditText(AlarmPreferencesActivity.this);

                        input.setText(alarmPreference.getValue().toString());

                        alert.setView(input);
                        alert.setPositiveButton(getString(R.string.str_ok), new OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                alarmPreference.setValue(input.getText().toString());

                                if (alarmPreference.getKey() == AlarmPreference.Key.ALARM_NAME) {
                                    alarm.setAlarmName(alarmPreference.getValue().toString());
                                }

                                alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                                alarmPreferenceListAdapter.notifyDataSetChanged();
                            }
                        });
                        alert.show();
                        break;

                    case LIST:
                        alert = new AlertDialog.Builder(AlarmPreferencesActivity.this);

                        alert.setTitle(alarmPreference.getTitle());
                        // alert.setMessage(message);

                        CharSequence[] items = new CharSequence[alarmPreference.getOptions().length];
                        for (int i = 0; i < items.length; i++)
                            items[i] = alarmPreference.getOptions()[i];

                        alert.setItems(items, new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (alarmPreference.getKey()) {
                                    case ALARM_DIFFICULTY:
                                        Alarm.Difficulty d = Alarm.Difficulty.values()[which];
                                        alarm.setDifficulty(d);
                                        break;
                                    case ALARM_TONE:
                                        alarm.setAlarmTonePath(alarmPreferenceListAdapter.getAlarmTonePaths()[which]);
                                        if (alarm.getAlarmTonePath() != null) {
                                            if (mediaPlayer == null) {
                                                mediaPlayer = new MediaPlayer();
                                            } else {
                                                if (mediaPlayer.isPlaying())
                                                    mediaPlayer.stop();
                                                mediaPlayer.reset();
                                            }
                                            try {
                                                // mediaPlayer.setVolume(1.0f, 1.0f);
                                                mediaPlayer.setVolume(0.2f, 0.2f);
                                                mediaPlayer.setDataSource(AlarmPreferencesActivity.this, Uri.parse(alarm.getAlarmTonePath()));
                                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                                                mediaPlayer.setLooping(false);
                                                mediaPlayer.prepare();
                                                mediaPlayer.start();

                                                // Force the mediaPlayer to stop after 3
                                                // seconds...
                                                if (alarmToneTimer != null)
                                                    alarmToneTimer.cancel();
                                                alarmToneTimer = new CountDownTimer(3000, 3000) {
                                                    @Override
                                                    public void onTick(long millisUntilFinished) {

                                                    }

                                                    @Override
                                                    public void onFinish() {
                                                        try {
                                                            if (mediaPlayer.isPlaying())
                                                                mediaPlayer.stop();
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                };
                                                alarmToneTimer.start();
                                            } catch (Exception e) {
                                                try {
                                                    if (mediaPlayer.isPlaying())
                                                        mediaPlayer.stop();
                                                } catch (Exception e2) {
                                                    e2.printStackTrace();
                                                }
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                                alarmPreferenceListAdapter.notifyDataSetChanged();
                            }

                        });

                        alert.show();
                        break;

                    case MULTIPLE_LIST:
                        alert = new AlertDialog.Builder(AlarmPreferencesActivity.this);

                        alert.setTitle(alarmPreference.getTitle());
                        // alert.setMessage(message);

                        CharSequence[] multiListItems = new CharSequence[alarmPreference.getOptions().length];
                        for (int i = 0; i < multiListItems.length; i++)
                            multiListItems[i] = alarmPreference.getOptions()[i];

                        boolean[] checkedItems = new boolean[multiListItems.length];
                        for (Alarm.Day day : getMathAlarm().getDays()) {
                            checkedItems[day.ordinal()] = true;
                        }
                        alert.setMultiChoiceItems(multiListItems, checkedItems, new OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(final DialogInterface dialog, int which, boolean isChecked) {

                                Alarm.Day thisDay = Alarm.Day.values()[which];

                                if (isChecked) {
                                    alarm.addDay(thisDay);
                                } else {
                                    // Only remove the day if there are more than 1
                                    // selected
                                    if (alarm.getDays().length > 1) {
                                        alarm.removeDay(thisDay);
                                    } else {
                                        // If the last day was unchecked, re-check
                                        // it
                                        ((AlertDialog) dialog).getListView().setItemChecked(which, true);
                                    }
                                }

                            }
                        });
                        alert.setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                                alarmPreferenceListAdapter.notifyDataSetChanged();

                            }
                        });
                        alert.show();
                        break;

                    case TIME:
                        TimePickerDialog timePickerDialog = new TimePickerDialog(AlarmPreferencesActivity.this, new OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                                Calendar newAlarmTime = Calendar.getInstance();
                                newAlarmTime.set(Calendar.HOUR_OF_DAY, hours);
                                newAlarmTime.set(Calendar.MINUTE, minutes);
                                newAlarmTime.set(Calendar.SECOND, 0);
                                alarm.setAlarmTime(newAlarmTime);
                                alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                                alarmPreferenceListAdapter.notifyDataSetChanged();
                            }
                        }, alarm.getAlarmTime().get(Calendar.HOUR_OF_DAY), alarm.getAlarmTime().get(Calendar.MINUTE), true);
                        timePickerDialog.setTitle(alarmPreference.getTitle());
                        timePickerDialog.show();

                    default:
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm_menu, menu);//Menu Resource, Menu
        if(getMathAlarm().getId() < 1)
            menu.findItem(R.id.menu_item_delete).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Intent intent1 = new Intent();
                setResult(5, intent1);
                finish();
                break;

            case R.id.menu_item_save:
//                Database.init(getApplicationContext());
                if (getMathAlarm().getId() < 1) {
//                    Database.create(getMathAlarm());
                    Intent intent = new Intent();
                    intent.putExtra("alarm_set", getMathAlarm());
                    setResult(2, intent);
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("alarm_set", getMathAlarm());
                    setResult(3, intent);
                    finish();
//                    Database.update(getMathAlarm());
                }
//                callMathAlarmScheduleService();
                Toast.makeText(AlarmPreferencesActivity.this, getMathAlarm().getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();
                finish();
                break;

            case R.id.menu_item_delete:
                AlertDialog.Builder dialog = new AlertDialog.Builder(AlarmPreferencesActivity.this);
                dialog.setTitle(getString(R.string.str_delete));
                dialog.setMessage(getString(R.string.str_delete_alarm));
                dialog.setPositiveButton(getString(R.string.str_ok), new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        Database.init(getApplicationContext());
                        if (getMathAlarm().getId() < 1) {
                            // Alarm not saved
                        } else {
//                            Database.deleteEntry(alarm);
//                            callMathAlarmScheduleService();
                            Intent intent = new Intent();
                            intent.putExtra("alarm_set", getMathAlarm());
                            setResult(4, intent);
                            finish();
                        }
                        finish();
                    }
                });
                dialog.setNegativeButton(getString(R.string.str_cancel), new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("alarm", getMathAlarm());
        outState.putSerializable("adapter", (AlarmPreferenceListAdapter) getListAdapter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mediaPlayer != null)
                mediaPlayer.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // setListAdapter(null);
    }

    private Alarm getMathAlarm() {
        return alarm;
    }

    private void setMathAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

    private ListAdapter getListAdapter() {
        return listAdapter;
    }

    private void setListAdapter(ListAdapter listAdapter) {
        this.listAdapter = listAdapter;
        getListView().setAdapter(listAdapter);
    }

    private ListView getListView() {
        if (listView == null)
            listView = (ListView) findViewById(android.R.id.list);
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    /*@Override
    public void onClick(View v) {
        // super.onClick(v);
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent();
        setResult(5, intent1);
        finish();
    }
}
