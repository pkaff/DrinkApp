package com.example.kines.myapplication;

import android.preference.PreferenceActivity;

import java.util.List;

/**
 * Created by Rebecca on 2016-06-30.
 */

public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.headers_preference, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return SettingsFragment.class.getName().equals(fragmentName);
    }
}
