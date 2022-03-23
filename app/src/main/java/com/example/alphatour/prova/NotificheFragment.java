package com.example.alphatour.prova;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
    Button createPath;
    private String myNotify;
    LinearLayout layoutList;


    public NotificheFragment() {
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
        createPath = view.findViewById(R.id.createPath);
        layoutList = view.findViewById(R.id.layout_list);

        closeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().hide(NotificheFragment.this).commit();
            }
        });
        return view;
    }

    public void addView(NotificationCounter notificationCounter) {

            View notify = getLayoutInflater().inflate(R.layout.row_add_notify, null, false);
            TextView textNotify = (TextView) notify.findViewById(R.id.textNotify);
            ImageView imageClose = (ImageView) notify.findViewById(R.id.closeNotify);

            Bundle data = getArguments();
            if(data!= null){
            myNotify = data.getString("Notify");
            textNotify.setText(myNotify);
            }

            layoutList.addView(notify);

            imageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    notificationCounter.decreaseNumber();
                    removeView(notify);

                }
            });
    }

    public void removeView(View view) {

        layoutList.removeView(view);

    }

}