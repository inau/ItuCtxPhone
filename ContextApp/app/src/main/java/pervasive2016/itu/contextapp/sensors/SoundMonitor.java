package pervasive2016.itu.contextapp.sensors;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by martinosecchi on 17/03/16.
 */
public class SoundMonitor {

    private MediaRecorder mRecorder = null;
    private String fileName;
    private volatile boolean going;

    public SoundMonitor(File directory){
        fileName = directory.getAbsolutePath() + "/tempaudiorec.3gp";
        going = true;
    }

    public double getSound(){
        start();
        while (mRecorder == null)
            restart();

        mRecorder.getMaxAmplitude(); //first call on the recorder returns 0
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        double sample = mRecorder.getMaxAmplitude(); //second call returns max in the period since last call
        stop();
        return sample;
    }

    private void start(){
        try {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(fileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.prepare();
            mRecorder.start();
        }catch (Exception exn){
            Log.i("Sound Monitor" ,"failed to prepare audio recorder");
            mRecorder = null;
        }
    }

    private void stop() {
        if (mRecorder != null) {
            try {
                mRecorder.stop();
                mRecorder.release();
            } catch (RuntimeException e){
             Log.i("Sound Monitor", "stop failed");
            }
            mRecorder = null;
        }
        File file = new File(fileName);
        boolean deleted = file.delete();
        Log.i("Sound Monitor", "file is deleted: " + deleted);
    }

    private void restart(){
        stop();
        start();
    }

    public boolean isGoing() {
        return going;
    }

    public void setGoing(boolean going) {
        this.going = going;
    }
}
