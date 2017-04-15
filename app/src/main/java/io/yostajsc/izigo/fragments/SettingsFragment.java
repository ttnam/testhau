package io.yostajsc.izigo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.core.fragments.CoreFragment;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.MainActivity;
import io.yostajsc.izigo.activities.WebViewActivity;
import io.yostajsc.view.OwnToolBar;

public class SettingsFragment extends CoreFragment {

    @BindView(R.id.own_toolbar)
    OwnToolBar ownToolBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, rootView);
        onApplyViews();
        return rootView;
    }

    public void onApplyViews() {
        ownToolBar.setRight(R.drawable.ic_vector_logout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onExpired();
            }
        });
    }

    @OnClick(R.id.layout_about)
    public void AboutSetting() {
        startActivity(new Intent(mContext, WebViewActivity.class));
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
