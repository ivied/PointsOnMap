package anywayanyday.pointsonmap;



import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AsyncGoogleJob extends AsyncDataDownload implements MapFragmentWithCreatedListener.MapCreatedListener {

    public static final int MAX_GEOCODE_RESULTS = 1;
    private DataRequest request;

    @Override
    public void dataDownload(DataRequest request, DownloaderListener downloaderListener) {
        this.downloaderListener = downloaderListener;
        this.request = request;

        if (!isNetworkOnline()) {
            downloaderListener.sendToast(downloaderListener.getContext().getResources().getString(R.id.toast_offline));
            return;
        }

        switch (request.getRequestType()) {
            case DataRequest.GEO_DATA:
                //downloadPoint(request);
                try {
                    LatLng latLng = getGeoData(request.getDotAddress(), downloaderListener.getContext());
                    String geoData = latLng.latitude + " " + latLng.longitude;
                    downloaderListener.onDownloaderResponse(request, geoData);
                } catch (IOException e) {
                    downloaderListener.sendToast("GeoCoder Error");
                    e.printStackTrace();
                }
                break;
            case DataRequest.MAP_TO_IMAGE_VIEW:
                showMap(request);
                break;

        }

    }

    public LatLng getGeoData(String requestAddress, Context context) throws IOException {
        if (context == null) throw new IOException();
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = geocoder.getFromLocationName(requestAddress, MAX_GEOCODE_RESULTS);
        Address address = addresses.get(0);
        return new LatLng(address.getLongitude(),address.getLatitude());
    }

    private static final String MAP_FRAGMENT_TAG = "map";
    private MapFragmentWithCreatedListener mMapFragment;

    private void showMap(DataRequest request) {
        RelativeLayout relativeLayout = request.getFrameWithMap();
        mMapFragment = new MapFragmentWithCreatedListener(this);


        FragmentTransaction fragmentTransaction =
                ((Fragment) downloaderListener).getFragmentManager().beginTransaction();
        fragmentTransaction.add(relativeLayout.getId(), mMapFragment, MAP_FRAGMENT_TAG);
        fragmentTransaction.commit();


    }



    public void onGoogleMapCreation() {

        GoogleMap mMap = mMapFragment.getMap();
        if (mMap != null) {
        ArrayList<Dot> dots = request.getDots();
        for(Dot dot : dots){
            try {
            LatLng geoLoc = getGeoData(dot.address, downloaderListener.getContext());
                mMap.addMarker(new MarkerOptions()
                        .position(geoLoc)
                        .title(dot.name + geoLoc.toString()));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        }
    }

}
