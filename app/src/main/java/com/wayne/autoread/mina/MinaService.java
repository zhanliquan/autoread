package com.wayne.autoread.mina;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;

public class MinaService extends Service {
    private ConnectionThread thread;
    @Override
    public void onCreate() {
        super.onCreate();
        thread = new ConnectionThread("mina", getApplicationContext());
        thread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thread.disConnection();
        thread = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class ConnectionThread extends HandlerThread {
        private Context context;
        boolean isConnection;
        ConnectionManager mManager;

        ConnectionThread(String string, Context context) {
            super(string);
            this.context = context;
            ConnectionConfig config = new ConnectionConfig.Builder(context)
                    .setIp("192.168.0.102")
                    .setPort(9123)
                    .setReadBufferSize(10240)
                    .setConnectionTimeout(10000).builder();
            mManager = new ConnectionManager(config);
        }

        @Override
        protected void onLooperPrepared() {

            for(;;) {
                isConnection = mManager.connect();
                if (isConnection) {
                    break;
                }

                try {
                    Thread.sleep(3000);
                } catch (Exception e) {

                }

            }
        }

        public void disConnection() {
            mManager.disConnection();
        }

    }
}
