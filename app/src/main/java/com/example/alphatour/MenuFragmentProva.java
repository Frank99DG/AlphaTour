package com.example.alphatour;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MenuFragmentProva extends Fragment {


    private LinearLayout closeLayout;
    private ImageView hamburger_home;
    private ImageView close_hamburger;
    private ConstraintLayout ignoreTabBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_prova, container, false);
        closeLayout = view.findViewById(R.id.closeLayout);
        hamburger_home = view.findViewById(R.id.hamburger_home);
        close_hamburger= view.findViewById(R.id.close_hamburger);
        ignoreTabBar= view.findViewById(R.id.ignoreTabBar);

        buttonClick();


        return view;
    }
/*
    @Override
    public void onResume(){
        super.onResume();
    }
 */

    private void buttonClick(){
        ignoreTabBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        closeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().hide(MenuFragmentProva.this).commit();
            }
        });

        hamburger_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), invioProva.class);
                startActivity(i);
            }
        });

        close_hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().hide(MenuFragmentProva.this).commit();
            }
        });

    }

}