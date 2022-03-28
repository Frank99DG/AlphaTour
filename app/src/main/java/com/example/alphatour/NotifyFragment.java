package com.example.alphatour;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotifyFragment extends Fragment {

    private ImageView closeNotification;
    private String myNotify;
    private String String_myNotify;
    private LinearLayout layoutList;
    private static final String KEY_NOTIFY="KEY_NOTIFY";

    public NotifyFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
     //   outState.putCharSequence(KEY_NOTIFY, myNotify.getText());
        outState.putString(KEY_NOTIFY, myNotify);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
            this.myNotify = savedInstanceState.getString("KEY_NOTIFY");
        }
    }

/*
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        this.myNotify = savedInstanceState.getString("KEY_NOTIFY");
    }
 */






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notifiche,container,false);
        closeNotification = view.findViewById(R.id.closeNotification);
        layoutList = view.findViewById(R.id.layout_list);

        closeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().hide(NotifyFragment.this).commit();
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