package io.yostajsc.izigo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.dialogs.DialogTripInvite;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        DialogTripInvite invite = new DialogTripInvite(this);
        invite.show();
    }
}
