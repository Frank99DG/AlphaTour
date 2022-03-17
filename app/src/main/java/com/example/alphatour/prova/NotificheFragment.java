package com.example.alphatour.prova;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alphatour.R;

public class NotificheFragment extends Fragment {

    ImageView closeNotification;
    Button creaPercorso;
    private String myStr;
    private TextView tvMyText;
    private String myNot;
    private TextView notifica2;
    LinearLayout layoutList;
    Bundle data = getArguments();

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
        tvMyText = view.findViewById(R.id.textNotify);
        creaPercorso = view.findViewById(R.id.creaPercorso);
        //notifica2 = view.findViewById(R.id.notifica2);
        layoutList = view.findViewById(R.id.layout_list);



/*
        Bundle data = getArguments();
        if(data!= null){
            myStr= data.getString("myData");
            myNot = data.getString("myData1");
            if(myNot!=null)notifica2.setText(myNot);
            tvMyText.setText(myStr);

        }
 */


        closeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().hide(NotificheFragment.this).commit();
            }
        });
        return view;
    }

    public void addView(NotificationCounter notificationCounter) {


            View notifica = getLayoutInflater().inflate(R.layout.row_add_notify, null, false);
            TextView tvMyText = (TextView) notifica.findViewById(R.id.textNotify);
            ImageView imageClose = (ImageView) notifica.findViewById(R.id.closeNotify);

            if(data!= null){
            myNot = data.getString("myData1");
            tvMyText.setText(myNot);
        }

            imageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notificationCounter.decreaseNumber();
                    removeView(notifica);

                }
            });

            layoutList.addView(notifica);

    }

    public void removeView(View view) {

        layoutList.removeView(view);

    }

}