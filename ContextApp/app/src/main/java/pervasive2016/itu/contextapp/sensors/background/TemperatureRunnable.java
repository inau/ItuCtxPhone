package pervasive2016.itu.contextapp.sensors.background;

import android.util.Log;

import com.google.gson.Gson;

import java.net.MalformedURLException;

import pervasive2016.itu.contextapp.data.UserLocation;
import pervasive2016.itu.contextapp.data.entity.ContextEntity;
import pervasive2016.itu.contextapp.sensors.TemperatureMonitor;
import pervasive2016.itu.contextapp.web.ApiAdapter;

/**
 * Created by martinosecchi on 28/03/16.
 */
public class TemperatureRunnable implements Runnable {
    private TemperatureMonitor tempMonitor;
    private int sleepTime;
    public TemperatureRunnable( TemperatureMonitor temperatureMonitor){
        this.tempMonitor = temperatureMonitor;
        this.sleepTime = 10000; // 10 seconds
    }
    public TemperatureRunnable( TemperatureMonitor temperatureMonitor, int sleepTime){
        this.tempMonitor = temperatureMonitor;
        this.sleepTime = sleepTime;
    }
    @Override
    public void run() {
        while (tempMonitor.getSensor() != null){
            if (UserLocation.getLongitude() != 0 && UserLocation.getLatitude() != 0) {

                Log.i("ContextService - TEMP", tempMonitor.toString());

                String body = ApiAdapter.ctxJsonFactory(UserLocation.getLatitude(), UserLocation.getLongitude(), "ambient temperature", "" + tempMonitor.value);
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
}
