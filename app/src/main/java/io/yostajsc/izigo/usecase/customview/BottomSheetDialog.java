package io.yostajsc.izigo.usecase.customview;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import io.yostajsc.izigo.R;

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

    LinearLayout mLayoutUpdateShare;

    private Context mContext = null;

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.view_bottom_sheet, null);
        dialog.setContentView(contentView);
        ButterKnife.bind(this, contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        this.mContext = getContext();
        onApplyView(contentView);
        onApplyEvent();
    }

    private void onApplyView(View contentView) {
        // mLayoutMember = (LinearLayout) contentView.findViewById(R.id.layout_member);
        // mLayoutShare = (LinearLayout) contentView.findViewById(R.id.layout_share);
    }

    private void onApplyEvent() {

/*        mLayoutMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               *//* Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client"));*//*
                startActivity(new Intent(mContext, MembersActivity.class));
                onDismiss();
            }
        });*/
       /* mLayoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getString(R.string.str_share)));
*//*
                // TODO
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.putExtra("sms_body",
                        "Xin chao ban! Hay tham gia ... voi toi nha. Hen gap ban tai su kien sap toi.");
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);*//*
                onDismiss();
            }
        });*/
       /* layoutGooglePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                *//**//*Toast.makeText(getActivity(), "onGooglePlus", Toast.LENGTH_SHORT).show();
                Intent shareIntent = new PlusShare.Builder(getActivity())
                        .setType("text/plain")
                        .setText("Xin Chào!")
                        .setContentUrl(Uri.parse("http://doantn.hcmus.edu.vn/"))
                        .getIntent();

                startActivityForResult(shareIntent, 0);
                onDismiss();
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
                onDismiss();
            }
        });*/
    }
}