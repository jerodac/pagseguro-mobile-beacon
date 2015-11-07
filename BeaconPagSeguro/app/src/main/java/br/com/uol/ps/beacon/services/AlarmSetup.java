package br.com.uol.ps.beacon.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import br.com.uol.ps.beacon.utils.ApplicationUtilities;

/**
 * Alarm Setup
 *
 * @author Jean Rodrigo Dalbon Cunha
 */
public class AlarmSetup {

    /**
     * Intent TAG
     */
    public static String TAG_INTENT = "ALARME_DISPARADO";

    /**
     * 5 Minutes
     */
    private static final long REPEAT_INTERVAL = 1000 * 60 * 1;

    public static void startAlarmIntent(Context context) {
        ApplicationUtilities.log(Log.INFO, "startAlarmIntent()");
        boolean activeAlarm = (PendingIntent.getBroadcast(context, 0, new Intent(TAG_INTENT), PendingIntent.FLAG_NO_CREATE) == null);

        if (activeAlarm) {
            ApplicationUtilities.log(Log.INFO, "Create intent start alarm");
            Intent intent = new Intent(TAG_INTENT);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            c.add(Calendar.SECOND, 3);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), REPEAT_INTERVAL, pendingIntent);
        } else {
            ApplicationUtilities.log(Log.INFO, "Alarm is already active");
        }
    }
}
