package anywayanyday.pointsonmap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener, AsyncYaJob.YaListener {

    public static final int VERTICAL_SPACING = 10;
    public static final int HORIZONTAL_SPACING = 10;
    public static final String YANDEX_MAP = "http://static-maps.yandex.ru/1.x/?l=map&pt=";
    public static final String BLUE_DOT = "pm2bll";
    private EditText editDotName;
    private EditText editDotAddress;
    private Button buttonAdd;
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private GridView gridForDots;
    private int displayWidth;
    private int displayHeight;
    private int buttonsID = 0;
    ArrayAdapter<String> adapter;
    ArrayList<String> dotsName;
    ImageView imageDotsMap;
    Map<String, String> dots = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editDotAddress = (EditText) findViewById(R.id.editDotAddress);
        editDotName = (EditText) findViewById(R.id.editDotName);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);
        database = new DBHelper(this).getWritableDatabase();
        gridForDots = (GridView) findViewById(R.id.gridForDots);
        dotsName = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.dot_button, R.id.textDot, dotsName);
        gridForDots.setAdapter(adapter);
        imageDotsMap = (ImageView) findViewById(R.id.imageDotsMap);
        getDisplaySize();
        imageDotsMap.setMaxHeight(displayHeight - 100);
        adjustGridView();
        renewLayout();


    }

    private void renewLayout() {
        dotsName.clear();
        String url = YANDEX_MAP;
        Cursor c = database.query("dots", null, null, null, null, null, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int numb = Integer.parseInt(c.getString(0));
            String dotName = numb + "." + c.getString(1);
            addDotToLayout(dotName);
            String geoLoc = c.getString(2).replace(" ", ",");
            if (numb != 1) url = url + "~";
            url = url + geoLoc + "," + BLUE_DOT + numb;

            dots.put(dotName, geoLoc);
        }
            new AsyncYaJob(imageDotsMap, url );
    }


    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    private void addToDB(String response, String name) {
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("address", response);
        database.insert("dots", null, cv);
        renewLayout();
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


}
