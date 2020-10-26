package com.lisungui.pharma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.models.MedicinePojo;

import java.util.ArrayList;
import java.util.List;

public class SelectMedicineListAdapter extends BaseAdapter {

    private static final String TAG = SelectMedicineListAdapter.class.getSimpleName();

    List<MedicinePojo> nListMedicines = new ArrayList<>();
    Context nContext;
    private LayoutInflater mInflater;

    public SelectMedicineListAdapter(List<MedicinePojo> nListMedicines, Context nContext) {
        this.nListMedicines = nListMedicines;
        this.nContext = nContext;
        mInflater = (LayoutInflater) nContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if (nListMedicines != null)
            return nListMedicines.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final SelectMedicineListAdapter.ViewHolder viewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.row_country_medicine_list, parent, false);
            viewHolder = new SelectMedicineListAdapter.ViewHolder();

            viewHolder.mTMedName = view.findViewById(R.id.txt_med_name);
            viewHolder.mTPrice = view.findViewById(R.id.txt_price);
            // viewHolder.mIMed = (ImageView) view.findViewById(R.id.img_med);
            viewHolder.tv_Pharmacy_name = view.findViewById(R.id.tv_Pharmacy_name);


            view.setTag(viewHolder);
        } else {
            viewHolder = (SelectMedicineListAdapter.ViewHolder) view.getTag();
        }

        viewHolder.mTMedName.setText(nListMedicines.get(position).getMed_name());
        viewHolder.mTPrice.setText("" + nListMedicines.get(position).getMed_price());

        viewHolder.tv_Pharmacy_name.setText(nListMedicines.get(position).getPharm_name());

        return view;
    }


    static class ViewHolder {
        TextView mTMedName, mTPrice,tv_Pharmacy_name;

    }
}
