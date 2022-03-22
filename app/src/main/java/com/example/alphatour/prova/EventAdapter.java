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

import java.util.List;

public class EventAdapter extends ArrayAdapter<EventProva> {

    public EventAdapter(@NonNull Context context, List<EventProva> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        EventProva event = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell_prova, parent, false);

        TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);

        String eventTitle = event.getName() + " " + CalendarUtilsProva.formattedTime(event.getTime());
        eventCellTV.setText(eventTitle);
        return convertView;
    }
}
