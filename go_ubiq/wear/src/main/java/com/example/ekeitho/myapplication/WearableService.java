package com.example.ekeitho.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.WearableListenerService;

public class WearableService extends WearableListenerService {
    public WearableService() {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.e("OOPS", "OOPS");
        super.onDataChanged(dataEvents);
    }
}
