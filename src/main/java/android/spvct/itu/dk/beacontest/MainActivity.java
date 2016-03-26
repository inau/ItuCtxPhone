package android.spvct.itu.dk.beacontest;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import org.altbeacon.beacon.Beacon;
import com.estimote.sdk.BeaconManager.MonitoringListener;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.GsonBuilder;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.JsonArray;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.JsonElement;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.JsonObject;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.JsonParser;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;


import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

/*
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
*/

public class MainActivity extends Activity implements BeaconConsumer, Observer {
    protected static final String TAG = "XXXXXX";
    private BeaconManager beaconManager;
    ListView mListView;
    ListViewAdapter mListViewAdapter;
    List<BeaconEntity> mListBeaconEntity = new ArrayList<BeaconEntity>();
    Double closetDistance = 100000.0;
    BeaconEntity closestBeacon = new BeaconEntity();
    SQLiteDatabase database = null;
    DatabaseHelper helper = null;
    BeaconEntity []mListBeaconFromServer;
    int storedOnServer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView)findViewById(R.id.listViewBeacon);
        mListViewAdapter = new ListViewAdapter(this.getBaseContext(),mListBeaconEntity);
        mListView.setAdapter(mListViewAdapter);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        //beaconManager.bind(this);

        helper = new DatabaseHelper(this);
        database = helper.getWritableDatabase();
        mListBeaconEntity.clear();

        findViewById(R.id.start_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startService(intent);
                //startActivity(intent);
                closestBeacon.setDistance(1000000.0);
                beaconManager.bind(MainActivity.this);
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                String data = String.format("geo:%s,%s", 55.697445, 12.580360);
//                intent.setData(Uri.parse(data));
//                startActivity(intent);
                Log.i("YYYYYY", "Service button presses");
            }
        });


        //service onDestroy callback method will be called
        findViewById(R.id.stop_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stopService(intent);
                //beaconManager.stopRangingBeaconsInRegion(new Region("E3B54450-AB73-4D79-85D6-519EAF0F45D9"));
                beaconManager.unbind(MainActivity.this);
                try{
                    beaconManager.stopRangingBeaconsInRegion(new Region("E3B54450-AB73-4D79-85D6-519EAF0F45D9", null, null, null));
                }catch (RemoteException e){

                }

            }
        });

        findViewById(R.id.beaconLocate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(BeaconEntity be :mListBeaconEntity){
                    if(closestBeacon.getDistance() > be.getDistance() &&
                            Integer.parseInt(be.getMajor()) <= 5 &&
                            Integer.parseInt(be.getMajor()) > 0){
                        closestBeacon.setBeaconValue(be);
                    }
                }
                Intent intent = new Intent(MainActivity.this, LocationChangeActivity.class);
                intent.putExtra("longitude", closestBeacon.getLongtitude());
                intent.putExtra("latitude", closestBeacon.getLatitude());
                intent.putExtra("major", closestBeacon.getMajor());
                intent.putExtra("minor", closestBeacon.getMinor());
                startActivity(intent);

                //------------------------------------------------

                try{

                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");

                    new ApiAdapter<BeaconEntity>(ApiAdapter.WebMethod.DELETE,//GET
                            null,//headers or null
                            null,//body or null
                            MainActivity.this,
                            BeaconEntity.class
                    ).execute(ApiAdapter.urlBuilder(ApiAdapter.APIS.BEACONS, "?key=e3b54450-ab73-4d79-85d6-519eaf0f45d9:5:1581"));//?key="2453543567:9:1534
                }catch(MalformedURLException e){
                    e.printStackTrace();
                }
            }
        });

        try {
            new ApiAdapter<BeaconEntity>(ApiAdapter.WebMethod.GET,
                    null, null,
                    MainActivity.this, BeaconEntity.class)
                    .execute(ApiAdapter.urlBuilder(ApiAdapter.APIS.BEACONS, ""));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.i("GGGGGGGGGG", "ONCreAte CALL");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
//        savedInstanceState.putSerializable("d", (Serializable) mListBeaconEntity);
//        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            mListBeaconEntity = (ArrayList<BeaconEntity>)savedInstanceState.getSerializable("d");
            mListView = (ListView)findViewById(R.id.listViewBeacon);
            mListViewAdapter = new ListViewAdapter(this.getBaseContext(),mListBeaconEntity);
            mListView.setAdapter(mListViewAdapter);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
        database.close();
        mListBeaconEntity.clear();
        Log.i("CCCCCCCCC", "Database close");
    }

    private void updateUI(){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListViewAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for (Beacon b : beacons) {
                    BeaconEntity bc = new BeaconEntity();
                    bc.setUUID(b.getId1().toString());
                    bc.setMajor(b.getId2().toString());
                    bc.setMinor(b.getId3().toString());
                    bc.setDistance(b.getDistance());
                    bc.setRssi(b.getRssi());
                    bc.setBeaconValue(bc);//set position of beacon base on Major and Minor


                    if (closestBeacon.getDistance() > bc.getDistance() &&
                            Integer.parseInt(bc.getMajor()) <= 5) {
                        //closestBeacon.setBeaconValue(bc);
                        Log.i("CCCCCCCCCC", "Closest beacon distance = " + closetDistance);
                    }
                    Log.i(TAG, "Beacon information is " + b.getId1() + " " +
                                    b.getId2() + " " + b.getId3() + " distance = " + b.getDistance()
                    );

                    int count = 0;
                    for (BeaconEntity be : mListBeaconEntity) {
                        if (be.getUUID().equals(bc.getUUID()) &&
                                be.getMajor().equals(bc.getMajor()) &&
                                be.getMinor().equals(bc.getMinor())) {//update list
                            be.setDistance(bc.getDistance());
                            be.setRssi(bc.getRssi());
                            count = 1;
                        }
                    }

                    if (count == 0) {//&& Integer.parseInt(bc.getMajor()) <= 5 && Integer.parseInt(bc.getMajor()) > 0
                        mListBeaconEntity.add(bc);//Example of adding Beacon
                        // Save BEACON ON CLOUD

                        storedOnServer = 0;
                        for(BeaconEntity it: mListBeaconEntity){
                            if(it.getUUID().equals(bc.getUUID()) &&
                                    it.getMajor().equals(bc.getMajor()) &&
                                    it.getMinor().equals(bc.getMinor())){
                                storedOnServer = 1;
                            }
                        }


                        String mKey = bc.getUUID() + bc.getMajor() + bc.getMinor();
                        String query = "SELECT * from " + DatabaseHelper.getTableName() +
                                " WHERE _key = " + "'" + mKey + "'";

                        Log.i("XXXXXXXX", "Query = " + query);

                        Cursor mCursor = database.rawQuery(query, null);


                        Log.i("XXXXXXXX", "cursor = " + mCursor.getCount());
                        if(mCursor.getCount() <= 0 && storedOnServer == 0){//not stored on both database and server
                            //STORE BEACON TO CLOUD

                            try{
                                String body =   "{" +
                                        "\"lat\":\""+bc.getLatitude().toString()+"\"," +
                                        "\"lng\":\""+bc.getLongtitude().toString()+"\"," +
                                        "\"uid\":\""+bc.getUUID()+"\"," +
                                        "\"major\":\""+bc.getMajor()+"\"," +
                                        "\"minor\":\""+bc.getMinor()+ "\"" +
                                        "}";

                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type","application/json");


                                new ApiAdapter<BeaconEntity>(ApiAdapter.WebMethod.POST,
                                        headers,//headers or null
                                        body,//body or null
                                        MainActivity.this,
                                        BeaconEntity.class
                                ).execute(ApiAdapter.urlBuilder(ApiAdapter.APIS.BEACONS, ""));



                                //WILL BE REMOVED !!!! ATENTION

                                /*
                                new ApiAdapter<BeaconEntity>(ApiAdapter.WebMethod.DELETE,
                                        null,//headers or null
                                        null,//body or null
                                        MainActivity.this,
                                        BeaconEntity.class
                                ).execute(ApiAdapter.urlBuilder(ApiAdapter.APIS.BEACONS, "?key=" +
                                        bc.getUUID() +":" + bc.getMajor() + ":" +bc.getMinor()));//?key="2453543567:9:1534

                                        */
                                Log.i("OOOOOOOOOOOO", "key = " +  "?key=" +
                                        bc.getUUID() +":" + bc.getMajor() + ":" +bc.getMinor());


                                ContentValues values = new ContentValues();
                                values.put("_uid", bc.getUUID());
                                values.put("_major", bc.getMajor());
                                values.put("_minor", bc.getMinor());
                                values.put("_lat", bc.getLatitude());
                                values.put("_lng", bc.getLongtitude());
                                values.put("_key", bc.getUUID() + bc.getMajor() + bc.getMinor());
                                database.insert(DatabaseHelper.getTableName(), null, values);
                                Log.i("JJJJJJJJJJJJJ", " value added: lat = " + bc.getLatitude() + " lng = " + bc.getLongtitude());
                                mCursor.close();


                            } catch (MalformedURLException e){
                                e.printStackTrace();
                            }catch (IOException e){
                                e.printStackTrace();
                            }

                        }

                    }

                }
                Log.i(TAG, " -----------------END LOOP --------------");

                updateUI();
                //mListViewAdapter.notifyDataSetChanged();
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            // Ranging ID = "E3B54450-AB73-4D79-85D6-519EAF0F45D9"
            //beaconManager.setForegroundScanPeriod(800);
            //beaconManager.setBackgroundScanPeriod(1000);
        } catch (RemoteException e) {    }

    }

    @Override
    public void update(Observable observable, Object data) {
        if(data instanceof BeaconEntity[]) {
            mListBeaconFromServer = (BeaconEntity[]) data;
            for (BeaconEntity be : mListBeaconFromServer) {
                Log.i("GGGGGGGGG FOUND ", be.getUUID() +"-" + be.getMajor() + "-"  + be.getMinor()+"-"
                        + be.getLatitude() + "-" + be.getLongtitude());
            }
        }
        /*else if(data instanceof ContextEntity[]) {
            ContextEntity[] result = (ContextEntity[]) data;
            for (ContextEntity ce : result) {
                //Log.i("FOUND", ""+ce.uid);
            }

        }*/
    }


}



//-----------------------------------------------------------------------------------------
