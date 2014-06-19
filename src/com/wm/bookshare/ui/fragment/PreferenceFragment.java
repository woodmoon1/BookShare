package com.wm.bookshare.ui.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;

import com.wm.bookshare.R;

/**
 * Created by storm on 14-4-16.
 */
@EFragment
public class PreferenceFragment extends android.preference.PreferenceFragment {

    @AfterViews
  	void initViews() {
        addPreferencesFromResource(R.xml.preference);

        // 设置版本号
        Preference versionPreference = findPreference(getString(R.string.pref_key_version));
        PackageInfo packageInfo;
        try {
            packageInfo = getActivity().getPackageManager().getPackageInfo(
                    getActivity().getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionPreference.setTitle("v" + packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
