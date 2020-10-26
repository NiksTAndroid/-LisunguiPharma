package com.lisungui.pharma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.models.Alarm;

import java.util.ArrayList;

/**
 * Created by siddeshwar on 5/1/17.
 */
public class AlarmAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Alarm> alarmArrayList;

    public AlarmAdapter(Context context, ArrayList<Alarm> alarmArrayList) {
        this.context = context;
        this.alarmArrayList = alarmArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(alarmArrayList != null)
            return alarmArrayList.size();

        return 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    static class ViewHolder {
        TextView txt_time, txt_day;
        ImageView img_alarm;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;

        if(view == null) {
            view = inflater.inflate(R.layout.row_alarm_list, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.txt_time = view.findViewById(R.id.txt_time);
            viewHolder.txt_day = view.findViewById(R.id.txt_day);
            viewHolder.img_alarm = view.findViewById(R.id.img_alarm);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.txt_time.setText(""+alarmArrayList.get(position).getAlarmTimeString());

        if(alarmArrayList.get(position).getRepeatDaysString().equalsIgnoreCase("Every Day")) {
            viewHolder.txt_day.setText(context.getString(R.string.str_everyday));
        } else {
//            viewHolder.txt_day.setText(""+alarmArrayList.get(position).getRepeatDaysString());

            String []str = alarmArrayList.get(position).getRepeatDaysString().split(",");
            StringBuilder stringBuilder = new StringBuilder();

            for (int x=0; x < str.length; x++) {
                stringBuilder.append(getDay(str[x]));
                stringBuilder.append(",");
            }

            if (stringBuilder.length() > 0) {
                stringBuilder.setLength(stringBuilder.length() - 1);
            }

            viewHolder.txt_day.setText(stringBuilder.toString());
        }


        if(alarmArrayList.get(position).getAlarmActive()) {
            viewHolder.img_alarm.setImageResource(R.drawable.toggle_on);
        } else {
            viewHolder.img_alarm.setImageResource(R.drawable.toggle_off);
        }

        return view;
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
