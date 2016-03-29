package pervasive2016.itu.contextapp.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import pervasive2016.itu.contextapp.LocationChangeActivity;
import pervasive2016.itu.contextapp.MainActivity;
import pervasive2016.itu.contextapp.R;
import pervasive2016.itu.contextapp.data.entity.BeaconEntity;

/**
 * Created by DIEM NGUYEN HOANG on 3/17/2016.
 */
public class ListViewAdapter extends ArrayAdapter<BeaconEntity> {

    public ListViewAdapter(Context context, Set<BeaconEntity> listThings) {
        this(context, listThings.toArray(new BeaconEntity[listThings.size()]));
    }
    public ListViewAdapter(Context context, List<BeaconEntity> listThings) {
        super(context, 0, listThings);
    }
    public ListViewAdapter(Context context, BeaconEntity[] items) {
        super(context, 0, items);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final BeaconEntity bc = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem, parent, false);
        }
        TextView mUUID = (TextView)convertView.findViewById(R.id.mBeaconUDID);
        TextView mMajor = (TextView)convertView.findViewById(R.id.mBeaconMajor);
        TextView mMinor = (TextView)convertView.findViewById(R.id.mBeaconMinor);
        TextView mDistance = (TextView)convertView.findViewById(R.id.mBeaconDistance);
        TextView mRSSI = (TextView)convertView.findViewById(R.id.mBeaconRSSI);

        convertView.findViewById(R.id.mLwAdd).setOnClickListener(new View.OnClickListener() {
            final Context mContext = parent.getContext();
            @Override
            public void onClick(View v) {
                Intent i = new Intent(((MainActivity)mContext), LocationChangeActivity.class);
                i.putExtra(MainActivity.keys[0], bc.getUUID());
                i.putExtra(MainActivity.keys[1], bc.getMajor());
                i.putExtra(MainActivity.keys[2], bc.getMinor());
                i.putExtra("dist", bc.getDistance());
                i.putExtra("pos", position);

                ((MainActivity)mContext).startActivityForResult(i, MainActivity.NEW_BEACON_ACTIVITY);
            }
        });

        mUUID.setText(bc.getUUID());
        mMajor.setText(bc.getMajor());
        mMinor.setText(bc.getMinor());
        mDistance.setText(String.format("%.2f", bc.getDistance()));
        mRSSI.setText(Integer.toString(bc.getRssi()));
        return convertView;
    }

}
