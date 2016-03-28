package pervasive2016.itu.contextapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService( new Intent(this, ContextService.class));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        stopService( new Intent(this, ContextService.class));
    }
}
