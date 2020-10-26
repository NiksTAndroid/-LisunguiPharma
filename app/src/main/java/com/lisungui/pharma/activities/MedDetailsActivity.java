package com.lisungui.pharma.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lisungui.pharma.R;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.models.MedicinePojo;


public class MedDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MedDetailsActivity.class.getSimpleName();

    private SQLiteDataBaseHelper dataBaseHelper;

    private MedicinePojo medicine;

    private int med_id;
    private String med_name;
    private double med_price;

    private TextView txt_cart;
    private TextView txt_price;
    private TextView txt_qty;
    private TextView txt_med_name;

//    private ImageView img_med;

    private int qty_count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            if (bundle.containsKey("MEDICINE")) {
                medicine = bundle.getParcelable("MEDICINE");
                med_id = medicine.getMed_id();
                med_name = medicine.getMed_name();
                med_price = medicine.getMed_price();

                txt_med_name.setText(med_name);
                txt_price.setText(String.valueOf(med_price));
            }
        }

    }

    private void init() {
        dataBaseHelper = new SQLiteDataBaseHelper(this);

        txt_cart = (TextView) findViewById(R.id.txt_cart);
        txt_cart.setOnClickListener(this);
        TextView txt_buy = (TextView) findViewById(R.id.txt_buy);
        txt_buy.setOnClickListener(this);
        txt_price = (TextView) findViewById(R.id.txt_price);
        txt_qty = (TextView) findViewById(R.id.txt_qty);
        txt_med_name = (TextView) findViewById(R.id.txt_med_name);

//        img_med = (ImageView) findViewById(R.id.img_med);
        ImageView img_minus = (ImageView) findViewById(R.id.img_minus);
        img_minus.setOnClickListener(this);
        ImageView img_plus = (ImageView) findViewById(R.id.img_plus);
        img_plus.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);//Menu Resource, Menu
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

            case R.id.action_cart:
                Intent intent = new Intent(MedDetailsActivity.this, PlaceOrderActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txt_cart:

                if (txt_cart.getText().toString().equalsIgnoreCase("Add to Cart")) {

                    String[] column = new String[]{
                            SQLiteDataBaseHelper.COLUMN_ORDER_MED_ID,
                            SQLiteDataBaseHelper.COLUMN_ORDER_MED_NAME,
                            SQLiteDataBaseHelper.COLUMN_ORDER_PRICE,
                            SQLiteDataBaseHelper.COLUMN_ORDER_TOTAL_PRICE
                    };

                    String[] values = new String[]{
                            String.valueOf(med_id),
                            med_name,
                            String.valueOf(med_price),
                            getString(R.string.str_pending),
                            txt_qty.getText().toString(),
                            txt_price.getText().toString()
                    };


                    long val = dataBaseHelper.insertOrUpdateOrderMyCart(med_id, column, values);
                    Log.d(TAG, "146 val : " + val);

                    if (val != 0) {
                        txt_cart.setText(getString(R.string.str_go_to_cart));
                        Toast.makeText(MedDetailsActivity.this, getString(R.string.str_added_to_cart), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Intent intent = new Intent(MedDetailsActivity.this, PlaceOrderActivity.class);
                    startActivity(intent);
                }

                break;

            case R.id.txt_buy:

                String[] column = new String[]{
                        SQLiteDataBaseHelper.COLUMN_ORDER_MED_ID,
                        SQLiteDataBaseHelper.COLUMN_ORDER_MED_NAME,
                        SQLiteDataBaseHelper.COLUMN_ORDER_PRICE,
                        SQLiteDataBaseHelper.COLUMN_ORDER_TOTAL_PRICE
                };

                String[] values = new String[]{
                        String.valueOf(medicine.getMed_id()),
                        medicine.getMed_name(),
                        String.valueOf(medicine.getMed_price()),
                        getString(R.string.str_pending),
                        txt_qty.getText().toString(),
                        txt_price.getText().toString()
                };


                long val = dataBaseHelper.insertOrUpdateOrderMyCart(medicine.getMed_id(), column, values);
                Log.d(TAG, "146 val : " + val);

                if (val > 0) {
                    Intent intent = new Intent(MedDetailsActivity.this, PlaceOrderActivity.class);
                    startActivity(intent);
                }

                break;

            case R.id.img_plus:

                qty_count++;

                txt_qty.setText(String.valueOf(qty_count));

                double price1 = med_price * qty_count;
                txt_price.setText(String.valueOf(price1));

                break;

            case R.id.img_minus:

                if (qty_count > 1)
                    --qty_count;

                txt_qty.setText(String.valueOf(qty_count));

                double price2 = med_price * qty_count;
                txt_price.setText(String.valueOf(price2));

                break;

            default:
                break;

        }

    }
}
