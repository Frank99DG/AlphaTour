package com.example.alphatour.connection;

import static com.example.alphatour.connection.NetworkControl.getConnectionStatus;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphatour.LoginActivity;
import com.example.alphatour.R;

public class Receiver extends BroadcastReceiver {

    private Dialog dialog;
    private Button yesFinal;
    private TextView titleDialog,textDialog;
    private Boolean flag=false;
    /**serve per coontrollare se la chiamata proviene dal login**/
    private int controlValue;

    public  Receiver(){

    }

    public  Receiver(int val){

        this.controlValue=val;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        /**chiamata alla connessione*/

        String status=getConnectionStatus(context);
        dialog= new Dialog(context);
        dialog.setContentView(R.layout.dialog_permission);
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.backgroun_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;

        yesFinal = dialog.findViewById(R.id.btn_termina_permission);
        titleDialog=dialog.findViewById(R.id.titleDialog_permission);
        textDialog=dialog.findViewById(R.id.textDialog_permission);
        yesFinal.setText("Riprova");
        titleDialog.setText("Connessione");
        textDialog.setText("Connessione internet assente");


        yesFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });

        if(status.isEmpty() || status.matches("No internet is available")){

            if(controlValue==1){
                dialog.show();
            }
            flag=false;
            Toast.makeText(context,status,Toast.LENGTH_LONG).show();
        }else{
            flag=true;
        }


    }



    public Boolean isConnected() {
        return flag;
    }
}
