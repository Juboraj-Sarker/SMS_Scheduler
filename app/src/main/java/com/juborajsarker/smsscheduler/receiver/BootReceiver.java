package com.juborajsarker.smsscheduler.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.juborajsarker.smsscheduler.java_class.DbHelper;
import com.juborajsarker.smsscheduler.java_class.Scheduler;
import com.juborajsarker.smsscheduler.java_class.SmsModel;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by jubor on 1/31/2018.
 */

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(getClass().getName(), "Rescheduling all the sms");
        String action = intent.getAction();
        if (TextUtils.isEmpty(action) || !action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            return;
        }
        ArrayList<SmsModel> pendingSms = DbHelper.getDbHelper(context).get(SmsModel.STATUS_PENDING);
        Iterator<SmsModel> i = pendingSms.iterator();
        Scheduler scheduler = new Scheduler(context);
        while (i.hasNext()) {
            scheduler.schedule(i.next());
        }
    }
}