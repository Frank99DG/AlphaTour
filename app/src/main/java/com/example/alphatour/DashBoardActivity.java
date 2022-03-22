package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class DashBoardActivity extends AppCompatActivity  {

    private ActionBarDrawerToggle toggle;

    DrawerLayout activity_dash_board;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        toggle = new ActionBarDrawerToggle(this, activity_dash_board,R.string.open,R.string.close);
        activity_dash_board.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.elem_1:
                        Toast.makeText(DashBoardActivity.this, "Clicca su Profilo", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.elem_2:
                        Toast.makeText(DashBoardActivity.this, "Clicca su Percorso", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.elem_3:
                        Toast.makeText(DashBoardActivity.this, "Clicca su Impostazioni", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.elem_4:
                        Toast.makeText(DashBoardActivity.this, "Clicca su Zona", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

 }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}