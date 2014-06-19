package com.wm.bookshare.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;

import android.app.ActionBar;

import com.wm.bookshare.R;
import com.wm.bookshare.ui.fragment.*;
import com.wm.bookshare.view.swipeback.SwipeBackActivity;

/**
 * Created by storm on 14-4-16.
 */
@EActivity(R.layout.activity_preference)
public class PreferenceActivity extends SwipeBackActivity {
    @AfterViews
   	void initViews() {
        try {
        //Fragment	fragment=(Fragment) GeneratedClassUtils.get(PreferenceFragment.class).newInstance();
        getFragmentManager().beginTransaction().replace(R.id.container,new PreferenceFragment_()).commit();
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.action_settings);
        } catch (Exception e) {
		
			e.printStackTrace();
		} 
    }
    
    @OptionsItem(android.R.id.home)
    void homeSelected() {
    	 this.finish();
    }
    
}
