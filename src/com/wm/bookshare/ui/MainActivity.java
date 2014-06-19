package com.wm.bookshare.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.wm.bookshare.R;
import com.wm.bookshare.model.Category;
import com.wm.bookshare.ui.fragment.BaseFragment;
import com.wm.bookshare.ui.fragment.DrawerFragment;
import com.wm.bookshare.ui.fragment.FeedsFragment;
import com.wm.bookshare.view.BlurFoldingActionBarToggle;
import com.wm.bookshare.view.FoldingDrawerLayout;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @StringRes(R.string.app_name)
	String title;
    
    @ViewById(R.id.blur_image)
    ImageView blurImage;
    
	@ViewById(R.id.drawer_layout)
    FoldingDrawerLayout mDrawerLayout;
	
    @ViewById(R.id.content_frame)
    FrameLayout contentLayout;
  

    private BlurFoldingActionBarToggle mDrawerToggle;

    private FeedsFragment mContentFragment;

    private Category mCategory;

    private Menu mMenu;

    @AfterViews
	void initViews() {
        this.setTitle(title);
        actionBar.setIcon(R.drawable.ic_launcher);
        mDrawerLayout.setScrimColor(Color.argb(100, 255, 255, 255));
        mDrawerToggle = new BlurFoldingActionBarToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                setTitle(R.string.app_name);
                mMenu.findItem(R.id.action_refresh).setVisible(false);
            }

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                setTitle(mCategory.getDisplayName());
                mMenu.findItem(R.id.action_refresh).setVisible(true);

                blurImage.setVisibility(View.GONE);
                blurImage.setImageBitmap(null);
            }
        };
        mDrawerToggle.setBlurImageAndView(blurImage, contentLayout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        setCategory(Category.bestBook);
        replaceFragment(R.id.left_drawer, new DrawerFragment());
    }

   @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    protected void replaceFragment(int viewId, BaseFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    
    }
  
    
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	getSupportMenuInflater().inflate(R.menu.main, menu);
         mMenu = menu;
         return true;
	}

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
         switch (item.getItemId()) {
             case android.R.id.home:
            	 mDrawerLayout.openDrawer(Gravity.START);
             case R.id.action_refresh:
                 mContentFragment.loadFirstAndScrollToTop();
                 return true;
             case R.id.action_camera:
            	 takePhoto();
                 return true;
             default:
                 return super.onOptionsItemSelected(item);
         }
	}
    /**
     * 拍照
     */
   private void takePhoto(){
    Intent intent = new Intent();  
    intent.setClass(this, CameraActivity.class);  
    startActivity(intent);  
   }
    public void setCategory(Category category) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        if (mCategory == category) {
            return;
        }
        mCategory = category;
        setTitle(mCategory.getDisplayName());
        mContentFragment = (FeedsFragment) FeedsFragment.newInstance(category);
       replaceFragment(R.id.content_frame, mContentFragment);
    }
}
