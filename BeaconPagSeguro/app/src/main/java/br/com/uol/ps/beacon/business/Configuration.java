package br.com.uol.ps.beacon.business;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class Configuration {

    public static final String TEST_HOST = "http://192.168.56.1:8081";

    public static API getApi() {
        return getApi("http://private-8e3b2-beaconpagseguro.apiary-mock.com");
    }

    public static API getApi(String host) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(host)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setErrorHandler(new ErrorHandler() {
                    @Override
                    public Throwable handleError(RetrofitError retrofitError) {
                        return retrofitError;
                    }
                })
                .build();
        return restAdapter.create(API.class);
    }
}
