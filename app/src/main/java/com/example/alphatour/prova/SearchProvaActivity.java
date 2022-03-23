package com.example.alphatour.prova;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.alphatour.R;

public class SearchProvaActivity extends AppCompatActivity {

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_search_prova);
       // searchView = findViewById(R.id.search);
        searchView.clearFocus();

    }


}