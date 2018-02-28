package com.juborajsarker.smsscheduler.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.juborajsarker.smsscheduler.R;
import com.juborajsarker.smsscheduler.java_class.DbHelper;
import com.juborajsarker.smsscheduler.java_class.Scheduler;
import com.juborajsarker.smsscheduler.java_class.SmsModel;
import com.juborajsarker.smsscheduler.view.BuilderCancel;
import com.juborajsarker.smsscheduler.view.BuilderContact;
import com.juborajsarker.smsscheduler.view.BuilderDate;
import com.juborajsarker.smsscheduler.view.BuilderMessage;
import com.juborajsarker.smsscheduler.view.BuilderRecurringMode;
import com.juborajsarker.smsscheduler.view.BuilderSimCard;
import com.juborajsarker.smsscheduler.view.BuilderTime;
import com.juborajsarker.smsscheduler.view.EmptinessTextWatcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

public class AddSmsActivity extends AppCompatActivity {

    Spinner mySpinner;
    InterstitialAd mInterstitialAd;
    long timesTrap;
    int status;
    int fStatus;

    Button scheduleBTN;

    LinearLayout scheduleDate, scheduleMonthName, scheduleMonthDay;

    final public static int RESULT_SCHEDULED = 1;
    final public static int RESULT_UNSCHEDULED = 2;
    final public static String INTENT_SMS_ID = "INTENT_SMS_ID";

    final private static String SMS_STATE = "SMS_STATE";
    final private static int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    final private String[] permissionsRequired = new String[] {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CONTACTS
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sms);


        scheduleDate = (LinearLayout) findViewById(R.id.schedule_date_LAYOUT);
        scheduleMonthName = (LinearLayout) findViewById(R.id.schedule_month_name_LAYOUT);
        scheduleMonthDay = (LinearLayout) findViewById(R.id.schedule_monthNo_LAYOUT);

        scheduleBTN = (Button) findViewById(R.id.button_add);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }



        mySpinner = (Spinner) findViewById(R.id.form_recurring_mode);









        long smsId = getIntent().getLongExtra(INTENT_SMS_ID, 0L);
        if (smsId > 0) {
            sms = DbHelper.getDbHelper(this).get(smsId);
        } else if (null != savedInstanceState) {
            sms = savedInstanceState.getParcelable(SMS_STATE);
        }
        if (null == sms) {
            sms = new SmsModel();
        }






    }






    private SmsModel sms;
    private ArrayList<String> permissionsGranted = new ArrayList<>();

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
    protected void onResume() {
        super.onResume();
        if (permissionsGranted()) {
            buildForm();

        }
    }

    private void buildForm() {
        EditText formMessage = findViewById(R.id.form_input_message);
        AutoCompleteTextView formContact = findViewById(R.id.form_input_contact);
        TextWatcher watcherEmptiness = new EmptinessTextWatcher(this, formContact, formMessage);
        formContact.addTextChangedListener(watcherEmptiness);
        formMessage.addTextChangedListener(watcherEmptiness);

        new BuilderMessage().setView(formMessage).setSms(sms).build();
        new BuilderContact().setView(formContact).setSms(sms).setActivity(this).build();

        new BuilderSimCard().setActivity(this).setView(findViewById(R.id.form_sim_card)).setSms(sms).build();
        new BuilderRecurringMode()
                .setRecurringDayView((Spinner) findViewById(R.id.form_recurring_day))
                .setRecurringMonthView((Spinner) findViewById(R.id.form_recurring_month))
                .setDateView((DatePicker) findViewById(R.id.form_date))
                .setDateLayoutView((LinearLayout) findViewById(R.id.schedule_date_LAYOUT))
                .setMonthNameLayoutView((LinearLayout) findViewById(R.id.schedule_monthNo_LAYOUT))
                .setMonthNoLayoutView((LinearLayout) findViewById(R.id.schedule_month_name_LAYOUT))
                .setWeekMonthTvText((TextView) findViewById(R.id.weekly_monthly_TV))
                .setActivity(this)
                .setView(findViewById(R.id.form_recurring_mode))
                .setSms(sms)
                .build()
        ;

        new BuilderTime().setActivity(this).setView(findViewById(R.id.form_time)).setSms(sms).build();
        new BuilderDate().setActivity(this).setView(findViewById(R.id.form_date)).setSms(sms).build();

        new BuilderCancel().setView(findViewById(R.id.button_cancel)).setSms(sms).build();




        Intent intent = getIntent();

        status = intent.getIntExtra("status", 0);
        String message = intent.getStringExtra("message");
        String number = intent.getStringExtra("number");
        timesTrap = intent.getLongExtra("timesTrap",0);
        fStatus = intent.getIntExtra("fStatus",0);

        if (status == 1){

            formMessage.setText(message);
            formContact.setText(number);
            scheduleBTN.setText("Re Schedule");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DbHelper.closeDbHelper();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DbHelper.closeDbHelper();
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != sms) {
            outState.putParcelable(SMS_STATE, sms);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (null == sms) {
            sms = savedInstanceState.getParcelable(SMS_STATE);
        }
        if (null == sms) {
            sms = new SmsModel();
        }
    }







    public void scheduleSms(View view) {

        ProgressDialog progressDialog = new ProgressDialog(AddSmsActivity.this);
        progressDialog.setMessage("Please wait ......");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (!validateForm()) {
            return;
        }


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen1));

        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);



        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });


        if ( (status == 1) ){


            if (fStatus == 1 || fStatus == 3){

                DbHelper.getDbHelper(AddSmsActivity.this).delete(timesTrap);
                new Scheduler(getApplicationContext()).unschedule(timesTrap);
                setResult(RESULT_UNSCHEDULED, new Intent());
            }



        }


        sms.setStatus(SmsModel.STATUS_PENDING);
        DbHelper.getDbHelper(this).save(sms);
        new Scheduler(getApplicationContext()).schedule(sms);
        setResult(RESULT_SCHEDULED, new Intent());
        progressDialog.dismiss();
        startActivity(new Intent(AddSmsActivity.this, HomeActivity.class));
        finish();
    }

    public void unscheduleSms(View view) {
        DbHelper.getDbHelper(this).delete(sms.getTimestampCreated());
        new Scheduler(getApplicationContext()).unschedule(sms.getTimestampCreated());
        setResult(RESULT_UNSCHEDULED, new Intent());
        finish();
    }

    private boolean validateForm() {
        boolean result = true;
        if (sms.getTimestampScheduled() < GregorianCalendar.getInstance().getTimeInMillis()) {
            Toast.makeText(getApplicationContext(), getString(R.string.form_validation_datetime), Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (TextUtils.isEmpty(sms.getRecipientNumber())) {
            ((AutoCompleteTextView) findViewById(R.id.form_input_contact)).setError(getString(R.string.form_validation_contact));
            result = false;
        }
        if (TextUtils.isEmpty(sms.getMessage())) {
            ((EditText) findViewById(R.id.form_input_message)).setError(getString(R.string.form_validation_message));
            result = false;
        }
        return result;
    }

    private boolean permissionsGranted() {
        boolean granted = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissionsNotGranted = new ArrayList<>();
            for (String required : this.permissionsRequired) {
                if (checkSelfPermission(required) != PackageManager.PERMISSION_GRANTED) {
                    permissionsNotGranted.add(required);
                } else {
                    this.permissionsGranted.add(required);
                }
            }
            if (permissionsNotGranted.size() > 0) {
                granted = false;
                String[] notGrantedArray = permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]);
                requestPermissions(notGrantedArray, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        }
        return granted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                List<String> requiredPermissions = Arrays.asList(this.permissionsRequired);
                String permission;
                for (int i = 0; i < permissions.length; i++) {
                    permission = permissions[i];
                    if (requiredPermissions.contains(permission) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        this.permissionsGranted.add(permission);
                    }
                }
                if (this.permissionsGranted.size() == this.permissionsRequired.length) {
                    buildForm();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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


        startActivity(new Intent(AddSmsActivity.this, AboutActivity.class));
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




    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }



}
