package io.yostajsc.izigo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.yosta.materialspinner.MaterialSpinner;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.SearchActivity;
import io.yostajsc.izigo.firebase.model.FirebaseTrip;
import io.yostajsc.izigo.firebase.model.FirebaseTrips;
import io.yostajsc.utils.AppUtils;
import io.yostajsc.utils.SearchTripHelper;

public class SearchFragment extends Fragment {

    @BindView(R.id.spinner_vehicle)
    MaterialSpinner mSpinnerVehicle;

    @BindView(R.id.txt_arrive)
    EditText txt_arrive;

    @BindView(R.id.txt_depart)
    EditText txt_depart;

    @BindView(R.id.txt_time)
    EditText txt_time;

    private Activity mActivity;
    private List<String> mVehicles = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);
        onApplyData();
        return rootView;
    }


    private void onApplyData() {
        this.mVehicles = Arrays.asList(this.mActivity.getResources().getStringArray(R.array.arr_vehicle));
        this.mSpinnerVehicle.setItems(this.mVehicles);
    }

    @OnClick(R.id.btn_search)
    public void Search() {

        new AsyncTask<String, Void, List<FirebaseTrip>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected List<FirebaseTrip> doInBackground(String... params) {
                String arrive = params[0];
                String depart = params[1];
                String time = params[2];
                String vehicle = params[3];
                return SearchTripHelper.search(arrive, depart, time, vehicle);
            }

            @Override
            protected void onPostExecute(List<FirebaseTrip> firebaseTrips) {
                super.onPostExecute(firebaseTrips);
                if (firebaseTrips != null) {
                    if (firebaseTrips.size() > 0) {
                        FirebaseTrips trips = new FirebaseTrips(firebaseTrips);
                        Intent intent = new Intent(mActivity, SearchActivity.class);
                        intent.putExtra(AppUtils.EXTRA_TRIPS, trips);
                        startActivity(intent);
                    }
                }
            }
        }.execute(txt_arrive.getText().toString(),
                txt_depart.getText().toString(),
                txt_time.getText().toString(),
                mVehicles.get(mSpinnerVehicle.getSelectedIndex()));
    }

}
