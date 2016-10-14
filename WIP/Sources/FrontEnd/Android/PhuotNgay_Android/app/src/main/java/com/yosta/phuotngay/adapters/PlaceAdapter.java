package com.yosta.phuotngay.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.databinding.ViewItemPlaceBinding;
import com.yosta.phuotngay.globalapp.UIUtils;
import com.yosta.phuotngay.model.place.Place;
import com.yosta.phuotngay.viewmodel.PlaceViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuc-Hau Nguyen on 10/14/2016.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.BindingHolder> {

    private Context mContext = null;
    private List<Place> mPlaces = null;

    public PlaceAdapter(Context context) {
        this.mContext = context;
        this.mPlaces = new ArrayList<>();
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ViewItemPlaceBinding placeBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.view_item_place,
                parent, false);
        return new BindingHolder(placeBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ViewItemPlaceBinding placeBinding = (ViewItemPlaceBinding) holder.binding;
        placeBinding.setPlace(new PlaceViewModel(mContext, mPlaces.get(position)));
    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }

    public void addPlaces(List<Place> places) {
        mPlaces.addAll(places);
        notifyDataSetChanged();
    }

    public int addPlace(Place place) {
        mPlaces.add(place);
        int index = mPlaces.size() - 1;
        notifyItemChanged(index);
        return index;
    }

    public void clear() {
        mPlaces.clear();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {

        private Context mContext = null;
        private ViewDataBinding binding = null;
        private AppCompatTextView txtTitle = null, txtDescription = null;

        public BindingHolder(ViewItemPlaceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            View view = binding.getRoot();
            this.mContext = itemView.getContext();
            txtTitle = (AppCompatTextView) view.findViewById(R.id.textView_title);
            txtDescription = (AppCompatTextView) itemView.findViewById(R.id.textView_description);
            onSetFonts();
        }
        private void onSetFonts(){
            UIUtils.setFont(mContext, UIUtils.FONT_LATO_BOLD, txtTitle);
            UIUtils.setFont(mContext, UIUtils.FONT_LATO_HEAVY, txtDescription);
        }
    }
}
