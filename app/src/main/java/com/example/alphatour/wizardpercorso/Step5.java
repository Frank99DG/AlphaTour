package com.example.alphatour.wizardpercorso;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.alphatour.R;
import com.example.alphatour.oggetti.MapZoneAndObject;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;


public class Step5 extends Fragment  {
    
    private ImageView close_fragment;
    private LinearLayout closeLayout;
    public static List<MapZoneAndObject> zoneAndObjectList_ = new ArrayList<MapZoneAndObject>();
    private static List<String> zone_select = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step5, container, false);
        close_fragment = view.findViewById(R.id.close_fragment);
        closeLayout = view.findViewById(R.id.closeLayout);

        buttonClick();

        return view;

    }

    private void buttonClick() {

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