package com.example.alphatour.mainUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alphatour.R;
import com.example.alphatour.objectclass.TourItem;

import java.util.ArrayList;
import java.util.List;

public class TourAdapter extends ArrayAdapter<TourItem> {

    private List<TourItem> tourListFull;

    public TourAdapter(@NonNull Context context, @NonNull List<TourItem> tourList) {
        super(context, 0 , tourList);
        tourListFull = new ArrayList<>(tourList);
    }

    @NonNull
    @Override
    public Filter getFilter(){
        return tourFilter;
    }



    private Filter tourFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<TourItem> suggestions = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                suggestions.addAll(tourListFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(TourItem item : tourListFull){
                    if(item.getTourText().toLowerCase().contains(filterPattern)){
                        suggestions.add(item);
                    }
                }

            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll( (List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((TourItem) resultValue).getTourText();
        }

    };


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_search_dashboard,parent,false);
        }

        ImageView tourImage = convertView.findViewById(R.id.imagePlaceZoneElement);
        TextView tourText = convertView.findViewById(R.id.displayPlaceZoneElement);

        TourItem tourItem = getItem(position);
        if (tourItem != null){
            tourImage.setImageResource(tourItem.getTourImage());
            tourText.setText(tourItem.getTourText());
        }

        return convertView;
    }

    public void updateList(@NonNull List<TourItem> newList) {
        tourListFull = new ArrayList<>(newList);
        clear();
        addAll(tourListFull);
    }


}
