package com.juborajsarker.smsscheduler.receiver;

import com.juborajsarker.smsscheduler.receiver.WakefulBroadcastReceiver;
import com.juborajsarker.smsscheduler.service.SmsDeliveredService;

/**
 * Created by jubor on 1/31/2018.
 */

public class SmsDeliveredReceiver extends WakefulBroadcastReceiver {

    @Override
    protected Class getServiceClass() {
        return SmsDeliveredService.class;
    }
}