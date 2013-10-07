package anywayanyday.pointsonmap;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class FragmentDotScreen extends Fragment implements View.OnClickListener {

    private OnChangeFragmentListener onChangeFragmentListener;
    TextView textDotName;
    Button buttonBack;
    Button buttonDeleteDot;
    ImageView imageDotOnMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.dot_on_map, null);
        textDotName = (TextView) view.findViewById(R.id.textDotName);
        buttonBack = (Button) view.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(this);
        buttonDeleteDot = (Button) view.findViewById(R.id.buttonDeleteDot);
        buttonDeleteDot.setOnClickListener(this);
        imageDotOnMap = (ImageView) view.findViewById(R.id.imageDotOnMap);
        Bundle bundle = getArguments();
        String dotName = bundle.getString(FragmentAddDots.BUNDLE_NAME).substring(2);
        textDotName.setText(dotName);
        String mapUrl = bundle.getString(FragmentAddDots.BUNDLE_URL);
        new AsyncYaJob(imageDotOnMap, mapUrl);
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
        FragmentAddDots fragmentAddDots = new FragmentAddDots();
        onChangeFragmentListener.fragmentChanged(fragmentAddDots);
        MainActivity.replaceFragment(fragmentAddDots, getActivity().getFragmentManager().beginTransaction());
    }
}
