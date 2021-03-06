package io.yostajsc.izigo.usecase.trip.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import butterknife.ButterKnife;
import io.yostajsc.sdk.designs.decorations.SpacesItemDecoration;
import io.yostajsc.sdk.designs.listeners.RecyclerItemClickListener;
import io.yostajsc.sdk.utils.DimensionUtil;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.trip.adapter.MemberActiveOnMapsAdapter;
import io.yostajsc.izigo.usecase.map.model.Person;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogActiveMembers extends Dialog {

    private RecyclerView recyclerView;

    private Activity mOwnerActivity = null;
    private MemberActiveOnMapsAdapter adapter = null;
    private OnItemSelectListener mOnItemSelected = null;

    public DialogActiveMembers(Context context, OnItemSelectListener onItemSelected) {
        super(context, R.style.CoreAppTheme_Dialog);

        this.mOwnerActivity = (context instanceof Activity) ? (Activity) context : null;
        if (this.mOwnerActivity != null)
            setOwnerActivity(mOwnerActivity);

        this.mOnItemSelected = onItemSelected;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mOwnerActivity = getOwnerActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_dialog_active_members);
        ButterKnife.bind(this);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.windowAnimations = R.style.CoreAppTheme_AnimDialog_Grow;
            params.width = DimensionUtil.getScreenWidth(mOwnerActivity) * 4 / 5;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
        setCancelable(false);
        setCanceledOnTouchOutside(true);
        onApplyViews();
    }

    private void onApplyViews() {
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.adapter = new MemberActiveOnMapsAdapter(mOwnerActivity);
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.addItemDecoration(new SpacesItemDecoration(3));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(mOwnerActivity));
        this.recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mOwnerActivity, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mOnItemSelected.selected(adapter.getItem(position).getId());
                dismiss();
            }
        }));
    }

    public void setData(Person[] people) {
        if (people == null)
            return;
        adapter.replaceAll(people);
    }

    public interface OnItemSelectListener {
        void selected(String s);
    }
}