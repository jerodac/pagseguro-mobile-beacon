package br.com.uol.ps.beacon.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;

import java.util.ArrayList;

import br.com.uol.ps.beacon.R;

/**
 * Application Utilies
 *
 * @author Jean Rodrigo Dalbon Cunha
 */
public class ApplicationUtilities {

    private static final boolean logEnabled = true;
    private final static String TAG = "BEACON.PS";

    /**
     * Armazena as dialogs abertas
     */
    private static ArrayList<Dialog> DIALOGS = new ArrayList<Dialog>();

    /**
     * Imprimi os logs somente se a flag estiver liberada
     */
    public static void log(int level, String message) {
        if (logEnabled) {
            switch (level) {
                case Log.INFO:
                    Log.i(TAG, message);
                    break;
                case Log.ERROR:
                    Log.e(TAG, message);
                    break;
                default:
                    Log.v(TAG, message);
            }
        }
    }

    /**
     * Imprimi os logs somente se a flag estiver liberada
     * Sobrecarga com Exception param
     */
    public static void log(int level, String message, Exception e) {
        if (logEnabled) {
            switch (level) {
                case Log.INFO:
                    Log.i(TAG, message, e);
                    break;
                case Log.ERROR:
                    Log.e(TAG, message, e);
                    break;
                default:
                    Log.v(TAG, message, e);
            }
        }
    }

    /**
     * Registra uma dialog para ser fechada quando a tela da aplicação for minimizada
     *
     * @param dialog
     */
    public static void registerDialog(Dialog dialog) {
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public synchronized void onDismiss(DialogInterface dialog) {
                DIALOGS.remove(dialog);
            }
        });
        DIALOGS.add(dialog);
    }

    public static void showQuestionDialog(Activity activity, String msg,
                                          DialogInterface.OnClickListener dialogClickListener) {
        if (activity != null) {
            if (!activity.isFinishing()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.dialog_title);
                builder.setMessage(msg);
                builder.setPositiveButton(R.string.text_yes, dialogClickListener);
                builder.setNegativeButton(R.string.text_no, dialogClickListener);
                registerDialog(builder.show());
            }
        }
    }


}
