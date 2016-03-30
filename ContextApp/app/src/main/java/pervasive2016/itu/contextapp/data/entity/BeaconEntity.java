package pervasive2016.itu.contextapp.data.entity;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by DIEM NGUYEN HOANG on 3/17/2016.
 */
public class BeaconEntity {

    @SerializedName("uid")
    private String UUID;

    @SerializedName("major")
    private String Major;

    @SerializedName("minor")
    private String Minor;

    @SerializedName("lat")
    private Double Latitude;

    @SerializedName("lng")
    private Double Longitude;

    @SerializedName("updated")
    private Date updated;


    private Double distance;
    private int Rssi;

    public BeaconEntity(){
    }

    public void setUUID(String s){
        UUID = s;
    }

    public void setMajor(String s){

        Major = s;
    }

    public void setMinor(String s){
        Minor = s;
    }
    public void setLatitute(Double s){
        Latitude = s;
    }

    public void setLongitude(Double s){
        Longitude = s;
    }


    public void setDistance(Double s){
        distance = s;
    }

    public void setRssi(int s){
        Rssi = s;
    }



    public String getUUID(){
        return UUID;
    }

    public String getMajor(){
        return Major;
    }

    public String getMinor(){
        return Minor;
    }

    public Double getLatitude(){
        return Latitude;
    }

    public Double getLongtitude(){
        return Longitude;
    }


    public Double getDistance(){
        return distance;
    }

    public int getRssi(){ return Rssi;}

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getKey() {
        return getUUID()+"_"+getMajor()+"_"+getMinor();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof BeaconEntity) {
        return this.getKey().equals( ((BeaconEntity)o).getKey() );
        }
        return false;
    }

    private String minorToRoom(String minor) {
        if(minor != null && minor.length() > 3)
            switch (minor.substring(0, 1)) {
                case "1":
                    return "A" + minor.substring(1, 3);
                case "2":
                    return "B" + minor.substring(1, 3);
                case "3":
                    return "C" + minor.substring(1, 3);
                case "4":
                    return "D" + minor.substring(1, 3);
                case "5":
                    return "E" + minor.substring(1, 3);
                default:
                    return minor;
            }
        else return minor;
    }

    public String getName() {
        return getMajor()+minorToRoom( getMinor() );
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }
}
