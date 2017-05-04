package io.yostajsc.izigo.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.AppConfig;
import io.yostajsc.izigo.activities.MainActivity;
import io.yostajsc.izigo.activities.OwnCoreActivity;
import io.yostajsc.core.utils.ValidateUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.model.user.IgUser;

import butterknife.BindView;
import io.yostajsc.sdk.model.IGCallback;
import io.yostajsc.sdk.api.IzigoSdk;

public class ProfileActivity extends OwnCoreActivity {

    @BindView(R.id.text_email)
    TextView textEmail;

    @BindView(R.id.text_gender)
    TextView textGender;

    @BindView(R.id.text_member_ship)
    TextView textMemberShip;

    @BindView(R.id.edit_name)
    EditText editName;

    @BindView(R.id.image_view)
    AppCompatImageView imageAvatar;

    @BindView(R.id.layout)
    FrameLayout layout;

    @BindView(R.id.button_logout)
    Button buttonLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        onApplyData();
    }

    @Override
    public void onApplyData() {

        showProgressBar();
        IzigoSdk.UserExecutor.getInfo(new IGCallback<IgUser, String>() {
            @Override
            public void onSuccessful(IgUser igUser) {
                hideProgressBar();
                updateValue(igUser);
            }

            @Override
            public void onFail(String error) {
                AppConfig.showToast(ProfileActivity.this, error);
                hideProgressBar();
            }

            @Override
            public void onExpired() {
                mOnExpiredCallBack.run();
            }
        });
    }

    private void showProgressBar() {
        layout.setVisibility(View.VISIBLE);
        buttonLogout.setEnabled(false);
    }

    private void hideProgressBar() {
        layout.setVisibility(View.GONE);
        buttonLogout.setEnabled(true);
    }

    private void updateValue(IgUser igUser) {
        if (igUser == null) {
            return;
        }
        editName.setText(igUser.getFullName());
        editName.setSelection(editName.getText().length());

        Glide.with(ProfileActivity.this)
                .load(igUser.getAvatar())
                .priority(Priority.IMMEDIATE)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageAvatar);

        textEmail.setText(igUser.getEmail());
        textGender.setText(igUser.getGender());
        textMemberShip.setText(igUser.getMemberShip());
    }

    @OnClick(R.id.button_logout)
    @Override
    protected void onExpired() {
        super.onExpired();
    }

    @OnClick(R.id.button)
    public void onConfirm() {

        String name = editName.getText().toString();

        if (ValidateUtils.canUse(name)) {
            Map<String, String> map = new HashMap<>();
            map.put("name", name);

            IzigoSdk.UserExecutor.updateInfo(map, new IGCallback<Void, String>() {
                @Override
                public void onSuccessful(Void aVoid) {
                    AppConfig.showToast(ProfileActivity.this, getString(R.string.str_success));
                }

                @Override
                public void onFail(String error) {
                    AppConfig.showToast(ProfileActivity.this, error);
                }

                @Override
                public void onExpired() {
                    mOnExpiredCallBack.run();
                }
            });
        } else {
            AppConfig.showToast(this, getString(R.string.error_message_empty));
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
