package anywayanyday.pointsonmap;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import static anywayanyday.pointsonmap.DBHelper.*;

public class FragmentAddDots extends Fragment implements View.OnClickListener, AsyncYaJob.DownloaderListener {

    public static final String DOT = "dot";

    private EditText editDotName;
    private EditText editDotAddress;
    private Button buttonAdd;
    private SQLiteDatabase database;
    private ListView listForDots;
    private DotListAdapter adapter;
    Switch switchSearch;
    private View view;
    private RelativeLayout frameMap;
    private AsyncDataDownload asyncDataDownload;
    private ArrayList<Dot> dotsForMap = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            Class clazz = Class.forName(MainActivity.currentDownloader);
            asyncDataDownload = (AsyncDataDownload) clazz.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | java.lang.InstantiationException e){
            e.printStackTrace();
        }
        initializeLayout(inflater);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currentDownloader =loadSearchSettings();
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
    public void onDownloaderResponse(DataRequest request, String response) {
        if(response == null){
            sendToast(getResources().getString(R.id.toast_no_one_object_found));
             return;
        }
        addToDB(request, response);
        renewLayout();
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
        int i = 1;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Dot dot = new Dot(c.getInt(0), c.getString(1), c.getString(2) , c.getString(3));
            addDotToLayout(dot);
            i++;
        }
        asyncDataDownload.dataDownload(new DataRequest( frameMap, dotsForMap), this);
        c.close();
    }

    private void addDotToLayout(Dot dot) {
        dotsForMap.add(dot);
        adapter.notifyDataSetChanged();
        listForDots.invalidateViews();
    }




    private void initializeLayout(LayoutInflater inflater) {
        MainActivity.currentDownloader =loadSearchSettings();
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
