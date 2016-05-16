package com.boxer.browser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;



public class AlarmReceiverTime extends BroadcastReceiver {

    private static final String DEBUG_TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(DEBUG_TAG, "Recurring alarm; requesting location tracking.");
        // start the service
        
       // Toast.makeText(context, "service call",Toast.LENGTH_SHORT).show();
        Intent tracking = new Intent(context, TimeService.class);
        context.startService(tracking);
    }
}

