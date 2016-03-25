package android.spvct.itu.dk.beacontest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by DIEM NGUYEN HOANG on 3/17/2016.
 */
public class ListViewAdapter extends ArrayAdapter<BeaconEntity> {
    public ListViewAdapter(Context context, List<BeaconEntity> listThings) {
        super(context, 0, listThings);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BeaconEntity bc = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem, parent, false);
        }
        TextView mUUID = (TextView)convertView.findViewById(R.id.mBeaconUDID);
        TextView mMajor = (TextView)convertView.findViewById(R.id.mBeaconMajor);
        TextView mMinor = (TextView)convertView.findViewById(R.id.mBeaconMinor);
        TextView mDistance = (TextView)convertView.findViewById(R.id.mBeaconDistance);
        TextView mRSSI = (TextView)convertView.findViewById(R.id.mBeaconRSSI);

        mUUID.setText(bc.getUUID());
        mMajor.setText(bc.getMajor());
        mMinor.setText(bc.getMinor());
        mDistance.setText(String.format("%.2f", bc.getDistance()));
        mRSSI.setText(Integer.toString(bc.getRssi()));
        return convertView;
    }

}
