package com.juborajsarker.smsscheduler.receiver;

import com.juborajsarker.smsscheduler.service.SmsSentService;

/**
 * Created by jubor on 1/31/2018.
 */

public class SmsSentReceiver extends WakefulBroadcastReceiver {

    @Override
    protected Class getServiceClass() {
        return SmsSentService.class;
    }
}

