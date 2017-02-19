package com.yosta.phuotngay.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.dialogs.DialogTripInvite;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        DialogTripInvite invite = new DialogTripInvite(this);
        invite.show();
    }
}
