package br.com.uol.ps.beacon.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import br.com.uol.ps.beacon.utils.ApplicationUtilities;

/**
 * @author Jean Rodrigo Dalbon Cunha
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            ApplicationUtilities.log(Log.INFO, "BOOT_COMPLETE: AlarmManager Set!");
            Toast.makeText(context, "BOOT_COMPLETE", Toast.LENGTH_SHORT).show();
            AlarmSetup.startAlarmIntent(context);
        }
    }

}
