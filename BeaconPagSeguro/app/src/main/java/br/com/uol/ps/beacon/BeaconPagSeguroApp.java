package br.com.uol.ps.beacon;

import br.com.uol.ps.beacon.business.Configuration;
import br.com.uol.ps.beacon.business.API;

/**
 * Created by jeanrodrigo on 04/11/15.
 */
public class BeaconPagSeguroApp {

    public static API getApi() {
        return Configuration.getApi();
    }

}
