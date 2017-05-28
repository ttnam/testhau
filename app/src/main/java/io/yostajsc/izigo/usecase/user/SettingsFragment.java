package io.yostajsc.izigo.usecase.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.sdk.utils.ToastUtils;
import io.yostajsc.izigo.AppConfig;
import io.yostajsc.sdk.fragments.CoreFragment;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.SplashActivity;
import io.yostajsc.izigo.usecase.webview.WebViewActivity;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.sdk.api.model.IgCallback;
import io.yostajsc.sdk.api.model.user.IgUser;

public class SettingsFragment extends CoreFragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, rootView);
        onApplyData();
        return rootView;
    }


    private void onApplyData() {
        IzigoSdk.UserExecutor.getInfo(new IgCallback<IgUser, String>() {
            @Override
            public void onSuccessful(IgUser igUser) {
                updateValue(igUser);
            }

            @Override
            public void onFail(String error) {
                ToastUtils.showToast(mContext, error);
            }

            @Override
            public void onExpired() {

            }
        });
    }

    private void updateValue(IgUser igUser) {
        if (igUser == null) {
            return;
        }
        editName.setText(igUser.getFullName());
        editName.setSelection(editName.getText().length());

        Glide.with(mContext)
                .load(igUser.getAvatar())
                .priority(Priority.IMMEDIATE)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageAvatar);

        textEmail.setText(Html.fromHtml(String.format("<b>Email: </b> %s", igUser.getEmail())));
        textGender.setText(Html.fromHtml(String.format("<b>Gender: </b> %s", igUser.getGender())));
        textMemberShip.setText(Html.fromHtml(String.format("<b>Membership: </b> %s", igUser.getMemberShip())));
    }

    @OnClick(R.id.layout_about)
    public void AboutSetting() {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("WEB_LINK", "https://izigovn.firebaseapp.com");
        startActivity(intent);
    }


    @OnClick(R.id.button_logout)
    public void logout() {
        AppConfig.getInstance().logout();
        startActivity(new Intent(mContext, SplashActivity.class));
        getActivity().finish();
    }

    @OnClick(R.id.button)
    public void onConfirm() {

        String name = editName.getText().toString();

        if (!TextUtils.isEmpty(name)) {
            Map<String, String> map = new HashMap<>();
            map.put("name", name);

            IzigoSdk.UserExecutor.updateInfo(map, new IgCallback<Void, String>() {
                @Override
                public void onSuccessful(Void aVoid) {
                    ToastUtils.showToast(mContext, getString(R.string.str_success));
                }

                @Override
                public void onFail(String error) {
                    ToastUtils.showToast(mContext, error);
                }

                @Override
                public void onExpired() {

                }
            });
        } else {
            ToastUtils.showToast(mContext, getString(R.string.error_message_empty));
        }
    }

    /*
        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            // presUtils = new PrefsUtils(getActivity());
            switchSync.setOnCheckedChangeListener(ListenerHelpers.SwitchSync);
        }


        @Override
        public void onPause() {
            super.onPause();

            try {
                AppSetting appSetting = new AppSetting(
                        switchSync.isChecked(),
                        switchNotify.isChecked()
                );
                presUtils.onApplyAppSetting(appSetting.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onResume() {
            super.onResume();

            String json = presUtils.onGetAppSetting();
            Gson gson = new GsonBuilder().create();
            AppSetting appSetting = gson.fromJson(json, AppSetting.class);
            if (appSetting != null) {
                switchSync.setChecked(appSetting.isSync());
                switchNotify.setChecked(appSetting.isNotify());
            }

            // Language
            int lang = (presUtils.getSettingInt(PrefsUtils.KEY_LANGUAGE) == 0) ? R.string.setting_language_vietnamese : R.string.setting_language_english;
            txtLang.setText(getResources().getString(lang));
        }

        @OnClick(R.id.layout_rating)
        public void onRating() {
            new StandardDialog(getContext())
                    .setButtonsColor(getResources().getColor(R.color.colorAccent))
                    .setCancelable(false)
                    .setTopColorRes(android.R.color.white)
                    .setTopColor(getResources().getColor(android.R.color.white))
                    .setTopColorRes(R.color.BlueTitle)
                    .setIcon(R.drawable.ic_vector_rate)
                    .setPositiveButton("Rate Now", ListenerHelpers.onRating)
                    .setNegativeButton("Later", null)
                    .setMessage("If you like or even dislike this app, Please rate for us. Thank you!!")
                    .show();
        }

        @OnClick(R.id.layout_change_language)
        public void LanguageSetting() {

            MenuAdapter languageAdapter = AppUtils.LoadListMenuAction(getContext(),
                    R.array.language_item_text,
                    R.array.language_item_icon);

            new ChoiceDialog(getContext()).setTopColorRes(R.color.BlueTitle)
                    .setTitle(R.string.str_action_change_language_title)
                    .setIcon(R.drawable.ic_vector_language)
                    .setMessage(getResources().getString(R.string.str_action_change_language_message))
                    .setItems(languageAdapter, new ChoiceDialog.OnItemSelectedListener<MenuItem>() {
                        @Override
                        public void onItemSelected(int position, MenuItem item) {
                            presUtils.changeAppLanguage();
                            getActivity().finish();
                            Intent i = new Intent(getContext(), MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra(AppUtils.EXTRA_INTENT, 4);
                            startActivity(i);
                        }
                    })
                    .show();
        }

        */
}
