package anywayanyday.pointsonmap;


import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class DataRequest {

    public static final int MAP_TO_IMAGE_VIEW = 1;
    public static final int GEO_DATA = 2;
    private ArrayList<Dot> dots;
    private String dotName;
    private String dotAddress;
    private int requestType;

    public DataRequest( @NotNull ArrayList<Dot> dots){
        this.dots = dots;
        requestType = MAP_TO_IMAGE_VIEW;
    }

    public DataRequest (@NotNull  String dotAddress,@NotNull  String dotName){
        this.dotAddress = dotAddress;
        this.dotName = dotName;
        requestType = GEO_DATA;
    }

    public ArrayList<Dot> getDots() {
        return dots;
    }

    public int getRequestType() {
        return requestType;
    }


    public String getDotName() {
        return dotName;
    }

    public String getDotAddress() {
        return dotAddress;
    }
}
