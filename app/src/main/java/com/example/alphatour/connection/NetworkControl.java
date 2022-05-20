package com.example.alphatour.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkControl {

    public static String getConnectionStatus(Context context){
        String status ="";

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activityNetwork = cm.getActiveNetworkInfo();
        if(activityNetwork!=null){
            if(activityNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                status="Wifi enabled";
                return status;
            }else{
                if(activityNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                    status="Mobile data enabled";
                    return status;
                }
            }
        }else{
            status="No internet is available";
            return status;
        }
        return status;
    }
}
