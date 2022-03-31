package com.example.alphatour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class invioProva extends AppCompatActivity {

     private EditText ageText;
     private Button button1;
     private Button button2;
     private Button button_path;
     private int age;
     private static int n_notify=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invio_prova);

        ageText = findViewById(R.id.mAge);
        button_path = findViewById(R.id.create_path);

    }

    public void create_path(View view){
        n_notify++;
    }

    public static int getN_notify() {
        return n_notify;
    }

    public static void setN_notify(int n_notify) {
        invioProva.n_notify = n_notify;
    }

    public void start(View view){
        Intent i = new Intent(invioProva.this, DashboardActivity.class);
        //i.putExtra(DashboardActivity.N_NOTIFY,n_notify);
        startActivity(i);
    }

    public void aaaaa(View view){
        Intent i = new Intent(invioProva.this, provaaaaaaaa.class);
        startActivity(i);
    }

/*
    public void send(View view){
        Intent i = new Intent(invioProva.this, DashboardActivity.class);
        age = Integer.parseInt(ageText.getText().toString().trim());
        i.putExtra(NotificationProvaActivity.N_NOTIFY,n_notify);

        i.putExtra(NotificationProvaActivity.AGE,age);
    }

 */
}