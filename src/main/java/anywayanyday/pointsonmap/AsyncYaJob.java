package anywayanyday.pointsonmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;

public class AsyncYaJob {

    private YaListener yaListener;

    public interface YaListener{
        public void onYaResponse(String response);
    }

    public AsyncYaJob(String address, YaListener yaListener) {
        this.yaListener = yaListener;
        new AsyncPointRequest().execute(address);
    }

    public AsyncYaJob(ImageView imageView, String url) {
        new DownloadImage(imageView).execute(url);
    }

    private class AsyncPointRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params)  {
            String address = params[0];
            address = address.replace(" ", "+");
            HttpGet request = new HttpGet("http://geocode-maps.yandex.ru/1.x/?results=1&geocode=Moskva+" + address );
            return getGeoData(httpRequest(request));
        }

        @Override
        protected void onPostExecute(String results)
        {
            yaListener.onYaResponse(results);
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try  {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
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
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    static private String parseForGeoData(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        while(eventType != XmlPullParser.END_DOCUMENT){
            if(eventType == XmlPullParser.START_TAG){
                String name = parser.getName();
                if ( name.equalsIgnoreCase("pos"))  {
                    parser.next();
                    return parser.getText();
                }
            }
            eventType = parser.next();
        }
        return null;
    }

}
