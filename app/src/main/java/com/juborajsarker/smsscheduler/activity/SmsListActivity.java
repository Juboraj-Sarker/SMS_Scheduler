package com.juborajsarker.smsscheduler.activity;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.juborajsarker.smsscheduler.java_class.DbHelper;
import com.juborajsarker.smsscheduler.R;
import com.juborajsarker.smsscheduler.java_class.SmsModel;

import java.io.File;

public class SmsListActivity extends ListActivity {

    private final static int REQUEST_CODE = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tab_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:{

                super.onBackPressed();

            }default:{

                return super.onOptionsItemSelected(item);
            }



        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE || resultCode == 0) {
            return;
        }
        int messageId;
        switch (resultCode) {
            case com.juborajsarker.smsscheduler.activity.AddSmsActivity.RESULT_SCHEDULED:
                messageId = R.string.successfully_scheduled;
                break;
            case com.juborajsarker.smsscheduler.activity.AddSmsActivity.RESULT_UNSCHEDULED:
                messageId = R.string.successfully_unscheduled;
                break;
            default:
                messageId = R.string.error_generic;
                break;
        }
        Toast.makeText(getApplicationContext(), getString(messageId), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListAdapter(getSmsListAdapter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((SimpleCursorAdapter) getListAdapter()).getCursor().close();
        DbHelper.closeDbHelper();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((SimpleCursorAdapter) getListAdapter()).getCursor().close();
        DbHelper.closeDbHelper();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View headerView = getLayoutInflater().inflate(R.layout.activity_sms_list, getListView(), false);
        headerView.setClickable(true);
        getListView().addHeaderView(headerView);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(),
                        com.juborajsarker.smsscheduler.activity.AddSmsActivity.class);
                intent.putExtra(com.juborajsarker.smsscheduler.activity.AddSmsActivity.INTENT_SMS_ID, id);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    public void gotoNextActivity(View view) {
        startActivityForResult(new Intent(getApplicationContext(),
                com.juborajsarker.smsscheduler.activity.AddSmsActivity.class), REQUEST_CODE);
    }

    private SimpleCursorAdapter getSmsListAdapter() {
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                DbHelper.getDbHelper(this).getCursor(),
                new String[] { DbHelper.COLUMN_MESSAGE, DbHelper.COLUMN_RECIPIENT_NAME },
                new int[] { android.R.id.text1, android.R.id.text2 }
        );
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                TextView textView = (TextView) view;
                if (textView.getId() == android.R.id.text2) {
                    textView.setText(getFormattedSmsInfo(cursor));
                    return true;
                }
                return false;
            }
        });
        return adapter;
    }

    private String getFormattedSmsInfo(Cursor cursor) {

        String recipient, recipientName, recipientNumber, status, datetime;
        recipientName = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_RECIPIENT_NAME));
        recipientNumber = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_RECIPIENT_NUMBER));
        recipient = recipientName.length() > 0 ? recipientName : recipientNumber;

        switch (cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_STATUS))) {
            case (SmsModel.STATUS_PENDING):
                status = getString(R.string.list_status_pending);
                break;
            case (SmsModel.STATUS_SENT):
                status = getString(R.string.list_status_sent);
                break;
            case (SmsModel.STATUS_DELIVERED):
                status = getString(R.string.list_status_delivered);
                break;
            default:
                status = getString(R.string.list_status_failed);
        }
        datetime = DateUtils.formatDateTime(
                this,
                cursor.getLong(cursor.getColumnIndex(DbHelper.COLUMN_TIMESTAMP_SCHEDULED)),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME
        );
        return getString(R.string.list_sms_info_template, status, recipient, datetime);
    }






    public void rateApp(MenuItem item) {

        rateApps();

    }


    public void shareApp(MenuItem item) {

        ApplicationInfo app = getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.createChooser(intent,"SMS Scheduler");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
        startActivity(Intent.createChooser(intent, "share SMS Scheduler using"));

    }





    public void moreApps(MenuItem item) {


        Intent intent;
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=6155570899607409709&hl"));
        startActivity(intent);

    }



    public void goToAboutActivity(MenuItem item) {


        startActivity(new Intent(SmsListActivity.this, AboutActivity.class));
    }


    public void rateApps() {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }


    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }
}
