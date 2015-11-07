package br.com.uol.ps.beacon.vo;

import com.google.gson.annotations.SerializedName;

import br.com.uol.ps.beacon.BuildConfig;

/**
 * @author Jean Rodrigo Dalbon Cunha
 */
public class BeaconRequestVO {

    @SerializedName("id")
    private final String id;

    @SerializedName("appVersion")
    private final String appVersion;


    public BeaconRequestVO(String id) {
        this.id = id;
        appVersion = BuildConfig.VERSION_NAME;
    }

    @Override
    public String toString() {
        return "BeaconRequestVO{" +
                "id='" + id + '\'' +
                ", appVersion='" + appVersion + '\'' +
                '}';
    }
}
