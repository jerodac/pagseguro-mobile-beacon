package br.com.uol.ps.beacon.mocks;

import android.content.Context;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;

import br.com.uol.ps.beacon.others.OffersFacade;
import br.com.uol.ps.beacon.others.OffersModel;
import br.com.uol.ps.beacon.business.DataStorageApp;
import br.com.uol.ps.beacon.utils.ApplicationUtilities;

/**
 * Mocks locais
 *
 * @author Jean Rodrigo Dalbon Cunha
 */
public class CardViewPopulateMock {

    public static ArrayList<OffersModel> getDataSet() {

        ArrayList<OffersModel> mockObjects = new ArrayList<OffersModel>();
        mockObjects.add(new OffersModel(001, "01/11 12:10hs", "Hoje tem Dog!! Aúuuu!", new BigDecimal(4.00), "http://oi68.tinypic.com/8wgihl.jpg"));
        mockObjects.add(new OffersModel(002, "02/11 13:20hs", "Nhoque Recheado + Arroz", new BigDecimal(23.00), "http://oi67.tinypic.com/6zroz6.jpg"));
        mockObjects.add(new OffersModel(003, "03/11 14:30hs", "Bateu fome? então vai de X-Burger", new BigDecimal(5.00), "http://oi65.tinypic.com/307oumh.jpg"));
        mockObjects.add(new OffersModel(004, "04/11 15:40hs", "Combo Kit de Esfirras (3 Pessoas)", new BigDecimal(10.00), "http://oi66.tinypic.com/dcsah4.jpg"));
        mockObjects.add(new OffersModel(005, "05/11 17:45hs", "Sobá Filé Frango", new BigDecimal(15.00), "http://oi63.tinypic.com/15yyc94.jpg"));
        mockObjects.add(new OffersModel(006, "06/11 10:18hs", "Hoje tem Dog!! Aúuuu!", new BigDecimal(4.00), "http://oi68.tinypic.com/8wgihl.jpg"));
        mockObjects.add(new OffersModel(007, "07/11 11:50hs", "Bateu fome? então vai de X-Burger", new BigDecimal(5.00), "http://oi65.tinypic.com/307oumh.jpg"));

        return mockObjects;
    }

    public static void initializeMockActivity(Context context) {
        //Mock Local itens (Popular CardView com itens mockado)
        if (DataStorageApp.retrieveOffers(context) == null) {
            ApplicationUtilities.log(Log.VERBOSE, "Persistindo itens mockado (LOCAL MOCK)");
            OffersFacade offersFacade = new OffersFacade(context);
            offersFacade.add(CardViewPopulateMock.getDataSet());
            offersFacade.save(context);
            ApplicationUtilities.log(Log.VERBOSE, "Preference: " + DataStorageApp.retrieveOffers(context));
        }
    }
}
