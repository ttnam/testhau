package com.yosta.phuotngay.ui.viewholder;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.ui.view.ImageryView;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class ImageryViewHolder extends RecyclerView.ViewHolder {

    private SimpleDraweeView simpleDraweeView;

    public ImageryViewHolder(View itemView) {
        super(itemView);
        this.simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.simple_drawee_view);
    }

    public void onSetEvent(final ImageryView galleryView) {
        final Uri myUri = Uri.parse(galleryView.getURL());
        this.simpleDraweeView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                simpleDraweeView.getViewTreeObserver().removeOnPreDrawListener(this);
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(myUri)
                        .setResizeOptions(new ResizeOptions(simpleDraweeView.getWidth(), simpleDraweeView.getHeight()))
                        .build();
                PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setOldController(simpleDraweeView.getController())
                        .setImageRequest(request)
                        .build();
                simpleDraweeView.setController(controller);
                simpleDraweeView.setImageURI(galleryView.getURL());
                return true;
            }
        });
    }

    public void onSetContent(ImageryView galleryView) {
        this.simpleDraweeView.setImageURI(galleryView.getURL());
    }

}
