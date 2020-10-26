package com.lisungui.pharma.activities;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lisungui.pharma.R;

import uk.co.senab.photoview.PhotoViewAttacher;

public class FullScreenImage extends AppCompatActivity {
    String bmp;
    ImageView imgDisplay;
    ImageButton btnClose;
    String url;
    PhotoViewAttacher pAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();

        imgDisplay = (ImageView) findViewById(R.id.imgDisplay);
        btnClose = (ImageButton) findViewById(R.id.btnClose);
        if (extras != null) {
            bmp = (String) extras.get("imagebitmap");
            if (bmp != null) {
                Glide.with(this).load(bmp).into(imgDisplay);
            }
        }

        pAttacher = new PhotoViewAttacher(imgDisplay);
        pAttacher.update();


        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FullScreenImage.this.finish();
            }
        });

    }
}