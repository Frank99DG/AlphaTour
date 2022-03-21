package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class DashBoardActivity extends AppCompatActivity  {

    private ActionBarDrawerToggle toggle;

    DrawerLayout activity_dash_board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        toggle = new ActionBarDrawerToggle(this, activity_dash_board,R.string.open,R.string.close);
        activity_dash_board.addDrawerListener(toggle);
        toggle.syncState();



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}