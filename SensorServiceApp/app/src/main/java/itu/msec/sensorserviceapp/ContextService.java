package itu.msec.sensorserviceapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import java.net.MalformedURLException;

import itu.msec.sensorserviceapp.wswrapper.data.entity.ContextEntity;
import itu.msec.sensorserviceapp.wswrapper.web.ApiAdapter;

/**
 * Created by martinosecchi on 17/03/16.
 */
public class ContextService extends Service {

    private SensorManager mSensorManager;
    private TemperatureMonitor tempMonitor;
    private PressureMonitor pressMonitor;
    private SoundMonitor soundMonitor;
    private Thread tempThread;
    private Thread pressThread;
    private Thread soundThread;
    private int sleepTime = 10000;

    public ContextService(){}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        final Sensor pressSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        final Sensor tempSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        if (pressSensor != null) {
            pressMonitor = new PressureMonitor(pressSensor);
        } else {
            Log.i("ContextService", "NO PRESSURE SENSORS ON THIS DEVICE");
            pressMonitor = new PressureMonitor();
        }
        if (tempSensor != null) {
            tempMonitor = new TemperatureMonitor(tempSensor);
        } else {
            Log.i("ContextService", "NO TEMPERATURE SENSORS ON THIS DEVICE");
            tempMonitor = new TemperatureMonitor();
        }

        mSensorManager.registerListener( pressMonitor, pressSensor, SensorManager.SENSOR_DELAY_NORMAL );
        mSensorManager.registerListener( tempMonitor, tempSensor, SensorManager.SENSOR_DELAY_NORMAL );

        tempThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (tempSensor != null){
                    if (UserLocation.getLongitude() != 0 && UserLocation.getLatitude() != 0) {
                        Log.i("ContextService - TEMP", tempMonitor.toString());
                        ContextEntity c = new ContextEntity(
                                (long) UserLocation.getLatitude(),
                                (long) UserLocation.getLongitude(),
                                "ambient temperature",
                                "" + tempMonitor.value
                        );
                        Gson gson = new Gson();
                        String body = gson.toJson(c);
                        try {
                            new ApiAdapter<ContextEntity>(ApiAdapter.WebMethod.POST, null, body, null, ContextEntity.class)
                                    .execute(ApiAdapter.urlBuilder(ApiAdapter.APIS.CONTEXTS, ""));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(sleepTime);
                    } catch( InterruptedException exn){}
                }
            }
        });

        pressThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (pressSensor != null){
                    if (UserLocation.getLongitude() != 0 && UserLocation.getLatitude() != 0) {

                        Log.i("ContextService - PRESS", pressMonitor.toString());

                        ContextEntity c = new ContextEntity(
                                (long) UserLocation.getLatitude(),
                                (long) UserLocation.getLongitude(),
                                "atmospheric pressure",
                                "" + pressMonitor.value
                        );
                        Gson gson = new Gson();
                        String body = gson.toJson(c);
                        try {
                            new ApiAdapter<ContextEntity>(ApiAdapter.WebMethod.POST, null, body, null, ContextEntity.class)
                                    .execute(ApiAdapter.urlBuilder(ApiAdapter.APIS.CONTEXTS, ""));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                    }
                    try {
                        Thread.sleep(sleepTime);
                    } catch( InterruptedException exn){}
                }
            }
        });

        soundMonitor = new SoundMonitor( getFilesDir() );

        soundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                double sum = 0.0, sample;
                final int count = 10;
                int i = 0;
                while (true) {
                    if (UserLocation.getLongitude() != 0 && UserLocation.getLatitude() != 0) {

                        sum = 0.0;
                        for (i = 0; i < count; i++) {
                            sample = soundMonitor.getSound();
                            Log.i("ContextService - SOUND", "" + sample);
                            sum += sample;
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException exn) {
                            }
                        }
                        double value = sum / count; //average noise
                        ContextEntity c = new ContextEntity(
                                (long) UserLocation.getLatitude(),
                                (long) UserLocation.getLongitude(),
                                "sound",
                                "" + value
                        );
                        Gson gson = new Gson();
                        String body = gson.toJson(c);
                        try {
                            new ApiAdapter<ContextEntity>(ApiAdapter.WebMethod.POST, null, body, null, ContextEntity.class)
                                    .execute(ApiAdapter.urlBuilder(ApiAdapter.APIS.CONTEXTS, ""));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                    }
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e){}
                }
            }
        });

        tempThread.start();
        pressThread.start();
        soundThread.start();

        return START_STICKY; //runs until explicitly stopped
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        tempThread.interrupt();
        pressThread.interrupt();
        soundThread.interrupt();
    }
}