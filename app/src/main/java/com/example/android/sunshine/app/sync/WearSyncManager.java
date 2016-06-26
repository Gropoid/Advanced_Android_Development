package com.example.android.sunshine.app.sync;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.math.BigInteger;

public class WearSyncManager implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "WearSyncManager";
    private static final String WEATHER_ITEM = "/WeatherItem";
    private PutDataRequest data;
    GoogleApiClient googleApiClient;

    public WearSyncManager(Context context) {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
    }

    public void sendWeatherValue(int value) {
        Log.d(TAG, String.format("sendWeatherValue : %s", value));
        BigInteger i = BigInteger.valueOf(value);
        data = PutDataRequest.create(WEATHER_ITEM);
        data.setData(i.toByteArray());
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
        Wearable.DataApi.putDataItem(googleApiClient, data);
        googleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Could not connect to Wearable API");
    }
}
