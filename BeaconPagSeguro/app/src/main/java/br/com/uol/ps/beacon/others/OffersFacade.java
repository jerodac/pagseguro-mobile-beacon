package br.com.uol.ps.beacon.others;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.uol.ps.beacon.business.DataStorageApp;
import br.com.uol.ps.beacon.utils.ApplicationUtilities;

/**
 * @author Jean Rodrigo Dalbon Cunha
 */
public class OffersFacade {

    /**
     * Items promocionais maximo
     */
    private static final int MAX_ITEM = 30;

    private List<OffersModel> mListOffers;

    public OffersFacade(Context context) {
        List<OffersModel> offers = retrieve(context);
        if (offers != null) {
            mListOffers = offers;
        } else {
            mListOffers = new ArrayList<OffersModel>();
        }
    }

    public List<OffersModel> retrieve(Context context) {
        return DataStorageApp.retrieveOffers(context);
    }

    public void save(Context context) {
        ApplicationUtilities.log(Log.INFO, "Save shared preferences");
        DataStorageApp.saveOffers(context, this);
    }

    public int getCount() {
        return mListOffers.size();
    }

    /**
     * Adiciona uma oferta, porém já estiver cadatrada ela não é adicionada novamente
     */
    public void add(OffersModel offersModel) {
        if (!mListOffers.contains(offersModel)) {
            mListOffers.add(offersModel);
        }
    }

    /**
     * Adiciona uma colection de ofertas, porém se alguma oferta já está cadatrada ela não é adicionada novamente
     *
     * @param listOffers
     */
    public void add(List<OffersModel> listOffers) {
        for (OffersModel offer : listOffers) {
            if (mListOffers.contains(offer)) {
                ApplicationUtilities.log(Log.VERBOSE, "Oferta já cadastrada");
                continue;
            }
            ApplicationUtilities.log(Log.VERBOSE, "Oferta adicionada");
            this.mListOffers.add(offer);
        }
    }

    public boolean containsOffer(OffersModel offersModel) {
        return mListOffers.contains(offersModel);
    }

    public List<OffersModel> getOffers() {
        return mListOffers;
    }

}
