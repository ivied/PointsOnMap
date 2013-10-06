package anywayanyday.pointsonmap;

import android.os.AsyncTask;

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
import java.io.InputStreamReader;
import java.io.StringReader;

public class AsyncYaJob {

    private YaListener yaListener;

    public interface YaListener{
        public void onYaResponse(String response);
    }

    public AsyncYaJob(String address, YaListener yaListener) {
        this.yaListener = yaListener;
        AsyncPointRequest pointRequest = new AsyncPointRequest();
        pointRequest.execute(address);
    }

    private class AsyncPointRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params)  {
            String address = params[0];
            address = address.replace(" ", "+");
            HttpGet request = new HttpGet("http://geocode-maps.yandex.ru/1.x/?results=1&geocode=" + address );
            return getGeoData(httpRequest(request));
        }

        @Override
        protected void onPostExecute(String results)
        {
            yaListener.onYaResponse(results);
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
        } catch (ClientProtocolException e) {
            e.printStackTrace();
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
