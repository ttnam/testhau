package io.yostajsc.izigo.usecase.trip;

import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import io.yostajsc.izigo.R;

/**
 * Created by nphau on 5/6/17.
 */

public class MyPlaceSelector extends PlaceAutocompleteFragment {

    private static String TAG = MyPlaceSelector.class.getSimpleName();

    public void setInputBackground(int color) {
        getView().findViewById(R.id.place_autocomplete_search_input)
                .setBackgroundColor(color);
    }

    public void setVisibilityOfSearchIcon(int visibility) {
        getView().findViewById(R.id.place_autocomplete_search_button)
                .setVisibility(visibility);
    }

    public void setOnClearButtonClickListener(final CallBack callBack) {
        getView().findViewById(R.id.place_autocomplete_clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setText("");
                callBack.run();
            }
        });
    }

    public void setOnPlaceSelectedListener(final OnPlaceSelecteListener callBack) {
        setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                callBack.select(place);
            }

            @Override
            public void onError(Status status) {
                Log.e(TAG, status.getStatusMessage());
            }
        });
    }

    public interface CallBack {
        void run();
    }

    public interface OnPlaceSelecteListener {
        void select(Place place);
    }
}
