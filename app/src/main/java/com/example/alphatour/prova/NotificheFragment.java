package com.example.alphatour.prova;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.alphatour.R;

public class NotificheFragment extends Fragment {

    ImageView closeNotification;

    public NotificheFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifiche,container,false);
        closeNotification = view.findViewById(R.id.closeNotification);
        closeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().remove(NotificheFragment.this).commit();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}