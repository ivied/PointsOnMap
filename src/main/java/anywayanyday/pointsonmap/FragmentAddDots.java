package anywayanyday.pointsonmap;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static anywayanyday.pointsonmap.DBHelper.*;
/**
 * Created by Serv on 07.10.13.
 */
public class FragmentAddDots extends Fragment implements View.OnClickListener, AsyncYaJob.YaListener {

    public static final String BUNDLE_NAME = "name";
    public static final String BUNDLE_URL = "url";
    public static final int VERTICAL_SPACING = 10;
    public static final int HORIZONTAL_SPACING = 10;
    public static final String YANDEX_MAP = "http://static-maps.yandex.ru/1.x/?l=map&pt=";
    public static final String BLUE_DOT = "pm2bll";

    private EditText editDotName;
    private EditText editDotAddress;
    private Button buttonAdd;
    private SQLiteDatabase database;
    private GridView gridForDots;
    private int displayWidth;
    private int displayHeight;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> dotsName;
    private ImageView imageDotsMap;
    private Map<String, String> dots = new HashMap<>();
    private View view;
    private OnChangeFragmentListener onChangeFragmentListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeLayout(inflater);
        initializeDotsGrid();
        adjustGridView();
        renewLayout();
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onChangeFragmentListener = (OnChangeFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }


    @Override
    public void onClick(View v) {
        new AsyncYaJob(editDotAddress.getText().toString(), this);

    }

    @Override
    public void onYaResponse(String response) {
        String  name = editDotName.getText().toString();
        addToDB(response, name);
    }

    private void initializeDotsGrid() {
        dotsName = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.dot_button, R.id.textDot, dotsName);
        gridForDots.setAdapter(adapter);
        gridForDots.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String name = dotsName.get(position);
                Bundle bundle = new Bundle();
                bundle.putString(BUNDLE_NAME, name );
                bundle.putString(BUNDLE_URL, YANDEX_MAP + dots.get(name) + "," + BLUE_DOT);
                Fragment dotScreen = new FragmentDotScreen();
                dotScreen.setArguments(bundle);
                onChangeFragmentListener.fragmentChanged(dotScreen);
                MainActivity.replaceFragment(dotScreen, getActivity().getFragmentManager().beginTransaction());
                /*Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT);*/
            }
        });
    }

    private void addToDB(String response, String name) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_ADDRESS, response);
        database.insert(TABLE_DOTS, null, cv);
        renewLayout();
    }

    private void renewLayout() {
        dotsName.clear();
        String url = YANDEX_MAP;
        Cursor c = database.query(TABLE_DOTS, null, null, null, null, null, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int numb = Integer.parseInt(c.getString(0));
            String dotName = numb + "." + c.getString(1);
            addDotToLayout(dotName);
            String geoLoc = c.getString(2).replace(" ", ",");
            if (numb != 1) url = url + "~";
            url = url + geoLoc + "," + BLUE_DOT + numb;
            dots.put(dotName, geoLoc);
        }
        c.close();
        new AsyncYaJob(imageDotsMap, url );
    }

    private void addDotToLayout(String name) {
        dotsName.add(name);
        adapter.notifyDataSetChanged();
        gridForDots.invalidateViews();
    }

    private void getDisplaySize(){
        DisplayMetrics displayMetrics  = getResources().getDisplayMetrics();
        displayWidth = Math.round(displayMetrics.widthPixels / displayMetrics.density);
        displayHeight = Math.round(displayMetrics.heightPixels / displayMetrics.density);
    }

    private void adjustGridView() {
        gridForDots.setNumColumns(GridView.AUTO_FIT);
        gridForDots.setVerticalSpacing(VERTICAL_SPACING);
        gridForDots.setHorizontalSpacing(HORIZONTAL_SPACING);
    }

    private void initializeLayout(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.add_dots, null);
        editDotAddress = (EditText) view.findViewById(R.id.editDotAddress);
        editDotName = (EditText) view.findViewById(R.id.editDotName);
        buttonAdd = (Button) view.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);
        database = new DBHelper(getActivity()).getWritableDatabase();
        gridForDots = (GridView) view.findViewById(R.id.gridForDots);
        imageDotsMap = (ImageView) view.findViewById(R.id.imageDotsMap);
    }


}
