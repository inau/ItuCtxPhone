package android.spvct.itu.dk.beacontest;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DIEM NGUYEN HOANG on 3/3/2016.
 */
public class ContextService extends Service {
    private static final String TAG = "HelloService";

    private boolean isRunning  = false;
    public List listOfMonitor = new ArrayList<ContextMonitor>();

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Sensor mProximitySensor;
    private Sensor mLightSensor;
    private AccelerometerMonitor mAcclerometerMonitor = new AccelerometerMonitor();
    private ProximityMonitor mProximityMonitor = new ProximityMonitor();
    private LightMonitor mLightMonitor = new LightMonitor();



    @Override
    public void onCreate() {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //listOfMonitor.add(mLocationMonitor);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //mSensorManager.registerListener(mContextMonitor, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        //mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        //mSensorManager.registerListener(mProximityMonitor, mProximitySensor, SensorManager.SENSOR_DELAY_NORMAL);

        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(mLightMonitor, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand");

        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR
        new Thread(new Runnable() {
            @Override
            public void run() {

                //Your logic that service will perform will be placed here
                //In this example we are just looping and waits for 1000 milliseconds in each loop.
                for (int i = 0; i < 5000; i++) {
                    try {
                        Thread.sleep(5000);
                        mLightMonitor.getValue();
                        //Log.i(TAG, "Service running");
                    } catch (Exception e) {
                    }

                    if(isRunning){
                        Log.i(TAG, "Service running");
                    }
                }

                //Stop service once it finishes its task
                stopSelf();
            }
        }).start();

        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {

        isRunning = false;
        mSensorManager.unregisterListener(mProximityMonitor);
        Log.i(TAG, "Service onDestroy");
    }

}
