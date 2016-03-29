package pervasive2016.itu.contextapp.sensors.background;

import android.util.Log;

import com.google.gson.Gson;

import java.net.MalformedURLException;

import pervasive2016.itu.contextapp.data.UserLocation;
import pervasive2016.itu.contextapp.data.entity.ContextEntity;
import pervasive2016.itu.contextapp.sensors.SoundMonitor;
import pervasive2016.itu.contextapp.web.ApiAdapter;

/**
 * Created by martinosecchi on 28/03/16.
 */
public class SoundRunnable implements Runnable {
    private SoundMonitor soundMonitor;
    private int sleepTime;
    public SoundRunnable( SoundMonitor soundMonitor){
        this.soundMonitor = soundMonitor;
        this.sleepTime = 10000; // 10 secs
    }
    public SoundRunnable( SoundMonitor soundMonitor, int sleepTime){
        this.soundMonitor = soundMonitor;
        this.sleepTime = sleepTime;
    }
    @Override
    public void run() {
        double sum = 0.0, sample;
        final int count = 10;
        int i = 0;
        while (soundMonitor.isGoing()) {
            if (UserLocation.getLongitude() != 0 && UserLocation.getLatitude() != 0) {
                Log.i("ContextService - LOC", UserLocation.getLatitude() + ", " + UserLocation.getLongitude());
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
                        UserLocation.getLatitude(),
                        UserLocation.getLongitude(),
                        "sound",
                        "" + value
                );
                Gson gson = new Gson();
                String body = gson.toJson(c);
                Log.i("Monitor Body", body);
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
}
