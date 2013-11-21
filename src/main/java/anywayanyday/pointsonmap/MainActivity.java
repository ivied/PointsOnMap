package anywayanyday.pointsonmap;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import com.google.android.gms.common.GooglePlayServicesUtil;

import static anywayanyday.pointsonmap.FragmentAddDots.*;

public class MainActivity extends Activity{
    public static final String CURRENT_FRAGMENT = "CurrentFragment";
    private Fragment currentFragment =new FragmentAddDots();
    public static final String CURRENT_DOWNLOADER = "anywayanyday.pointsonmap.AsyncYaJob";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        setContentView(R.layout.main_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        replaceFragment(currentFragment, getFragmentManager().beginTransaction());
    }

    public static void replaceFragment(Fragment fragment, FragmentTransaction fragmentTransaction) {
        fragmentTransaction.replace(R.id.fragmentContainer, fragment, CURRENT_FRAGMENT);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle state)
    {
        Fragment currentFragment = getFragmentManager().findFragmentByTag(CURRENT_FRAGMENT);
        if(currentFragment.getClass().equals(FragmentDotScreen.class)){
            Bundle bundle = currentFragment.getArguments();
          state.putSerializable(DOT, bundle.getSerializable(DOT));
        }
        super.onSaveInstanceState(state);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        if(state.containsKey(DOT)){
            Fragment dotScreen = new FragmentDotScreen();
            dotScreen.setArguments(state);
            currentFragment = dotScreen;
        }
        super.onRestoreInstanceState(state);
    }
}
