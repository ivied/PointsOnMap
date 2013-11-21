package anywayanyday.pointsonmap;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;


class MapFragmentWithCreatedListener extends MapFragment {

    private MapCreatedListener listener;

    public interface MapCreatedListener {
        void onGoogleMapCreation();
    }

    MapFragmentWithCreatedListener (MapCreatedListener mapCreatedListener){
        newInstance(/*new GoogleMapOptions()*/);
        listener = mapCreatedListener;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listener.onGoogleMapCreation();


    }
}
