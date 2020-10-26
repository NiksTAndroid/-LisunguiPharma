package com.lisungui.pharma.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lisungui.pharma.R;

public class PromotionDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.setTitle(getResources().getString(R.string.promotion_details));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = this.getIntent();
//        String promotionId = intent.getStringExtra("promotionId");
//        String promotionDate = intent.getStringExtra("promotionDate");
//        String promotionMsg = intent.getStringExtra("promotionMsg");
//        String promotionImg = intent.getStringExtra("promotionImg");

        String promotionId = intent.getStringExtra("medPromotionId");
        String promotionDate = intent.getStringExtra("medPromotionDate");
        String promotionMsg = intent.getStringExtra("medPromotionMsg");
        String promotionImg = intent.getStringExtra("medPromotionImg");

        TextView idText = (TextView)findViewById(R.id.promotion_id_txt);
        TextView dateText = (TextView)findViewById(R.id.promotion_title_txt);
        TextView msgText = (TextView)findViewById(R.id.promotion_msg_txt);
        ImageView promotionImage = (ImageView)findViewById(R.id.promotion_img);

        idText.setText(getResources().getString(R.string.promotion_id)+":\t"+promotionId);
        dateText.setText(getResources().getString(R.string.time)+":\t"+promotionDate);
        msgText.setText("Message : \n"+promotionMsg);

        if(!promotionImg.equals(""))
        Glide.with(getApplicationContext())
                .load(promotionImg)
                .into(promotionImage);
    }
}
