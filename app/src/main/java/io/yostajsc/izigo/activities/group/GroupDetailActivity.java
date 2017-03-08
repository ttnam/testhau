package io.yostajsc.izigo.activities.group;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.view.OwnToolBar;
import io.yostajsc.utils.StorageUtils;
import io.yostajsc.utils.validate.ValidateUtils;

public class GroupDetailActivity extends AppCompatActivity {

    @BindView(R.id.own_toolbar)
    OwnToolBar ownToolBar;

    @BindView(R.id.image_view)
    AppCompatImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        ButterKnife.bind(this);
        onApplyViews();

    }

    private void onApplyViews() {
        ownToolBar.setTitle("Group name").setLeft(R.drawable.ic_vector_back_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        onApplyData();
    }

    private void onApplyData() {
        String groupId = StorageUtils.inject(this).getString(AppDefine.GROUP_ID);
        if (ValidateUtils.canUse(groupId)) {

        }
    }
}
