package com.example.alphatour.prova;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alphatour.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class NotificationProvaActivity extends AppCompatActivity {

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

        creaPercorso = findViewById(R.id.creaPercorso);
        notificationCounter = new NotificationCounter(findViewById(R.id.notificationNumber));
        openFragment = findViewById(R.id.notificationIcon);
        closeNotification = findViewById(R.id.closeNotification);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,myFragment).hide(myFragment).commit();

    }


    public void openFragment(View v) {

        getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).show(myFragment).commit();

    }

    public void creaPercorso(View v) {
        data.putString("Notifica", "Sei un grande hai creato un percorso");
        myFragment.setArguments(data);
        notificationCounter.increaseNumber();
        myFragment.addView(notificationCounter);
    }


    public void buttonNotify(View view) {
        data.putString("Notifica", "Notifica a caso");
        myFragment.setArguments(data);
        notificationCounter.increaseNumber();
        myFragment.addView(notificationCounter);
    }
}
