package anywayanyday.pointsonmap;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class FragmentDotScreen extends Fragment implements View.OnClickListener {

    private TextView textDotName;
    private Button buttonBack;
    private Button buttonDeleteDot;
    private ImageView imageDotOnMap;
    private View view;
    private Dot dot;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeLayout(inflater);
        Bundle bundle = getArguments();
        dot = (Dot) bundle.getSerializable(FragmentAddDots.DOT);
        String dotName = dot.name;
        textDotName.setText(dotName);
        new AsyncYaJob(imageDotOnMap,  dot.getYaMapUrl());
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonDeleteDot){
            SQLiteDatabase database = new DBHelper(getActivity()).getWritableDatabase();
            database.delete(DBHelper.TABLE_DOTS, DBHelper.COLUMN_ID + " = ?", new String[] {String.valueOf(dot.id)});
        }
        backOnMainFragment();
    }

    private void backOnMainFragment() {
        FragmentAddDots fragmentAddDots = new FragmentAddDots();
        MainActivity.replaceFragment(fragmentAddDots, getActivity().getFragmentManager().beginTransaction());
    }

    private void initializeLayout(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.dot_on_map, null);
        textDotName = (TextView) view.findViewById(R.id.textDotName);
        buttonBack = (Button) view.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(this);
        buttonDeleteDot = (Button) view.findViewById(R.id.buttonDeleteDot);
        buttonDeleteDot.setOnClickListener(this);
        imageDotOnMap = (ImageView) view.findViewById(R.id.imageDotOnMap);
    }
}
