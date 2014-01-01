package anywayanyday.pointsonmap.UI;

import android.content.Context;

import anywayanyday.pointsonmap.Core.Dot;

public interface DotChangedListener {
	public static int STATE_DELETE = 0;
    public static int STATE_RENEW = 1;

    public void onDotStateChanged(Dot dot, int state);

	public Context getContext();
}
