package android.spvct.itu.dk.beacontest;

/**
 * Created by DIEM NGUYEN HOANG on 3/3/2016.
 */
public class ContextEntity {
    public int mID;
    public String mType;
    public String mTimestamp;
    public String mValue;
    public String mSensor;

    public void setID(int id){
        mID = id;
    }

    public void setType(String type){
        mType = type;
    }

    public void setTimestamp(String timestamp){
        mTimestamp = timestamp;
    }

    public void setValue(String value){
        mValue = value;
    }


    public int gettID(){
        return mID;
    }

    public String getType(){
        return mType;
    }

    public String getTimestamp(){
        return mTimestamp;
    }

    public String getValue(){
        return mValue;
    }


}
