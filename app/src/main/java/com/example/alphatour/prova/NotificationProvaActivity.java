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
        Button creaPercorso;
        NotificationCounter notificationCounter;
        ImageView openFragment;
        ImageView closeNotification;
        NotificheFragment myFragment = new NotificheFragment();
        Bundle data = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_prova);

        button = findViewById(R.id.bottone);
        creaPercorso = findViewById(R.id.creaPercorso);
        notificationCounter = new NotificationCounter(findViewById(R.id.notificationNumber));
        openFragment = findViewById(R.id.notificationIcon);
        closeNotification = findViewById(R.id.closeNotification);



    }


    public void increaseNotification(View v){
        notificationCounter.increaseNumber();
    }

    public void openFragment(View v){
        data.putString("myData", "Welcome to AlphaTour");
        myFragment.setArguments(data);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,myFragment).commit();
    }

    public void creaPercorso(View v){
        data.putString("myData1", "Sei un grande hai creato un percorso");
        myFragment.setArguments(data);
        increaseNotification(v);
    }

}