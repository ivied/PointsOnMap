package anywayanyday.pointsonmap;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DotListAdapter extends BaseAdapter implements AsyncDataDownload.DownloaderListener , NotifyingAsyncQueryHandler.AsyncQueryListener {
    private final Context context;
    private LayoutInflater lInflater;
    private ArrayList<Dot> dots;
    private Map<Dot,ViewHolder> dotsHolders = new HashMap<Dot, ViewHolder>();
    private DotChangedListener listener;


    DotListAdapter(@NotNull DotChangedListener listener, @NotNull ArrayList<Dot> dots) {
        this.context = listener.getContext();
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
            ViewHolder holder = new ViewHolder();
            holder.textDotAddress = (TextView) view.findViewById(R.id.textDotAddress);
            holder.textDotName = (TextView) view.findViewById(R.id.textDotName);
            holder.textDotGeoCode = (TextView) view.findViewById(R.id.textDotGeoCode);
            holder.textDeleteDot = (TextView) view.findViewById(R.id.textDeleteDot);
            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        final Dot dot = (Dot) getItem(position);
        dotsHolders.put(dot, holder);
        getGeoCode(dot);


        holder.textDotAddress.setText(dot.address);
        holder.textDotName.setText(dot.name);

        final View finalView = view;
        holder.textDeleteDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDotDeleted(dot);
                dots.remove(dot);
                notifyDataSetChanged();
            }
        });
        return view;
    }

    private void getGeoCode(Dot dot) {
        if( MainActivity.currentDownloader.equalsIgnoreCase("anywayanyday.pointsonmap.AsyncGoogleJob") ){
                AsyncGoogleJob asyncGoogleJob = new AsyncGoogleJob();
                DataRequest dataRequest = new DataRequest(dot);
                asyncGoogleJob.dataDownload( dataRequest , this);
        }
    }

    @Override
    public void onDownloaderResponse(DataRequest request, Object response) {
        ViewHolder holder = dotsHolders.get(request.getDot());
        if (holder != null){

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
