package anywayanyday.pointsonmap;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static anywayanyday.pointsonmap.DBHelper.*;

public class FragmentAddDots extends Fragment implements View.OnClickListener, AsyncYaJob.YaListener {

    public static final String YANDEX_MAP = "http://static-maps.yandex.ru/1.x/?l=map&pt=";
    public static final String DOT = "dot";
    public static final float COLUMN_ADDITIONAL_SIZE = 90f;

    private EditText editDotName;
    private EditText editDotAddress;
    private Button buttonAdd;
    private SQLiteDatabase database;
    private GridView gridForDots;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> dotsName;
    private ImageView imageDotsMap;
    private Map<String, Dot> dots = new HashMap<>();
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeLayout(inflater);
        initializeDotsGrid();
        renewLayout();
        return view;
    }


    @Override
    public void onClick(View v) {
        String dotName = editDotName.getText().toString();
        if(dotName.trim().equalsIgnoreCase("")) {
            sendToast(getResources().getString(R.id.toast_wrong_name));
            return;
        }
        new AsyncYaJob(new String[] {editDotAddress.getText().toString(), dotName} , this);

    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onYaResponse(String[] request, String response) {
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
        dotsName = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.dot_button, R.id.textDot, dotsName);
        gridForDots.setAdapter(adapter);
        gridForDots.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String name = dotsName.get(position);
                Dot dot = dots.get(name);
                Bundle bundle = new Bundle();
                bundle.putSerializable(DOT, dot);
                Fragment dotScreen = new FragmentDotScreen();
                dotScreen.setArguments(bundle);
                MainActivity.replaceFragment(dotScreen, getActivity().getFragmentManager().beginTransaction());

            }
        });
    }

    private void addToDB(String[] request, String response) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, request[1]);
        cv.put(COLUMN_GEO_LOCATION, response);
        cv.put(COLUMN_ADDRESS, request[0]);
        database.insert(TABLE_DOTS, null, cv);
    }

    private void renewLayout() {
        dotsName.clear();
        String url = YANDEX_MAP;
        Cursor c = database.query(TABLE_DOTS, null, null, null, null, null, null);
        int i = 1;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Dot dot = new Dot(c.getInt(0), c.getString(1), c.getString(2) , c.getString(3));
            String dotName = i + "." + dot.name;
            addDotToLayout(dotName);
            if (i != 1) url = url + "~";
            url = url + dot.getYaDotPostfix() + i;
            dots.put(dotName, dot);
            i++;
        }
        adjustGridView();
        new AsyncYaJob(imageDotsMap, url );
        c.close();
    }

    private void addDotToLayout(String name) {
        dotsName.add(name);
        adapter.notifyDataSetChanged();
        gridForDots.invalidateViews();
    }

    private void adjustGridView() {
        float columnWidth = getMaxTextSize();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int  numbColumns = (int) ( metrics.widthPixels /(columnWidth+ COLUMN_ADDITIONAL_SIZE *(metrics.densityDpi / 160f)));
        gridForDots.setNumColumns(numbColumns);
    }

    private float getMaxTextSize() {
        float maxTextWidth = 0;
        Rect bounds = new Rect();
        Paint p = new Paint();
        p.setTextSize(18);
        for ( String dotName : dotsName){
            p.getTextBounds(dotName, 0, dotName.length(), bounds);
            float dotTextWidth = bounds.width();
            maxTextWidth = maxTextWidth > dotTextWidth ? maxTextWidth:dotTextWidth;
        }
        return maxTextWidth;
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
