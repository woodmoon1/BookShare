package com.wm.bookshare.ui;

import java.util.Date;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wm.bookshare.R;
import com.wm.bookshare.model.Category;
import com.wm.bookshare.ui.fragment.DrawerFragment;
import com.wm.bookshare.ui.fragment.FeedsFragment;
import com.wm.bookshare.view.BlurFoldingActionBarToggle;
import com.wm.bookshare.view.FoldingDrawerLayout;

@EActivity(R.layout.activity_main)
public class TestActivity  extends BaseActivity {

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
	        actionBar.setIcon(R.drawable.ic_actionbar);
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

	        //setCategory(Category.hot);
	        //replaceFragment(R.id.left_drawer, new DrawerFragment());
	}

}
