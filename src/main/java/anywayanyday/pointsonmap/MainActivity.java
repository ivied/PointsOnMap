package anywayanyday.pointsonmap;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Activity;

import com.google.android.gms.common.GooglePlayServicesUtil;

import static anywayanyday.pointsonmap.FragmentAddDots.*;

public class MainActivity extends Activity{
    public static final String CURRENT_FRAGMENT = "CurrentFragment";
    private Fragment currentFragment =new FragmentAddDots();
    public static final String CURRENT_DOWNLOADER = "anywayanyday.pointsonmap.AsyncGoogleJob";
    static  boolean isDualPane = false;
   FragmentDotScreen fragmentDotScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        setContentView(R.layout.main_layout) ;
        isDualPane = getResources().getBoolean(R.bool.has_two_panes)/* && isTablet(this)*/;

        fragmentDotScreen = (FragmentDotScreen) getFragmentManager().findFragmentById(
                 R.id.fragment_dot_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isDualPane)  replaceFragment(currentFragment, getFragmentManager().beginTransaction());
    }

    public static void replaceFragment(Fragment fragment, FragmentTransaction fragmentTransaction) {
        fragmentTransaction.replace(R.id.fragmentContainer, fragment, CURRENT_FRAGMENT);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle state){
        Fragment currentFragment;
        if (!isDualPane) currentFragment = getFragmentManager().findFragmentByTag(CURRENT_FRAGMENT);
        else currentFragment = getFragmentManager().findFragmentById(R.id.fragment_dot_screen);
        if( currentFragment != null && currentFragment.getClass().equals(FragmentDotScreen.class)){
            Bundle bundle = currentFragment.getArguments();
            if(bundle != null) state.putSerializable(DOT, bundle.getSerializable(DOT));
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

    private boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }
}
