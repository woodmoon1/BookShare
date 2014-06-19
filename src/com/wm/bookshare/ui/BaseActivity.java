package com.wm.bookshare.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;

import android.app.ActionBar;
import android.content.Intent;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wm.bookshare.R;
import com.wm.bookshare.data.RequestManager;
import com.wm.bookshare.util.ToastUtils;

/**
 * Created by storm on 14-3-24.
 */
@EActivity
public abstract class BaseActivity extends SherlockFragmentActivity {
    protected ActionBar actionBar;

    @AfterViews
	protected void initBaseViews() {
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
   
 /*   @OptionsItem(android.R.id.home)
    public boolean home() {
    	 onBackPressed();
    	 return true;
    }*/

    @OptionsItem(R.id.action_settings)
    public boolean setting() {
    	  startActivity(new Intent(this, PreferenceActivity_.class));
    	  return true;
    }

   
    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
    }

    protected void executeRequest(Request<?> request) {
        RequestManager.addRequest(request, this);
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.showLong(error.getMessage());
            }
        };
    }
}
