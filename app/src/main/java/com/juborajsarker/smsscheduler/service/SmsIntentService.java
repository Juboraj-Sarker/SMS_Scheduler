package com.juborajsarker.smsscheduler.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.juborajsarker.smsscheduler.java_class.DbHelper;

/**
 * Created by jubor on 1/31/2018.
 */

abstract public class SmsIntentService extends IntentService {

    protected long timestampCreated;

    public SmsIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(getClass().getName(), "Handling intent");
        timestampCreated = intent.getLongExtra(DbHelper.COLUMN_TIMESTAMP_CREATED, 0L);
        if (timestampCreated == 0) {
            Log.i(getClass().getName(), "Cannot identify sms: no creation timestamp provided");
        }
    }
}
