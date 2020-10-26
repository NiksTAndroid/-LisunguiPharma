package com.lisungui.pharma.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lisungui.pharma.R;
import com.lisungui.pharma.activities.CountryPlaceOrderActivity;
import com.lisungui.pharma.constant.StringConstant;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.CountryMedicineList;
import com.lisungui.pharma.utility.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class CountryMedicineListAdapter extends BaseAdapter implements Filterable {

    private static final String TAG = CountryMedicineListAdapter.class.getSimpleName();

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<CountryMedicineList> mMedList;
    private List<CountryMedicineList> sortMedList;
    private SQLiteDataBaseHelper dataBaseHelper;
    private String countryId;
    private String cityId;


//    String[] column;

    public CountryMedicineListAdapter(Context mContext, ArrayList<CountryMedicineList> mMedList, SQLiteDataBaseHelper dataBaseHelper,String countryId,String cityid) {
        this.mContext = mContext;
        this.mMedList = mMedList;
        this.dataBaseHelper = dataBaseHelper;
        this.countryId = countryId;
        this.cityId = cityid;
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
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;
        Log.d(TAG, "getView: adapterview"+countryId);

        if (view == null) {
            view = mInflater.inflate(R.layout.row_med_list, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.mTMedName = view.findViewById(R.id.txt_med_name);
            viewHolder.mTPrice = view.findViewById(R.id.txt_price);
            viewHolder.mTQty = view.findViewById(R.id.txt_qty);
            viewHolder.mTCart = view.findViewById(R.id.txt_cart);
            viewHolder.mTBuy = view.findViewById(R.id.txt_buy);
            viewHolder.mIMinus = view.findViewById(R.id.img_minus);
            viewHolder.mIPlus = view.findViewById(R.id.img_plus);
            // viewHolder.mIMed = (ImageView) view.findViewById(R.id.img_med);
            viewHolder.tv_Pharmacy_name = view.findViewById(R.id.tv_Pharmacy_name);


            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

//        String str_euro = mContext.getResources().getString(R.string.euro_symbol);

        viewHolder.mTMedName.setText(mMedList.get(position).getMedcName());
        String price=mMedList.get(position).getMedcPrice().replaceAll("[!#$%&'()*+,-./:;<=>?@[]^_`{|}~]]","");
        Log.d(TAG, "getView: "+price);
        viewHolder.mTPrice.setText("" + Integer.parseInt(mMedList.get(position).getMedcPrice()) * Integer.parseInt(viewHolder.mTQty.getText().toString()));

        viewHolder.tv_Pharmacy_name.setText(mMedList.get(position).getPharmName());

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
                if (PrefManager.getOrderType(mContext)!=null && PrefManager.getOrderType(mContext).equals(StringConstant.NormalOrder)){
                    Toast.makeText(mContext, "Different medicines in cart please remove", Toast.LENGTH_SHORT).show();
                }else {
                if (viewHolder.mTCart.getText().toString().equals(mContext.getString(R.string.str_go_to_cart))) {

                    Intent intent = new Intent(mContext, CountryPlaceOrderActivity.class);
                    intent.putExtra("countryid",countryId);
                    intent.putExtra("cityId",cityId);
                    intent.putExtra("pharmaid",mMedList.get(position).getPharmId());
                    mContext.startActivity(intent);
                }
                else {
                    if (!dataBaseHelper.isSameId(mMedList.get(position).getPharmId())) {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.str_please_add_same_pharmacy), Toast.LENGTH_SHORT).show();
                    } else {
                        if (viewHolder.mTCart.getText().toString().equals(mContext.getString(R.string.str_go_to_cart))) {

                            Intent intent = new Intent(mContext, CountryPlaceOrderActivity.class);
                            intent.putExtra("countryid",countryId);
                            intent.putExtra("pharmaid",mMedList.get(position).getPharmId());
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
                                    String.valueOf(mMedList.get(position).getMedcId()),
                                    mMedList.get(position).getMedcName(),
                                    String.valueOf(mMedList.get(position).getMedcPrice()),
                                    viewHolder.mTQty.getText().toString(),
                                    viewHolder.mTPrice.getText().toString(),
                                    String.valueOf(mMedList.get(position).getPharmId()), viewHolder.mTQty.getText().toString()


                            };

                            String selection = String.format(SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_MED_ID + "='%s'", mMedList.get(position).getMedcId());
                            Log.d(TAG, "150 selection : " + selection);
                            long val = dataBaseHelper.insertOrUpdate(SQLiteDataBaseHelper.TEMP_ORDER_TABLE, selection, column, values);
//                    long val = dataBaseHelper.insertOrUpdateOrderMyCart(mMedList.get(position).getMed_id(), column, values);
                            if (val > 0) {
//                        iMethodCaller.addToCart(position);
                                viewHolder.mTCart.setText(mContext.getString(R.string.str_go_to_cart));
                                PrefManager.setOrderType(mContext, StringConstant.CountryOrder);
                                notifyDataSetChanged();

//                        BaseActivity baseActivity = new BaseActivity();
//                        baseActivity.itemCart();
                            }

                        }
                    }
                }
                }
            }
        });

        viewHolder.mTBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!dataBaseHelper.isSameId(mMedList.get(position).getPharmId())) {
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
                            String.valueOf(mMedList.get(position).getMedcId()),
                            mMedList.get(position).getMedcName(),
                            String.valueOf(mMedList.get(position).getMedcPrice()),
                            viewHolder.mTQty.getText().toString(),
                            viewHolder.mTPrice.getText().toString(),
                            String.valueOf(mMedList.get(position).getPharmId()), viewHolder.mTQty.getText().toString()


                    };

                    String selection = String.format(SQLiteDataBaseHelper.COLUMN_TEMP_ORDER_MED_ID + "='%s'", mMedList.get(position).getMedcId());
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
                        Intent intent = new Intent(mContext, CountryPlaceOrderActivity.class);
                        intent.putExtra("countryid",countryId);
                        intent.putExtra("pharmaid",String.valueOf(mMedList.get(position).getPharmId()));
                        mContext.startActivity(intent);
                    }

                }


            }
        });

        return view;
    }
    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                mMedList = (ArrayList<CountryMedicineList>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<CountryMedicineList> FilteredMedicines = new ArrayList<CountryMedicineList>();

                if (sortMedList == null) {
                    sortMedList = new ArrayList<CountryMedicineList>(mMedList);
                }
                if (constraint == null || constraint.length() == 0) {
                    results.count = sortMedList.size();
                    results.values = sortMedList;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < sortMedList.size(); i++) {
                        CountryMedicineList dataNames = sortMedList.get(i);
                        if (dataNames.getMedcName().toLowerCase()
                                .contains(constraint.toString())) {
                            FilteredMedicines.add(dataNames);
                        }
                    }

                    results.count = FilteredMedicines.size();
                    // System.out.println(results.count);

                    results.values = FilteredMedicines;
                    // Log.e("VALUES", results.values.toString());
                }

                return results;
            }
        };

        return filter;
    }

    static class ViewHolder {
        TextView mTMedName, mTPrice, mTQty, mTCart, mTBuy, tv_Pharmacy_name;
        ImageView mIMinus, mIPlus;
    }
}