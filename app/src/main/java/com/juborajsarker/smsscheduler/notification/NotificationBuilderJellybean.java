package com.juborajsarker.smsscheduler.notification;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

/**
 * Created by jubor on 1/31/2018.
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
class NotificationBuilderJellybean extends NotificationBuilderHoneycomb {

    @Override
    public NotificationBuilder addAction(int iconId, int stringId, PendingIntent pendingIntent) {
        builder.addAction(iconId, context.getString(stringId), pendingIntent);
        return this;
    }

    public NotificationBuilderJellybean(Context context) {
        super(context);
    }
}
