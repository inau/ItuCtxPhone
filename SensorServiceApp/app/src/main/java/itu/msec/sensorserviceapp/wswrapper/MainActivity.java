//package itu.msec.sensorserviceapp.wswrapper;
//
//import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//
//import java.net.MalformedURLException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Observable;
//import java.util.Observer;
//
//import itu.msec.sensorserviceapp.R;
//import itu.msec.sensorserviceapp.wswrapper.data.entity.BeaconEntity;
//import itu.msec.sensorserviceapp.wswrapper.data.entity.ContextEntity;
//import itu.msec.sensorserviceapp.wswrapper.web.ApiAdapter;
//
//public class MainActivity extends Activity implements Observer {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    new ApiAdapter<BeaconEntity>(ApiAdapter.WebMethod.GET,
//                            null, null,
//                            MainActivity.this, BeaconEntity.class)
//                            .execute(ApiAdapter.urlBuilder(ApiAdapter.APIS.BEACONS, ""));
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//
//        final EditText _LAT = (EditText) findViewById(R.id.lat);
//        final EditText _LNG = (EditText) findViewById(R.id.lng);
//
//        final EditText _TYPE = (EditText) findViewById(R.id.type);
//        final EditText _VAL = (EditText) findViewById(R.id.values);
//        findViewById(R.id.postCtx).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                try {
//                    String body =   "{" +
//                                        "\"lat\":\""+_LAT.getText()+"\"," +
//                                        "\"lng\":\""+_LNG.getText()+"\"," +
//                                        "\"type\":\""+_TYPE.getText()+"\"," +
//                                        "\"values\":\""+_VAL.getText()+"\"" +
//                                    "}";
//                    Map<String, String> headers = new HashMap<String, String>();
//                    headers.put("Content-Type","application/json");
//
//                    new ApiAdapter<ContextEntity>(  ApiAdapter.WebMethod.POST,
//                                                    headers,
//                                                    body,
//                                                    MainActivity.this,
//                                                    ContextEntity.class).execute(
//                                                        ApiAdapter.urlBuilder(ApiAdapter.APIS.CONTEXTS, "")
//                                                    );
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        final EditText _UID = (EditText) findViewById(R.id.uid);
//        final EditText _MAJ = (EditText) findViewById(R.id.major);
//        final EditText _MIN = (EditText) findViewById(R.id.minor);
//        findViewById(R.id.postBeac).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                try {
//                    String body =   "{" +
//                                        "\"lat\":\""+_LAT.getText()+"\"," +
//                                        "\"lng\":\""+_LNG.getText()+"\"," +
//                                        "\"uid\":\""+_UID.getText()+"\"," +
//                                        "\"major\":\""+_MAJ.getText()+"\"," +
//                                        "\"minor\":\""+_MIN.getText()+"\"" +
//                                    "}";
//                    Map<String, String> headers = new HashMap<String, String>();
//                    headers.put("Content-Type","application/json");
//
//                    new ApiAdapter<BeaconEntity>(   ApiAdapter.WebMethod.POST,
//                                                    headers,
//                                                    body,
//                                                    MainActivity.this,
//                                                    BeaconEntity.class
//                    ).execute( ApiAdapter.urlBuilder(ApiAdapter.APIS.BEACONS, "") );
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                    Log.i("ERR", "malformed url");
//                }
//            }
//        });
//
//    }
//
//    @Override
//    public void update(Observable observable, Object data) {
//        if(data instanceof BeaconEntity[]) {
//            BeaconEntity[] result = (BeaconEntity[]) data;
//            for (BeaconEntity be : result) {
//                Log.i("FOUND", be.key);
//            }
//        }
//        else if(data instanceof ContextEntity[]) {
//            ContextEntity[] result = (ContextEntity[]) data;
//            for (ContextEntity ce : result) {
//                Log.i("FOUND", ""+ce.uid);
//            }
//
//        }
//    }
//}
