package anywayanyday.pointsonmap;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity implements OnChangeFragmentListener {

    @Override
    public void fragmentChanged(Fragment fragment) {
        currentFragment = fragment;
    }

    Fragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        currentFragment = new FragmentAddDots();
    }

    @Override
    protected void onResume() {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add( R.id.fragmentContainer, currentFragment);
        fragmentTransaction.commit();
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle state)
    {

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(currentFragment);
            ft.commit();


        super.onSaveInstanceState(state);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
