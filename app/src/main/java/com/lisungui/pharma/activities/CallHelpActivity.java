package com.lisungui.pharma.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lisungui.pharma.R;

import static android.Manifest.permission.CALL_PHONE;

public class CallHelpActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_help);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.setTitle(getResources().getString(R.string.help));

        initValues();
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

    private void initValues()
    {
        context = CallHelpActivity.this;
        //final String contectNumber = "9087654309";
        final String contactNumber = "069488908";
        TextView callNumberTextView = (TextView)findViewById(R.id.callhelp_number_textview);
        callNumberTextView.setText(contactNumber );
        callNumberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall(contactNumber);
            }
        });
    }

    public void makePhoneCall(String contactNumber)
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        //callIntent.setData(Uri.parse("tel:"+8802177690));//change the number
        callIntent.setData(Uri.parse("tel:"+contactNumber));//change the number
        if (ActivityCompat.checkSelfPermission(context, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            //return;
            startActivity(callIntent);
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{CALL_PHONE}, 1);
            }
        }
        //startActivity(callIntent);
    }


}
