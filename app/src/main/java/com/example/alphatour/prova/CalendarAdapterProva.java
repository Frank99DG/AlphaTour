package com.example.alphatour.prova;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alphatour.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapterProva extends RecyclerView.Adapter<CalendarViewHolderProva> {

    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;

    public CalendarAdapterProva(ArrayList<LocalDate> days, OnItemListener onItemListener) {

        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolderProva onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell_prova, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if(days.size() > 15) //month view
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        else //week view
            layoutParams.height = (int) parent.getHeight();


        return new CalendarViewHolderProva(view, onItemListener, days);
    }


    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolderProva holder, int position) {

        final LocalDate date = days.get(position);

        holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));

        if(date.equals(CalendarUtilsProva.selectedDate))
            holder.parentView.setBackgroundColor(Color.LTGRAY);

        if(date.getMonth().equals(CalendarUtilsProva.selectedDate.getMonth()))
            holder.dayOfMonth.setTextColor(Color.BLACK);
        else
            holder.dayOfMonth.setTextColor(Color.LTGRAY);
    }


    @Override
    public int getItemCount() {

        return days.size();
    }


    public interface OnItemListener{

        void onItemClick(int position, LocalDate date);
    }

}
