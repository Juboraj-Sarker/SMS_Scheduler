package com.juborajsarker.smsscheduler.service;

import android.content.Intent;

import com.juborajsarker.smsscheduler.java_class.DbHelper;
import com.juborajsarker.smsscheduler.java_class.SmsModel;
import com.juborajsarker.smsscheduler.receiver.WakefulBroadcastReceiver;

/**
 * Created by jubor on 1/31/2018.
 */

public class SmsDeliveredService extends SmsIntentService {

    public SmsDeliveredService() {
        super("SmsDeliveredService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
        if (timestampCreated == 0) {
            return;
        }
        SmsModel sms = DbHelper.getDbHelper(this).get(timestampCreated);
        sms.setStatus(SmsModel.STATUS_DELIVERED);
        DbHelper.getDbHelper(this).save(sms);
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }
}

