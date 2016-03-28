package pervasive2016.itu.contextapp.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Created by martinosecchi on 17/03/16.
 */
public class PressureMonitor extends ContextMonitor {

    public float value; // atmospheric pressure in hPa (millibar)

    public PressureMonitor(){
        setId();
    }

    public PressureMonitor(Sensor sensor){
        setId();
        if (sensor.getType() == Sensor.TYPE_PRESSURE) {
            setSensor(sensor);
            setType("Pressure");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        value = event.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public String toString(){
        return  super.toString() + "value: " + value;
    }
}
