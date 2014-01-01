package anywayanyday.pointsonmap.WorkWithMapsAPI;

import android.os.Bundle;

import com.google.android.gms.maps.MapFragment;

public class MapFragmentWithCreatedListener extends MapFragment {

	private static MapCreatedListener listener;

	public interface MapCreatedListener {
		void onGoogleMapCreation();
	}

	public static MapFragmentWithCreatedListener newInstanceCreate(MapCreatedListener mapCreatedListener) {
		MapFragmentWithCreatedListener mapFragmentWithCreatedListener = new MapFragmentWithCreatedListener();
		listener = mapCreatedListener;
		return mapFragmentWithCreatedListener;
	}

	public MapFragmentWithCreatedListener() {
		super();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (listener != null) {
			listener.onGoogleMapCreation();

		}
	}
}
