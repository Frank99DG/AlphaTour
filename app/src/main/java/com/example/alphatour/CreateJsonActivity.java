package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.oggetti.ElementString;
import com.example.alphatour.oggetti.Zone;
import com.example.alphatour.oggetti.ZoneChoosed;
import com.example.alphatour.wizardpercorso.Step4;
import com.google.android.material.snackbar.Snackbar;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.util.mxCellRenderer;

import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;


import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.IntegerIdProvider;
import org.jgrapht.nio.json.JSONExporter;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class CreateJsonActivity extends AppCompatActivity {

    private Button btn,btn1,yesFinal,cancelFinal;
    private ConstraintLayout layt;
    private Dialog dialog;
    private TextView titleDialog,textDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_json);
        btn=findViewById(R.id.button3);
        layt=findViewById(R.id.layoutJson);
        btn1=findViewById(R.id.button4);

        dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialog_permission);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroun_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;


        yesFinal= dialog.findViewById(R.id.btn_termina_permission);
        titleDialog=dialog.findViewById(R.id.titleDialog_permission);
        textDialog=dialog.findViewById(R.id.textDialog_permission);

        if(checkPermission()){
            Toast.makeText(this,"Permission granted",Toast.LENGTH_LONG).show();
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(CreateJsonActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                dialog.show();
                yesFinal.setText("OK");

                titleDialog.setText("Richiesta Permesso");
                textDialog.setText("Caro utente, la prossima schermata ti chiederà di concedere il permesso di scrittura nella memoria " +
                        "del tuo dispositivo. Ciò è necessario esclusivamente per scaricare sul tuo dispositivo il percorso che hai creato" +
                        ", così da poterlo avere sempre a portata di mano. Non accederemo in alcun modo ad altri file nè " +
                        "divulgeremo dati a terze parti. Ti ringraziamo in anticipo !");
                textDialog.setTextColor(getResources().getColor(R.color.black));

                yesFinal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        requestPermission();
                    }
                });
            }else{
                requestPermission();
            }

        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Graph<ZoneChoosed,DefaultEdge> graph= Step4.getGraph();
                JSONExporter<ZoneChoosed, DefaultEdge> exporter = new JSONExporter<>(v -> String.valueOf(v));
                exporter.setEdgeIdProvider(new IntegerIdProvider<>(1));
                // ByteArrayOutputStream os = new ByteArrayOutputStream();
                File file= null;
                try {

                    File fil=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"Percorso.json");
                    FileOutputStream fos= new FileOutputStream(fil);
                   // file = File.createTempFile("Percorso",".json");
                    exporter.exportGraph(graph, fos);
                    fos.close();
                    Toast.makeText(CreateJsonActivity.this,"Saved to "+getFilesDir()+"/"+ "Percorso.json",Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context=CreateJsonActivity.this;
                String str="{ \"zona\": \"medioevo\",\n"+
                            " \"oggetto\": \"oggetto1\",\n"+
                           " \"oggetto\": \"oggetto 2\",\n"+
                           " \"zona1\": \"zona2\", \n"+
                           " \"oggetto\": \"oggetto1\", \n"+
                           " \"oggettto\": \"oggetto2\" }";
                try {
                    Graph<ZoneChoosed,DefaultEdge> graph= Step4.getGraph();

                    /*Zone zone=new Zone();
                    Zone zone1=new Zone();
                    Zone zone2=new Zone();

                    ElementString elm=new ElementString();
                    ElementString elm1=new ElementString();


                    zone.setName("Medioevo");
                    zone1.setName("Assiri");
                    zone2.setName("Sumeri");
                    elm.setIdZone("Medioevo"); elm.setTitle("Oggetto1"); elm.setDescription("descrizione");
                    elm.setPhoto("link foto"); elm.setQrCode("qr code");
                    elm1.setIdZone("Assiri"); elm1.setTitle("Oggetto2"); elm1.setDescription("descr");
                    elm1.setPhoto("link foto oggetto 2"); elm1.setQrCode("qr code oggetto 2");

                    graph.addVertex(zone);
                    graph.addVertex(zone1);
                    graph.addVertex(zone2);

                    graph.addEdge(zone,zone1);
                    graph.addEdge(zone,zone2);
                    graph.addEdge(zone1,zone2);
                    graph.addEdge(zone2,zone1);*/

                    JSONExporter<ZoneChoosed, DefaultEdge> exporter = new JSONExporter<>(v -> String.valueOf(v));
                    exporter.setEdgeIdProvider(new IntegerIdProvider<>(1));
                   // ByteArrayOutputStream os = new ByteArrayOutputStream();
                    File file= File.createTempFile("Percorso",".json");
                    exporter.exportGraph(graph, file);



                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.setType("application/json");
                    Uri uri= FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                            BuildConfig.APPLICATION_ID + ".provider", file);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(intent);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private boolean checkPermission(){

        int cameraPermission= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return cameraPermission== PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        int permissionCode=200; //codice definito da me, servirà nel caso in cui serva controllare più permessi
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},permissionCode);

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED ) {

            Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Permission denied",Toast.LENGTH_LONG).show();

        }
    }

}