package com.yosta.phuotngay.activities.user;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.MainActivity;
import com.yosta.phuotngay.firebase.model.User;
import com.yosta.phuotngay.managers.EventManager;
import com.yosta.phuotngay.helpers.StorageHelper;
import com.yosta.phuotngay.helpers.validate.ValidateHelper;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.interfaces.CallBack;
import com.yosta.phuotngay.interfaces.CallBackStringParam;
import com.yosta.phuotngay.services.api.APIManager;

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

    /*@BindView(R.id.image_view)
    CircleImageView imageViewAvatar;*/

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
        user = StorageHelper.inject(this).getUser();
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
        String authen = StorageHelper.inject(this).getUser().getAuthen();
        String email = editEmail.getText().toString();
        String firstName = editFirstName.getText().toString();
        String lastName = editLastName.getText().toString();
        String gender = user.getGender();
        String dateOfBirth = editDob.getText().toString();
        String avatar = user.getAvatar();
        Map<String, String> map = new HashMap<>();
        if (ValidateHelper.IsNotEmpty(email)
                && ValidateHelper.IsNotEmpty(firstName)
                && ValidateHelper.IsNotEmpty(lastName)
                && ValidateHelper.IsNotEmpty(dateOfBirth)) {

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
