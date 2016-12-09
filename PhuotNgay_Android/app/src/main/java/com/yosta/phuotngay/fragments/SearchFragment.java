package com.yosta.phuotngay.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.yosta.materialspinner.MaterialSpinner;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapters.FilterAdapter;
import com.yosta.phuotngay.firebase.FirebaseTripAdapter;
import com.yosta.phuotngay.firebase.FirebaseUtils;
import com.yosta.phuotngay.helpers.app.SearchTripHelper;
import com.yosta.phuotngay.models.trip.FirebaseTrip;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends Fragment {

    @BindView(R.id.spinner_vehicle)
    MaterialSpinner mSpinnerVehicle;

    @BindView(R.id.txt_arrive)
    EditText txt_arrive;

    @BindView(R.id.txt_depart)
    EditText txt_depart;

    @BindView(R.id.txt_time)
    EditText txt_time;

    @BindView(R.id.btn_search)
    Button btn_search;

    private FilterAdapter filterAdapter = null;
    private Context mContext = null;

    private FirebaseUtils firebaseUtils = null;
    private FirebaseTripAdapter tripAdapter = null;

    private Activity mActivity;
    private List<String> mVehicles = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
        this.mContext = getContext();
    }

    private void Search() {
        List<FirebaseTrip> trips = SearchTripHelper.search(txt_arrive.getText().toString(), txt_depart.getText().toString(),
                txt_time.getText().toString(), mVehicles.get(mSpinnerVehicle.getSelectedIndex()));

        Log.e("SEARCH", "" + trips.size());
        for (FirebaseTrip trip : trips)
            Log.e("SEARCH", trip.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search();
            }
        });

        this.firebaseUtils = FirebaseUtils.initializeWith(mContext);
        this.tripAdapter = new FirebaseTripAdapter(mContext, this.firebaseUtils.TRIPRef());
        this.filterAdapter = new FilterAdapter(mContext);

        onApplyData();

        return rootView;
    }

    private void onApplyData() {
        this.mVehicles = Arrays.asList(this.mActivity.getResources().getStringArray(R.array.arr_vehicle));
        this.mSpinnerVehicle.setItems(this.mVehicles);
    }
}
