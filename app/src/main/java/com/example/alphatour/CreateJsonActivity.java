package com.example.alphatour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alphatour.oggetti.ElementString;
import com.example.alphatour.oggetti.Zone;
import com.example.alphatour.oggetti.ZoneChoosed;
import com.example.alphatour.wizardpercorso.Step4;
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

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_json);
        btn=findViewById(R.id.button3);

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

}