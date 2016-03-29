package pervasive2016.itu.contextapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import pervasive2016.itu.contextapp.sensors.PressureMonitor;
import pervasive2016.itu.contextapp.sensors.SoundMonitor;
import pervasive2016.itu.contextapp.sensors.TemperatureMonitor;
import pervasive2016.itu.contextapp.sensors.background.PressureRunnable;
import pervasive2016.itu.contextapp.sensors.background.SoundRunnable;
import pervasive2016.itu.contextapp.sensors.background.TemperatureRunnable;


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

        tempThread = new Thread( new TemperatureRunnable(tempMonitor) );
        pressThread = new Thread( new PressureRunnable(pressMonitor) );

        soundMonitor = new SoundMonitor( getFilesDir() );
        soundThread = new Thread( new SoundRunnable(soundMonitor) );

        tempThread.start();
        pressThread.start();
        soundThread.start();

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
        tempMonitor.setSensor(null);
        pressMonitor.setSensor(null);
        soundMonitor.setGoing(false);
        tempThread.interrupt();
        pressThread.interrupt();
        soundThread.interrupt();
    }
}