package br.com.uol.ps.beacon.services;

/**
 * Created by dario on 28/10/15.
 */
public final class CommonConstants {

    public CommonConstants() {

        // don't allow the class to be instantiated
    }

    // Milliseconds in the snooze duration, which translates
    // to 20 seconds.
    public static final int DEFAULT_TIMER_DURATION = 10000;
    public static final String ACTION_PING = "Ping";
    public static final String EXTRA_MESSAGE = "Beacon";
    public static final String EXTRA_TIMER = "Tempo";
    public static final int NOTIFICATION_ID = 001;
}
