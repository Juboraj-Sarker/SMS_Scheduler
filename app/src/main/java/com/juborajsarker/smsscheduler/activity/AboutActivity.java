package com.juborajsarker.smsscheduler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.juborajsarker.smsscheduler.R;

public class AboutActivity extends AppCompatActivity {

   // InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }



        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_home_footer_1));
        AdView mAdView = (AdView) findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
        mAdView.loadAd(adRequest);
    }


    public void myWebsite(View view) {

//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen1));
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mInterstitialAd.loadAd(adRequest);
//
//
//
//        mInterstitialAd.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                showInterstitial();
//            }
//        });


        Intent intent = new Intent(AboutActivity.this, WebviewActivity.class);
        intent.putExtra("url", "https://www.juborajsarker.com");
        startActivity(intent);

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

//    private void showInterstitial() {
//        if (mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//        }
//    }

}
