package com.example.alphatour.prova;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alphatour.R;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HourAdapterProva extends ArrayAdapter<HourEventProva> {

    public HourAdapterProva(@NonNull Context context, List<HourEventProva> hourEvents) {

        super(context, 0, hourEvents);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        HourEventProva event = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hour_cell_prova, parent, false);

        setHour(convertView, event.time);
        setEvents(convertView, event.events);
        
        return convertView;
    }


    private void setHour(View convertView, LocalTime time) {

        TextView timeTV = convertView.findViewById(R.id.timeTV);
        timeTV.setText(CalendarUtilsProva.formattedShortTime(time));
    }


    private void setEvents(View convertView, ArrayList<EventProva> events) {

        TextView event1 = convertView.findViewById(R.id.event1);
        TextView event2 = convertView.findViewById(R.id.event2);
        TextView event3 = convertView.findViewById(R.id.event3);

        if(events.size() == 0){

            hideEvent(event1);
            hideEvent(event2);
            hideEvent(event3);
        }

        else if(events.size() == 1){

            setEvent(event1, events.get(0));
            hideEvent(event2);
            hideEvent(event3);
        }

        else if(events.size() == 2){

            setEvent(event1, events.get(0));
            setEvent(event2, events.get(1));
            hideEvent(event3);
        }

        else if(events.size() == 3){

            setEvent(event1, events.get(0));
            setEvent(event2, events.get(1));
            setEvent(event3, events.get(2));
        }

        else{

            setEvent(event1, events.get(0));
            setEvent(event2, events.get(1));
            event3.setVisibility(View.VISIBLE);
            String eventsNotShown = String.valueOf(events.size() - 2);
            eventsNotShown += " More Events";
            event3.setText(eventsNotShown);
        }
    }

    private void setEvent(TextView textView, EventProva event) {

        textView.setText(event.getName());
        textView.setVisibility(View.VISIBLE);
    }


    private void hideEvent(TextView tv) {

        tv.setVisibility(View.INVISIBLE);
    }

}
