package com.lisungui.pharma.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lisungui.pharma.R;
import com.lisungui.pharma.activities.PlaceOrderActivity;
import com.lisungui.pharma.constant.StringConstant;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.MedicinePojo;
import com.lisungui.pharma.utility.PrefManager;

import java.util.ArrayList;

public class MedicineListAdapter extends BaseAdapter {

    private static final String TAG = MedicineListAdapter.class.getSimpleName();

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<MedicinePojo> mMedList;
    private SQLiteDataBaseHelper dataBaseHelper;

//    String[] column;

    public MedicineListAdapter(Context mContext, ArrayList<MedicinePojo> mMedList, SQLiteDataBaseHelper dataBaseHelper) {
        this.mContext = mContext;
        this.mMedList = mMedList;
        this.dataBaseHelper = dataBaseHelper;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (mMedList != null)
            return mMedList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.row_med_list, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.mTMedName = (TextView) view.findViewById(R.id.txt_med_name);
            viewHolder.mTPrice = (TextView) view.findViewById(R.id.txt_price);
            viewHolder.mTQty = (TextView) view.findViewById(R.id.txt_qty);
            viewHolder.mTCart = (TextView) view.findViewById(R.id.txt_cart);
            viewHolder.mTBuy = (TextView) view.findViewById(R.id.txt_buy);
            viewHolder.mIMinus = (ImageView) view.findViewById(R.id.img_minus);
            viewHolder.mIPlus = (ImageView) view.findViewById(R.id.img_plus);
            // viewHolder.mIMed = (ImageView) view.findViewById(R.id.img_med);
            viewHolder.tv_Pharmacy_name = (TextView) view.findViewById(R.id.tv_Pharmacy_name);


            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

//        String str_euro = mContext.getResources().getString(R.string.euro_symbol);

        viewHolder.mTMedName.setText(mMedList.get(position).getMed_name());
        viewHolder.mTPrice.setText("" + mMedList.get(position).getMed_price() * Integer.parseInt(viewHolder.mTQty.getText().toString()));

        viewHolder.tv_Pharmacy_name.setText(mMedList.get(position).getPharm_name());
        viewHolder.mIPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty_count = Integer.parseInt(viewHolder.mTQty.getText().toString());
                qty_count++;
                viewHolder.mTQty.setText(String.valueOf(qty_count));
//                double price = mMedList.get(position).getMed_price() * qty_count;
//                viewHolder.mTPrice.setText(String.valueOf(price));
                notifyDataSetChanged();
            }
        });

        viewHolder.mIMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty_count = Integer.parseInt(viewHolder.mTQty.getText().toString());
                if (qty_count > 1)
                    --qty_count;

                viewHolder.mTQty.setText(String.valueOf(qty_count));
//                double price = mMedList.get(position).getMed_price() * qty_count;
//                viewHolder.mTPrice.setText(String.valueOf(price));
                notifyDataSetChanged();
            }
        });

        viewHolder.mTCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PrefManager.getOrderType(mContext) != null && PrefManager.getOrderType(mContext).equals(StringConstant.CountryOrder)) {
                    Toast.makeText(mContext, "Different medicines in cart please remove", Toast.LENGTH_SHORT).show();
                } else {

                    if (!dataBaseHelper.isSameId(mMedList.get(position).getPharm_id())) {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.str_please_add_same_pharmacy), Toast.LENGTH_SHORT).show();
                    } else {

                        if (viewHolder.mTCart.getText().toString().equals(mContext.getString(R.string.str_go_to_cart))) {
                            Intent intent = new Intent(mContext, PlaceOrderActivity.class);
                            mContext.startActivity(intent);
                        } else {

                    /*String[] values = new String[] {
                            String.valueOf(mMedList.get(position).getMed_id()),
                            mMedList.get(position).getMed_name(),
                            String.valueOf(mMedList.get(position).getMed_price()),
                            "PENDING",
                            String.valueOf(viewHolder.mTQty.getText().toString()),
                            String.valueOf(viewHolder.mTPrice.getText().toString())
                    };*/

                            String[] column = new String[]{
                                    SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_MED_ID,
                                    SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_MED_NAME,
                                    SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_MED_PRICE,
                                    SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_QTY,
                                    SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_TOTAL_PRICE,
                                    SQLiteDataBaseHelper.COLUMN_TEMP_PHARMACY_ID,
                                    SQLiteDataBaseHelper.COLUMN_TEMP_QTY
                            };

                            String[] values = new String[]{
                                    String.valueOf(mMedList.get(position).getMed_id()),
                                    mMedList.get(position).getMed_name(),
                                    String.valueOf(mMedList.get(position).getMed_price()),
                                    String.valueOf(viewHolder.mTQty.getText().toString()),
                                    String.valueOf(viewHolder.mTPrice.getText().toString()),
                                    mMedList.get(position).getPharm_id(), viewHolder.mTQty.getText().toString()


                            };

                            String selection = String.format(SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_MED_ID + "='%s'", mMedList.get(position).getMed_id());
                            Log.d(TAG, "150 selection : " + selection);
                            long val = dataBaseHelper.insertOrUpdate(SQLiteDataBaseHelper.TEMP_ORDER_TABLE, selection, column, values);
//                    long val = dataBaseHelper.insertOrUpdateOrderMyCart(mMedList.get(position).getMed_id(), column, values);

                            if (val > 0) {
//                        iMethodCaller.addToCart(position);
                                viewHolder.mTCart.setText(mContext.getString(R.string.str_go_to_cart));
                                PrefManager.setOrderType(mContext, StringConstant.NormalOrder);
                                notifyDataSetChanged();

//                        BaseActivity baseActivity = new BaseActivity();
//                        baseActivity.itemCart();
                            }
                        }
                    }

                }
            }
        });

        viewHolder.mTBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!dataBaseHelper.isSameId(mMedList.get(position).getPharm_id())) {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.str_please_add_same_pharmacy), Toast.LENGTH_SHORT).show();
                } else {
                    String[] column = new String[]{
                            SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_MED_ID,
                            SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_MED_NAME,
                            SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_MED_PRICE,
                            SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_QTY,
                            SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_TOTAL_PRICE,
                            SQLiteDataBaseHelper.COLUMN_TEMP_PHARMACY_ID,
                            SQLiteDataBaseHelper.COLUMN_TEMP_QTY
                    };

                    String[] values = new String[]{
                            String.valueOf(mMedList.get(position).getMed_id()),
                            mMedList.get(position).getMed_name(),
                            String.valueOf(mMedList.get(position).getMed_price()),
                            String.valueOf(viewHolder.mTQty.getText().toString()),
                            String.valueOf(viewHolder.mTPrice.getText().toString()),
                            mMedList.get(position).getPharm_id(), viewHolder.mTQty.getText().toString()


                    };

                    String selection = String.format(SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_MED_ID + "='%s'", mMedList.get(position).getMed_id());
                    Log.d(TAG, "150 selection : " + selection);
                    long val = dataBaseHelper.insertOrUpdate(SQLiteDataBaseHelper.TEMP_ORDER_TABLE, selection, column, values);
//                    long val = dataBaseHelper.insertOrUpdateOrderMyCart(mMedList.get(position).getMed_id(), column, values);



/*
                String[] column = new String[]{
                        SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_MED_ID,
                        SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_MED_NAME,
                        SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_MED_PRICE,
                        SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_QTY,
                        SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_TOTAL_PRICE
                };

                String[] values = new String[]{
                        String.valueOf(mMedList.get(position).getMed_id()),
                        mMedList.get(position).getMed_name(),
                        String.valueOf(mMedList.get(position).getMed_price()),
                        String.valueOf(viewHolder.mTQty.getText().toString()),
                        String.valueOf(viewHolder.mTPrice.getText().toString())
                };

                String selection = String.format(SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_MED_ID + "='%s'", mMedList.get(position).getMed_id());
                Log.d(TAG, "150 selection : " + selection);
                long val = dataBaseHelper.insertOrUpdate(SQLiteDataBaseHelper.TEMP_ORDER_TABLE, selection, column, values);*/

                    if (val > 0) {
                        Intent intent = new Intent(mContext, PlaceOrderActivity.class);
                        mContext.startActivity(intent);
                    }

                }


            }
        });

        return view;
    }

    static class ViewHolder {
        TextView mTMedName, mTPrice, mTQty, mTCart, mTBuy, tv_Pharmacy_name;
        ImageView mIMinus, mIPlus;
    }
}