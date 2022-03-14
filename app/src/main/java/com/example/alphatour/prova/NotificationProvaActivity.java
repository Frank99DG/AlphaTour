package com.example.alphatour.prova;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.alphatour.R;

public class NotificationProvaActivity extends AppCompatActivity {

        Button button;
        NotificationCounter notificationCounter;
        ImageView openFragment;
        ImageView closeNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_prova);

        button = findViewById(R.id.bottone);
        notificationCounter = new NotificationCounter(findViewById(R.id.notificationNumber));
        openFragment = findViewById(R.id.notificationIcon);
        closeNotification = findViewById(R.id.closeNotification);


    }


    public void increaseNotification(View v){
        notificationCounter.increaseNumber();
    }

    public void openFragment(View v){
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new NotificheFragment()).commit();
    }

}