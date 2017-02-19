package com.yosta.phuotngay.activities.user;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.MainActivity;
import com.yosta.phuotngay.firebase.model.User;
import com.yosta.phuotngay.helpers.StorageHelper;
import com.yosta.phuotngay.interfaces.CallBack;
import com.yosta.phuotngay.interfaces.CallBackStringParam;
import com.yosta.phuotngay.services.PhuotNgayService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class FirstSetupActivity extends AppCompatActivity {

    @BindView(R.id.edit_first_name)
    TextInputEditText editFirstName;

    @BindView(R.id.edit_last_name)
    TextInputEditText editLastName;

    @BindView(R.id.edit_email)
    TextInputEditText editEmail;

    @BindView(R.id.image_view)
    CircleImageView imageViewAvatar;

    @BindView(R.id.edit_dob)
    TextInputEditText editDob;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setup);
        ButterKnife.bind(this);
        onUpdateData();
    }

    @OnClick(R.id.button)
    public void onSave() {
        onUpdateDataToServer();
    }

    private void onUpdateData() {
        user = StorageHelper.inject(this).getUser();
        editFirstName.setText(user.getFirstName());
        editLastName.setText(user.getLastName());
        editEmail.setText(user.getEmail());
        String avatar = user.getAvatar();
        if (avatar != null && !avatar.equals("")) {
            Glide.with(this).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageViewAvatar);
        }
    }

    private void onUpdateDataToServer() {
        String authen = StorageHelper.inject(this).getUser().getAuthen();
        String email = editEmail.getText().toString();
        String firstName = editFirstName.getText().toString();
        String lastName = editLastName.getText().toString();
        String gender = "";
        String avatar = user.getAvatar();

        PhuotNgayService.connect().onUpdate(authen, email, firstName, lastName, gender, avatar, new CallBack() {
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
    }
}
