package com.example.alphatour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

public class DashboardActivity extends AppCompatActivity {

    public static final String AGE = "AGE";
    public static final String N_NOTIFY = "N_NOTIFY";
    private static final String KEY_COUNTER="KEY_COUNTER";

    private TextView ageText;
    private int n_notify;
    private int a=0;
    private NotificationCounter notificationCounter;
    private NotifyFragment myFragment = new NotifyFragment();
    private MenuFragmentProva myFragmentMenu = new MenuFragmentProva();
    private Bundle data = new Bundle();
    private FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    private FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    private LinearLayout closeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        if (savedInstanceState != null) {
            //Restore the fragment's instance
           // myFragment = (NotifyFragment) getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
        }

        Intent i = getIntent();
        n_notify = i.getIntExtra(N_NOTIFY,0);
       // ageText.setText("Counter is: "+n_notify);


       // ageText = findViewById(R.id.mAge2);
        notificationCounter = new NotificationCounter(findViewById(R.id.notificationNumber));
        ft.replace(R.id.container,myFragment).hide(myFragment).commit();
        fragmentTransaction.replace(R.id.menu,myFragmentMenu).hide(myFragmentMenu).commit();
        SearchView SearchView = (SearchView) findViewById(R.id.SearchView);
        closeLayout = findViewById(R.id.closeLayout);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(invioProva.getN_notify()>0  ){
            n_notify= invioProva.getN_notify();
            for(a=0;  a<n_notify; a++) {
                notificationCounter.increaseNumber();
            }invioProva.setN_notify(0);
            ageText.setText("Counter is: " + n_notify);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        //getSupportFragmentManager().putFragment(outState, "myFragment", myFragment);
       outState.putCharSequence(KEY_COUNTER, notificationCounter.getNotificationNumber().getText());
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

       this.notificationCounter.setTextNotify(savedInstanceState,KEY_COUNTER);
    }

    public void openFragment(View v) {

        getSupportFragmentManager().beginTransaction().show(myFragment).commit();

        for (a = 0; a < n_notify; a++){             //Ciclo per aggiungere n_notify da
            notifyPath();                           //un'altra activity al fragment
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

    public void menu(View view){
        getSupportFragmentManager().beginTransaction().show(myFragmentMenu).commit();

    }
}
