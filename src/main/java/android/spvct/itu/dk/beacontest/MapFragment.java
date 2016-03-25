package android.spvct.itu.dk.beacontest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by DIEM NGUYEN HOANG on 3/18/2016.
 */
public class MapFragment extends SupportMapFragment {
    public static final String TAG = "mapfragmenttag";

    OnMapReadyCallback callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        callback = MapGoogleHandle.getInstance( getActivity() );
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMapAsync(callback);
    }
}
