package com.example.alphatour.prova;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.alphatour.R;

public class DashboardActivityProva extends AppCompatActivity {
    CardView cv1,cv2,cv3,cv4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);



        cv1 = (CardView) findViewById(R.id.cv_route);
        cv2 = (CardView) findViewById(R.id.cv_place);
        cv3 = (CardView) findViewById(R.id.cv_show);
        cv4 = (CardView) findViewById(R.id.cv_calendar);

        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Funonzia",Toast.LENGTH_LONG);
            }
        });
    }

}