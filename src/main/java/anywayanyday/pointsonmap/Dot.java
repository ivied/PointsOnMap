package anywayanyday.pointsonmap;

import java.io.Serializable;
import static anywayanyday.pointsonmap.FragmentAddDots.*;
/**
 * Created by Serv on 08.10.13.
 */
public class Dot implements Serializable{

    public static final String DOT_TYPE = "pm2bll";

    int id;
    String name;
    String address;

    public Dot(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    String getYaMapUrl(){
       return YANDEX_MAP + getYaDotPostfix();
    }

    String getYaDotPostfix() {
        return address.replace(" ", ",") + "," + DOT_TYPE;
    }
}
