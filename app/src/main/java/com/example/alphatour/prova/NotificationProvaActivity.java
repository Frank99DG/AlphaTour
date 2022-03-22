package com.example.alphatour.prova;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;

import android.content.Intent;
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

    public static final String AGE = "AGE";
    public static final String N_NOTIFY = "N_NOTIFY";
    private TextView ageText;
    private int age;
    private int n_notify;

    Button creaPercorso;
    NotificationCounter notificationCounter;
    ImageView openFragment;
    ImageView closeNotification;
    NotificheFragment myFragment = new NotificheFragment();
    Bundle data = new Bundle();
    int a=0;
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_prova);

        ageText = findViewById(R.id.mAge2);

        creaPercorso = findViewById(R.id.creaPercorso);
        notificationCounter = new NotificationCounter(findViewById(R.id.notificationNumber));
        openFragment = findViewById(R.id.notificationIcon);
        closeNotification = findViewById(R.id.closeNotification);

        Intent i = getIntent();
        n_notify = i.getIntExtra(N_NOTIFY,0);
        ageText.setText("Counter is: "+n_notify);

        ft.replace(R.id.container,myFragment).hide(myFragment).commit();




/*
        if(n_notify>0) {

           // for (a = 0; a < n_notify; a++) {
                data.putString("Notifica", "Sei un grande hai creato un percorso");
                myFragment.setArguments(data);
                notificationCounter.increaseNumber();
                //myFragment.addView(notificationCounter);
                if (myFragment.isHidden()) {

                    myFragment.addView(notificationCounter);

                }
         //   }

        }

 */
    }




    public void fairoba(){
        data.putString("Notifica", "Sei un grande hai creato un percorso");
        myFragment.setArguments(data);
        notificationCounter.increaseNumber();
        myFragment.addView(notificationCounter);

    }

    public void openFragment(View v) {

        ft.show(myFragment).commit();
        boolean b=myFragment.isHidden();
        boolean c=myFragment.isAdded();

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
