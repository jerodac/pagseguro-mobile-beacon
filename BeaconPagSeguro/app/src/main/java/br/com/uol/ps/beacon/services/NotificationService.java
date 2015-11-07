package br.com.uol.ps.beacon.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;

import br.com.uol.ps.beacon.MainActivity;
import br.com.uol.ps.beacon.R;
import br.com.uol.ps.beacon.utils.ApplicationUtilities;

/**
 * NotificationService class
 *
 * @author Dario Calzoli
 */
public class NotificationService extends IntentService {

    private NotificationManager mNotificationManager;
    private String mMessage;
    private int mMillis;
    NotificationCompat.Builder builder;

    public NotificationService() {
        super("br.com.uol.ps.beacon");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mMessage = intent.getStringExtra(CommonConstants.EXTRA_MESSAGE);
        mMillis = intent.getIntExtra(CommonConstants.EXTRA_TIMER,
                CommonConstants.DEFAULT_TIMER_DURATION);

        issueNotification(intent, mMessage);
    }

    private void issueNotification(Intent intent, String msg) {
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        builder =
                new NotificationCompat.Builder(this)
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setSmallIcon(R.drawable.ic_add_shopping_cart_white_24dp)
                        .setContentTitle("Beacon PagSeguro")
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setContentText(msg)
                        .setColor(Color.BLUE)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg));


        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra(CommonConstants.EXTRA_MESSAGE, msg);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setContentIntent(resultPendingIntent);
        startTimer(mMillis);
    }

    private void issueNotification(NotificationCompat.Builder builder) {
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(CommonConstants.NOTIFICATION_ID, builder.build());
    }

    private void startTimer(int millis) {
        try {
            Thread.sleep(millis);

        } catch (InterruptedException e) {
            ApplicationUtilities.log(Log.ERROR, "Falha ao iniciar startTimer()", e);
        }
        issueNotification(builder);
    }

    public static void callNotification(Context context, String message,  final List devicesFounds) {
        Intent mServiceIntent = new Intent(context, NotificationService.class);
        mServiceIntent.putExtra(CommonConstants.EXTRA_MESSAGE, message+"\n"+devicesFounds);
        mServiceIntent.setAction(CommonConstants.ACTION_PING);

        int milliseconds = (0);
        mServiceIntent.putExtra(CommonConstants.EXTRA_TIMER, milliseconds);
        context.startService(mServiceIntent);
    }

    public static void callNotification(Context context, String message) {
        Intent mServiceIntent = new Intent(context, NotificationService.class);
        mServiceIntent.putExtra(CommonConstants.EXTRA_MESSAGE, message);
        mServiceIntent.setAction(CommonConstants.ACTION_PING);

        int milliseconds = (0);
        mServiceIntent.putExtra(CommonConstants.EXTRA_TIMER, milliseconds);
        context.startService(mServiceIntent);
    }
}
