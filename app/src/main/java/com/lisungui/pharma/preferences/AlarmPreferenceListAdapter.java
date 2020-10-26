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

import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.models.Alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class AlarmPreferenceListAdapter extends BaseAdapter implements Serializable {
    private static final String TAG = AlarmPreferenceListAdapter.class.getSimpleName();

    /*private final String[] repeatDays = {
                                            getContext().getString(R.string.str_sunday),
                                            getContext().getString(R.string.str_monday),
                                            getContext().getString(R.string.str_tuesday),
                                            getContext().getString(R.string.str_wednesday),
                                            getContext().getString(R.string.str_thursday),
                                            getContext().getString(R.string.str_friday),
                                            getContext().getString(R.string.str_saturday)
                                        };*/
    private final String[] repeatDays;

    /*private final String[] alarmDifficulties = {
                                                    getContext().getString(R.string.str_easy),
                                                    getContext().getString(R.string.str_medium),
                                                    getContext().getString(R.string.str_difi)
                                                };*/

    private final String[] alarmDifficulties;

    private Context context;
    private Alarm alarm;
    private List<AlarmPreference> preferences = new ArrayList<AlarmPreference>();
    private String[] alarmTones;
    private String[] alarmTonePaths;

    public AlarmPreferenceListAdapter(Context context, Alarm alarm) {
        setContext(context);

        alarmDifficulties = new String[] {
                context.getString(R.string.str_easy),
                context.getString(R.string.str_medium),
                context.getString(R.string.str_difi)
        };

        repeatDays = new String[]{
                context.getString(R.string.str_sunday),
                context.getString(R.string.str_monday),
                context.getString(R.string.str_tuesday),
                context.getString(R.string.str_wednesday),
                context.getString(R.string.str_thursday),
                context.getString(R.string.str_friday),
                context.getString(R.string.str_saturday)
        };

//		(new Runnable(){
//
//			@Override
//			public void run() {
        Log.d("AlarmPreferenceList", "Loading Ringtones...");

        RingtoneManager ringtoneMgr = new RingtoneManager(getContext());

        ringtoneMgr.setType(RingtoneManager.TYPE_ALARM);

        Cursor alarmsCursor = ringtoneMgr.getCursor();

        alarmTones = new String[alarmsCursor.getCount() + 1];
        alarmTones[0] = context.getString(R.string.str_silent);
        alarmTonePaths = new String[alarmsCursor.getCount() + 1];
        alarmTonePaths[0] = "";

        if (alarmsCursor.moveToFirst()) {
            do {
                alarmTones[alarmsCursor.getPosition() + 1] = ringtoneMgr.getRingtone(alarmsCursor.getPosition()).getTitle(getContext());
                alarmTonePaths[alarmsCursor.getPosition() + 1] = ringtoneMgr.getRingtoneUri(alarmsCursor.getPosition()).toString();
            } while (alarmsCursor.moveToNext());
        }
        Log.d("AlarmPreferenceList", "Finished Loading " + alarmTones.length + " Ringtones.");
        alarmsCursor.close();
//
//			}
//
//		}).run();
//
        setMathAlarm(alarm);
    }

    @Override
    public int getCount() {
        return preferences.size();
    }

    @Override
    public Object getItem(int position) {
        return preferences.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlarmPreference alarmPreference = (AlarmPreference) getItem(position);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        Log.d(TAG, "Type : "+alarmPreference.getType());

        switch (alarmPreference.getType()) {
            case BOOLEAN:
                if (null == convertView || convertView.getId() != android.R.layout.simple_list_item_checked)
                    convertView = layoutInflater.inflate(android.R.layout.simple_list_item_checked, null);

                CheckedTextView checkedTextView = convertView.findViewById(android.R.id.text1);
                checkedTextView.setText(alarmPreference.getTitle());
                checkedTextView.setChecked((Boolean) alarmPreference.getValue());
                break;
            case LIST:
                if (null == convertView || convertView.getId() != android.R.layout.simple_list_item_2)
                    convertView = layoutInflater.inflate(android.R.layout.simple_list_item_2, null);

                TextView text3 = convertView.findViewById(android.R.id.text1);
                text3.setTextSize(18);
                text3.setText(alarmPreference.getTitle());

                TextView text4 = convertView.findViewById(android.R.id.text2);
                Log.d(TAG, "155 Summary : "+alarmPreference.getSummary());
                if(alarmPreference.getSummary().equalsIgnoreCase("Easy")
                        ||alarmPreference.getSummary().equalsIgnoreCase("Medium")
                        ||alarmPreference.getSummary().equalsIgnoreCase("Difficult")) {
                    text4.setText(getDifficulty(alarmPreference.getSummary()));
                } else {
                    text4.setText(alarmPreference.getSummary());
                }

                break;

            case MULTIPLE_LIST:
                if (null == convertView || convertView.getId() != android.R.layout.simple_list_item_2)
                    convertView = layoutInflater.inflate(android.R.layout.simple_list_item_2, null);

                TextView text5 = convertView.findViewById(android.R.id.text1);
                text5.setTextSize(18);
                text5.setText(alarmPreference.getTitle());

                TextView text6 = convertView.findViewById(android.R.id.text2);
                Log.d(TAG, "174 Summary : "+alarmPreference.getSummary());

                String[] token = alarmPreference.getSummary().split(",");


                if(token.length == 1) {
                   if(token[0].equalsIgnoreCase("Every Day")) {
                       text6.setText(context.getString(R.string.str_everyday));
                   } else {
                       text6.setText(getDay(token[0]));
                   }

                } else {

                    StringBuilder builder = new StringBuilder();

                    for (int j = 0; j < token.length; j++) {
                        builder.append(getDay(token[j]));
                        builder.append(",");
                    }

                    builder.setLength(builder.length() - 1);
                    text6.setText(builder.toString());
                }

                /*if(alarmPreference.getSummary().equalsIgnoreCase(context.getString(R.string.str_mon))
                        ||alarmPreference.getSummary().equalsIgnoreCase(context.getString(R.string.str_tue))
                        ||alarmPreference.getSummary().equalsIgnoreCase("Wed")
                        ||alarmPreference.getSummary().equalsIgnoreCase("Thu")
                        ||alarmPreference.getSummary().equalsIgnoreCase("Fri")
                        ||alarmPreference.getSummary().equalsIgnoreCase("Sat")
                        ||alarmPreference.getSummary().equalsIgnoreCase(context.getString(R.string.str_sun))) {
                    text6.setText(getDay(alarmPreference.getSummary()));
                } else {
                    text6.setText(alarmPreference.getSummary());
                }*/
                break;
            case INTEGER:
            case STRING:
            case TIME:
            default:
                if (null == convertView || convertView.getId() != android.R.layout.simple_list_item_2)
                    convertView = layoutInflater.inflate(android.R.layout.simple_list_item_2, null);

                TextView text1 = convertView.findViewById(android.R.id.text1);
                text1.setTextSize(18);
                text1.setText(alarmPreference.getTitle());

                TextView text2 = convertView.findViewById(android.R.id.text2);
                text2.setText(alarmPreference.getSummary());
                break;
        }

        return convertView;
    }

    /*public Alarm getMathAlarm() {
        for (AlarmPreference preference : preferences) {
            switch (preference.getKey()) {
                case ALARM_ACTIVE:
                    alarm.setAlarmActive((Boolean) preference.getValue());
                    break;
                case ALARM_NAME:
                    alarm.setAlarmName((String) preference.getValue());
                    break;
                case ALARM_TIME:
                    alarm.setAlarmTime((String) preference.getValue());
                    break;
                case ALARM_DIFFICULTY:
                    alarm.setDifficulty(Alarm.Difficulty.valueOf((String) preference.getValue()));
                    break;
                case ALARM_TONE:
                    alarm.setAlarmTonePath((String) preference.getValue());
                    break;
                case ALARM_VIBRATE:
                    alarm.setVibrate((Boolean) preference.getValue());
                    break;
                case ALARM_REPEAT:
                    alarm.setDays((Alarm.Day[]) preference.getValue());
                    break;
            }
        }

        return alarm;
    }*/

    public void setMathAlarm(Alarm alarm) {
        this.alarm = alarm;
        preferences.clear();
        preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_ACTIVE, context.getString(R.string.str_active), null, null, alarm.getAlarmActive(), AlarmPreference.Type.BOOLEAN));
        preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_NAME, context.getString(R.string.str_label), alarm.getAlarmName(), null, alarm.getAlarmName(), AlarmPreference.Type.STRING));
        preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_TIME, context.getString(R.string.str_set_time), alarm.getAlarmTimeString(), null, alarm.getAlarmTime(), AlarmPreference.Type.TIME));
        preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_REPEAT, context.getString(R.string.str_repeat), alarm.getRepeatDaysString(), repeatDays, alarm.getDays(), AlarmPreference.Type.MULTIPLE_LIST));
        preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_DIFFICULTY, context.getString(R.string.str_difi2), alarm.getDifficulty().toString(), alarmDifficulties, alarm.getDifficulty(), AlarmPreference.Type.LIST));

        Uri alarmToneUri = Uri.parse(alarm.getAlarmTonePath());
        Ringtone alarmTone = RingtoneManager.getRingtone(getContext(), alarmToneUri);

        if (alarmTone instanceof Ringtone && !alarm.getAlarmTonePath().equalsIgnoreCase("")) {
            preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_TONE, context.getString(R.string.str_ringtone), alarmTone.getTitle(getContext()), alarmTones, alarm.getAlarmTonePath(), AlarmPreference.Type.LIST));
        } else {
            preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_TONE, context.getString(R.string.str_ringtone), getAlarmTones()[0], alarmTones, null, AlarmPreference.Type.LIST));
        }
        preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_VIBRATE, context.getString(R.string.str_vibrate), null, null, alarm.getVibrate(), AlarmPreference.Type.BOOLEAN));
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String[] getRepeatDays() {
        return repeatDays;
    }

    public String[] getAlarmDifficulties() {
        return alarmDifficulties;
    }

    public String[] getAlarmTones() {
        return alarmTones;
    }

    public String[] getAlarmTonePaths() {
        return alarmTonePaths;
    }

    private String getDifficulty(String difficulty) {

        switch (difficulty) {

            case "Easy" :
                difficulty = context.getString(R.string.str_easy);
                break;

            case "Medium" :
                difficulty = context.getString(R.string.str_medium);
                break;

            case "Difficult" :
                difficulty = context.getString(R.string.str_difficult);
                break;

            default:
                difficulty = context.getString(R.string.str_easy);
                break;

        }

        return difficulty;
    }

    private String getDay(String day) {

        switch (day) {

            case "Sun" :
                day = context.getString(R.string.str_sun);
                break;

            case "Mon" :
                day = context.getString(R.string.str_mon);
                break;

            case "Tue" :
                day = context.getString(R.string.str_tue);
                break;

            case "Wed" :
                day = context.getString(R.string.str_wed);
                break;

            case "Thu" :
                day = context.getString(R.string.str_thu);
                break;

            case "Fri" :
                day = context.getString(R.string.str_fri);
                break;

            case "Sat" :
                day = context.getString(R.string.str_sat);
                break;

            default:
                day = context.getString(R.string.str_mon);
                break;

        }

        return day;
    }

}
