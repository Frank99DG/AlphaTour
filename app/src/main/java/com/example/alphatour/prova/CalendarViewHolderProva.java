package com.example.alphatour.prova;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alphatour.R;

public class CalendarViewHolderProva extends RecyclerView.ViewHolder implements View.OnClickListener{

    public final TextView dayOfMonth;
    private final CalendarAdapterProva.OnItemListener onItemListener;


    public CalendarViewHolderProva(@NonNull View itemView, CalendarAdapterProva.OnItemListener onItemListener) {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());
    }
}
