package pervasive2016.itu.contextapp.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;

/**
 * Created by martinosecchi on 17/03/16.
 */

// NB the motorola only has accelerometer and proximity sensors
public abstract class ContextMonitor implements SensorEventListener{
    private static int COUNT = 0;
    private int id;
    private String type;

    private Sensor sensor;

    protected void setId(){
        id = COUNT;
        COUNT = COUNT + 1;
    }

    public String toString(){
        return "id: " + id + " sensor-type: " + type + " ";
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}



