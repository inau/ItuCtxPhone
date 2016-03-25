package itu.msec.sensorserviceapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        UserLocation.setLatitude(1234);
        UserLocation.setLongitude(4242);

        LinearLayout ll = new LinearLayout(this);
        ServiceButton servBtn = new ServiceButton(this);
        ll.addView(servBtn,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        StartTestActivityButton actBtn = new StartTestActivityButton(this);
        ll.addView(actBtn,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        setContentView(ll);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        stopService(new Intent(this, ContextService.class));
    }

    class ServiceButton extends Button {
        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                startService(new Intent( getContext(), ContextService.class));
            }
        };

        public ServiceButton(Context ctx) {
            super(ctx);
            setText("Start service test");
            setOnClickListener(clicker);
        }
    }

    class StartTestActivityButton extends Button {
        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent( getContext(), AudioRecActivity.class));
            }
        };

        public StartTestActivityButton(Context ctx) {
            super(ctx);
            setText("Start audio recording test activity");
            setOnClickListener(clicker);
        }
    }
}
