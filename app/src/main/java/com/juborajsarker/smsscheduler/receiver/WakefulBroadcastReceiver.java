package com.juborajsarker.smsscheduler.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.util.SparseArray;

/**
 * Created by jubor on 1/31/2018.
 */

public abstract class WakefulBroadcastReceiver extends BroadcastReceiver {
    private static final String EXTRA_WAKE_LOCK_ID = "android.support.content.wakelockid";

    private static final SparseArray<PowerManager.WakeLock> mActiveWakeLocks
            = new SparseArray<PowerManager.WakeLock>();
    private static int mNextId = 1;

    /**
     * Do a {@link android.content.Context#startService(android.content.Intent)
     * Context.startService}, but holding a wake lock while the service starts.
     * This will modify the Intent to hold an extra identifying the wake lock;
     * when the service receives it in {@link android.app.Service#onStartCommand
     * Service.onStartCommand}, it should pass back the Intent it receives there to
     * {@link #completeWakefulIntent(android.content.Intent)} in order to release
     * the wake lock.
     *
     * @param context The Context in which it operate.
     * @param intent The Intent with which to start the service, as per
     * {@link android.content.Context#startService(android.content.Intent)
     * Context.startService}.
     */
    public static ComponentName startWakefulService(Context context, Intent intent) {
        synchronized (mActiveWakeLocks) {
            int id = mNextId;
            mNextId++;
            if (mNextId <= 0) {
                mNextId = 1;
            }

            intent.putExtra(EXTRA_WAKE_LOCK_ID, id);
            ComponentName comp = context.startService(intent);
            if (comp == null) {
                return null;
            }

            PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "wake:" + comp.flattenToShortString());
            wl.setReferenceCounted(false);
            wl.acquire(60*1000);
            mActiveWakeLocks.put(id, wl);
            return comp;
        }
    }

    /**
     * Finish the execution from a previous {@link #startWakefulService}.  Any wake lock
     * that was being held will now be released.
     *
     * @param intent The Intent as originally generated by {@link #startWakefulService}.
     * @return Returns true if the intent is associated with a wake lock that is
     * now released; returns false if there was no wake lock specified for it.
     */
    public static boolean completeWakefulIntent(Intent intent) {
        final int id = intent.getIntExtra(EXTRA_WAKE_LOCK_ID, 0);
        if (id == 0) {
            return false;
        }
        synchronized (mActiveWakeLocks) {
            PowerManager.WakeLock wl = mActiveWakeLocks.get(id);
            if (wl != null) {
                wl.release();
                mActiveWakeLocks.remove(id);
                return true;
            }
            // We return true whether or not we actually found the wake lock
            // the return code is defined to indicate whether the Intent contained
            // an identifier for a wake lock that it was supposed to match.
            // We just log a warning here if there is no wake lock found, which could
            // happen for example if this function is called twice on the same
            // intent or the process is killed and restarted before processing the intent.
            Log.w("WakefulBroadcastReceive", "No active wake lock id #" + id);
            return true;
        }
    }

    static public final String RESULT_CODE = "resultCode";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(getClass().getName(), "Received intent");
        Intent serviceIntent = new Intent(context, getServiceClass());
        if (null != intent.getExtras()) {
            serviceIntent.putExtras(intent.getExtras());
        }
        serviceIntent.putExtra(RESULT_CODE, getResultCode());
        startWakefulService(context, serviceIntent);
    }

    abstract protected Class getServiceClass();
}

