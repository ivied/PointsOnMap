package anywayanyday.pointsonmap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class DotListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater lInflater;
    ArrayList<Dot> dots;


    DotListAdapter(@NotNull Context context, @NotNull ArrayList<Dot> dots) {
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
            ViewHolder holder = new ViewHolder();
            holder.textDotAddress = (TextView) view.findViewById(R.id.textDotAddress);
            holder.textDotName = (TextView) view.findViewById(R.id.textDotName);
            holder.textDotGeoCode = (TextView) view.findViewById(R.id.textDotGeoCode);
            holder.textDeleteDot = (TextView) view.findViewById(R.id.textDeleteDot);
            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        final Dot dot = (Dot) getItem(position);
        String geoCode = getGeoCode(dot);

        holder.textDotAddress.setText(dot.address);
        holder.textDotName.setText(dot.name);
        holder.textDotGeoCode.setText(geoCode);

        holder.textDeleteDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    SQLiteDatabase database = new DBHelper(context).getWritableDatabase();
                    if (database != null) database.delete(DBHelper.TABLE_DOTS, DBHelper.COLUMN_ID + " = ?", new String[] {String.valueOf(dot.id)});
                    dots.remove(dot);
                    notifyDataSetChanged();
            }
        });
        return view;
    }

    private String getGeoCode(Dot dot) {
        if( MainActivity.currentDownloader.equalsIgnoreCase("anywayanyday.pointsonmap.AsyncGoogleJob") ){
            AsyncGoogleJob asyncGoogleJob = new AsyncGoogleJob();
            try {
                return  asyncGoogleJob.getGeoData(dot.address, context).toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    static class ViewHolder {
        TextView textDotAddress;
        TextView textDotName;
        TextView textDotGeoCode;
        TextView textDeleteDot;
    }

}
