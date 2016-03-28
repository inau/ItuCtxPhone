package pervasive2016.itu.contextapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by DIEM NGUYEN HOANG on 3/24/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION  = 1;
    private static final String DATABASE_NAME = "beaconBase.db";
    private static final String TABLE_NAME = "beaconEntities";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Log.i("KKKKKKKKKKK", "Table DROPPED");
        db.execSQL("CREATE TABLE if not exists " + TABLE_NAME
                        + "(" +
                        " _key TEXT primary key , " +
                        "_uid TEXT NOT NULL, " +
                        "_major TEXT NOT NULL, " +
                        "_minor TEXT NOT NULL, " +
                        "_lat REAL NOT NULL, " +
                        "_lng REAL NOT NULL" +
                        ");"

        );
        Log.i("KKKKKKKKKKK", "Table CREATED");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public static String getTableName(){
        return TABLE_NAME;
    }
}
