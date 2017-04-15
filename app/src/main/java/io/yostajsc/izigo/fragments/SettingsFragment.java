package io.yostajsc.izigo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.core.fragments.CoreFragment;
import io.yostajsc.izigo.R;

public class SettingsFragment extends CoreFragment {

    @BindView(R.id.layout_rating)
    LinearLayout layoutRating;

    @BindView(R.id.txt_lang)
    TextView txtLang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, rootView);
        onApplyViews();
        return rootView;
    }
    public void onApplyViews() {


    }
}
