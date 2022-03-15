package com.example.alphatour.prova;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alphatour.R;

public class NotificheFragment extends Fragment {

    ImageView closeNotification;
    Button creaPercorso;
    private String myStr;
    private TextView tvMyText;
    private String myNot;
    private TextView notifica2;

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
        tvMyText = view.findViewById(R.id.notifica1);
        creaPercorso = view.findViewById(R.id.creaPercorso);
        notifica2 = view.findViewById(R.id.notifica2);

        Bundle data = getArguments();
        if(data!= null){
            myStr= data.getString("myData");
            myNot = data.getString("myData1");
            if(myNot!=null)notifica2.setText(myNot);
            tvMyText.setText(myStr);
        }

        closeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().remove(NotificheFragment.this).commit();
            }
        });
        return view;
    }

}