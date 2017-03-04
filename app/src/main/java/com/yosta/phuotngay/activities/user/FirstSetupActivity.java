package com.yosta.phuotngay.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.MainActivity;
import com.yosta.phuotngay.firebase.model.User;
import com.yosta.utils.validate.ValidateUtils;
import com.yosta.interfaces.ActivityBehavior;
import com.yosta.interfaces.CallBack;
import com.yosta.interfaces.CallBackStringParam;
import com.yosta.phuotngay.managers.EventManager;
import com.yosta.backend.config.APIManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FirstSetupActivity extends ActivityBehavior {

    @BindView(R.id.edit_first_name)
    TextInputEditText editFirstName;

    @BindView(R.id.edit_last_name)
    TextInputEditText editLastName;

    @BindView(R.id.img_gender)
    AppCompatImageView imgGender;

    @BindView(R.id.txt_gender)
    TextView txtGender;

    @BindView(R.id.edit_email)
    TextInputEditText editEmail;

    @BindView(R.id.edit_dob)
    TextInputEditText editDob;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setup);
        ButterKnife.bind(this);

        onUpdateData();
        onApplyEvents();
    }

    @Override
    @OnClick(R.id.button)
    public void onBackPressed() {
        onUpdateDataToServer();
    }

    private void onUpdateData() {
        // user = StorageUtils.inject(this).getUser();
        editFirstName.setText(user.getFirstName());
        editLastName.setText(user.getLastName());
        editEmail.setText(user.getEmail());
        String avatar = user.getAvatar();
        if (avatar != null && !avatar.equals("")) {
            //Glide.with(this).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageViewAvatar);
        }
        String gender = user.getGender();
        if (gender.equals("male")) {
            imgGender.setImageResource(R.drawable.ic_vector_male_icon);
        } else {
            imgGender.setImageResource(R.drawable.ic_vector_female_icon);
        }
        txtGender.setText(gender);
    }

    private void onUpdateDataToServer() {
        String authen = "";//StorageUtils.inject(this).getUser().getAuthen();
        String email = editEmail.getText().toString();
        String firstName = editFirstName.getText().toString();
        String lastName = editLastName.getText().toString();
        String gender = user.getGender();
        String dateOfBirth = editDob.getText().toString();
        String avatar = user.getAvatar();
        Map<String, String> map = new HashMap<>();
        if (ValidateUtils.canUse(email)
                && ValidateUtils.canUse(firstName)
                && ValidateUtils.canUse(lastName)
                && ValidateUtils.canUse(dateOfBirth)) {

            map.clear();
            map.put("email", email);
            map.put("firstName", firstName);
            map.put("lastName", lastName);
            map.put("gender", gender);
            map.put("dateOfBirth", dateOfBirth);
            map.put("avatar", avatar);

            APIManager.connect().onUpdate(authen, map, new CallBack() {
                @Override
                public void run() {
                    startActivity(new Intent(FirstSetupActivity.this, MainActivity.class));
                    finish();
                }
            }, new CallBackStringParam() {
                @Override
                public void run(String error) {
                    Toast.makeText(FirstSetupActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Please fill in all of fields.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onApplyEvents() {
        editFirstName.addTextChangedListener(EventManager.connect().addTextWatcherEvent(new CallBack() {
            @Override
            public void run() {

            }
        }));
        editLastName.addTextChangedListener(EventManager.connect().addTextWatcherEvent(new CallBack() {
            @Override
            public void run() {

            }
        }));
    }
}
