package anywayanyday.pointsonmap.UI;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;

import anywayanyday.pointsonmap.WorkWithMapsAPI.AsyncDataDownload;
import anywayanyday.pointsonmap.WorkWithMapsAPI.AsyncGoogleJob;
import anywayanyday.pointsonmap.WorkWithMapsAPI.AsyncYaJob;
import anywayanyday.pointsonmap.Core.DataRequest;
import anywayanyday.pointsonmap.Core.Dot;
import anywayanyday.pointsonmap.R;

public class FragmentDotScreen extends Fragment implements AsyncDataDownload.DownloaderListener {

	private TextView textDotName;
	private TextView textDotAddress;
	private RelativeLayout frameMap;
	private View view;
	private Dot dot;
	AsyncDataDownload asyncDataDownload;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initializeLayout(inflater);
		Bundle bundle = getArguments();
		if (bundle != null)
			initializeMap(bundle);

		return view;
	}

	void initializeMap(Bundle bundle) {

		dot = (Dot) bundle.getSerializable(FragmentAddDots.DOT);
		textDotName.setText(dot.getName());
		textDotAddress.setText(dot.getAddress());
		if (MainActivity.currentDownloader.equalsIgnoreCase(MainActivity.GOOGLE_DOWNLOADER)) {
			asyncDataDownload = new AsyncGoogleJob();
		} else {
			asyncDataDownload = new AsyncYaJob();
		}

		ArrayList<Dot> dots = new ArrayList<Dot>();
		dots.add(dot);
		asyncDataDownload.dataDownload(new DataRequest(dots), this);
	}

	private void initializeLayout(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.dot_on_map, null);
		frameMap = (RelativeLayout) view.findViewById(R.id.frameMap);
		textDotName = (TextView) view.findViewById(R.id.textDotName);
		textDotAddress = (TextView) view.findViewById(R.id.textDotAddress);
	}

	@Override
	public void onDownloaderResponse(DataRequest request, Object response) {
		Fragment mapFragment = null;
		if (asyncDataDownload instanceof AsyncGoogleJob) {
			mapFragment = (MapFragment) response;
		}
		if (asyncDataDownload instanceof AsyncYaJob) {
			mapFragment = FragmentWithMap.newInstance((Bitmap) response);
		}
		try {
			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			fragmentTransaction.replace(frameMap.getId(), mapFragment, FragmentAddDots.MAP_FRAGMENT_TAG);
			fragmentTransaction.commit();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendToast(String text) {
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public Context getContext() {
		return getActivity();
	}

}
