package com.juborajsarker.smsscheduler.receiver;

import com.juborajsarker.smsscheduler.service.ReminderService;

/**
 * Created by jubor on 1/31/2018.
 */

public class ReminderReceiver extends WakefulBroadcastReceiver {

    @Override
    protected Class getServiceClass() {
        return ReminderService.class;
    }
}
