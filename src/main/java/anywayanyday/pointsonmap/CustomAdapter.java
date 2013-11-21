package anywayanyday.pointsonmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter implements AsyncDataDownload.DownloaderListener{
    Context context;
    LayoutInflater lInflater;
    ArrayList<Dot> dots;

    String geoCode = "";

    CustomAdapter(@NotNull Context context,@NotNull ArrayList<Dot> dots) {
        this.context = context;
        this.dots = dots;
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
        if (view == null) {
            view = lInflater.inflate(R.layout.dot_field, parent, false);
        }

        Dot dot = (Dot) getItem(position);

        ((TextView) view.findViewById(R.id.textDotAddress)).setText(dot.address);
        ((TextView) view.findViewById(R.id.textDotName)).setText(dot.name);
        if( MainActivity.CURRENT_DOWNLOADER.equalsIgnoreCase("anywayanyday.pointsonmap.AsyncGoogleJob") ){
            AsyncGoogleJob asyncGoogleJob = new AsyncGoogleJob();
            asyncGoogleJob.dataDownload(new DataRequest(dot.address, dot.name), this);
        }
        ((TextView) view.findViewById(R.id.textDotGeoCode)).setText(geoCode);
        TextView textDeleteDot = (TextView) view.findViewById(R.id.textDeleteDot);
        //textDeleteDot.setOnClickListener();
        return view;
    }

    @Override
    public void onDownloaderResponse(DataRequest request, String response) {
        geoCode = response;
    }

    @Override
    public void sendToast(String text) {

    }

    @Override
    public Context getContext() {
        return context;
    }
}
