package anywayanyday.pointsonmap;

import java.io.Serializable;
import static anywayanyday.pointsonmap.FragmentAddDots.*;

public class Dot implements Serializable{

    public static final String DOT_TYPE = "pm2pnl";

    final String address;
    final int id;
    final String name;
    final String geoLocation;

    public Dot(int id, String name, String geoLocation, String address) {
        this.id = id;
        this.name = name;
        this.geoLocation = geoLocation;
        this.address = address;
    }

    String getYaMapUrl(){
       return YANDEX_MAP + getYaDotPostfix();
    }

    String getYaDotPostfix() {
        return geoLocation.replace(" ", ",") + "," + DOT_TYPE;
    }
}
