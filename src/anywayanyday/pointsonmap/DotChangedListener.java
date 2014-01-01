package anywayanyday.pointsonmap;

import android.content.Context;

public interface DotChangedListener {
	public static int STATE_DELETE = 0;
    public static int STATE_RENEW = 1;

    public void onDotStateChanged(Dot dot, int state);

	public Context getContext();
}
