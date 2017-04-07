package io.yostajsc.izigo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.core.ActivityCoreBehavior;
import io.yostajsc.izigo.activities.user.ProfileActivity;
import io.yostajsc.izigo.configs.AppConfig;
import io.yostajsc.view.OwnToolBar;

public class SettingActivity extends ActivityCoreBehavior {

    @BindView(R.id.own_toolbar)
    OwnToolBar mOwnToolbar;

    @BindView(R.id.layout_rating)
    LinearLayout layoutRating;

    @BindView(R.id.txt_lang)
    TextView txtLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        onApplyViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onApplyViews() {

        mOwnToolbar.setTitle(getString(R.string.setting)).setLeft(R.drawable.ic_vector_back_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onInternetConnected() {

    }

    @Override
    public void onInternetDisConnected() {

    }

    @OnClick(R.id.layout_profile)
    public void onCallProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(AppConfig.FIRST_TIME, false);
        startActivity(intent);
    }

    @Override
    @OnClick(R.id.layout_logout)
    protected void onExpired() {
        super.onExpired();
    }

    @OnClick(R.id.layout_about)
    public void AboutSetting() {
        startActivity(new Intent(this, WebViewActivity.class));
    }

    /*
        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            // presUtils = new StorageUtils(getActivity());
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
            int lang = (presUtils.getSettingInt(StorageUtils.KEY_LANGUAGE) == 0) ? R.string.setting_language_vietnamese : R.string.setting_language_english;
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
