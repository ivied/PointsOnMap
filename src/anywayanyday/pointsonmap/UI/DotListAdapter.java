package anywayanyday.pointsonmap.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import anywayanyday.pointsonmap.WorkWithMapsAPI.AsyncDataDownload;
import anywayanyday.pointsonmap.WorkWithMapsAPI.AsyncGoogleJob;
import anywayanyday.pointsonmap.Core.DataRequest;
import anywayanyday.pointsonmap.Core.Dot;
import anywayanyday.pointsonmap.DataBase.NotifyingAsyncQueryHandler;
import anywayanyday.pointsonmap.R;

public class DotListAdapter extends BaseAdapter implements AsyncDataDownload.DownloaderListener, NotifyingAsyncQueryHandler.AsyncQueryListener {
	private final Context context;
	private final LayoutInflater lInflater;
	private final ArrayList<Dot> dots;
	private final Map<Dot, ViewHolder> dotsHolders = new HashMap<Dot, ViewHolder>();
	private final DotChangedListener listener;

	public DotListAdapter(@NotNull DotChangedListener listener, @NotNull ArrayList<Dot> dots) {
		this.context = listener.getContext();
		this.dots = dots;
		this.listener = listener;
		lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return dots.size();
	}

	@Override
	public Object getItem(int position) {
		return dots.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final Dot dot = (Dot) getItem(position);
		if (view == null) {
			view = lInflater.inflate(R.layout.dot_field, parent, false);
			ViewHolder holder = new ViewHolder();
			holder.textDotAddress = (TextView) view.findViewById(R.id.textDotAddress);
			holder.textDotName = (TextView) view.findViewById(R.id.textDotName);
			holder.textDotGeoCode = (TextView) view.findViewById(R.id.textDotGeoCode);
			holder.textDeleteDot = (TextView) view.findViewById(R.id.textDeleteDot);
			view.setTag(holder);
			listener.onDotStateChanged(dot, DotChangedListener.STATE_RENEW);
		}

		ViewHolder holder = (ViewHolder) view.getTag();
		dotsHolders.put(dot, holder);
		getGeoCode(dot);

		holder.textDotAddress.setText(dot.getAddress());
		holder.textDotName.setText(dot.getName());
		holder.textDeleteDot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onDotStateChanged(dot, DotChangedListener.STATE_DELETE);
				dots.remove(dot);
				notifyDataSetChanged();
			}
		});
		return view;
	}

	private void getGeoCode(Dot dot) {
		if (MainActivity.currentDownloader.equalsIgnoreCase("anywayanyday.pointsonmap.WorkWithAPI.AsyncGoogleJob")) {
			AsyncGoogleJob asyncGoogleJob = new AsyncGoogleJob();
			DataRequest dataRequest = new DataRequest(dot);
			asyncGoogleJob.dataDownload(dataRequest, this);
		}
	}

	@Override
	public void onDownloaderResponse(DataRequest request, Object response) {
		ViewHolder holder = dotsHolders.get(request.getDot());
		if (holder != null) {

			holder.textDotGeoCode.setText(response.toString());
		}
	}

	@Override
	public void sendToast(String text) {

	}

	@Override
	public Context getContext() {
		return context;
	}

	@Override
	public void onQueryComplete(int token, Object cookie, Cursor cursor) {

	}

	static class ViewHolder {
		TextView textDotAddress;
		TextView textDotName;
		TextView textDotGeoCode;
		TextView textDeleteDot;
	}

}
