package anywayanyday.pointsonmap;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Dot implements Serializable{

    public static final String DOT_TYPE = "pm2pnl";

    final String address;
    final int id;
    final String name;
    final String geoLocation;

    public Dot(int id,@NotNull  String name,@NotNull  String geoLocation,@NotNull  String address) {
        this.id = id;
        this.name = name;
        this.geoLocation = geoLocation;
        this.address = address;
    }

    String getYaMapUrl(){
       return AsyncYaJob.YANDEX_MAP + getYaDotPostfix();
    }

    String getYaDotPostfix() {
        return geoLocation.replace(" ", ",") + "," + DOT_TYPE;
    }
}
