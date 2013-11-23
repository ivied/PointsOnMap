package anywayanyday.pointsonmap;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class FragmentDotScreen extends Fragment implements View.OnClickListener, AsyncDataDownload.DownloaderListener {

    private TextView textDotName;
    private TextView textDotAddress;
    private Button buttonBack;
    private RelativeLayout frameLayout;
    private View view;
    private Dot dot;
    AsyncDataDownload asyncDataDownload;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeLayout(inflater);
        Bundle bundle = getArguments();
        if(bundle != null)initializeMap(bundle);

        return view;
    }

    void initializeMap(Bundle bundle) {

        dot = (Dot) bundle.getSerializable(FragmentAddDots.DOT);
        textDotName.setText(dot.name);
        textDotAddress.setText(dot.address);
        try{
            Class clazz = Class.forName(MainActivity.currentDownloader);
            asyncDataDownload = (AsyncDataDownload) clazz.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | java.lang.InstantiationException e){
            e.printStackTrace();
        }
        ArrayList<Dot> dots = new ArrayList<Dot>();
        dots.add(dot);
        asyncDataDownload.dataDownload(new DataRequest(frameLayout,dots), this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonBack && !MainActivity.isDualPane)  backOnMainFragment();
    }

    private void backOnMainFragment() {
        FragmentAddDots fragmentAddDots = new FragmentAddDots();
        MainActivity.replaceFragment(fragmentAddDots, getActivity().getFragmentManager().beginTransaction());
    }

    private void initializeLayout(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.dot_on_map, null);
        frameLayout = (RelativeLayout) view.findViewById(R.id.frameMap);
        textDotName = (TextView) view.findViewById(R.id.textDotName);
        buttonBack = (Button) view.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(this);
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
