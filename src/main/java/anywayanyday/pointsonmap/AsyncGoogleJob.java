package anywayanyday.pointsonmap;



import android.app.Fragment;
import android.app.FragmentTransaction;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AsyncGoogleJob extends AsyncDataDownload {

    public static final int MAX_GEOCODE_RESULTS = 1;
    private DataRequest request;

    @Override
    public void dataDownload(DataRequest request, DownloaderListener downloaderListener) {
        this.downloaderListener = downloaderListener;
        geocoder = new Geocoder(downloaderListener.getContext());
        this.request = request;

        if (!isNetworkOnline()) {
            downloaderListener.sendToast(downloaderListener.getContext().getResources().getString(R.id.toast_offline));
            return;
        }

        switch (request.getRequestType()) {
            case DataRequest.GEO_DATA:
                //downloadPoint(request);
                try {
                    LatLng latLng = getGeoData(request.getDotAddress());
                    String geoData = latLng.latitude + " " + latLng.longitude;
                    downloaderListener.onDownloaderResponse(request, geoData);
                } catch (IOException e) {
                    downloaderListener.sendToast("GeoCoder Error");
                    e.printStackTrace();
                }
                break;
            case DataRequest.MAP_TO_IMAGE_VIEW:
                // new DownloadImage(request.getFrameWithMap()).execute(request.getDots());
                showMap(request);
                break;

        }

    }

    private LatLng getGeoData(String requestAddress) throws IOException {
        List<Address> addresses = geocoder.getFromLocationName(requestAddress, MAX_GEOCODE_RESULTS);
        Address address = addresses.get(0);
        return new LatLng(address.getLongitude(),address.getLatitude());
    }

    private static final String MAP_FRAGMENT_TAG = "map";
    private GoogleMap mMap;
    private MapFragmentWithCreatedListener mMapFragment;
    private Geocoder geocoder;

    private void showMap(DataRequest request) {

        mMapFragment = new MapFragmentWithCreatedListener();


        FragmentTransaction fragmentTransaction =
                ((Fragment) downloaderListener).getFragmentManager().beginTransaction();
        fragmentTransaction.add(request.getFrameWithMap().getId(), mMapFragment, MAP_FRAGMENT_TAG);
        fragmentTransaction.commit();


    }


    public void onGoogleMapCreation() {

        mMap = mMapFragment.getMap();
        if (mMap != null) {
        ArrayList<Dot> dots = request.getDots();
        for(Dot dot : dots){
            try {
                mMap.addMarker(new MarkerOptions()
                        .position(getGeoData(dot.address)));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        }
    }
    class MapFragmentWithCreatedListener extends MapFragment {


        MapFragmentWithCreatedListener (){
            newInstance(/*new GoogleMapOptions()*/);

        }



        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            onGoogleMapCreation();


        }
    }
}
