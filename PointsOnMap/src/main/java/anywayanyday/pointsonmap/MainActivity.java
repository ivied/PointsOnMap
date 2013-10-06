package anywayanyday.pointsonmap;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements View.OnClickListener, AsyncYaJob.YaListener {

    EditText editDotName;
    EditText editDotAddress;
    Button buttonAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editDotAddress = (EditText) findViewById(R.id.editDotAddress);
        editDotName = (EditText) findViewById(R.id.editDotName);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);

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
        editDotName.setText("");
        editDotName.setText(response);
    }
}
