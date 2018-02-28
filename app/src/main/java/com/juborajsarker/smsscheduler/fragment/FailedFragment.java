package com.juborajsarker.smsscheduler.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.juborajsarker.smsscheduler.R;
import com.juborajsarker.smsscheduler.activity.AddSmsActivity;
import com.juborajsarker.smsscheduler.activity.DetailsActivity;
import com.juborajsarker.smsscheduler.java_class.CustomAdapter;
import com.juborajsarker.smsscheduler.java_class.DbHelper;
import com.juborajsarker.smsscheduler.java_class.SmsModel;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class FailedFragment extends Fragment {

    InterstitialAd mInterstitialAd;


    View view;
    private final static int REQUEST_CODE = 1;

    private SmsModel sms;
    final public static int RESULT_SCHEDULED = 1;
    DbHelper dbHelper = new DbHelper(getContext());

    CustomAdapter customAdapter;
    ArrayList<SmsModel> failedList;

    ListView listView;

    String finalTime;
    String finalMonth;


    FloatingActionButton fab;



    public FailedFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_failed, container, false);


        fab = (FloatingActionButton) view.findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Please wait ......");
                progressDialog.setCancelable(false);
                progressDialog.show();


                mInterstitialAd = new InterstitialAd(getContext());
                mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen1));

                AdRequest adRequest = new AdRequest.Builder().build();
                mInterstitialAd.loadAd(adRequest);



                mInterstitialAd.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                        showInterstitial();
                    }
                });


                progressDialog.dismiss();
                startActivity(new Intent(getContext(), AddSmsActivity.class));
            }
        });






        listView = (ListView) view.findViewById(R.id.failed_LV);

        try {

             failedList = DbHelper.getDbHelper(getContext()).get(SmsModel.STATUS_FAILED);

            if (failedList.isEmpty()){

                Log.d("Empty", "No failed message");

            }else {


                customAdapter = new CustomAdapter(getContext(),failedList);
                listView.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(getContext(), DetailsActivity.class);

                        String message = failedList.get(position).getMessage();
                        String number = failedList.get(position).getRecipientNumber();
                        String name = failedList.get(position).getRecipientName();
                        String status = failedList.get(position).getStatus();




                        String year = String.valueOf(failedList.get(position).getCalendar().get(GregorianCalendar.YEAR));
                        String month = String.valueOf(failedList.get(position).getCalendar().get(GregorianCalendar.MONTH));
                        String day = String.valueOf(failedList.get(position).getCalendar().get(GregorianCalendar.DAY_OF_MONTH));

                        String hour = String.valueOf(failedList.get(position).getCalendar().get(Calendar.HOUR_OF_DAY));
                        String minute = String.valueOf(failedList.get(position).getCalendar().get(Calendar.MINUTE));



                        boolean pm = false;

                        if (Integer.parseInt(hour) > 12){

                            pm = true;
                            int tempHour = Integer.parseInt(hour) - 12;
                            hour = String.valueOf(tempHour);
                        }

                        if (pm){

                            finalTime = hour + ":" + minute + " pm";

                        }else {

                            finalTime = hour + ":" + minute + " am";
                        }

                        finalMonth = getMonthForInt(Integer.parseInt(month)) + " " + day+ ", " + year;




                        intent.putExtra("message", message);
                        intent.putExtra("number", number);
                        intent.putExtra("name", name);
                        intent.putExtra("status", status);
                        intent.putExtra("time", finalTime);
                        intent.putExtra("date", finalMonth);
                        intent.putExtra("timesTrap", failedList.get(position).getTimestampCreated());
                        intent.putExtra("fStatus",3);

                        startActivity(intent);

                    }
                });
            }

        }catch (Exception e){

            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }








        return view;
    }



    @Override
    public void onResume() {
        super.onResume();

        failedList = DbHelper.getDbHelper(getContext()).get(SmsModel.STATUS_FAILED);
        customAdapter = new CustomAdapter(getContext(),failedList);
        listView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
    }




    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }




    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }



}
