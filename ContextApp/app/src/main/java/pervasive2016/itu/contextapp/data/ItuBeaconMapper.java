package pervasive2016.itu.contextapp.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

import pervasive2016.itu.contextapp.data.entity.BeaconEntity;

/**
 * Created by Ivan on 30-Mar-16.
 * Based on Diems data - to clean up the beacon entity
 */
public class ItuBeaconMapper {

    public static void SetToKnownItuLocation(BeaconEntity be) {
        majorMatch(be);
    }

    private static void majorMatch(BeaconEntity be) {
        switch ( be.getMajor() ){
            case "2"://Floor 2
                switch (be.getMinor().substring(0, 1)){
                    case "1"://Region A
                        minorMatchAtriumFloor2(be);
                        break;
                    case "2"://Region B
                        be.setLongitude(12.590945);
                        be.setLatitute(55.660060);
                        break;
                    case "3"://Region C
                        be.setLongitude(12.591527);
                        be.setLatitute(55.660027);
                        break;
                    case "4"://Region D
                        be.setLongitude(12.591272);
                        be.setLatitute(55.659222);
                        break;
                    case "5"://Region E
                        be.setLongitude(12.590691);
                        be.setLatitute(55.659327);
                        break;
                    default:
                        break;
                }
            case "3"://Floor 3
                switch (be.getMinor().substring(0, 1)){
                    case "1":
                        minorMatchAtriumFloor3(be);
                        break;
                    case "2"://Region B
                        be.setLongitude(12.590945);
                        be.setLatitute(55.660060);
                        break;
                    case "3"://Region C
                        be.setLongitude(12.591527);
                        be.setLatitute(55.660027);
                        break;
                    case "4"://Region D
                        be.setLongitude(12.591272);
                        be.setLatitute(55.659222);
                        break;
                    case "5"://Region E
                        be.setLongitude(12.590691);
                        be.setLatitute(55.659327);
                        break;
                }

                break;

            case "4"://Floor 4
                switch (be.getMinor().substring(0, 1)){
                    case "1"://Region A
                        minorMatchAtriumFloor4(be);
                        break;
                    case "2"://Region B
                        be.setLongitude(12.590945);
                        be.setLatitute(55.660060);
                        break;
                    case "3"://Region C
                        be.setLongitude(12.591527);
                        be.setLatitute(55.660027);
                        break;
                    case "4"://Region D
                        be.setLongitude(12.591272);
                        be.setLatitute(55.659222);
                        break;
                    case "5"://Region E
                        be.setLongitude(12.590691);
                        be.setLatitute(55.659327);
                        break;
                    default: break;

                }
                break;

            case "5"://Floor 5
                switch (be.getMinor().substring(0, 1)){
                    case "1"://Region A
                        minorMatchAtriumFloor5(be);
                        break;
                    case "2"://Region B
                        be.setLongitude(12.590945);
                        be.setLatitute(55.660060);
                        break;
                    case "3"://Region C
                        be.setLongitude(12.591527);
                        be.setLatitute(55.660027);
                        break;
                    case "4"://Region D
                        be.setLongitude(12.591272);
                        be.setLatitute(55.659222);
                        break;
                    case "5"://Region E
                        be.setLongitude(12.590691);
                        be.setLatitute(55.659327);
                        break;
                    default: break;
                }
                break;
            default:
                //Longitude12.591299);
                //Latitude55.659626); THIS MAKES THE BEACON MAPPING ONLY EVER WORK @ ITU
                // DEFAULT should be not to set values at all
                break;
        }
    }

    private static void minorMatchAtriumFloor2(BeaconEntity be) {
        switch (be.getMinor().substring(1, 3)){
            case "01"://room 01
                be.setLongitude(12.591081);
                be.setLatitute(55.659802);
                break;
            case "03":
                be.setLongitude(12.590949);
                be.setLatitute(55.659599);
                break;
            case "05":
                be.setLongitude(12.591214);
                be.setLatitute(55.659519);
                break;
            case "07":
                be.setLongitude(12.591300);
                be.setLatitute(55.659736);
                break;
            case "08":
                be.setLongitude(12.590843);
                be.setLatitute(55.659918);
                break;
            case "12":
                be.setLongitude(12.590870);
                be.setLatitute(55.659842);
                break;
            case "14":
                be.setLongitude(12.590803);
                be.setLatitute(55.659691);
                break;
            case "18":
                be.setLongitude(12.590746);
                be.setLatitute(55.659625);
                break;
            case "20":
                be.setLongitude(12.590714);
                be.setLatitute(55.659555);
                break;
            case "28":
                be.setLongitude(12.590689);
                be.setLatitute(55.659503);
                break;
            case "30":
                be.setLongitude(12.590675);
                be.setLatitute(55.659456);
                break;
            case "40":
                be.setLongitude(12.591381);
                be.setLatitute(55.659405);
                break;
            case "42":
                be.setLongitude(12.591377);
                be.setLatitute(55.659372);
                break;
            case "50":
                be.setLongitude(12.591425);
                be.setLatitute(55.659499);
                break;
            case "52":
                be.setLongitude(12.591416);
                be.setLatitute(55.659586);
                break;
            case "54":
                be.setLongitude(12.591462);
                be.setLatitute(55.659674);
                break;
            case "56":
                be.setLongitude(12.591469);
                be.setLatitute(55.659782);
                break;
            case "58":
                be.setLongitude(12.591542);
                be.setLatitute(55.659855);
                break;
            case "60":
                be.setLongitude(12.591565);
                be.setLatitute(55.659884);
                break;
            default:
                break;
        }
    }

    private static void minorMatchAtriumFloor3(BeaconEntity be) {
        switch (be.getMinor().substring(1,3)){
            case "01"://room 01
                be.setLongitude(12.590969);
                be.setLatitute(55.659733);
                break;
            case "03":
                be.setLongitude(12.590916);
                be.setLatitute(55.659548);
                break;
            case "05":
                be.setLongitude(12.591189);
                be.setLatitute(55.659478);
                break;
            case "07":
                be.setLongitude(12.591193);
                be.setLatitute(55.659568);
                break;
            case "08":
                be.setLongitude(12.590843);
                be.setLatitute(55.659918);
                break;
            case "12":
                be.setLongitude(12.590870);
                be.setLatitute(55.659842);
                break;
            case "14":
                be.setLongitude(12.590803);
                be.setLatitute(55.659691);
                break;
            case "18":
                be.setLongitude(12.590746);
                be.setLatitute(55.659625);
                break;
            case "20":
                be.setLongitude(12.590714);
                be.setLatitute(55.659555);
                break;
            case "28":
                be.setLongitude(12.590689);
                be.setLatitute(55.659503);
                break;
            case "50":
                be.setLongitude(12.591425);
                be.setLatitute(55.659499);
                break;
            case "52":
                be.setLongitude(12.591416);
                be.setLatitute(55.659586);
                break;
            case "54":
                be.setLongitude(12.591462);
                be.setLatitute(55.659674);
                break;
            case "58":
                be.setLongitude(12.591542);
                be.setLatitute(55.659855);
                break;
            case "60":
                be.setLongitude(12.591565);
                be.setLatitute(55.659884);
                break;
        }
    }

    private static void minorMatchAtriumFloor4(BeaconEntity be) {
        switch (be.getMinor().substring(1, 3)){
            case "01"://room 01
                be.setLongitude(12.591018);
                be.setLatitute(55.659808);
                break;
            case "03":
                be.setLongitude(12.590990);
                be.setLatitute(55.659741);
                break;
            case "05":
                be.setLongitude(12.590949);
                be.setLatitute(55.659597);
                break;
            case "07":
                be.setLongitude(12.591193);
                be.setLatitute(55.659568);
                break;
            case "09":
                be.setLongitude(12.591249);
                be.setLatitute(55.659681);
                break;
            case "08":
                be.setLongitude(12.590807);
                be.setLatitute(55.659948);
                break;
            case "14":
                be.setLongitude(12.590827);
                be.setLatitute(55.659852);
                break;
            case "16":
                be.setLongitude(12.590774);
                be.setLatitute(55.659684);
                break;
            case "20":
                be.setLongitude(12.590723);
                be.setLatitute(55.659640);
                break;
            case "30":
                be.setLongitude(12.590666);
                be.setLatitute(55.659503);
                break;
            case "32":
                be.setLongitude(12.590678);
                be.setLatitute(55.659476);
                break;
            case "34":
                be.setLongitude(12.590663);
                be.setLatitute(55.659448);
                break;
            case "40":
                be.setLongitude(12.591392);
                be.setLatitute(55.659403);
                break;
            case "42":
                be.setLongitude(12.591373);
                be.setLatitute(55.659366);
                break;
            case "54":
                be.setLongitude(12.591379);
                be.setLatitute(55.659486);
                break;
            case "56":
                be.setLongitude(12.591415);
                be.setLatitute(55.659580);
                break;
            case "58":
                be.setLongitude(12.591439);
                be.setLatitute(55.659667);
                break;
            case "60":
                be.setLongitude(12.591447);
                be.setLatitute(55.659749);
                break;
            case "62":
                be.setLongitude(12.591543);
                be.setLatitute(55.659844);
                break;
            case "64":
                be.setLongitude(12.591541);
                be.setLatitute(55.659879);
                break;
            default:
                break;
        }
    }

    private static void minorMatchAtriumFloor5(BeaconEntity be) {
        switch (be.getMinor().substring(1, 3)){
            case "01"://room 01
                be.setLongitude(12.591014);
                be.setLatitute(55.659859);
                break;
            case "03":
                be.setLongitude(12.590946);
                be.setLatitute(55.659658);
                break;
            case "05":
                be.setLongitude(12.591176);
                be.setLatitute(55.659471);
                break;
            case "07":
                be.setLongitude(12.591266);
                be.setLatitute(55.659715);
                break;
            case "09":
                be.setLongitude(12.591266);
                be.setLatitute(55.659783);
                break;
            case "08":
                be.setLongitude(12.590835);
                be.setLatitute(55.659948);
                break;
            case "10":
                be.setLongitude(12.590809);
                be.setLatitute(55.659908);
                break;
            case "14":
                be.setLongitude(12.590845);
                be.setLatitute(55.659852);
                break;
            case "16":
                be.setLongitude(12.590793);
                be.setLatitute(55.659694);
                break;
            case "20":
                be.setLongitude(12.590755);
                be.setLatitute(55.659643);
                break;
            case "22":
                be.setLongitude(12.590706);
                be.setLatitute(55.659551);
                break;
            case "30":
                be.setLongitude(12.590693);
                be.setLatitute(55.659502);
                break;
            case "32":
                be.setLongitude(12.590679);
                be.setLatitute(55.659478);
                break;
            case "34":
                be.setLongitude(12.590671);
                be.setLatitute(55.659450);
                break;
            case "40":
                be.setLongitude(12.591385);
                be.setLatitute(55.659372);
                break;
            case "42":
                be.setLongitude(12.591387);
                be.setLatitute(55.659403);
                break;
            case "54":
                be.setLongitude(12.591400);
                be.setLatitute(55.659486);
                break;
            case "56":
                be.setLongitude(12.591396);
                be.setLatitute(55.659574);
                break;
            case "58":
                be.setLongitude(12.591426);
                be.setLatitute(55.659666);
                break;
            case "60":
                be.setLongitude(12.591467);
                be.setLatitute(55.659756);
                break;
            case "62":
                be.setLongitude(12.591517);
                be.setLatitute(55.659855);
                break;
            case "64":
                be.setLongitude(12.591541);
                be.setLatitute(55.659877);
                break;
            default: break;
        }
    }

}
