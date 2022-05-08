package com.example.alphatour;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class NotificationCounter {

    private static int count;
    private static boolean send_notify=false;


    private TextView notificationNumber;

    private final int MAX_NUMBER = 99;
    private int notification_number_counter= 0;

    public NotificationCounter(View view){
        notificationNumber = view.findViewById(R.id.notificationNumber);

    }

    public void increaseNumber(){
        notification_number_counter++;

        if(notification_number_counter > MAX_NUMBER){
            Log.d("Counter", "Maximum Number Reached!");
        }else{
            notificationNumber.setText(String.valueOf(notification_number_counter));
        }
    }

    public void decreaseNumber(){
        notification_number_counter--;
        if(notification_number_counter < 0){
            Log.d("Counter", "Min Number Reached");
        }else notificationNumber.setText(String.valueOf(notification_number_counter));
    }

    public TextView getNotificationNumber() {
        return notificationNumber;
    }

    public void setNotificationNumber(TextView notificationNumber) {
        this.notificationNumber = notificationNumber;
    }

    public void setTextNotify(Bundle d, String abcd){
        this.notificationNumber.setText(d.getCharSequence(abcd));
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        NotificationCounter.count = count;
    }

    public static boolean getSend_notify() {
        return send_notify;
    }

    public static void setSend_notify(boolean send_notify) {
        NotificationCounter.send_notify = send_notify;
    }

}
