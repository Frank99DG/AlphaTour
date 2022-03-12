package com.example.alphatour.prova;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.alphatour.R;

public class NotificationProvaActivity extends AppCompatActivity {

        Button button;
        NotificationCounter notificationCounter;
        ImageView openFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_prova);

        button = findViewById(R.id.bottone);
        notificationCounter = new NotificationCounter(findViewById(R.id.notificationNumber));
        openFragment = findViewById(R.id.notificationIcon);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               notificationCounter.increaseNumber();
            }
        });

        openFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialogFragment myDialogFragment= new MyDialogFragment();
                myDialogFragment.show(getSupportFragmentManager(),"MyFragment");
            }
        });
    }
}