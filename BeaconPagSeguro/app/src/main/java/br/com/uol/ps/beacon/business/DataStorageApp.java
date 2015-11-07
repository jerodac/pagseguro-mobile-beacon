package br.com.uol.ps.beacon.business;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

import br.com.uol.ps.beacon.others.DeviceFound;
import br.com.uol.ps.beacon.others.DeviceFoundBuffer;
import br.com.uol.ps.beacon.others.OffersFacade;
import br.com.uol.ps.beacon.others.OffersModel;

/**
 * Classe responsável por armazenar e recuperar dados que deverão ficar salvos na aplicação.
 *
 * @author Jean Rodrigo Dalbon Cunha
 */
public class DataStorageApp {

    /**
     * Arquivo de preferências.
     */
    private static SharedPreferences sharedPreferences;

    /**
     * Editor do arquivo de preferências.
     */
    private static SharedPreferences.Editor editor;

    /**
     * Chave do Shared Preferences.
     */
    private static final String PREFS_NAME = "pagSegPrefsFile";

    /**
     * Offers
     */
    private static final String OFFERS = "OFFERS";

    /**
     * Found Devices
     */
    private static final String FOUND_DEVICE_BUFFER = "FOUND_DEVICE_BUFFER";

    /**
     * Persiste as ofertas
     */
    public static void saveOffers(Context ctx, OffersFacade offersFacade) {
        sharedPreferences = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(offersFacade);
        editor.putString(OFFERS, json);
        editor.commit();
    }

    /**
     * Recupera ofertas salva
     */
    public static List<OffersModel> retrieveOffers(Context ctx) {
        List<OffersModel> offers = null;
        Gson gson = new Gson();
        sharedPreferences = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(OFFERS)) {
            String json = sharedPreferences.getString(OFFERS, "");
            OffersFacade offersFacade = gson.fromJson(json, OffersFacade.class);
            offers = offersFacade.getOffers();
        }
        return offers;
    }

    /**
     * Persiste os devices bluetooth encontrados pelo broadcast receiver
     */
    public static void saveFoundDeviceBuffer(Context ctx, DeviceFoundBuffer deviceFoundBuffer) {
        sharedPreferences = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(deviceFoundBuffer);
        editor.putString(FOUND_DEVICE_BUFFER, json);
        editor.commit();
    }

    /**
     * Recupera buffer devices found
     */
    public static List<DeviceFound> retrieveDeviceFoundBuffer(Context ctx) {
        List<DeviceFound> listFoundDevicesBuffer = null;
        Gson gson = new Gson();
        sharedPreferences = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(FOUND_DEVICE_BUFFER)) {
            String json = sharedPreferences.getString(FOUND_DEVICE_BUFFER, "");
            DeviceFoundBuffer deviceFoundBuffer = gson.fromJson(json, DeviceFoundBuffer.class);
            listFoundDevicesBuffer = deviceFoundBuffer.getListDeviceFound();
        }
        return listFoundDevicesBuffer;
    }

    public static void clearPersistenceDeviceBuffer(Context ctx) {
        sharedPreferences = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.remove(FOUND_DEVICE_BUFFER);
        editor.commit();
    }

}
