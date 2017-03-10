package io.yostajsc.izigo.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.designs.decorations.SpacesItemDecoration;
import io.yostajsc.designs.listeners.RecyclerItemClickListener;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.trip.AddTripActivity;
import io.yostajsc.izigo.activities.trip.GroupDetailActivity;
import io.yostajsc.izigo.adapters.GroupAdapter;
import io.yostajsc.view.OwnToolBar;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class OwnTripFragment extends Fragment {

    @BindView(R.id.own_toolbar)
    OwnToolBar mOwnToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.layout_empty)
    LinearLayout layoutEmpty;

    private Context mContext = null;
    private GroupAdapter groupAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_own_trip, container, false);
        ButterKnife.bind(this, rootView);
        // this.groupAdapter = new GroupAdapter(mContext);

        mOwnToolbar.setRight(R.drawable.ic_vector_add_group, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGroup();
            }
        });

        onApplyRecyclerView();
        onApplyData();
        return rootView;
    }

    public void onApplyData() {
       /* this.groupAdapter.clear();
        for (int i = 0; i < 20; i++) {
            this.groupAdapter.add(new FirebaseGroup());
        }*/
    }

    private void onApplyRecyclerView() {
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setItemAnimator(new SlideInLeftAnimator());
        this.recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.recyclerView.addItemDecoration(new SpacesItemDecoration(16));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.mContext, LinearLayoutManager.VERTICAL, false);
        this.recyclerView.setNestedScrollingEnabled(false);
        this.recyclerView.setLayoutManager(layoutManager);
        // this.recyclerView.setAdapter(this.groupAdapter);
        this.recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(getActivity(), GroupDetailActivity.class));
            }
        }));
    }

    @OnClick(R.id.button)
    public void addGroup() {
        startActivity(new Intent(getActivity(), AddTripActivity.class));
    }
}
