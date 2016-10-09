package com.yosta.fresco;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.yosta.fresco.drawee.ZoomableDraweeView;
import java.util.ArrayList;

public class ImageViewerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> urls;
    private ArrayList<ZoomableDraweeView> drawees;

    public ImageViewerAdapter(Context context, ArrayList<String> urls) {
        this.context = context;
        this.urls = urls;
        generateDrawees();
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ZoomableDraweeView photoDraweeView = drawees.get(position);

        try {
            container.addView(photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return photoDraweeView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((SimpleDraweeView) object);
    }

    public boolean isScaled(int index) {
        return drawees.get(index).getScale() > 1.0f;
    }

    public void resetScale(int index) {
        drawees.get(index).setScale(1.0f, true);
    }

    private void generateDrawees() {
        drawees = new ArrayList<>();
        for (String url : urls) {
            final ZoomableDraweeView drawee = new ZoomableDraweeView(context);
            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            controller.setUri(url);
            controller.setOldController(drawee.getController());
            controller.setControllerListener(getDraweeControllerListener(drawee));
            drawee.setController(controller.build());

            drawees.add(drawee);
        }
    }

    private BaseControllerListener<ImageInfo>
    getDraweeControllerListener(final ZoomableDraweeView drawee) {
        return new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null) {
                    return;
                }
                drawee.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        };
    }
}
