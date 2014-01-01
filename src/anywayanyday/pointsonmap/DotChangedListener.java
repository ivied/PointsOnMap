package anywayanyday.pointsonmap;


import android.content.Context;

public interface DotChangedListener {
  public void onDotDeleted(Dot dot);
  public Context getContext();
}
