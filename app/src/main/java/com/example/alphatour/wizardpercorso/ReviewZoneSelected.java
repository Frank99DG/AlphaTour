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
import com.example.alphatour.oggetti.MapZoneAndObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReviewZoneSelected extends AppCompatActivity {


    private String zone;
    private LinearLayout list_object_zone_review;
    private TextView zoneReview,backToReview;
    private static List<MapZoneAndObject> zoneAndObjectList = new ArrayList<MapZoneAndObject>();
    private int index;
    private static List<String> listObj = new ArrayList<String>();

    public static List<MapZoneAndObject> getZoneAndObjectList() {
        return zoneAndObjectList;
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
        index = intent.getIntExtra("index",0);

        zoneReview.setText(zone);

        backToReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        zoneAndObjectList = Step4.getZoneAndObjectList_();

        for(int i =0; i<zoneAndObjectList.size();i++){
            if(i == index) {
                listObj = zoneAndObjectList.get(i).getListObj();

                for (int c = 0; c < listObj.size(); c++) {
                    View object = getLayoutInflater().inflate(R.layout.row_add_object_for_zone_review, null, false);
                    TextView objects = (TextView) object.findViewById(R.id.textObjectReview);
                    ImageView delete_object = object.findViewById(R.id.deleteObject_review_zone);

                    object.setId(c);

                    delete_object.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int index = object.getId();
                            listObj.remove(index);
                            list_object_zone_review.removeView(object);
                        }
                    });

                    objects.setText(listObj.get(c));
                    list_object_zone_review.addView(object);

                }
            }
        }

    }

}