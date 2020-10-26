package com.lisungui.pharma.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.activities.AddTreatAlarmActivity1;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.TreatmentModel;

import java.util.ArrayList;

public class MonitoringAdapter extends BaseAdapter {

    private static final String TAG = MonitoringAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<TreatmentModel> treatmentModelArrayList;
    private LayoutInflater inflater;
    private SQLiteDataBaseHelper dataBaseHelper;
//    String[] day_array;

    public MonitoringAdapter(Context context, ArrayList<TreatmentModel> treatmentModelArrayList) {
        this.context = context;
        this.treatmentModelArrayList = treatmentModelArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataBaseHelper = new SQLiteDataBaseHelper(context);
//        day_array = context.getResources().getStringArray(R.array.day_array);
    }

    @Override
    public int getCount() {

        if(treatmentModelArrayList != null)
            return treatmentModelArrayList.size();

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
        TextView txt_med_name, txt_time, txt_date, txt_remove, txt_update;
        ImageView img_toggle;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        final ViewHolder viewHolder;

        if(view == null) {
            view = inflater.inflate(R.layout.row_treat_list, viewGroup, false);
            viewHolder = new ViewHolder();

            viewHolder.txt_med_name = view.findViewById(R.id.txt_med_name);
            viewHolder.txt_time = view.findViewById(R.id.txt_time);
            viewHolder.txt_date = view.findViewById(R.id.txt_date);
            viewHolder.txt_update = view.findViewById(R.id.txt_update);
            viewHolder.txt_remove = view.findViewById(R.id.txt_remove);
//            viewHolder.img_toggle = (ImageView) view.findViewById(R.id.img_toggle);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.txt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TreatmentModel treatmentModel = treatmentModelArrayList.get(i);

                Bundle mBundle = new Bundle();
                mBundle.putSerializable("Treatment", treatmentModel);

                Intent intent = new Intent(context, AddTreatAlarmActivity1.class);
                intent.putExtras(mBundle);
                context.startActivity(intent);
            }
        });

        /*if(treatmentModelArrayList.get(i).getToggle().equals("True")) {
            viewHolder.img_toggle.setImageResource(R.drawable.toggle_on);
        } else {
            viewHolder.img_toggle.setImageResource(R.drawable.toggle_off);
        }*/

//        String to = context.getResources().getString(R.string.str_to);
//        int from_day = Integer.parseInt(treatmentModelArrayList.get(i).getFrom_day());
//        int to_day = Integer.parseInt(treatmentModelArrayList.get(i).getTo_day());

        viewHolder.txt_med_name.setText(treatmentModelArrayList.get(i).getMed_name());

        if(treatmentModelArrayList.get(i).getArrayList().size() > 0) {
            viewHolder.txt_time.setText(treatmentModelArrayList.get(i).getArrayList().get(0).getAlarmTimeString());

            if(treatmentModelArrayList.get(i).getArrayList().get(0).getRepeatDaysString().equalsIgnoreCase("Every Day")) {
                viewHolder.txt_date.setText(context.getString(R.string.str_everyday));
            } else {
//                viewHolder.txt_date.setText(treatmentModelArrayList.get(i).getArrayList().get(0).getRepeatDaysString());

                String []str = treatmentModelArrayList.get(i).getArrayList().get(0).getRepeatDaysString().split(",");
                StringBuilder stringBuilder = new StringBuilder();

                for (int x=0; x < str.length; x++) {
                    stringBuilder.append(getDay(str[x]));
                    stringBuilder.append(",");
                }

                if (stringBuilder.length() > 0) {
                    stringBuilder.setLength(stringBuilder.length() - 1);
                }

                viewHolder.txt_date.setText(stringBuilder.toString());
            }

        }

        viewHolder.txt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseHelper.deleteTreatAlarm(treatmentModelArrayList.get(i).getId());
                treatmentModelArrayList.remove(i);
                notifyDataSetChanged();
            }
        });

        /*viewHolder.img_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(treatmentModelArrayList.get(i).getToggle().equals("True")) {

                    String[] col_name = new String[] { "treat_alarm"};
                    String[] values = new String[] { "False"};

                    int id = treatmentModelArrayList.get(i).getId();
                    dataBaseHelper.updateAlarm(id, col_name, values);

                    treatmentModelArrayList.get(i).setToggle("False");

                    viewHolder.img_toggle.setImageResource(R.drawable.toggle_off);
                    notifyDataSetChanged();
                } else {

                    String[] col_name = new String[] { "treat_alarm"};
                    String[] values = new String[] { "True"};

                    int id = treatmentModelArrayList.get(i).getId();
                    dataBaseHelper.updateAlarm(id, col_name, values);

                    treatmentModelArrayList.get(i).setToggle("True");

                    viewHolder.img_toggle.setImageResource(R.drawable.toggle_on);
                    notifyDataSetChanged();
                }

            }
        });*/

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