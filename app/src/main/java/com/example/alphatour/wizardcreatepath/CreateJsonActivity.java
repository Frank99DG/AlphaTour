package com.example.alphatour.wizardcreatepath;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.BuildConfig;
import com.example.alphatour.mainUI.DashboardActivity;
import com.example.alphatour.R;
import com.example.alphatour.objectclass.MapZoneAndObject;
import com.example.alphatour.objectclass.ZoneChoosed;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jgrapht.nio.IntegerIdProvider;
import org.jgrapht.nio.json.JSONExporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateJsonActivity extends AppCompatActivity {

    private Button yesFinal, cancelFinal;
    private ConstraintLayout layt;
    private Dialog dialog;
    private TextView titleDialog, textDialog;
    private static List<MapZoneAndObject> zoneAndObjectListReviewPath = new ArrayList<MapZoneAndObject>();
    private LinearLayout list_path;
    private ConstraintLayout layout;
    private BottomAppBar bottomAppBarPath;
    private TextView name_path,description_path;
    private int i=0;
    private FloatingActionButton home_path_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_json);
        layt = findViewById(R.id.layoutJson);
        name_path = findViewById(R.id.name_path);
        description_path = findViewById(R.id.description_path);
        home_path_button = findViewById(R.id.home_path_button);


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_permission);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroun_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        yesFinal = dialog.findViewById(R.id.btn_termina_permission);
        titleDialog = dialog.findViewById(R.id.titleDialog_permission);
        textDialog = dialog.findViewById(R.id.textDialog_permission);

        bottomAppBarPath = findViewById(R.id.bottomAppBarPath);
        setSupportActionBar(bottomAppBarPath);

        bottomAppBarPathClick();

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
                            if (j > 0) sb.append(",\n");
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
                            if (j > 0) sb.append(",\n");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bottom_path_menu,menu);

        return true;
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

    public void bottomAppBarPathClick(){


        home_path_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                moveToDashboard();
                finishAffinity();
                overridePendingTransition(0,0);
            }
        });

        bottomAppBarPath.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch(item.getItemId()){
                    case R.id.download_path:
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
                    case R.id.share_path:

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

    public static List<MapZoneAndObject> getZoneAndObjectListReviewPath() {
        return zoneAndObjectListReviewPath;
    }

    public static void setZoneAndObjectListReviewPath(List<MapZoneAndObject> zoneAndObjectListReviewPath) {
        CreateJsonActivity.zoneAndObjectListReviewPath = zoneAndObjectListReviewPath;
    }


    @Override
    public void onBackPressed() {

     moveToDashboard();

    }

    private void moveToDashboard(){

        List<String> a = Step4.getZone_select();
        List<String> b = Step4.getOggetti_select();
        List<String> c = Step2.getArray_database();
        List<MapZoneAndObject> d = Step4.getZoneAndObjectList_();
        List<MapZoneAndObject> e = ReviewZoneSelected.getZoneAndObjectList();
        List<MapZoneAndObject> f = CreateJsonActivity.getZoneAndObjectListReviewPath();

        if(a.isEmpty()) Step4.getZone_select().clear();
        if(b.isEmpty()) Step4.getOggetti_select().clear();
        if(c.isEmpty()) Step2.getArray_database().clear();
        if(d.isEmpty()) Step4.getZoneAndObjectList_().clear();
        if(e.isEmpty()) ReviewZoneSelected.getZoneAndObjectList().clear();
        if(f.isEmpty()) CreateJsonActivity.getZoneAndObjectListReviewPath().clear();

        zoneAndObjectListReviewPath.clear();
        DashboardActivity.setFirstZoneChosen(false);
        PercorsoWizard.setDescriptionPath("");
        PercorsoWizard.setNamePath("");
        PercorsoWizard.setPlace(null);

        Intent intent= new Intent(CreateJsonActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

}