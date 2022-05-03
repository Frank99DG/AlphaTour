package com.example.alphatour.wizardpercorso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devzone.checkabletextview.CheckableTextView;
import com.example.alphatour.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ReviewZoneSelected extends AppCompatActivity {


    private String zone;
    private LinearLayout list_object_zone_review;
    private TextView zoneReview,backToReview;
    private static Map<String, String> map_review_object = new HashMap<String, String>();

    public static Map<String, String> getMap_review_object() {
        return map_review_object;
    }

    public static void setMap_review_object(Map<String, String> map_review_object) {
        ReviewZoneSelected.map_review_object = map_review_object;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_zone_selected);

        zoneReview = findViewById(R.id.zoneReview);
        backToReview = findViewById(R.id.backToReview);
        list_object_zone_review = findViewById(R.id.list_object_zone_review);


        Intent intent = getIntent();
        zone = intent.getStringExtra("zone");
        zoneReview.setText(zone);

        backToReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        map_review_object = Step3.getMap_review();

        Iterator it = map_review_object.entrySet().iterator();

        while(it.hasNext()){
            Map.Entry valori = (Map.Entry) it.next();
            if(valori.getValue().equals(zone)){
                View object = getLayoutInflater().inflate(R.layout.row_add_object_for_zone_review, null, false);
                TextView objects = (TextView) object.findViewById(R.id.textObjectReview);
                ImageView delete_object = object.findViewById(R.id.deleteObject_review_zone);

                delete_object.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        map_review_object.remove(valori.getKey(),valori.getValue());
                        list_object_zone_review.removeView(object);
                    }
                });

                objects.setText(valori.getKey().toString());
                list_object_zone_review.addView(object);

            }

        }


    }
}