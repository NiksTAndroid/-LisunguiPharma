package com.lisungui.pharma.FirebaseChating;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import com.lisungui.pharma.R;

public class ChattingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        getSupportActionBar().show();
        Bundle bundle = getIntent().getExtras();
        ChattingFragment fragment = new ChattingFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
        fragment.setArguments(bundle);
        fragmentTransaction.commitAllowingStateLoss();

    }
}
