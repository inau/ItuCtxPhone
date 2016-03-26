package com.inau.ctxph.wswrapper.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ivan on 23-Mar-16.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    final static String DB_NAME = "ctxphoneDB";

    final static class CTX {
        final static String TBL_NAME = "contexts";

    }
    final static class BEAC {
        final static String TBL_NAME = "beacons";

    }


    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
