package anywayanyday.pointsonmap;


import android.widget.ImageView;


public class DataRequest {

    public static final int MAP_TO_IMAGE_VIEW = 1;
    public static final int GEO_DATA = 2;
    private String url;
    private ImageView imageView;
    private String dotName;
    private String dotAddress;
    private int requestType;

    public DataRequest( ImageView imageView,  String url){
        this.imageView = imageView;
        this.url = url;
        requestType = MAP_TO_IMAGE_VIEW;
    }

    public DataRequest ( String dotAddress, String dotName){
        this.dotAddress = dotAddress;
        this.dotName = dotName;
        requestType = GEO_DATA;
    }

    public String getUrl() {
        return url;
    }

    public int getRequestType() {
        return requestType;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public String getDotName() {
        return dotName;
    }

    public String getDotAddress() {
        return dotAddress;
    }
}
