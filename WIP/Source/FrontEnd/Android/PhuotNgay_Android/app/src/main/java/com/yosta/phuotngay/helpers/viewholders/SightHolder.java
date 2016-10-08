package com.yosta.phuotngay.helpers.viewholders;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yosta.circleimageview.CircleImageView;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.dialogs.DialogComment;
import com.yosta.phuotngay.models.views.SightView;

import java.util.Random;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class SightHolder extends RecyclerView.ViewHolder {

    private TextView txtAuthor, txtTime, txtStatus, txtDescription;
    private TextView txtNCmt, txtNLike, txtNShare;
    private CircleImageView imgAvatar;
    private AppCompatImageView imgContent, imgStatus;

    public SightHolder(View itemView) {
        super(itemView);

        this.txtTime = (TextView) itemView.findViewById(R.id.txt_time);
        this.txtAuthor = (TextView) itemView.findViewById(R.id.txt_author);
        this.txtStatus = (TextView) itemView.findViewById(R.id.txt_status);
        this.txtDescription = (TextView) itemView.findViewById(R.id.txt_description);

        this.imgAvatar = (CircleImageView) itemView.findViewById(R.id.img_avatar);
        this.imgContent = (AppCompatImageView) itemView.findViewById(R.id.image);
        this.imgStatus = (AppCompatImageView) itemView.findViewById(R.id.img_Status);


        this.txtNCmt = (TextView) itemView.findViewById(R.id.txt_ncmt);
        this.txtNLike = (TextView) itemView.findViewById(R.id.txt_nlike);
        this.txtNShare = (TextView) itemView.findViewById(R.id.txt_nshare);
    }

    public void onSetEvent(SightView views) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogComment dialog = new DialogComment(itemView.getContext());
                dialog.show();
            }
        });
    }

    private Random random = new Random();
    private int a[] = new int[]{
            R.drawable.ic_vector_circle_blue,
            R.drawable.ic_vector_circle_green,
            R.drawable.ic_vector_circle_yellow};

    public void onSetContent(SightView views) {

        Picasso.with(itemView.getContext()).load(R.drawable.ic_avatar).into(this.imgAvatar); // Avatar
        Picasso.with(itemView.getContext()).load(R.drawable.ic_sample).into(this.imgContent); // Content

        this.imgStatus.setImageResource(a[random.nextInt() % 2 + 1]);
        this.txtAuthor.setText("Phuc Henry");
        this.txtTime.setText(String.valueOf(random.nextInt() % 10 + 2) + " MINS AGO");
    }
}
