package com.wm.bookshare.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.wm.bookshare.R;
import com.wm.bookshare.util.DrawableUtil;
import com.wm.bookshare.view.ProgressWheel;
import com.wm.bookshare.view.swipeback.SwipeBackActivity;


@EActivity(R.layout.activity_imageview)
public class ImageViewActivity extends SwipeBackActivity {
    public static final String IMAGE_URL = "image_url";
    public static final String IMAGE_BYTE = "image_byte";

    @ViewById(R.id.photoView)
    PhotoView photoView;

    @ViewById(R.id.progressWheel)
    ProgressWheel progressWheel;

    private PhotoViewAttacher mAttacher;

    @AfterViews
   	void initViews() {

        setTitle(R.string.view_big_image);
    
        mAttacher = new PhotoViewAttacher(photoView);
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish();
            }
        });

        String imageUrl = getIntent().getStringExtra(IMAGE_URL);
        byte[] imageByte = getIntent().getByteArrayExtra(IMAGE_BYTE);
    
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc(true)
                .considerExifParams(true).build();
        
        if(imageUrl!=null && imageUrl.length()>0){
        ImageLoader.getInstance().displayImage(imageUrl, photoView, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressWheel.setVisibility(View.GONE);
                mAttacher.update();
            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                progressWheel.setProgress(360 * current / total);
            }
        });
            }
        else if(imageByte !=null && imageByte.length>0){
        	
        	Bitmap  bmp=DrawableUtil.bytes2Bimap(imageByte);
        	photoView.setImageBitmap(bmp);
        	progressWheel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAttacher != null) {
            mAttacher.cleanup();
        }
    }
}
