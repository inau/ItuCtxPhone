package pervasive2016.itu.contextapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import pervasive2016.itu.contextapp.data.DatabaseHelper;
import pervasive2016.itu.contextapp.data.ListViewAdapter;
import pervasive2016.itu.contextapp.data.entity.BeaconEntity;
import pervasive2016.itu.contextapp.web.ApiAdapter;

public class MainActivity extends Activity implements BeaconConsumer, Observer {
    protected static final String TAG = "MAIN";
    private BeaconManager beaconManager;
    ListView mListView;
    ListViewAdapter mListViewAdapter;
    List<BeaconEntity> mListBeaconEntity = new ArrayList<BeaconEntity>();
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

        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        //beaconManager.bind(this);

        helper = new DatabaseHelper(this);
        database = helper.getWritableDatabase();
        mListBeaconEntity.clear();

        findViewById(R.id.start_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closestBeacon.setDistance(1000000.0);
                beaconManager.bind(MainActivity.this);
                Log.i(TAG, "Service button presses");
            }
        });


        //service onDestroy callback method will be called
        findViewById(R.id.stop_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    if(Integer.parseInt(be.getMajor()) <= 5 &&
                            Integer.parseInt(be.getMajor()) > 0 &&
                            closestBeacon.getDistance() > be.getDistance()){
                        closestBeacon.setBeaconValue(be);
                    }
                }
                Intent intent = new Intent(MainActivity.this, LocationChangeActivity.class);
                intent.putExtra("longitude", closestBeacon.getLongtitude());
                intent.putExtra("latitude", closestBeacon.getLatitude());
                intent.putExtra("major", closestBeacon.getMajor());
                intent.putExtra("minor", closestBeacon.getMinor());
                startActivity(intent);
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

        Log.i(TAG, "ONCreAte CALL");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
        database.close();
        mListBeaconEntity.clear();
        Log.i(TAG, "Database close");
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
                        mListBeaconEntity.add(bc);//Add beacon on the display list
                        // Save BEACON ON CLOUD

                        //Find if beacon stored on server
                        storedOnServer = 0;
                        for(BeaconEntity it: mListBeaconFromServer){
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
