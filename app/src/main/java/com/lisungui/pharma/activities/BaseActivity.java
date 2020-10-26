package com.lisungui.pharma.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lisungui.pharma.R;
import com.lisungui.pharma.constant.StringConstant;
import com.lisungui.pharma.helper.SQLiteDataBaseHelper;
import com.lisungui.pharma.utility.PrefManager;

/**
 * Created by siddeshwar on 9/1/17.
 */
public class BaseActivity extends AppCompatActivity {

    private int count = 0;
    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        itemCart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_refresh).setVisible(false);
        menu.findItem(R.id.menu_item_save).setVisible(false);
        menu.findItem(R.id.menu_item_delete).setVisible(false);

        menu.findItem(R.id.text_message_item).setVisible(false);
        menu.findItem(R.id.photo_camera_item).setVisible(false);
        menu.findItem(R.id.call_help_item).setVisible(false);

        MenuItem menuItem = menu.findItem(R.id.action_cart);
        menuItem.setIcon(buildCounterDrawable(0, R.drawable.ic_action_shopping_cart));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                if (PrefManager.getOrderType(this).equals(StringConstant.CountryOrder)){
                    Intent CountryCartIntent = new Intent(this, CountryPlaceOrderActivity.class);
                    startActivity(CountryCartIntent);
                }else{
                    Intent NormalCartIntent = new Intent(this, PlaceOrderActivity.class);
                    startActivity(NormalCartIntent);
                }
                break;



/*
            case R.id.call_help_item:
                Intent callHelpIntent = new Intent(this, CallHelpActivity.class);
                startActivity(callHelpIntent);
                break;
*/
        }
        return super.onOptionsItemSelected(item);
    }


    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.counter_menuitem_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }

    public void itemCart() {
        try {
            Log.d(TAG, "Order Count :" + count);

            SQLiteDataBaseHelper dataBaseHelper = new SQLiteDataBaseHelper(this);
            count = dataBaseHelper.getOrderCount();
            Log.d(TAG, "Order Count :" + count);
            invalidateOptionsMenu();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
