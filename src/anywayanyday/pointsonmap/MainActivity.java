package anywayanyday.pointsonmap;

import android.content.ContentValues;
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

public class MainActivity extends Activity implements View.OnClickListener, AsyncYaJob.YaListener {

    public static final int VERTICAL_SPACING = 10;
    public static final int HORIZONTAL_SPACING = 10;
    private EditText editDotName;
    private EditText editDotAddress;
    private Button buttonAdd;
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private GridView gridForDots;
    private int displayWidth;
    private int buttonsID = 0;
    ArrayAdapter<String> adapter;
    ArrayList<String> dotsName;
    ImageView imageDotsMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editDotAddress = (EditText) findViewById(R.id.editDotAddress);
        editDotName = (EditText) findViewById(R.id.editDotName);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        gridForDots = (GridView) findViewById(R.id.gridForDots);
        dotsName = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.dot_button, R.id.textDot, dotsName);
        gridForDots.setAdapter(adapter);
        imageDotsMap = (ImageView) findViewById(R.id.imageDotsMap);
        adjustGridView();
        /*new AsyncYaJob(imageDotsMap, "http://static-maps.yandex.ru/1.x/?ll=37.677751,55.757718&spn=0.016457,0.00619&l=map&key=ANpUFEkBAAAAf7jmJwMAHGZHrcKNDsbEqEVjEUtCmufxQMwAAAAAAAAAAAAvVrubVT4btztbduoIgTLAeFILaQ==");
*/

    }


    @Override
    protected void onResume() {
        displayWidth = getDisplayWidth();

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
        addDotToLayout(name);
    }

    private void addDotToLayout(String name) {
        dotsName.add(name);
        adapter.notifyDataSetChanged();
        gridForDots.invalidateViews();
    }

    private int getDisplayWidth(){
        DisplayMetrics displayMetrics  = getResources().getDisplayMetrics();
        return Math.round(displayMetrics.widthPixels / displayMetrics.density);
    }

    private void adjustGridView() {
        gridForDots.setNumColumns(GridView.AUTO_FIT);
        gridForDots.setVerticalSpacing(VERTICAL_SPACING);
        gridForDots.setHorizontalSpacing(HORIZONTAL_SPACING);
    }
}
