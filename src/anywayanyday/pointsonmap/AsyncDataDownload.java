package anywayanyday.pointsonmap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

abstract public class AsyncDataDownload {

	public interface DownloaderListener {
		public void onDownloaderResponse(DataRequest request, Object response);

		public void sendToast(String text);

		public Context getContext();
	}

	public abstract void dataDownload(DataRequest request, DownloaderListener downloaderListener);

	protected DownloaderListener downloaderListener;

	protected boolean isNetworkOnline() {
		ConnectivityManager CManager = (ConnectivityManager) downloaderListener.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo NInfo = CManager.getActiveNetworkInfo();
		return NInfo != null && NInfo.isConnectedOrConnecting();

	}

}
