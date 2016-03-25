package android.spvct.itu.dk.beacontest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by DIEM NGUYEN HOANG on 3/3/2016.
 */
public class ContextMonitor{
    public String mType;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mSensor;
    protected float value;

    public void ContextMonitor(String type, Context mContext, Sensor sensor){
        mType = type;
        mSensor = sensor;
        //mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        //mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //mAccelerometer = ssmgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void getValue(){
        Log.i("GGGGGGGGGGG", "get Value = " + value);
    }


}

class AccelerometerMonitor extends ContextMonitor implements SensorEventListener{
    public AccelerometerMonitor(){

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i("AAAAAAAAA", "ACCELEROMETER ACCURACY CHANGE START");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float axisX = event.values[0];
        float axisY = event.values[1];
        float axisZ = event.values[2];
        value = axisX;

        Log.i("AAAAAAAAA", "CHANGE x = " + axisX + " y = " + axisY + " z = " + axisZ);

    }
}

class ProximityMonitor extends ContextMonitor implements SensorEventListener{
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i("PPPPPPPP", "PROXIMITY ACCURACY CHANGE START");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        value = event.values[0];
        Log.i("PPPPPPPP", "PROXIMITY VALUES = " + event.values[0]);


    }
}

class LightMonitor extends ContextMonitor implements SensorEventListener {
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i("LLLLLLLL", "LIGHT ACCURACY CHANGE START");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        value = event.values[0];
        //Log.i("LLLLLLLL", "LIGHT VALUES = " + event.values[0]);


    }
}
