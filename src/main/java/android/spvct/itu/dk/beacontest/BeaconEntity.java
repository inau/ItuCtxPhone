package android.spvct.itu.dk.beacontest;
import com.google.gson.annotations.SerializedName;

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
    private Double distance;
    private int Rssi;

    public BeaconEntity(){
        Latitude = 12.0;
        Longitude = 55.0;
    }

    protected void setUUID(String s){
        UUID = s;
    }

    protected void setMajor(String s){

        Major = s;
    }

    protected void setMinor(String s){
        Minor = s;
    }
    protected void setLatitute(Double s){

        Latitude = s;
    }

    protected void setLongitude(Double s){

        Longitude = s;
    }


    protected void setDistance(Double s){
        distance = s;
    }

    protected void setRssi(int s){
        Rssi = s;
    }



    protected String getUUID(){
        return UUID;
    }

    protected String getMajor(){
        return Major;
    }

    protected String getMinor(){
        return Minor;
    }

    protected Double getLatitude(){
        return Latitude;
    }

    protected Double getLongtitude(){
        return Longitude;
    }


    protected Double getDistance(){
        return distance;
    }

    protected int getRssi(){ return Rssi;}

    protected void setBeaconValue(BeaconEntity e){
        UUID = e.getUUID();
        Major = e.getMajor();
        Minor = e.getMinor();
        distance = e.getDistance();
        Rssi = e.getRssi();
        switch (Major){
            case "3"://Floor 3
                switch (Minor.substring(0,1)){
                    case "1"://Region A
                        switch (Minor.substring(1,3)){
                            case "01"://room 01
                                Latitude = 12.590969;
                                Longitude = 55.659733;
                                break;
                            case "03":
                                Latitude = 12.590916;
                                Longitude = 55.659548;
                                break;
                            case "05":
                                Latitude = 12.591189;
                                Longitude = 55.659478;
                                break;
                            case "07":
                                Latitude = 12.591193;
                                Longitude = 55.659568;
                                break;
                            case "52":
                                Latitude = 12.591408;
                                Longitude = 55.659571;
                                break;
                            case "54":
                                Latitude = 12.591415;
                                Longitude = 55.659668;
                                break;
                        }
                        break;
                    case "2"://Region B
                        break;
                    case "3"://Region C
                        break;
                    case "4"://Region D
                        break;
                    case "5"://Region E
                        break;

                }


                break;

            case "4"://Floor 4
                switch (Minor.substring(0,1)){
                    case "1"://Region A
                        switch (Minor.substring(1,3)){
                            case "01"://room 01
                                Latitude = 12.591018;
                                Longitude = 55.659808;
                                break;
                            case "03":
                                Latitude = 12.590990;
                                Longitude = 55.659741;
                                break;
                            case "05":
                                Latitude = 12.590949;
                                Longitude = 55.659597;
                                break;
                            case "07":
                                Latitude = 12.591193;
                                Longitude = 55.659568;
                                break;
                            case "52":
                                Latitude = 12.591242;
                                Longitude = 55.659622;
                                break;
                            case "54":
                                Latitude = 12.591250;
                                Longitude = 55.659680;
                                break;
                        }
                        break;
                    case "2"://Region B
                        break;
                    case "3"://Region C
                        break;
                    case "4"://Region D
                        break;
                    case "5"://Region E
                        break;

                }
                break;

            case "5"://Floor 5
                switch (Minor.substring(0,1)){
                    case "1"://Region A
                        switch (Minor.substring(1,3)){
                            case "01"://room 01
                                Latitude = 12.591014;
                                Longitude = 55.659859;
                                break;
                            case "03":
                                Latitude = 12.590946;
                                Longitude = 55.659658;
                                break;
                            case "05":
                                Latitude = 12.591176;
                                Longitude = 55.659471;
                                break;
                            case "07":
                                Latitude = 12.591266;
                                Longitude = 55.659715;
                                break;
                            case "09":
                                Latitude = 12.591266;
                                Longitude = 55.659783;
                                break;
                            case "16":
                                Latitude = 12.590789;
                                Longitude = 55.659710;
                                break;
                            case "54":
                                Latitude = 12.591377;
                                Longitude = 55.659480;
                                break;
                            case "56":
                                Latitude = 12.591374;
                                Longitude = 55.659571;
                                break;
                            case "58":
                                Latitude = 12.591426;
                                Longitude = 55.659666;
                                break;
                        }
                        break;
                    case "2"://Region B
                        break;
                    case "3"://Region C
                        break;
                    case "4"://Region D
                        break;
                    case "5"://Region E
                        break;
                    default:
                        break;

                }
                break;
            default:
                Latitude = 12.591299;
                Longitude = 55.659626;
                break;
        }
    }


}
