package anywayanyday.pointsonmap;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;

import static anywayanyday.pointsonmap.DBHelper.*;

public class FragmentAddDots extends Fragment implements View.OnClickListener, AsyncDataDownload.DownloaderListener {

    public static final String DOT = "dot";

    private EditText editDotName;
    private EditText editDotAddress;
    private Button buttonAdd;
    private SQLiteDatabase database;
    private ListView listForDots;
    private DotListAdapter adapter;
    private Switch switchSearch;
    private View view;
    private RelativeLayout frameMap;
    private AsyncDataDownload  asyncDataDownload;
    private ArrayList<Dot> dotsForMap = new ArrayList<Dot>();
    public static final String MAP_FRAGMENT_TAG = "map";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     
        initializeLayout(inflater);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.currentDownloader =loadSearchSettings();
        if (MainActivity.currentDownloader.equalsIgnoreCase(MainActivity.GOOGLE_DOWNLOADER)){
            asyncDataDownload = new AsyncGoogleJob();
        }else {
            asyncDataDownload = new AsyncYaJob();
        }
        initializeDotsGrid();
        renewLayout();

    }

    @Override
    public void onClick(View v) {
        String dotName = editDotName.getText().toString();
        if(dotName.trim().equalsIgnoreCase("")) {
            sendToast(getResources().getString(R.id.toast_wrong_name));
            return;
        }
        asyncDataDownload.dataDownload(new DataRequest(editDotAddress.getText().toString(), dotName), this);

    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onDownloaderResponse(DataRequest request, Object response) {
        if(response == null){
            sendToast(getResources().getString(R.id.toast_no_one_object_found));
             return;
        }
        switch (request.getRequestType()){
        
        case DataRequest.MAP_TO_IMAGE_VIEW:
            Fragment mapFragment = null;
            if(asyncDataDownload instanceof AsyncGoogleJob){
                mapFragment = (MapFragmentWithCreatedListener) response;
            }
            if(asyncDataDownload instanceof AsyncYaJob){
                mapFragment = FragmentWithMap.newInstance((Bitmap) response);
            }
            try{
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(frameMap.getId(), mapFragment, MAP_FRAGMENT_TAG);
                fragmentTransaction.commit();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            break;
        case DataRequest.GEO_DATA:
        	addToDB(request, (String) response);
            renewLayout();
        
        }
    }

    @Override
    public void sendToast(String text) {
        Toast.makeText(getActivity(),  text, Toast.LENGTH_SHORT).show();
    }

    private void initializeDotsGrid() {
        adapter = new DotListAdapter(this.getContext(),  dotsForMap);
        listForDots.setAdapter(adapter);
        listForDots.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Dot dot = dotsForMap.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(DOT, dot);
                if(MainActivity.isDualPane) {
                    FragmentDotScreen fragmentDotScreen = (FragmentDotScreen) getActivity()
                            .getFragmentManager().findFragmentById(R.id.fragment_dot_screen);
                    fragmentDotScreen.initializeMap(bundle);
                }else{
                Fragment dotScreen = new FragmentDotScreen();
                dotScreen.setArguments(bundle);
                MainActivity.replaceFragment(dotScreen, getActivity().getFragmentManager().beginTransaction());
                }
            }
        });
    }


    private void addToDB(DataRequest request, String response) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, request.getDotName());
        cv.put(COLUMN_GEO_LOCATION, response);
        cv.put(COLUMN_ADDRESS, request.getDotAddress());
        database.insert(TABLE_DOTS, null, cv);
    }



    private void renewLayout() {
        dotsForMap.clear();
        Cursor c = database.query(TABLE_DOTS, null, null, null, null, null, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Dot dot = new Dot(c.getInt(0), c.getString(1), c.getString(2) , c.getString(3));
            addDotToLayout(dot);
        }
        asyncDataDownload.dataDownload(new DataRequest(dotsForMap), this);
        c.close();
    }

    private void addDotToLayout(Dot dot) {
        dotsForMap.add(dot);
        adapter.notifyDataSetChanged();
        listForDots.invalidateViews();
    }




    private void initializeLayout(LayoutInflater inflater) {
        MainActivity.currentDownloader = loadSearchSettings();
        view = inflater.inflate(R.layout.add_dots, null);
        editDotAddress = (EditText) view.findViewById(R.id.editDotAddress);
        editDotName = (EditText) view.findViewById(R.id.editDotName);
        buttonAdd = (Button) view.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);
        database = new DBHelper(getActivity()).getWritableDatabase();
        listForDots = (ListView) view.findViewById(R.id.listForDots);
        frameMap = (RelativeLayout) view.findViewById(R.id.frameMapOnAdd);
        switchSearch = (Switch) view.findViewById(R.id.switchSearch);
        switchSearch.setChecked(MainActivity.currentDownloader.equalsIgnoreCase(MainActivity.GOOGLE_DOWNLOADER));
        switchSearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                saveSearchSettings(isChecked);
                getActivity().recreate();
            }
        });
    }

    void saveSearchSettings(boolean isChecked) {
        String searchService = isChecked? MainActivity.GOOGLE_DOWNLOADER:MainActivity.YANDEX_DOWNLOADER;
        SharedPreferences sPref = getActivity().getPreferences(Activity.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(MainActivity.SEARCH, searchService);
        ed.commit();
    }

    private String loadSearchSettings() {
        SharedPreferences sPref = getActivity().getPreferences(Activity.MODE_PRIVATE);
        return sPref.getString(MainActivity.SEARCH, "anywayanyday.pointsonmap.AsyncGoogleJob");
    }

    public void onYaResponse(String[] request, String response) {
        if(response == null){
            sendToast(getResources().getString(R.id.toast_no_one_object_found));
            return;
        }
        // addToDB(request, response);
        renewLayout();
    }
}
