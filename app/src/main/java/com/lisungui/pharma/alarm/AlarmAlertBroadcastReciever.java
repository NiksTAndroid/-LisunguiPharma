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
package com.lisungui.pharma.alarm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import com.lisungui.pharma.activities.AddTreatAlarmActivity;
import com.lisungui.pharma.activities.AddTreatAlarmActivity1;
import com.lisungui.pharma.activities.MonitoringActivity;
import com.lisungui.pharma.models.Alarm;

import java.util.Timer;
import java.util.TimerTask;

public class AlarmAlertBroadcastReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mathAlarmServiceIntent = new Intent(
                context,
                AlarmServiceBroadcastReciever.class);
        context.sendBroadcast(mathAlarmServiceIntent, null);
        StaticWakeLock.lockOn(context);
        Bundle bundle = intent.getExtras();

        final String  alarm = bundle.getString("alarmID");
        Intent mathAlarmAlertActivityIntent = new Intent(context, AddTreatAlarmActivity1.class);
        mathAlarmAlertActivityIntent.putExtra("alarm", alarm);
        mathAlarmAlertActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ///

      /*  Uri alarmUri =null;

        if (alarm.getAlarmTonePath()!=null && alarm.getAlarmTonePath().isEmpty()){
            alarmUri = Uri.parse(alarm.getAlarmTonePath());
        }
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        final Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        Timer timer =new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ringtone.stop();
            }
        },5000);*/
        ///
        context.startActivity(mathAlarmAlertActivityIntent);
    }

}