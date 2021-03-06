package anywayanyday.pointsonmap.WorkWithMapsAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import anywayanyday.pointsonmap.Core.DataRequest;
import anywayanyday.pointsonmap.Core.Dot;
import anywayanyday.pointsonmap.R;

public class AsyncYaJob extends AsyncDataDownload {

	public static final String YANDEX_MAP = "http://static-maps.yandex.ru/1.x/?l=map&pt=";

	@Override
	public void dataDownload(DataRequest request, DownloaderListener downloaderListener) {
		this.downloaderListener = downloaderListener;
		if (!isNetworkOnline()) {
			downloaderListener.sendToast(downloaderListener.getContext().getResources().getString(R.id.toast_offline));
			return;
		}
		switch (request.getRequestType()) {
		case DataRequest.GEO_DATA:
			downloadPoint(request);
			break;
		case DataRequest.MAP_TO_IMAGE_VIEW:
			downloadImage(request);
			break;

		}

	}

	private void downloadImage(DataRequest request) {

		new DownloadImage().execute(request);
	}

	private void downloadPoint(DataRequest request) {
		AsyncPointRequest searchInMoscow = new AsyncPointRequest();
		searchInMoscow.isInMoscow = true;
		searchInMoscow.execute(request);
	}

	private class AsyncPointRequest extends AsyncTask<DataRequest, Void, String> {

		public static final String REQUEST_IN_MOSCOW = "http://geocode-maps.yandex.ru/1.x/?rspn=1&ll=37.609218,55.753559&spn=0.552069,0.400552&results=1&geocode=";
		public static final String REQUEST_IN_WORLD = "http://geocode-maps.yandex.ru/1.x/?ll=37.609218,55.753559&spn=0.552069,0.400552&results=1&geocode=";
		private DataRequest request;
		boolean isInMoscow;

		@Override
		protected String doInBackground(DataRequest... dataRequest) {
			this.request = dataRequest[0];
			String address = request.getDot().getAddress();
			address = address.replace(" ", "+");
			HttpGet httpRequest = isInMoscow ? new HttpGet(REQUEST_IN_MOSCOW + address) : new HttpGet(REQUEST_IN_WORLD + address);
			return getGeoData(httpRequest(httpRequest));
		}

		@Override
		protected void onPostExecute(String results) {
			if (results == null && isInMoscow) {
				downloaderListener.sendToast(downloaderListener.getContext().getResources().getString(R.id.start_adv_search));
				AsyncPointRequest searchInMoscow = new AsyncPointRequest();
				searchInMoscow.isInMoscow = false;
				searchInMoscow.execute(request);
			} else {
				downloaderListener.onDownloaderResponse(request, results);
			}
		}
	}

	private class DownloadImage extends AsyncTask<DataRequest, Void, Bitmap> {
		DataRequest request;

		@Override
		protected Bitmap doInBackground(DataRequest... dataRequest) {
			this.request = dataRequest[0];
			String urlDisplay = AsyncYaJob.YANDEX_MAP;
			int i = 1;
			for (Dot dot : request.getDots()) {
				if (i != 1)
					urlDisplay = urlDisplay + "~";
				urlDisplay = urlDisplay + dot.getYaDotPostfix() + i;
				i++;
			}
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urlDisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return mIcon11;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			downloaderListener.onDownloaderResponse(request, result);
		}
	}

	static private String httpRequest(HttpGet request) {
		String data = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(request);
			response.getStatusLine().getStatusCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuilder sb = new StringBuilder("");
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
			client.getConnectionManager().shutdown();
			data = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	static private String getGeoData(String data) {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			factory.setNamespaceAware(true);
			parser.setInput(new StringReader(data));
			return parseForGeoData(parser);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	static private String parseForGeoData(XmlPullParser parser) throws XmlPullParserException, IOException {
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				String name = parser.getName();
				if (name.equalsIgnoreCase("pos")) {
					parser.next();
					return parser.getText();
				}
			}
			eventType = parser.next();
		}
		return null;
	}

}
