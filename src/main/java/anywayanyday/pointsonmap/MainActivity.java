package anywayanyday.pointsonmap;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import static anywayanyday.pointsonmap.FragmentAddDots.*;

public class MainActivity extends Activity implements OnChangeFragmentListener {
    private Fragment currentFragment =new FragmentAddDots();

    @Override
    public void fragmentChanged(Fragment fragment) {
        currentFragment = fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    @Override
    protected void onResume() {
        replaceFragment(currentFragment, getFragmentManager().beginTransaction());
        super.onResume();
    }

    public static void replaceFragment(Fragment fragment, FragmentTransaction fragmentTransaction) {
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle state)
    {
        if(currentFragment.getClass().equals(FragmentDotScreen.class)){
           Bundle bundle = currentFragment.getArguments();
           state.putString(BUNDLE_NAME, bundle.getString(BUNDLE_NAME));
           state.putString(BUNDLE_URL, bundle.getString(BUNDLE_URL));
        }
        super.onSaveInstanceState(state);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        if(state.containsKey(BUNDLE_NAME)){
            Fragment dotScreen = new FragmentDotScreen();
            dotScreen.setArguments(state);
            currentFragment = dotScreen;
        }
        super.onRestoreInstanceState(state);
    }
}
