package com.inau.gstur.gesturepresenter.Comms;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.inau.gstur.gesturepresenter.Gestures.GestureListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Ivan on 29-May-16.
 */
public class WebHelper extends AsyncTask<Void, Void, GestureInfo[]> {
    private final GestureHandler gestureHandler;
    String web_uri = "http://gesturews.appspot.com/g/";



    public WebHelper(GestureHandler gh) {
        this.gestureHandler = gh;
    }


    @Override
    protected GestureInfo[] doInBackground(Void... params) {
        GestureInfo[] responseData = null;

        try {
            URL url = new URL(web_uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.i("KKKKKKK", "URL = " + url);
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                // Do normal input or output stream reading
                InputStreamReader in = new InputStreamReader((InputStream) conn.getContent());

                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonElement root = parser.parse(in);
                if (root.isJsonArray()) {
                    JsonArray rootArr = root.getAsJsonArray();
                    responseData = new GestureInfo[rootArr.size()];
                    for (int i = 0; i < rootArr.size(); i++) {
                        String element = rootArr.get(i).getAsString();
                        String[] vals = element.split(",");
                        //responseData[i] = be;
                        GestureListener.Gesture gest;
                        int count;
                        try {
                            gest = GestureListener.Gesture.valueOf(vals[0].toUpperCase());
                            count = Integer.parseInt(vals[1]);
                        }
                        catch (Exception e) {
                            gest = GestureListener.Gesture.IDLE;
                            count = 1;
                        }
                        responseData[i] = new GestureInfo(gest, count);
                    }
                } else {
                    //response = "FAILED"; // See documentation for more info on response handling
                    return null;
                }
            }
        } catch (IOException e) {
            //TODO Handle problems..
        }
        return responseData;
    }

    @Override
    protected void onPostExecute(GestureInfo[] gestureInfos) {
        //super.onPostExecute(gestureInfos);
        gestureHandler.onGesturesReceived(gestureInfos);
    }
}
