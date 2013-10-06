package anywayanyday.pointsonmap;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener, AsyncYaJob.YaListener {

    private EditText editDotName;
    private EditText editDotAddress;
    private Button buttonAdd;
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private GridLayout buttonsGrid;

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
        buttonsGrid = (GridLayout) findViewById(R.id.buttonsGrid);
        ArrayList<View> btns = new ArrayList<View>();
        for (int i = 0; i<20; i++){
            Button btnTag = new Button(this);
            btnTag.setLayoutParams(new GridLayout.LayoutParams());
            btnTag.setText("Button " + i);
            btnTag.setId(i);
            btns.add(btnTag);
        }
       // buttonsGrid.addChildrenForAccessibility(btns);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        AsyncYaJob yaJob = new AsyncYaJob(editDotAddress.getText().toString(), this);

    }

    @Override
    public void onYaResponse(String response) {
        ContentValues cv = new ContentValues();
        cv.put("name", editDotName.getText().toString());
        cv.put("address", response);


    }
}
