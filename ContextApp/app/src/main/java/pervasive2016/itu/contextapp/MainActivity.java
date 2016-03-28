package pervasive2016.itu.contextapp;

import android.app.Activity;
import android.content.Intent;
import android.os.RemoteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ToggleButton;

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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;

import pervasive2016.itu.contextapp.data.ListViewAdapter;
import pervasive2016.itu.contextapp.data.UserLocation;
import pervasive2016.itu.contextapp.data.entity.BeaconEntity;
import pervasive2016.itu.contextapp.web.ApiAdapter;

public class MainActivity extends Activity implements BeaconConsumer, Observer {
    public static final int NEW_BEACON_ACTIVITY = 1314123;
    public static final String TAG = "MAIN";
    public static final String[] keys = {"uid","major","minor","lat", "lng"};
    private BeaconManager beaconManager;

    ListView mListView;
    ListViewAdapter mListViewAdapter;

    BeaconEntity closestBeacon = new BeaconEntity();

    BeaconEntity[] mListBeaconFromServer;
    List<BeaconEntity> newBeacons = new ArrayList<>();

    Map<String, Double> distances = new HashMap<>();

    int storedOnServer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestBeaconsFromWeb();

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));

        findViewById(R.id.toggleScan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToggleButton btn = (ToggleButton) v;
                if (btn.isChecked()) {
                    Log.i(TAG, "ON");
                    closestBeacon.setDistance(1000000.0);
                    beaconManager.bind(MainActivity.this);
                } else {
                    Log.i(TAG, "OFF");
                    beaconManager.unbind(MainActivity.this);
                    try {
                        beaconManager.stopRangingBeaconsInRegion(new Region("E3B54450-AB73-4D79-85D6-519EAF0F45D9", null, null, null));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //service onDestroy callback method will be called
        findViewById(R.id.beaconLocate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (BeaconEntity be : mListBeaconFromServer) {
                    String bkey = be.getKey();
                    String skey = closestBeacon.getKey();
                    be.setDistance(distances.get(bkey));
                    be.setDistance(be.getDistance() == null ? Integer.MAX_VALUE : be.getDistance());
                    closestBeacon.setDistance(distances.get(skey));
                    closestBeacon.setDistance(closestBeacon.getDistance() == null ?
                            Integer.MAX_VALUE : closestBeacon.getDistance());
                    if (Integer.parseInt(be.getMajor()) <= 5 &&
                            Integer.parseInt(be.getMajor()) > 0 &&
                            closestBeacon.getDistance() > be.getDistance()) {
                        closestBeacon.setToKnownItuBeaconLocation(be);
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

        Log.i(TAG, "ONCreAte CALL");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
        Log.i(TAG, "Database close");
    }

    private void requestBeaconsFromWeb(){
                try {
                    ApiAdapter.getApihandlerBCS(MainActivity.this, null, ApiAdapter.WebMethod.GET)
                            .execute(ApiAdapter.urlBuilder(ApiAdapter.APIS.BEACONS, ""));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
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
                    bc.setToKnownItuBeaconLocation(bc);//set position of beacon base on Major and Minor


                    Log.i(TAG, "Beacon information is " + b.getId1() + " " +
                                    b.getId2() + " " + b.getId3() + " distance = " + b.getDistance()
                    );

                    //Beacon without location (non-itu most likely)
                    if (bc.getLatitude() == null || bc.getLongtitude() == null) {
                        boolean exists = false;

                        //iterate through existing beacons from ws
                        for (BeaconEntity be : mListBeaconFromServer) {
                            if (be.getKey().equals(bc.getKey())) { //update list
                                distances.put(be.getKey(), bc.getDistance());

                                //update local values
                                be.setDistance(bc.getDistance());
                                be.setRssi(bc.getRssi());
                                exists = true;
                                break;
                            }
                        }

                        if (!exists) {
                            //START ACTIVITY FOR RESULT
                            if( !newBeacons.contains(bc) ) {
                                newBeacons.add(bc);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mListViewAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    }

                    Log.i(TAG, " -----------------END LOOP --------------");
                    requestBeaconsFromWeb();
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == NEW_BEACON_ACTIVITY ) {
            if(resultCode != RESULT_OK) return;
            Bundle ex = data.getExtras();
            newBeacons.remove( ex.getInt("pos") );
            try{
                Log.i("RESULT", "Received activity result");
                String body =   "{" +
                        "\""+keys[0]+"\":\""+ ex.get(keys[0]) +"\"," +
                        "\""+keys[1]+"\":\""+ ex.get(keys[1])+"\"," +
                        "\""+keys[2]+"\":\""+ ex.get(keys[2])+"\"," +
                        "\""+keys[3]+"\":\""+ ex.get(keys[3])+"\"," +
                        "\""+keys[4]+"\":\""+ ex.get(keys[4])+"\"" +
                        "}";

                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");

                new ApiAdapter<BeaconEntity>(ApiAdapter.WebMethod.POST,
                        headers,//headers or null
                        body,//body or null
                        MainActivity.this,
                        BeaconEntity.class
                ).execute(ApiAdapter.urlBuilder(ApiAdapter.APIS.BEACONS, ""));

            } catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if(data instanceof BeaconEntity[]) {
            mListBeaconFromServer = (BeaconEntity[]) data;
            if(mListViewAdapter == null) {
                //only display new beacons
                mListView = (ListView)findViewById(R.id.listViewBeacon);
                mListViewAdapter = new ListViewAdapter(this.getBaseContext(), newBeacons);
                mListView.setAdapter(mListViewAdapter);
            }
            mListViewAdapter.notifyDataSetChanged();

            for (BeaconEntity be : mListBeaconFromServer) {
                Log.i("GGGGGGGGG FOUND ", be.getUUID() +"-" + be.getMajor() + "-"  + be.getMinor()+"-"
                        + be.getLatitude() + "-" + be.getLongtitude());
            }
        }
    }


}



//-----------------------------------------------------------------------------------------
