package com.example.alphatour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {

    public static final String AGE = "AGE";
    public static final String N_NOTIFY = "N_NOTIFY";
    private TextView ageText;
    private int age;
    private int n_notify;
    private int a=0;

    Button createPath;
    NotificationCounter notificationCounter;
    ImageView openFragment;
    ImageView closeNotification;
    NotifyFragment myFragment = new NotifyFragment();
    Bundle data = new Bundle();

    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //DESIRè
        CardView cv1,cv2,cv3,cv4;

        cv1 = (CardView) findViewById(R.id.cv_city);
        cv2 = (CardView) findViewById(R.id.cv_museum);
        cv3 = (CardView) findViewById(R.id.cv_show);
        cv4 = (CardView) findViewById(R.id.cv_monument);

        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Funonzia",Toast.LENGTH_LONG);
            }
        });
        //DESIRè

        ageText = findViewById(R.id.mAge2);

        createPath = findViewById(R.id.createPath);
        notificationCounter = new NotificationCounter(findViewById(R.id.notificationNumber));
        openFragment = findViewById(R.id.notificationIcon);
        closeNotification = findViewById(R.id.closeNotification);

        Intent i = getIntent();
        n_notify = i.getIntExtra(N_NOTIFY,0);
        ageText.setText("Counter is: "+n_notify);

        ft.replace(R.id.container,myFragment).hide(myFragment).commit();

        if(n_notify>0) {
            for (a = 0; a < n_notify; a++) {
                notificationCounter.increaseNumber();
            }
        }


    }


    public void openFragment(View v) {

        getSupportFragmentManager().beginTransaction().show(myFragment).commit();

        for (a = 0; a < n_notify; a++){
            notifyPath();
            if(a==n_notify-1) n_notify =0;
        }
    }

    public void createPath(View v) {
        data.putString("Notify", "You have created a path");
        myFragment.setArguments(data);
        notificationCounter.increaseNumber();
        myFragment.addView(notificationCounter);
    }


    public void buttonNotify(View view) {
        data.putString("Notify", "Random notify");
        myFragment.setArguments(data);
        notificationCounter.increaseNumber();
        myFragment.addView(notificationCounter);
    }

    public void notifyPath(){
        data.putString("Notify", "Sei un grande hai creato un percorso");
        myFragment.setArguments(data);
        myFragment.addView(notificationCounter);

    }
}
