package anywayanyday.pointsonmap.Core;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

import anywayanyday.pointsonmap.WorkWithMapsAPI.AsyncYaJob;

public class Dot implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String DOT_TYPE = "pm2pnl";

	private final String address;

    private final int id;
	private final String name;
	private final String geoLocation;

	public Dot(int id, @NotNull String name, @NotNull String geoLocation,
			@NotNull String address) {
		this.id = id;
		this.name = name;
		this.geoLocation = geoLocation;
		this.address = address;
	}

	String getYaMapUrl() {
		return AsyncYaJob.YANDEX_MAP + getYaDotPostfix();
	}

	public String getAddress() {
		return address;
	}

	public String getName() {
		return name;
	}

	public String getGeoLocation() {
		return geoLocation;
	}

	public String getYaDotPostfix() {
		return geoLocation.replace(" ", ",") + "," + DOT_TYPE;
	}

	public int getId() {
		return id;
	}
}
