package com.inau.gstur.gesturepresenter.Comms;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.inau.gstur.gesturepresenter.MainActivity;


/**
 * Created by martinosecchi on 17/03/16.
 */
public class GestureService extends Service {

    private Thread thread;

    public GestureService(){}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("BB", "btr " + MainActivity.getBTRef() );

        thread = new Thread( new WebPeriodicReceiver(MainActivity.getBTRef()) );
        thread.start();

        return START_STICKY; //runs until explicitly stopped
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
        //return null;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}