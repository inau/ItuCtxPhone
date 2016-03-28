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

        Longitude = s;
    }

    public void setLongitude(Double s){

        Latitude = s;
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

    /**
     * Maps to known ITU beacons following the pattern that 'major' = floor and 'minor' = Letter+room
     * @param e
     */
    public void setToKnownItuBeaconLocation(BeaconEntity e){
        UUID = e.getUUID();
        Major = e.getMajor();
        Minor = e.getMinor();
        distance = e.getDistance();
        Rssi = e.getRssi();
        switch (Major){
            case "2"://Floor 2
                switch (Minor.substring(0,1)){
                    case "1"://Region A
                        switch (Minor.substring(1,3)){
                            case "01"://room 01
                                Longitude = 12.591081;
                                Latitude = 55.659802;
                                break;
                            case "03":
                                Longitude = 12.590949;
                                Latitude = 55.659599;
                                break;
                            case "05":
                                Longitude = 12.591214;
                                Latitude = 55.659519;
                                break;
                            case "07":
                                Longitude = 12.591300;
                                Latitude = 55.659736;
                                break;
                            case "08":
                                Longitude = 12.590843;
                                Latitude = 55.659918;
                                break;
                            case "12":
                                Longitude = 12.590870;
                                Latitude = 55.659842;
                                break;
                            case "14":
                                Longitude = 12.590803;
                                Latitude = 55.659691;
                                break;
                            case "18":
                                Longitude = 12.590746;
                                Latitude = 55.659625;
                                break;
                            case "20":
                                Longitude = 12.590714;
                                Latitude = 55.659555;
                                break;
                            case "28":
                                Longitude = 12.590689;
                                Latitude = 55.659503;
                                break;
                            case "30":
                                Longitude = 12.590675;
                                Latitude = 55.659456;
                                break;
                            case "40":
                                Longitude = 12.591381;
                                Latitude = 55.659405;
                                break;
                            case "42":
                                Longitude = 12.591377;
                                Latitude = 55.659372;
                                break;
                            case "50":
                                Longitude = 12.591425;
                                Latitude = 55.659499;
                                break;
                            case "52":
                                Longitude = 12.591416;
                                Latitude = 55.659586;
                                break;
                            case "54":
                                Longitude = 12.591462;
                                Latitude = 55.659674;
                                break;
                            case "56":
                                Longitude = 12.591469;
                                Latitude = 55.659782;
                                break;
                            case "58":
                                Longitude = 12.591542;
                                Latitude = 55.659855;
                                break;
                            case "60":
                                Longitude = 12.591565;
                                Latitude = 55.659884;
                                break;
                        }
                        break;
                    case "2"://Region B
                        Longitude = 12.590945;
                        Latitude = 55.660060;
                        break;
                    case "3"://Region C
                        Longitude = 12.591527;
                        Latitude = 55.660027;
                        break;
                    case "4"://Region D
                        Longitude = 12.591272;
                        Latitude = 55.659222;
                        break;
                    case "5"://Region E
                        Longitude = 12.590691;
                        Latitude = 55.659327;
                        break;
                }
                break;



            case "3"://Floor 3
                switch (Minor.substring(0,1)){
                    case "1"://Region A
                        switch (Minor.substring(1,3)){
                            case "01"://room 01
                                Longitude = 12.590969;
                                Latitude = 55.659733;
                                break;
                            case "03":
                                Longitude = 12.590916;
                                Latitude = 55.659548;
                                break;
                            case "05":
                                Longitude = 12.591189;
                                Latitude = 55.659478;
                                break;
                            case "07":
                                Longitude = 12.591193;
                                Latitude = 55.659568;
                                break;
                            case "08":
                                Longitude = 12.590843;
                                Latitude = 55.659918;
                                break;
                            case "12":
                                Longitude = 12.590870;
                                Latitude = 55.659842;
                                break;
                            case "14":
                                Longitude = 12.590803;
                                Latitude = 55.659691;
                                break;
                            case "18":
                                Longitude = 12.590746;
                                Latitude = 55.659625;
                                break;
                            case "20":
                                Longitude = 12.590714;
                                Latitude = 55.659555;
                                break;
                            case "28":
                                Longitude = 12.590689;
                                Latitude = 55.659503;
                                break;
                            case "50":
                                Longitude = 12.591425;
                                Latitude = 55.659499;
                                break;
                            case "52":
                                Longitude = 12.591416;
                                Latitude = 55.659586;
                                break;
                            case "54":
                                Longitude = 12.591462;
                                Latitude = 55.659674;
                                break;
                            case "58":
                                Longitude = 12.591542;
                                Latitude = 55.659855;
                                break;
                            case "60":
                                Longitude = 12.591565;
                                Latitude = 55.659884;
                                break;

                        }
                        break;
                    case "2"://Region B
                        Longitude = 12.590945;
                        Latitude = 55.660060;
                        break;
                    case "3"://Region C
                        Longitude = 12.591527;
                        Latitude = 55.660027;
                        break;
                    case "4"://Region D
                        Longitude = 12.591272;
                        Latitude = 55.659222;
                        break;
                    case "5"://Region E
                        Longitude = 12.590691;
                        Latitude = 55.659327;
                        break;
                }

                break;

            case "4"://Floor 4
                switch (Minor.substring(0,1)){
                    case "1"://Region A
                        switch (Minor.substring(1,3)){
                            case "01"://room 01
                                Longitude = 12.591018;
                                Latitude = 55.659808;
                                break;
                            case "03":
                                Longitude = 12.590990;
                                Latitude = 55.659741;
                                break;
                            case "05":
                                Longitude = 12.590949;
                                Latitude = 55.659597;
                                break;
                            case "07":
                                Longitude = 12.591193;
                                Latitude = 55.659568;
                                break;
                            case "09":
                                Longitude = 12.591249;
                                Latitude = 55.659681;
                                break;
                            case "08":
                                Longitude = 12.590807;
                                Latitude = 55.659948;
                                break;
                            case "14":
                                Longitude = 12.590827;
                                Latitude = 55.659852;
                                break;
                            case "16":
                                Longitude = 12.590774;
                                Latitude = 55.659684;
                                break;
                            case "20":
                                Longitude = 12.590723;
                                Latitude = 55.659640;
                                break;
                            case "30":
                                Longitude = 12.590666;
                                Latitude = 55.659503;
                                break;
                            case "32":
                                Longitude = 12.590678;
                                Latitude = 55.659476;
                                break;
                            case "34":
                                Longitude = 12.590663;
                                Latitude = 55.659448;
                                break;
                            case "40":
                                Longitude = 12.591392;
                                Latitude = 55.659403;
                                break;
                            case "42":
                                Longitude = 12.591373;
                                Latitude = 55.659366;
                                break;
                            case "54":
                                Longitude = 12.591379;
                                Latitude = 55.659486;
                                break;
                            case "56":
                                Longitude = 12.591415;
                                Latitude = 55.659580;
                                break;
                            case "58":
                                Longitude = 12.591439;
                                Latitude = 55.659667;
                                break;
                            case "60":
                                Longitude = 12.591447;
                                Latitude = 55.659749;
                                break;
                            case "62":
                                Longitude = 12.591543;
                                Latitude = 55.659844;
                                break;
                            case "64":
                                Longitude = 12.591541;
                                Latitude = 55.659879;
                                break;
                            default:
                                break;
                        }
                        break;
                    case "2"://Region B
                        Longitude = 12.590945;
                        Latitude = 55.660060;
                        break;
                    case "3"://Region C
                        Longitude = 12.591527;
                        Latitude = 55.660027;
                        break;
                    case "4"://Region D
                        Longitude = 12.591272;
                        Latitude = 55.659222;
                        break;
                    case "5"://Region E
                        Longitude = 12.590691;
                        Latitude = 55.659327;
                        break;
                    default: break;

                }
                break;

            case "5"://Floor 5
                switch (Minor.substring(0,1)){
                    case "1"://Region A
                        switch (Minor.substring(1,3)){
                            case "01"://room 01
                                Longitude = 12.591014;
                                Latitude = 55.659859;
                                break;
                            case "03":
                                Longitude = 12.590946;
                                Latitude = 55.659658;
                                break;
                            case "05":
                                Longitude = 12.591176;
                                Latitude = 55.659471;
                                break;
                            case "07":
                                Longitude = 12.591266;
                                Latitude = 55.659715;
                                break;
                            case "09":
                                Longitude = 12.591266;
                                Latitude = 55.659783;
                                break;
                            case "08":
                                Longitude = 12.590835;
                                Latitude = 55.659948;
                                break;
                            case "10":
                                Longitude = 12.590809;
                                Latitude = 55.659908;
                                break;
                            case "14":
                                Longitude = 12.590845;
                                Latitude = 55.659852;
                                break;
                            case "16":
                                Longitude = 12.590793;
                                Latitude = 55.659694;
                                break;
                            case "20":
                                Longitude = 12.590755;
                                Latitude = 55.659643;
                                break;
                            case "22":
                                Longitude = 12.590706;
                                Latitude = 55.659551;
                                break;
                            case "30":
                                Longitude = 12.590693;
                                Latitude = 55.659502;
                                break;
                            case "32":
                                Longitude = 12.590679;
                                Latitude = 55.659478;
                                break;
                            case "34":
                                Longitude = 12.590671;
                                Latitude = 55.659450;
                                break;
                            case "40":
                                Longitude = 12.591385;
                                Latitude = 55.659372;
                                break;
                            case "42":
                                Longitude = 12.591387;
                                Latitude = 55.659403;
                                break;
                            case "54":
                                Longitude = 12.591400;
                                Latitude = 55.659486;
                                break;
                            case "56":
                                Longitude = 12.591396;
                                Latitude = 55.659574;
                                break;
                            case "58":
                                Longitude = 12.591426;
                                Latitude = 55.659666;
                                break;
                            case "60":
                                Longitude = 12.591467;
                                Latitude = 55.659756;
                                break;
                            case "62":
                                Longitude = 12.591517;
                                Latitude = 55.659855;
                                break;
                            case "64":
                                Longitude = 12.591541;
                                Latitude = 55.659877;
                                break;
                            default: break;
                        }
                        break;
                    case "2"://Region B
                        Longitude = 12.590945;
                        Latitude = 55.660060;
                        break;
                    case "3"://Region C
                        Longitude = 12.591527;
                        Latitude = 55.660027;
                        break;
                    case "4"://Region D
                        Longitude = 12.591272;
                        Latitude = 55.659222;
                        break;
                    case "5"://Region E
                        Longitude = 12.590691;
                        Latitude = 55.659327;
                        break;
                    default: break;
                }
                break;
            default:
                //Longitude = 12.591299;
                //Latitude = 55.659626; THIS MAKES THE BEACON MAPPING ONLY EVER WORK @ ITU
                // DEFAULT should be not to set values at all
                break;
        }
    }

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

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }
}
