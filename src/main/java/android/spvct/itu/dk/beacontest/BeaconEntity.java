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
            case "2"://Floor 2
                switch (Minor.substring(0,1)){
                    case "1"://Region A
                        switch (Minor.substring(1,3)){
                            case "01"://room 01
                                Latitude = 12.591081;
                                Longitude = 55.659802;
                                break;
                            case "03":
                                Latitude = 12.590949;
                                Longitude = 55.659599;
                                break;
                            case "05":
                                Latitude = 12.591214;
                                Longitude = 55.659519;
                                break;
                            case "07":
                                Latitude = 12.591300;
                                Longitude = 55.659736;
                                break;
                            case "08":
                                Latitude = 12.590843;
                                Longitude = 55.659918;
                                break;
                            case "12":
                                Latitude = 12.590870;
                                Longitude = 55.659842;
                                break;
                            case "14":
                                Latitude = 12.590803;
                                Longitude = 55.659691;
                                break;
                            case "18":
                                Latitude = 12.590746;
                                Longitude = 55.659625;
                                break;
                            case "20":
                                Latitude = 12.590714;
                                Longitude = 55.659555;
                                break;
                            case "28":
                                Latitude = 12.590689;
                                Longitude = 55.659503;
                                break;
                            case "30":
                                Latitude = 12.590675;
                                Longitude = 55.659456;
                                break;
                            case "40":
                                Latitude = 12.591381;
                                Longitude = 55.659405;
                                break;
                            case "42":
                                Latitude = 12.591377;
                                Longitude = 55.659372;
                                break;
                            case "50":
                                Latitude = 12.591425;
                                Longitude = 55.659499;
                                break;
                            case "52":
                                Latitude = 12.591416;
                                Longitude = 55.659586;
                                break;
                            case "54":
                                Latitude = 12.591462;
                                Longitude = 55.659674;
                                break;
                            case "56":
                                Latitude = 12.591469;
                                Longitude = 55.659782;
                                break;
                            case "58":
                                Latitude = 12.591542;
                                Longitude = 55.659855;
                                break;
                            case "60":
                                Latitude = 12.591565;
                                Longitude = 55.659884;
                                break;
                        }
                        break;
                    case "2"://Region B
                        Latitude = 12.590945;
                        Longitude = 55.660060;
                        break;
                    case "3"://Region C
                        Latitude = 12.591527;
                        Longitude = 55.660027;
                        break;
                    case "4"://Region D
                        Latitude = 12.591272;
                        Longitude = 55.659222;
                        break;
                    case "5"://Region E
                        Latitude = 12.590691;
                        Longitude = 55.659327;
                        break;
                }
                break;



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
                            case "08":
                                Latitude = 12.590843;
                                Longitude = 55.659918;
                                break;
                            case "12":
                                Latitude = 12.590870;
                                Longitude = 55.659842;
                                break;
                            case "14":
                                Latitude = 12.590803;
                                Longitude = 55.659691;
                                break;
                            case "18":
                                Latitude = 12.590746;
                                Longitude = 55.659625;
                                break;
                            case "20":
                                Latitude = 12.590714;
                                Longitude = 55.659555;
                                break;
                            case "28":
                                Latitude = 12.590689;
                                Longitude = 55.659503;
                                break;
                            case "50":
                                Latitude = 12.591425;
                                Longitude = 55.659499;
                                break;
                            case "52":
                                Latitude = 12.591416;
                                Longitude = 55.659586;
                                break;
                            case "54":
                                Latitude = 12.591462;
                                Longitude = 55.659674;
                                break;
                            case "58":
                                Latitude = 12.591542;
                                Longitude = 55.659855;
                                break;
                            case "60":
                                Latitude = 12.591565;
                                Longitude = 55.659884;
                                break;

                        }
                        break;
                    case "2"://Region B
                        Latitude = 12.590945;
                        Longitude = 55.660060;
                        break;
                    case "3"://Region C
                        Latitude = 12.591527;
                        Longitude = 55.660027;
                        break;
                    case "4"://Region D
                        Latitude = 12.591272;
                        Longitude = 55.659222;
                        break;
                    case "5"://Region E
                        Latitude = 12.590691;
                        Longitude = 55.659327;
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
                            case "09":
                                Latitude = 12.591249;
                                Longitude = 55.659681;
                                break;
                            case "08":
                                Latitude = 12.590807;
                                Longitude = 55.659948;
                                break;
                            case "14":
                                Latitude = 12.590827;
                                Longitude = 55.659852;
                                break;
                            case "16":
                                Latitude = 12.590774;
                                Longitude = 55.659684;
                                break;
                            case "20":
                                Latitude = 12.590723;
                                Longitude = 55.659640;
                                break;
                            case "30":
                                Latitude = 12.590666;
                                Longitude = 55.659503;
                                break;
                            case "32":
                                Latitude = 12.590678;
                                Longitude = 55.659476;
                                break;
                            case "34":
                                Latitude = 12.590663;
                                Longitude = 55.659448;
                                break;
                            case "40":
                                Latitude = 12.591392;
                                Longitude = 55.659403;
                                break;
                            case "42":
                                Latitude = 12.591373;
                                Longitude = 55.659366;
                                break;
                            case "54":
                                Latitude = 12.591379;
                                Longitude = 55.659486;
                                break;
                            case "56":
                                Latitude = 12.591415;
                                Longitude = 55.659580;
                                break;
                            case "58":
                                Latitude = 12.591439;
                                Longitude = 55.659667;
                                break;
                            case "60":
                                Latitude = 12.591447;
                                Longitude = 55.659749;
                                break;
                            case "62":
                                Latitude = 12.591543;
                                Longitude = 55.659844;
                                break;
                            case "64":
                                Latitude = 12.591541;
                                Longitude = 55.659879;
                                break;
                        }
                        break;
                    case "2"://Region B
                        Latitude = 12.590945;
                        Longitude = 55.660060;
                        break;
                    case "3"://Region C
                        Latitude = 12.591527;
                        Longitude = 55.660027;
                        break;
                    case "4"://Region D
                        Latitude = 12.591272;
                        Longitude = 55.659222;
                        break;
                    case "5"://Region E
                        Latitude = 12.590691;
                        Longitude = 55.659327;
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
                            case "08":
                                Latitude = 12.590835;
                                Longitude = 55.659948;
                                break;
                            case "10":
                                Latitude = 12.590809;
                                Longitude = 55.659908;
                                break;
                            case "14":
                                Latitude = 12.590845;
                                Longitude = 55.659852;
                                break;
                            case "16":
                                Latitude = 12.590793;
                                Longitude = 55.659694;
                                break;
                            case "20":
                                Latitude = 12.590755;
                                Longitude = 55.659643;
                                break;
                            case "22":
                                Latitude = 12.590706;
                                Longitude = 55.659551;
                                break;
                            case "30":
                                Latitude = 12.590693;
                                Longitude = 55.659502;
                                break;
                            case "32":
                                Latitude = 12.590679;
                                Longitude = 55.659478;
                                break;
                            case "34":
                                Latitude = 12.590671;
                                Longitude = 55.659450;
                                break;
                            case "40":
                                Latitude = 12.591385;
                                Longitude = 55.659372;
                                break;
                            case "42":
                                Latitude = 12.591387;
                                Longitude = 55.659403;
                                break;
                            case "54":
                                Latitude = 12.591400;
                                Longitude = 55.659486;
                                break;
                            case "56":
                                Latitude = 12.591396;
                                Longitude = 55.659574;
                                break;
                            case "58":
                                Latitude = 12.591426;
                                Longitude = 55.659666;
                                break;
                            case "60":
                                Latitude = 12.591467;
                                Longitude = 55.659756;
                                break;
                            case "62":
                                Latitude = 12.591517;
                                Longitude = 55.659855;
                                break;
                            case "64":
                                Latitude = 12.591541;
                                Longitude = 55.659877;
                                break;
                        }
                        break;
                    case "2"://Region B
                        Latitude = 12.590945;
                        Longitude = 55.660060;
                        break;
                    case "3"://Region C
                        Latitude = 12.591527;
                        Longitude = 55.660027;
                        break;
                    case "4"://Region D
                        Latitude = 12.591272;
                        Longitude = 55.659222;
                        break;
                    case "5"://Region E
                        Latitude = 12.590691;
                        Longitude = 55.659327;
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
