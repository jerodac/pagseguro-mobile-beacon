package br.com.uol.ps.beacon.business;

import br.com.uol.ps.beacon.vo.BeaconRequestVO;
import br.com.uol.ps.beacon.vo.BeaconResponseVO;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * API
 *
 * @author Jean Rodrigo Dalbon Cunha
 */
public interface API {

    @POST("/beacons")
    BeaconResponseVO beacons(@Body BeaconRequestVO beaconRequestVO);
}
