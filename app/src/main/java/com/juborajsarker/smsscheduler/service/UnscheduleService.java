package com.juborajsarker.smsscheduler.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.juborajsarker.smsscheduler.java_class.DbHelper;
import com.juborajsarker.smsscheduler.java_class.Scheduler;
import com.juborajsarker.smsscheduler.notification.NotificationManagerWrapper;

/**
 * Created by jubor on 1/31/2018.
 */

public class UnscheduleService extends SmsIntentService {

    public UnscheduleService() {
        super("UnscheduleService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
        if (timestampCreated == 0) {
            return;
        }
        Log.i(getClass().getName(), "Removing sms " + timestampCreated);
        unschedule(getApplicationContext(), timestampCreated);
    }

    static private void unschedule(Context context, long timestampCreated) {
        new Scheduler(context).unschedule(timestampCreated);
        DbHelper.getDbHelper(context).delete(timestampCreated);
        int id = (int) (timestampCreated / 1000) + 1;
        Log.i(UnscheduleService.class.getName(), "Deleting notification with id " + id);
        new NotificationManagerWrapper(context).cancel(id);
    }
}

