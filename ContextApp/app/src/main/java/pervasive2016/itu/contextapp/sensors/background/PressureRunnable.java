package pervasive2016.itu.contextapp.sensors.background;

import android.util.Log;

import com.google.gson.Gson;

import java.net.MalformedURLException;

import pervasive2016.itu.contextapp.data.UserLocation;
import pervasive2016.itu.contextapp.data.entity.ContextEntity;
import pervasive2016.itu.contextapp.sensors.PressureMonitor;
import pervasive2016.itu.contextapp.web.ApiAdapter;

/**
 * Created by martinosecchi on 28/03/16.
 */
public class PressureRunnable implements Runnable {
    private PressureMonitor pressMonitor;
    private int sleepTime;
    public PressureRunnable( PressureMonitor pressureMonitor){
        this.pressMonitor = pressureMonitor;
        this.sleepTime = 10000; // 10 seconds
    }
    public PressureRunnable( PressureMonitor pressureMonitor, int sleepTime){
        this.pressMonitor = pressureMonitor;
        this.sleepTime = sleepTime;
    }
    @Override
    public void run() {
        while (pressMonitor.getSensor() != null){
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
}
