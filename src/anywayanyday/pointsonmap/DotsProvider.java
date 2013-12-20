package anywayanyday.pointsonmap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class DotsProvider extends ContentProvider {


    public static final int DB_VERSION = 7;
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_GEO_LOCATION = "geo_location";
    public static final String COLUMN_ID = "id";
    public static final String TABLE_DOTS = "dots";
    static final String DOTS_PATH = "dots";
    static final String AUTHORITY = "anywayanyday.pointsonmap";

    static final int URI_DOTS = 1;


    public static final Uri DOTS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + DOTS_PATH);

    static final String DOT_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + DOTS_PATH;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, DOTS_PATH, URI_DOTS);
    }

    DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String table = getTableName(uri);
        SQLiteDatabase dataBase=dbHelper.getWritableDatabase();
        return dataBase.delete(table, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    public static String getTableName(Uri uri){
        String value = uri.getPath();
        value = value.replace("/", "");
        return value;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String table = getTableName(uri);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(table, null, null, null, null, null, null);
        if (cursor != null) cursor.setNotificationUri(getContext().getContentResolver(), DOTS_CONTENT_URI);
        return cursor;
    }


    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_DOTS:
                return DOT_CONTENT_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String table = getTableName(uri);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long value = database.insert(table, null, values);
        Uri resultUri = ContentUris.withAppendedId(DOTS_CONTENT_URI, value);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }



    private static class DBHelper extends SQLiteOpenHelper {


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
}
