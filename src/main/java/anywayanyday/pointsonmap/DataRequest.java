package anywayanyday.pointsonmap;


import android.widget.FrameLayout;

import java.util.ArrayList;


public class DataRequest {

    public static final int MAP_TO_IMAGE_VIEW = 1;
    public static final int GEO_DATA = 2;
    public static final int UPDATE_MAP = 3;
    private ArrayList<Dot> dots;
    private FrameLayout frameWithMap;
    private String dotName;
    private String dotAddress;
    private int requestType;

    public DataRequest( FrameLayout frameWithMap,  ArrayList<Dot> dots){
        this.frameWithMap = frameWithMap;
        this.dots = dots;
        requestType = MAP_TO_IMAGE_VIEW;
    }

    public DataRequest ( String dotAddress, String dotName){
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

    public FrameLayout getFrameWithMap() {
        return frameWithMap;
    }

    public String getDotName() {
        return dotName;
    }

    public String getDotAddress() {
        return dotAddress;
    }
}
