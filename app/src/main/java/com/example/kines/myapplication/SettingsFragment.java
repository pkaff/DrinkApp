package com.example.kines.myapplication;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Rebecca on 2016-06-30.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);
    }
}
