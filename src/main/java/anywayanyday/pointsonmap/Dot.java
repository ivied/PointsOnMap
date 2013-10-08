package anywayanyday.pointsonmap;

import java.io.Serializable;
import static anywayanyday.pointsonmap.FragmentAddDots.*;
/**
 * Created by Serv on 08.10.13.
 */
public class Dot implements Serializable{

    public static final String DOT_TYPE = "pm2bll";

    final String address;
    final int id;
    final String name;
    final String geoLocation;

    public Dot(int id, String name, String geoLoc, String address) {
        this.id = id;
        this.name = name;
        this.geoLocation = geoLoc;
        this.address = address;
    }

    String getYaMapUrl(){
       return YANDEX_MAP + getYaDotPostfix();
    }

    String getYaDotPostfix() {
        return geoLocation.replace(" ", ",") + "," + DOT_TYPE;
    }
}
