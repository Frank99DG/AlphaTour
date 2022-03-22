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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell_prova, parent, false);

        setHour(convertView, event.time);
        
        return convertView;
    }

    private void setHour(View convertView, LocalTime time) {

        TextView timeTV = convertView.findViewById(R.id.timeTV);
    }
}
