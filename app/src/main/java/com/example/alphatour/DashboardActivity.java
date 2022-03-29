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

    private static final String KEY_DIOCANE="DIOCANE";
    public static final String AGE = "AGE";
    public static final String N_NOTIFY = "N_NOTIFY";
    private TextView ageText;
    private int n_notify;
    private int a=0;
    private TextView sadp;

    private NotificationCounter notificationCounter;
    private NotifyFragment myFragment = new NotifyFragment();
    private Bundle data = new Bundle();

    private FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

/*
        if (savedInstanceState != null) {
            //Restore the fragment's instance
           // myFragment = (NotifyFragment) getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");

        }


 */



        ageText = findViewById(R.id.mAge2);
        notificationCounter = new NotificationCounter(findViewById(R.id.notificationNumber));


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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
      //  getSupportFragmentManager().putFragment(outState, "myFragment", myFragment);
       outState.putCharSequence(KEY_DIOCANE, notificationCounter.getNotificationNumber().getText());
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

       this.notificationCounter.setTextNotify(savedInstanceState,KEY_DIOCANE);
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
        data.putString("Notify", "You have created a path");
        myFragment.setArguments(data);
        myFragment.addView(notificationCounter);

    }

    public void back_SecondActivity(View view) {
        Intent i = new Intent(DashboardActivity.this, invioProva.class);
        startActivity(i);
    }



}
