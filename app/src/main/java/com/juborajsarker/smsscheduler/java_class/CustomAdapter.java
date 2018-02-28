package com.juborajsarker.smsscheduler.java_class;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.juborajsarker.smsscheduler.R;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by jubor on 2/1/2018.
 */

public class CustomAdapter extends BaseAdapter {

    Context context;
    private ArrayList<SmsModel> smsModels;

    public CustomAdapter(Context context, ArrayList<SmsModel> smsModels) {
        this.context = context;
        this.smsModels = smsModels;
    }

    @Override
    public int getCount() {
        return smsModels.size();
    }

    @Override
    public Object getItem(int position) {
        return smsModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.sms_layout, parent, false);
        }


        SmsModel currentItem = (SmsModel) getItem(position);

        TextView messageTV = (TextView) convertView.findViewById(R.id.message_TV);
        TextView phoneTV = (TextView) convertView.findViewById(R.id.phone_TV);
        TextView dateTV = (TextView) convertView.findViewById(R.id.date_TV);
        TextView timeTV = (TextView) convertView.findViewById(R.id.time_TV);
        TextView yearTV = (TextView) convertView.findViewById(R.id.year_TV);

        ImageView iv = (ImageView) convertView.findViewById(R.id.image_view);
     //   TextView statusTV = (TextView) convertView.findViewById(R.id.status_TV);

        //sets the text for item name and item description from the current item object
        messageTV.setText(currentItem.getMessage());
     //   phoneTV.setText(currentItem.getRecipientNumber());

        String status = currentItem.getStatus();

        String year = String.valueOf(currentItem.getCalendar().get(GregorianCalendar.YEAR));
        String month = String.valueOf(currentItem.getCalendar().get(GregorianCalendar.MONTH));
        String day = String.valueOf(currentItem.getCalendar().get(GregorianCalendar.DAY_OF_MONTH));

        String hour = String.valueOf(currentItem.getCalendar().get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(currentItem.getCalendar().get(Calendar.MINUTE));



        boolean pm = false;
        String finalTime = "";

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


        String finalMonth = getMonthForInt(Integer.parseInt(month)) + " " + day+ ", " + year;


        dateTV.setText(getMonthForInt(Integer.parseInt(month)).substring(0,3) + " " + day);
        yearTV.setText(year);
        timeTV.setText(finalTime);

        if (status.equals("PENDING")){

            if (currentItem.getRecipientName().equals("")){

                phoneTV.setText("Will send to: " + currentItem.getRecipientNumber());
                iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pending));

            }else {

                phoneTV.setText("Will send to: " + currentItem.getRecipientName());
                iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pending));
            }



        }else if (status.equals("SENT")){

            if (currentItem.getRecipientName().equals("")){

                phoneTV.setText("Sent to: " + currentItem.getRecipientNumber());
                iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.message_sent));

            }else {

                phoneTV.setText("Sent to: " + currentItem.getRecipientName());
                iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.message_sent));
            }

        }else if (status.equals("FAILED")){

            if (currentItem.getRecipientName().equals("")){

                phoneTV.setText("Failed to sent: " + currentItem.getRecipientNumber());
                iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_failed));

            }else {

                phoneTV.setText("Failed to sent: " + currentItem.getRecipientName());
                iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_failed));
            }
        }
       // statusTV.setText(status);



        return convertView;
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
}
