package com.example.alphatour.wizardpercorso;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devzone.checkabletextview.CheckableTextView;
import com.example.alphatour.DashboardActivity;
import com.example.alphatour.ListZonesActivity;
import com.example.alphatour.R;
import com.example.alphatour.dblite.AlphaTourContract;
import com.example.alphatour.dblite.AlphaTourDbHelper;
import com.example.alphatour.dblite.CommandDbAlphaTour;
import com.example.alphatour.oggetti.MapZoneAndObject;
import com.example.alphatour.oggetti.Path;
import com.example.alphatour.oggetti.ZoneChoosed;
import com.google.firebase.auth.FirebaseUser;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.List;


public class Step5 extends Fragment  {
    
    private ImageView close_fragment;
    private LinearLayout closeLayout;
    public static List<MapZoneAndObject> zoneAndObjectList_ = new ArrayList<MapZoneAndObject>();
    private LinearLayout list_zoneR;
    private static List<String> zone_select = new ArrayList<>();
    private static int counter;
    private int i=0;
    private ProgressBar progressBar;
    private static List<ZoneChoosed> zoneChooseds = new ArrayList<ZoneChoosed>();
    private static Graph<ZoneChoosed, DefaultEdge> graph=new SimpleGraph<>(DefaultEdge .class);
    private FirebaseUser user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step5, container, false);

        close_fragment = view.findViewById(R.id.close_fragment);
        closeLayout = view.findViewById(R.id.closeLayout);
        buttonClick();
        list_zoneR = view.findViewById(R.id.list_zoneR);




        return view;

    }


    private void buttonClick() {

        /*list_zoneR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Step4.getZoneAndObjectList_();
                getFragmentManager().beginTransaction().show(Step5.this).commit();

            }
        });*/
        closeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().hide(Step5.this).commit();


            }
        });
        close_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().hide(Step5.this).commit();
            }
            });



    }
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if (isVisibleToUser) {

            counter = zone_select.size();
            for (i = 0; i < zone_select.size(); i++) {
                View zone = getLayoutInflater().inflate(R.layout.row_zone_review_path, null, false);
                TextView textZone = (TextView) zone.findViewById(R.id.textViewZone);


                zone.setId(i);

                textZone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), ReviewZoneSelected.class);
                        intent.putExtra("zone", textZone.getText().toString());
                        int index = zone.getId();
                        intent.putExtra("index", index);
                        startActivity(intent);
                        /*list_zoneR.addView(view); */


                        textZone.setText(zone_select.get(i));
                        list_zoneR.addView(zone);
                    }
                });
            }
        }
        else{

                }
            }
    private void savePath() {

        progressBar.setVisibility(View.VISIBLE);

        List<MapZoneAndObject> zoneAndObjectList = zoneAndObjectList_;

        MapZoneAndObject mapZoneAndOb = zoneAndObjectList.get(0);
        Path path = new Path();
        path.setName(mapZoneAndOb.getName());
        path.setDescription(mapZoneAndOb.getDescription());
        path.setIdUser(user.getUid());
        for (int i = 0; i < zoneAndObjectList_.size(); i++) {
            MapZoneAndObject mapZoneAndObject = zoneAndObjectList.get(i);
            //String delete = "delete";
            // if(!mapZoneAndObject.getZone().matches(delete)) {
            ZoneChoosed zoneChoosed = new ZoneChoosed();
            zoneChoosed.setName(mapZoneAndObject.getZone());

            List<String> obj = mapZoneAndObject.getListObj();
            for (int j = 0; j < obj.size(); j++) {
                zoneChoosed.setObjectChoosed(obj.get(j));
            }
            graph.addVertex(zoneChoosed);
            zoneChooseds.add(zoneChoosed);
            if (i >= 1) {
                graph.addEdge(zoneChooseds.get(i - 1), zoneChooseds.get(i));
            }
            path.setZonePath(zoneChoosed);
            // }
        }
    }
    public static List<MapZoneAndObject> getZoneAndObjectList_() {
        return zoneAndObjectList_;
    }

    public static void setZoneAndObjectList_(List<MapZoneAndObject> zoneAndObjectList_) {
        Step5.zoneAndObjectList_ = zoneAndObjectList_;
    }
    public static List<String> getZone_select() {
        return zone_select;
    }

}







        /*
        textView
        .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ReviewZoneSelected.class);
                intent.putExtra("zone",list_zoneR.getText().toString());
                int index = zone.getId();
                intent.putExtra("index", index);
                startActivity(intent);
                list_zoneR.addView(view);





    public static void List<MapZoneAndObject> getZoneAndObjectList_() {






        return zoneAndObjectList_;
    }

    public static void setZoneAndObjectList_(List<MapZoneAndObject> zoneAndObjectList_) {
        Step5.zoneAndObjectList_ = zoneAndObjectList_;

    } */

