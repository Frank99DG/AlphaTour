package com.example.alphatour.wizardpercorso;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.alphatour.HamburgerMenuFragment;
import com.example.alphatour.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;


public class Step5 extends Fragment  {
    
    private ImageView close_fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step5, container, false);
        close_fragment = view.findViewById(R.id.close_fragment);

        buttonClick();
        return inflater.inflate(R.layout.fragment_step5, container, false);

    }

    private void buttonClick() {
        close_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().hide(Step5.this).commit();
            }
            })
    ;}

}