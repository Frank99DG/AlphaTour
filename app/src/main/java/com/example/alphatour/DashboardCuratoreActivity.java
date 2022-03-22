package com.example.alphatour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class DashboardCuratoreActivity extends AppCompatActivity {
    CardView cv1,cv2,cv3,cv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_curator);

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
    }
}