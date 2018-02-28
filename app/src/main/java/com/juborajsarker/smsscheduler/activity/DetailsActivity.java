package com.juborajsarker.smsscheduler.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.juborajsarker.smsscheduler.R;
import com.juborajsarker.smsscheduler.java_class.DbHelper;
import com.juborajsarker.smsscheduler.java_class.Scheduler;
import com.juborajsarker.smsscheduler.java_class.SmsModel;

public class DetailsActivity extends AppCompatActivity {


    InterstitialAd mInterstitialAd;

    TextView messageTV, numberTV, dateTV, timeTV, statusTV;
    Button editBTN, deleteBTN;

    final public static int RESULT_UNSCHEDULED = 2;

    private SmsModel sms;
    long timesTrap;
    int fStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }



        messageTV = (TextView) findViewById(R.id.details_message_TV);
        numberTV = (TextView) findViewById(R.id.details_phone_TV);
        dateTV = (TextView) findViewById(R.id.details_date_TV);
        timeTV = (TextView) findViewById(R.id.details_time_TV);
        statusTV = (TextView) findViewById(R.id.details_status_TV);

        editBTN = (Button) findViewById(R.id.editBTN);
        deleteBTN = (Button) findViewById(R.id.deleteBTN);


        Intent intent = getIntent();

        final String message = intent.getStringExtra("message");
        final String number = intent.getStringExtra("number");
        String name = intent.getStringExtra("name");
        String status = intent.getStringExtra("status");
        String time = intent.getStringExtra("time");
        String date = intent.getStringExtra("date");
        int id = intent.getIntExtra("id",0);
        timesTrap = intent.getLongExtra("timesTrap", 0);
        fStatus = intent.getIntExtra("fStatus",0);

        try {

            messageTV.setText(message);

            if (name.equals("")){

                numberTV.setText(number);

            }else {

                numberTV.setText(number + " ( " +  name + " )");
            }

            statusTV.setText(status);
            timeTV.setText(time);
            dateTV.setText(date);

        }catch (Exception e){

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        editBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(DetailsActivity.this, AddSmsActivity.class);
                intent.putExtra("status", 1);
                intent.putExtra("number", number);
                intent.putExtra("message", message);
                intent.putExtra("timesTrap", timesTrap);
                intent.putExtra("fStatus", fStatus);

                startActivity(intent);


            }
        });


        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ProgressDialog progressDialog = new ProgressDialog(DetailsActivity.this);
                progressDialog.setMessage("Please wait ......");
                progressDialog.setCancelable(false);
                progressDialog.show();

                mInterstitialAd = new InterstitialAd(DetailsActivity.this);
                mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen1));

                AdRequest adRequest = new AdRequest.Builder().build();
                mInterstitialAd.loadAd(adRequest);



                mInterstitialAd.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                        showInterstitial();
                    }
                });



                DbHelper.getDbHelper(DetailsActivity.this).delete(timesTrap);
                new Scheduler(getApplicationContext()).unschedule(timesTrap);
                setResult(RESULT_UNSCHEDULED, new Intent());
                Toast.makeText(DetailsActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                DetailsActivity.super.onBackPressed();

            }
        });


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



    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

}
