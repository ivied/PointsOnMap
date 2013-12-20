package anywayanyday.pointsonmap;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
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

import java.util.ArrayList;


public class FragmentAddDots extends Fragment implements View.OnClickListener, AsyncDataDownload.DownloaderListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String DOT = "dot";
    private EditText editDotName;
    private EditText editDotAddress;
    private Button buttonAdd;
    private ListView listForDots;
    private DotListAdapter adapter;
    private Switch switchSearch;
    private View view;
    private RelativeLayout frameMap;
    private AsyncDataDownload  asyncDataDownload;
    private ArrayList<Dot> dotsForMap = new ArrayList<Dot>();
    public static final String MAP_FRAGMENT_TAG = "map";
    private static final int URL_LOADER = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeLayout(inflater);

        getLoaderManager().initLoader(URL_LOADER, null, this);
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
        cv.put(DotsProvider.COLUMN_NAME, request.getDotName());
        cv.put(DotsProvider.COLUMN_GEO_LOCATION, response);
        cv.put(DotsProvider.COLUMN_ADDRESS, request.getDotAddress());
        getActivity().getContentResolver().insert(DotsProvider.DOTS_CONTENT_URI, cv);
    }



    private void renewLayout() {
        asyncDataDownload.dataDownload(new DataRequest(dotsForMap), this);
    }

    private void addDotToLayout(Dot dot) {
        for( Dot oldDot : dotsForMap){
            if( oldDot.getId() == dot.getId()) return;
        }
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
        listForDots = (ListView) view.findViewById(R.id.listForDots);
        listForDots.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                renewLayout();
            }
        });
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader= new CursorLoader(
                getActivity(),
                DotsProvider.DOTS_CONTENT_URI,
                null,
                null,
                null,
                null
        );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        try{
            for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
                Dot dot = new Dot(data.getInt(0), data.getString(1), data.getString(2) , data.getString(3));
                addDotToLayout(dot);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
