package com.juborajsarker.smsscheduler.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.juborajsarker.smsscheduler.receiver.CalendarResolver;

import java.text.DateFormatSymbols;
import java.util.Calendar;

/**
 * Created by jubor on 1/31/2018.
 */

public class BuilderRecurringMonth extends BuilderSpinner {

    @Override
    protected boolean shouldBeVisible() {
        return sms.getRecurringMode().equals(CalendarResolver.RECURRING_YEARLY);
    }

    @Override
    protected void onAdapterItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sms.getCalendar().set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        new CalendarResolver().setCalendar(sms.getCalendar()).setRecurringMode(sms.getRecurringMode()).setMonth(position + 1).advance();
    }

    @Override
    protected int getSelection() {
        return sms.getCalendar().get(Calendar.MONTH);
    }

    @Override
    public View build() {
        values.clear();
        for (String month: new DateFormatSymbols().getMonths()) {
            if (TextUtils.isEmpty(month)) {
                continue;
            }
            values.add(month);
        }
        return super.build();
    }
}
