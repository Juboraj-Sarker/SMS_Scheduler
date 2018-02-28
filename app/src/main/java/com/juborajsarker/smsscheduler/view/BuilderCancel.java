package com.juborajsarker.smsscheduler.view;

import android.view.View;
import android.widget.Button;

import com.juborajsarker.smsscheduler.R;
import com.juborajsarker.smsscheduler.java_class.SmsModel;

/**
 * Created by jubor on 1/31/2018.
 */

public class BuilderCancel extends Builder {

    @Override
    protected Button getView() {
        return (Button) view;
    }

    @Override
    public Button build() {
        getView().setVisibility(sms.getTimestampCreated() > 0 ? View.VISIBLE : View.GONE);
        getView().setText(sms.getStatus().contentEquals(SmsModel.STATUS_PENDING)
                ? R.string.form_button_cancel
                : R.string.form_button_delete
        );
        return getView();
    }
}
