package anywayanyday.pointsonmap;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        protected String doInBackground(String... params) {
            String address = params[0];
            address = address.replace(" ", "+");
            HttpGet request = new HttpGet("http://geocode-maps.yandex.ru/1.x/?geocode=" + address );
            BufferedReader in;
            String data = null;

            try {
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(request);
                response.getStatusLine().getStatusCode();

                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder sb = new StringBuilder("");
                String l;
                String nl = System.getProperty("line.separator");
                while ((l = in.readLine()) != null) {
                    sb.append(l).append(nl);
                }
                in.close();
                data = sb.toString();


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e ){
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String results)
        {
            yaListener.onYaResponse(results);
        }
    }
}
