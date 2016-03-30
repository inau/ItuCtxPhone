package pervasive2016.itu.contextapp.presentation;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;

import pervasive2016.itu.contextapp.MainActivity;
import pervasive2016.itu.contextapp.R;
import pervasive2016.itu.contextapp.data.entity.BeaconEntity;
import pervasive2016.itu.contextapp.web.ApiAdapter;

/**
 * Created by Ivan on 29-Mar-16.
 */
public class ActionFragment extends DialogFragment {
    final public static String mark_popup = "markerpopupMenu";
    final public static String rlat_crd = "rlatcrd";
    final public static String rlng_crd = "rlngcrd";
    final public static String time = "timestamp";

    Double relocate_lat, relocate_lng;
    long times;
    String relocateText;

    public static ActionFragment newInstance(final Context c,final Double rlat, final Double rlng, final BeaconEntity b, final long times) {
        final ActionFragment dialogFrag = new ActionFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.keys[0], b.getUUID());
        args.putString(MainActivity.keys[1], b.getMajor());
        args.putString(MainActivity.keys[2], b.getMinor());
        args.putDouble(MainActivity.keys[3], b.getLatitude());
        args.putDouble(MainActivity.keys[4], b.getLongtitude());
        Log.i("CLICKAF", "B:" + b.getLatitude() + " " + b.getLongtitude() );
        if(rlat != null || rlng != null ) {
            args.putDouble(rlat_crd, rlat);
            args.putDouble(rlng_crd, rlng);
        }
        args.putLong(time, times);
        dialogFrag.setArguments(args);
        return dialogFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.popup_marker, container, false);

        relocate_lat = getArguments().getDouble(rlat_crd);
        relocate_lng = getArguments().getDouble(rlng_crd);
        times = getArguments().getLong(time);
        relocateText = null;
        if( relocate_lat != null || relocate_lng != null) {
            relocateText = "Relocate (" + relocate_lat + ", " + relocate_lng +")";
            Log.i("CLICKAF", relocateText);
        }
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window w = getDialog().getWindow();
        w.setBackgroundDrawableResource(android.R.color.transparent);
        //lp.copyFrom(w.getAttributes());
        //lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //w.setAttributes(lp);


        final String[] keys = MainActivity.keys;

        Dialog d = getDialog();

        ImageView getStats = (ImageView) d.findViewById(R.id.pop_stats);
        ImageView relocate = (ImageView) d.findViewById(R.id.pop_relocate);

        final Bundle ex = getArguments();
        final Context c = getActivity();
        getStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Double l = ex.getDouble(keys[3]);
                    Double ll = ex.getDouble(keys[4]);
                    Log.i("CLICKAF", "Request B " + l + " " + ll);

                    ApiAdapter
                            .getApihandlerCTX(((Observer) c), null, ApiAdapter.WebMethod.GET)
                            .execute(ApiAdapter
                                            .urlBuilderFilter(ApiAdapter.APIS.CONTEXTS,
                                                    l,
                                                    ll,
                                                    new Date(times)
                                            )
                            );
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                dismiss();
            }
        });

            relocate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (relocateText == null) {
                        dismiss();
                    } else {
                        Log.i("CLICKAF", "Reloc to " + relocate_lat + ", " +relocate_lng);
                        String body = "{" +
                                "\"" + keys[0] + "\":\"" + ex.get(keys[0]) + "\"," +
                                "\"" + keys[1] + "\":\"" + ex.get(keys[1]) + "\"," +
                                "\"" + keys[2] + "\":\"" + ex.get(keys[2]) + "\"," +
                                "\"" + keys[3] + "\":\"" + relocate_lat + "\"," +
                                "\"" + keys[4] + "\":\"" + relocate_lng + "\"" +
                                "}";
                        Log.i("CLICK", body);
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");

                        try {
                            new ApiAdapter<BeaconEntity>(ApiAdapter.WebMethod.POST,
                                    headers,//headers or null
                                    body,//body or null
                                    ((Observer) c),
                                    BeaconEntity.class
                            ).execute(ApiAdapter.urlBuilder(ApiAdapter.APIS.BEACONS, ""));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                    dismiss();
                }
            });


    }

}
