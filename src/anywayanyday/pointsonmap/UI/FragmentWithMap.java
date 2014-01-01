package anywayanyday.pointsonmap.UI;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import anywayanyday.pointsonmap.R;

public class FragmentWithMap extends Fragment {
	private static Bitmap map;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_with_map, null);
		ImageView imageView = (ImageView) v.findViewById(R.id.image_map);
		imageView.setImageBitmap(map);
		return v;
	}

	public static FragmentWithMap newInstance(Bitmap response) {
		map = response;
		return new FragmentWithMap();
	}
}
