package anywayanyday.pointsonmap;

import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;

public class DataRequest {

	public static final int MAP_TO_IMAGE_VIEW = 1;
	public static final int GEO_DATA = 2;
	private ArrayList<Dot> dots;
	private final int requestType;
	private Dot dot;

	public DataRequest(@NotNull ArrayList<Dot> dots) {
		this.dots = dots;
		requestType = MAP_TO_IMAGE_VIEW;
	}

	public DataRequest(@NotNull Dot dot) {
		this.dot = dot;
		requestType = GEO_DATA;
	}

	public ArrayList<Dot> getDots() {
		return dots;
	}

	public int getRequestType() {
		return requestType;
	}

	public Dot getDot() {
		return dot;
	}
}
