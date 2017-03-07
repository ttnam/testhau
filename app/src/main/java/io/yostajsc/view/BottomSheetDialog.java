package io.yostajsc.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.designs.decorations.SpacesItemDecoration;
import io.yostajsc.izigo.R;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by HenryPhuc on 6/9/2016.
 */
public class BottomSheetDialog extends android.support.design.widget.BottomSheetDialogFragment {

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };
/*

    private LinearLayout layoutEmail;
    private LinearLayout layoutMessage;
    private LinearLayout layoutGooglePlus;
    private LinearLayout layoutFacebook;
    private TextView textView;*/

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Context mContext = null;
    private BottomSheetAdapter mAdapter = null;

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        /*setupFB();*/

        View contentView = View.inflate(getContext(), R.layout.view_bottom_sheet, null);
        dialog.setContentView(contentView);
        ButterKnife.bind(this, contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        this.mContext = getContext();
        this.mAdapter = new BottomSheetAdapter(this.mContext);

        onApplyRecycleView();
/*

        onApplyView(contentView);
        onApplyAnimation();
*/


        /*onApplyEvent();*/

    }

    private void onApplyRecycleView() {
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setItemAnimator(new SlideInUpAnimator());
        this.recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.recyclerView.addItemDecoration(new SpacesItemDecoration(3));
        this.recyclerView.setLayoutManager(new GridLayoutManager(this.mContext, 5, GridLayoutManager.VERTICAL, false));
        this.recyclerView.setAdapter(this.mAdapter);
    }
/*
    private void onApplyView(View contentView) {
        layoutGooglePlus = (LinearLayout) contentView.findViewById(R.id.layout_google_plus);
        layoutMessage = (LinearLayout) contentView.findViewById(R.id.layout_message);
        layoutEmail = (LinearLayout) contentView.findViewById(R.id.layout_email);
        layoutFacebook = (LinearLayout) contentView.findViewById(R.id.layout_facebook);
        textView = (TextView) contentView.findViewById(R.id.text_view);
        textView.setText(String.format("%s", "Share every where you want ..."));
    }

    private void onApplyAnimation() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_up_from_bottom);
        layoutEmail.startAnimation(animation);
        layoutMessage.startAnimation(animation);
        layoutGooglePlus.startAnimation(animation);
        layoutFacebook.startAnimation(animation);
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_down_from_top);
        textView.startAnimation(animation);
    }*/

    /*private void onApplyEvent() {

        layoutEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client"));

                dismiss();
            }
        });
        layoutMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.putExtra("sms_body",
                        "Xin chao ban! Hay tham gia ... voi toi nha. Hen gap ban tai su kien sap toi.");
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);
                dismiss();
            }
        });
        layoutGooglePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                *//*Toast.makeText(getActivity(), "onGooglePlus", Toast.LENGTH_SHORT).show();
                Intent shareIntent = new PlusShare.Builder(getActivity())
                        .setType("text/plain")
                        .setText("Xin Chào!")
                        .setContentUrl(Uri.parse("http://doantn.hcmus.edu.vn/"))
                        .getIntent();

                startActivityForResult(shareIntent, 0);
                dismiss();*//*
            }
        });
        layoutFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                Toast.makeText(getActivity(), "onFacebook", Toast.LENGTH_SHORT).show();
                ShareLinkContent contentFB = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("http://doantn.hcmus.edu.vn/")).setContentDescription("Xin Chào")
                        .build();
                ShareDialog shareDialog = new ShareDialog(mActivity);
                shareDialog.show(contentFB, ShareDialog.Mode.AUTOMATIC);
                dismiss();
            }
        });
    }*/
    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }*/
    /*public void setupFB()
    {
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        AppEventsLogger.activateApp(getActivity().getApplication());
    }*/
}