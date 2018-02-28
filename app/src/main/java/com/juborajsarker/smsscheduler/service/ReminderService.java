package com.juborajsarker.smsscheduler.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.juborajsarker.smsscheduler.java_class.DbHelper;
import com.juborajsarker.smsscheduler.R;
import com.juborajsarker.smsscheduler.java_class.SmsModel;
import com.juborajsarker.smsscheduler.activity.SmsListActivity;
import com.juborajsarker.smsscheduler.notification.NotificationManagerWrapper;
import com.juborajsarker.smsscheduler.receiver.WakefulBroadcastReceiver;

/**
 * Created by jubor on 1/31/2018.
 */

public class ReminderService extends SmsIntentService {

    public ReminderService() {
        super("ReminderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
        if (timestampCreated == 0) {
            return;
        }
        SmsModel sms = DbHelper.getDbHelper(getApplicationContext()).get(timestampCreated);
        if (null == sms) {
            Log.i(getClass().getName(), "No sms created at " + timestampCreated + " found");
            return;
        }
        Log.i(getClass().getName(), "Reminding about sms " + timestampCreated);
        remind(getApplicationContext(), sms);
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }

    static private void remind(Context context, SmsModel sms) {
        Intent intentUnschedule = new Intent(context, UnscheduleService.class);
        intentUnschedule.putExtra(DbHelper.COLUMN_TIMESTAMP_CREATED, sms.getTimestampCreated());
        new NotificationManagerWrapper(context).show(
                sms.getId() + 1,
                NotificationManagerWrapper.getBuilder(context)
                        .setTitle(context.getString(R.string.notification_title_will_send_in_an_hour))
                        .setMessage(context.getString(R.string.notification_message_will_send_in_an_hour, sms.getRecipientName()))
                        .setIntent(new Intent(context, SmsListActivity.class))
                        .addAction(
                                android.R.drawable.ic_menu_close_clear_cancel,
                                R.string.form_button_cancel,
                                PendingIntent.getService(context, sms.getId(), intentUnschedule, PendingIntent.FLAG_UPDATE_CURRENT & Intent.FILL_IN_DATA)
                        )
                        .build()
        );
    }
}
