package anywayanyday.pointsonmap;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class FragmentDotScreen extends Fragment implements View.OnClickListener, AsyncDataDownload.DownloaderListener {

    private TextView textDotName;
    private TextView textDotAddress;
    private Button buttonBack;
    private Button buttonDeleteDot;
    private ImageView imageDotOnMap;
    private View view;
    private Dot dot;
    AsyncDataDownload asyncDataDownload;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeLayout(inflater);
        Bundle bundle = getArguments();
        dot = (Dot) bundle.getSerializable(FragmentAddDots.DOT);
        textDotName.setText(dot.name);
        textDotAddress.setText(dot.address);
        try{
            Class clazz = Class.forName(MainActivity.CURRENT_DOWNLOADER);
            asyncDataDownload = (AsyncDataDownload) clazz.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | java.lang.InstantiationException e){
            e.printStackTrace();
        }
        asyncDataDownload.dataDownload(new DataRequest(imageDotOnMap,  dot.getYaMapUrl()), this);
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
        textDotAddress = (TextView) view.findViewById(R.id.textDotAddress);
    }

    @Override
    public void onDownloaderResponse(DataRequest request, String response) {

    }

    @Override
    public void sendToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
