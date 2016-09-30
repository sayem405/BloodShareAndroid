package com.bloodshare.bloodshareandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AppBroadcastReceiver extends BroadcastReceiver {
    public AppBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "fired", Toast.LENGTH_SHORT).show();

        String URL = "http://androidexample.com/media/webservice/httpget.php?user=";

        //Log.i("httpget", URL);


    }
}
