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

    public SoundMonitor(File directory){
        fileName = directory.getAbsolutePath() + "/tempaudiorec.3gp";
    }

    public double getSound(){
        start();
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
        }
    }

    private void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            File file = new File(fileName);
            boolean deleted = file.delete();
            Log.i("Sound Monitor", "file is deleted: " + deleted);
        }
    }

    private void restart(){
        stop();
        start();
    }
}
