package br.com.uol.ps.beacon.vo;

import java.util.List;

import br.com.uol.ps.beacon.others.OffersModel;

/**
 * @author Jean Rodrigo D. Cunha
 */
public class BeaconResponseVO extends BaseResponseVO {

    private String lastUpdate;

    private List<OffersModel> offers;

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<OffersModel> getOffers() {
        return offers;
    }

    public void setOffers(List<OffersModel> offers) {
        this.offers = offers;
    }

    @Override
    public String toString() {
        super.toString();
        return "BeaconResponseVO{" +
                "lastUpdate='" + lastUpdate + '\'' +
                ", offers=" + offers +
                '}';
    }
}
