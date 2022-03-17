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

    Button button;
    Button creaPercorso;
    NotificationCounter notificationCounter;
    ImageView openFragment;
    ImageView closeNotification;
    NotificheFragment myFragment = new NotificheFragment();
    Bundle data = new Bundle();
    TextView tvMyText;
    String myNot;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_prova);

        creaPercorso = findViewById(R.id.creaPercorso);
        notificationCounter = new NotificationCounter(findViewById(R.id.notificationNumber));
        openFragment = findViewById(R.id.notificationIcon);
        closeNotification = findViewById(R.id.closeNotification);
        tvMyText = findViewById(R.id.textNotify);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,myFragment).hide(myFragment).commit();

    }


    public void openFragment(View v) {
       // data.putString("myData", "Welcome to AlphaTour");
      //  myFragment.setArguments(data);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).show(myFragment).commit();

    }

    public void creaPercorso(View v) {
        data.putString("myData1", "Sei un grande hai creato un percorso");
        myFragment.setArguments(data);
        /*if(data!= null){
            myNot = data.getString("myData1");
            tvMyText.setText(myNot);
        }

         */
        notificationCounter.increaseNumber();
        myFragment.addView(notificationCounter);


    }


}
