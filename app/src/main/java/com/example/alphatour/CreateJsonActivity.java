package com.example.alphatour;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Parcelable;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.oggetti.ElementString;
import com.example.alphatour.oggetti.MapZoneAndObject;
import com.example.alphatour.oggetti.Zone;
import com.example.alphatour.oggetti.ZoneChoosed;
import com.example.alphatour.wizardpercorso.PercorsoWizard;
import com.example.alphatour.wizardpercorso.ReviewZoneSelected;
import com.example.alphatour.wizardpercorso.Step4;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import org.w3c.dom.Text;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CreateJsonActivity extends AppCompatActivity {

    private Button yesFinal, cancelFinal;
    private ConstraintLayout layt;
    private Dialog dialog;
    private TextView titleDialog, textDialog;
    private static List<MapZoneAndObject> zoneAndObjectListReviewPath = new ArrayList<MapZoneAndObject>();
    private LinearLayout list_path;
    private ConstraintLayout layout;
    private BottomNavigationView bottomNavigationViewPath;
    private TextView name_path,description_path;
    private int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_json);
        layt = findViewById(R.id.layoutJson);
        name_path = findViewById(R.id.name_path);
        description_path = findViewById(R.id.description_path);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_permission);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroun_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        yesFinal = dialog.findViewById(R.id.btn_termina_permission);
        titleDialog = dialog.findViewById(R.id.titleDialog_permission);
        textDialog = dialog.findViewById(R.id.textDialog_permission);

        bottomNavigationViewPath = findViewById(R.id.bottomNavigationBarPath);
        bottomNavigationViewPath.setSelectedItemId(R.id.tb_home_path);

        bottomNavBarPathClick();

        zoneAndObjectListReviewPath = Step4.getZoneAndObjectList_();

        list_path = findViewById(R.id.list_path);

        for(i=0; i<zoneAndObjectListReviewPath.size();i++){
            View zone = getLayoutInflater().inflate(R.layout.row_zone_cardview, null,false);
            CardView cardZone = (CardView)  zone.findViewById(R.id.card_zone);
            TextView nameZone = (TextView) zone.findViewById(R.id.zone_name);
            TextView objectZone = (TextView) zone.findViewById(R.id.object_name);
            LinearLayout transition = zone.findViewById(R.id.layout_transition);

            zone.setId(i);

            if(i==zoneAndObjectListReviewPath.size()-1) {
                nameZone.setText(zoneAndObjectListReviewPath.get(i).getZone());
                list_path.addView(zone);
                View end = getLayoutInflater().inflate(R.layout.row_end_path, null, false);
                list_path.addView(end);

                cardZone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    public void onClick(View view) {
                        int v = (objectZone.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                        TransitionManager.beginDelayedTransition(transition, new AutoTransition());

                        final List<String> listObj = zoneAndObjectListReviewPath.get(zone.getId()).getListObj();
                        final StringBuilder sb = new StringBuilder();

                        for (int j = 0; j < listObj.size(); j++) {

                            String objects = listObj.get(j);
                            if (j > 0) sb.append("\n");
                            sb.append(objects);

                            objectZone.setText(sb);

                        }

                        objectZone.setVisibility(v);

                    }
                });
            }else {
                nameZone.setText(zoneAndObjectListReviewPath.get(i).getZone());
                list_path.addView(zone);
                cardZone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    public void onClick(View view) {
                        int v = (objectZone.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                        TransitionManager.beginDelayedTransition(transition, new AutoTransition());

                        final List<String> listObj = zoneAndObjectListReviewPath.get(zone.getId()).getListObj();
                        final StringBuilder sb = new StringBuilder();

                        for (int j = 0; j < listObj.size(); j++) {

                            String objects = listObj.get(j);
                            if (j > 0) sb.append("\n");
                            sb.append(objects);

                            objectZone.setText(sb);

                        }

                        objectZone.setVisibility(v);

                    }
                });
            }

            name_path.setText(zoneAndObjectListReviewPath.get(0).getName());
            description_path.setText(zoneAndObjectListReviewPath.get(0).getDescription());


        }


        if (checkPermission()) {
            Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_LONG).show();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(CreateJsonActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                dialog.show();
                yesFinal.setText("OK");
                titleDialog.setText(R.string.permit_required);
                textDialog.setText(R.string.permission_text);
                textDialog.setTextColor(getResources().getColor(R.color.black));

                yesFinal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        requestPermission();
                    }
                });
            } else {
                requestPermission();
            }

        }

    }


    private boolean checkPermission(){

        int storagePermission= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return storagePermission== PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        int permissionCode=200; //codice definito da me, servirà nel caso in cui serva controllare più permessi
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},permissionCode);

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED ) {

            Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,R.string.permission_denied,Toast.LENGTH_LONG).show();

        }
    }

    public static void setZoneAndObjectListReviewPath(List<MapZoneAndObject> zoneAndObjectListReviewPath) {
        CreateJsonActivity.zoneAndObjectListReviewPath = zoneAndObjectListReviewPath;
    }




    public void bottomNavBarPathClick(){

        bottomNavigationViewPath.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.tb_download:
                        Graph<ZoneChoosed, DefaultEdge> graph = Step4.getGraph();
                        JSONExporter<ZoneChoosed, DefaultEdge> exporter = new JSONExporter<>(v -> String.valueOf(v));
                        exporter.setEdgeIdProvider(new IntegerIdProvider<>(1));
                        // ByteArrayOutputStream os = new ByteArrayOutputStream();
                        File file = null;
                        try {

                            File fil = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Percorso.json");
                            FileOutputStream fos = new FileOutputStream(fil);
                            // file = File.createTempFile("Percorso",".json");
                            exporter.exportGraph(graph, fos);
                            fos.close();
                            Toast.makeText(CreateJsonActivity.this, "Saved to " + getFilesDir() + "/" + "Percorso.json", Toast.LENGTH_LONG).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;

                    case R.id.tb_home_path:
                        Step4.setZoneAndObjectList_(null);
                        zoneAndObjectListReviewPath.clear();
                        PercorsoWizard.setDescriptionPath("");
                        PercorsoWizard.setNamePath("");
                        PercorsoWizard.setPlace(null);
                        ReviewZoneSelected.getZoneAndObjectList().clear();
                        Intent i = new Intent(CreateJsonActivity.this, DashboardActivity.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.tb_share:

                        Context context = CreateJsonActivity.this;
                        String str = "{ \"zona\": \"medioevo\",\n" +
                                " \"oggetto\": \"oggetto1\",\n" +
                                " \"oggetto\": \"oggetto 2\",\n" +
                                " \"zona1\": \"zona2\", \n" +
                                " \"oggetto\": \"oggetto1\", \n" +
                                " \"oggettto\": \"oggetto2\" }";
                        try {
                            Graph<ZoneChoosed, DefaultEdge> graphh = Step4.getGraph();

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

                            JSONExporter<ZoneChoosed, DefaultEdge> exporterr = new JSONExporter<>(v -> String.valueOf(v));
                            exporterr.setEdgeIdProvider(new IntegerIdProvider<>(1));
                            // ByteArrayOutputStream os = new ByteArrayOutputStream();
                            File filee = File.createTempFile("Percorso", ".json");
                            exporterr.exportGraph(graphh, filee);


                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("application/json");
                            Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                                    BuildConfig.APPLICATION_ID + ".provider", filee);
                            intent.putExtra(Intent.EXTRA_STREAM, uri);
                            startActivity(intent);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return true;

                }

                return false;
            }
        });

    }
}