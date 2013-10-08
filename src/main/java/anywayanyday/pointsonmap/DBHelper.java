package anywayanyday.pointsonmap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_DOTS = "dots";
    public static final int DB_VERSION = 6;
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_GEO_LOCATION = "geo_location";
    public static final String COLUMN_ID = "id";

    public DBHelper(Context context) {
        super(context, "myDB", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_DOTS + " ("
                + COLUMN_ID + " integer primary key autoincrement,"
                + COLUMN_NAME + " text,"
                + COLUMN_GEO_LOCATION + " text,"
                + COLUMN_ADDRESS + " text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_DOTS);
        onCreate(db);

    }
}