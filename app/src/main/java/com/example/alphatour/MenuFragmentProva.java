package com.example.alphatour;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MenuFragmentProva extends Fragment {


    private LinearLayout closeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_prova, container, false);
        closeLayout = view.findViewById(R.id.closeLayout);

        closeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().hide(MenuFragmentProva.this).commit();
            }
        });



        return view;
    }


}