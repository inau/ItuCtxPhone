package com.inau.ctxph.bluetoothapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by Ivan on 20-Apr-16.
 */
public class MainActivity extends Activity {
    String TAG = "BTa";

    TextView[] gyro, acce;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket btSocket;
    private InputStream inStream;

    Handler handler = new Handler();
    byte delimiter = 10;
    boolean stopWorker = false;
    int readBufferPosition = 0;
    byte[] readBuffer = new byte[1024];
    private static String address = "20:16:01:20:34:38";
    private static final UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        gyro = new TextView[]{
                (TextView) findViewById(R.id.gyro_x),
                (TextView) findViewById(R.id.gyro_y),
                (TextView) findViewById(R.id.gyro_z)
        };
        acce = new TextView[]{
                (TextView) findViewById(R.id.acce_x),
                (TextView) findViewById(R.id.acce_y),
                (TextView) findViewById(R.id.acce_z)
        };

        if( CheckBt() ) Connect();
        else {
            try {
                wait(3000);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void Connect() {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        Log.d(TAG, "Connecting to ... " + device);
        mBluetoothAdapter.cancelDiscovery();
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            btSocket.connect();
            Log.d(TAG, "Connection made.");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                Log.d(TAG, "Unable to end the connection");
            }
            Log.d(TAG, "Socket creation failed");
        }

        beginListenForData();
        /* this is a method used to read what the Arduino says for example when you write Serial.print("Hello world.") in your Arduino code */
    }


    public void beginListenForData()   {
        try {
            inStream = btSocket.getInputStream();
        } catch (IOException e) {
        }

        Thread workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = inStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            inStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "UTF-8");
                                    readBufferPosition = 0;
                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            try {
                                                JSONObject jo = new JSONObject(data);
                                                JSONArray vals = jo.getJSONArray("values");
                                                for(int i = 0; i < vals.length(); i++) {
                                                    if(i > 2 ) acce[i%3].setText(""+vals.getDouble(i));
                                                    else gyro[i%3].setText(""+vals.getDouble(i));
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    private boolean CheckBt() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "Bluetooth Disabled !",
                    Toast.LENGTH_SHORT).show();
                   /* It tests if the bluetooth is enabled or not, if not the app will show a message. */
            return false;
        }

        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),
                    "Bluetooth null !", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;
    }
}
