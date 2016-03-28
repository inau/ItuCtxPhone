package pervasive2016.itu.contextapp.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Created by martinosecchi on 17/03/16.
 */
public class TemperatureMonitor extends ContextMonitor {

    public float value; // ambient temperature in degree Celsius

    public TemperatureMonitor(){
        setId();
    }

    public TemperatureMonitor(Sensor sensor){
        setId();
        if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            setSensor(sensor);
            setType("Temperature");
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
