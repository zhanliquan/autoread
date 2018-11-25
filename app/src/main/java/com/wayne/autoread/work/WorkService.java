package com.wayne.autoread.work;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WorkService extends Service {
    public WorkService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
