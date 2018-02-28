package com.juborajsarker.smsscheduler.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.juborajsarker.smsscheduler.R;

/**
 * Created by jubor on 1/31/2018.
 */

public class SmsSchedulerPreferenceActivity extends PreferenceActivity {

    public static final String PREFERENCE_DELIVERY_REPORTS = "PREFERENCE_DELIVERY_REPORTS";
    public static final String PREFERENCE_REMINDERS = "PREFERENCE_REMINDERS";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);


    }
}
