package com.yosta.phuotngay.activities.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.dialogs.DialogUpdateProfile;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FirstSetupActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setup);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void onUpdateInfo() {
        DialogUpdateProfile dialogUpdateProfile = new DialogUpdateProfile(this);
        dialogUpdateProfile.show();
    }
}
