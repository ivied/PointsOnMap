package anywayanyday.pointsonmap;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.widget.CompoundButton;
import android.widget.Switch;

import static anywayanyday.pointsonmap.FragmentAddDots.*;

public class MainActivity extends Activity{
    public static final String CURRENT_FRAGMENT = "CurrentFragment";
    public static final String SEARCH = "search";
    private Fragment currentFragment =new FragmentAddDots();
    public static final String GOOGLE_DOWNLOADER = "anywayanyday.pointsonmap.AsyncGoogleJob";
    public static final String YANDEX_DOWNLOADER = "anywayanyday.pointsonmap.AsyncYaJob";
    static  boolean isDualPane = false;
    public static String currentDownloader = GOOGLE_DOWNLOADER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout) ;
        isDualPane = getResources().getBoolean(R.bool.has_two_panes)/* && isTablet(this)*/;
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

}
