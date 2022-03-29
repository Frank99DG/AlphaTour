package com.example.alphatour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class provaaaaaaaa extends AppCompatActivity {

    private static final String KEY_DIOCANE="DIOCANE";
    private TextView blalba;
    private int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provaaaaaaaa);

        blalba = findViewById(R.id.contatore);

    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        outState.putCharSequence(KEY_DIOCANE, blalba.getText());

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        this.blalba.setText(savedInstanceState.getCharSequence(KEY_DIOCANE));

    }

    public void incrementaa(View view) {
        n++;
        blalba.setText(String.valueOf(n));

    }

    public void abcd(View view) {
        Intent i = new Intent(provaaaaaaaa.this, invioProva.class);
        startActivity(i);
    }





}