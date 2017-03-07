package io.yostajsc.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.yostajsc.izigo.R;


public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetViewHolder> {

    private Context mContext = null;
    private List<Integer> mIcons = null;

    public BottomSheetAdapter(Context context) {
        this.mContext = context;
        this.mIcons = new ArrayList<>();


        TypedArray icon = this.mContext.getResources().obtainTypedArray(R.array.bottom_sheet_icon);
        int length = icon.length();
        for (int i = 0; i < length; i++) {
            this.mIcons.add(icon.getResourceId(i, -1));
        }
        icon.recycle();

    }

    @Override
    public BottomSheetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(mContext).inflate(R.layout.item_bottom_sheet, parent, false);
        return new BottomSheetViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(BottomSheetViewHolder holder, int position) {
        holder.bind(mIcons.get(position));
    }

    @Override
    public int getItemCount() {
        return mIcons.size();
    }

}

