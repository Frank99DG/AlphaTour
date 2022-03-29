package com.example.alphatour;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.alphatour.R;

public class NotificationCounter {

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
}
