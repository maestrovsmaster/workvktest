package com.example.workvk.workvktest;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final String DATABASE_NAME = "mydatabase.db";


    public static final String OPTIONS_TABLE = "OPTIONS_TABLE";
    public static final String OPTION_PARAM = "OPTION_PARAM";
    public static final String OPTION_VALUE = "OPTION_VALUE";

    private static final String DATABASE_CREATE_OPTIONS_TABLE_SCRIPT = "CREATE TABLE " + OPTIONS_TABLE + "(" +
            BaseColumns._ID
            + " integer primary key autoincrement, " +
            OPTION_PARAM + " text unique  not null," +
            OPTION_VALUE + "      text  NOT NULL)";


    public DatabaseHelper(Context context, String name, CursorFactory factory,
                          int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        // TODO Auto-generated constructor stub
    }

    public DatabaseHelper(Context context, String name, CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL(DATABASE_CREATE_OPTIONS_TABLE_SCRIPT);
        Log.d("my","option table script = "+DATABASE_CREATE_OPTIONS_TABLE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub



        db.execSQL("DROP TABLE IF IT EXIST " + DATABASE_CREATE_OPTIONS_TABLE_SCRIPT);


        onCreate(db);
    }







    /////-----------------------------------------------OPTIONS----------------------------------------

    //public static final String OPTIONS_TABLE = "OPTIONS_TABLE";
    //public static final String OPTION_PARAM = "OPTION_PARAM";
    //public static final String OPTION_VALUE = "OPTION_VALUE";

    public void insertOrReplaceOption(String param, String value) {


        //insert or replace into Book (ID, Name, TypeID, Level, Seen) values
        //((select ID from Book where Name = "SearchName"), "SearchName", ...);

        try {
            String insertQuery = " insert or replace into  "
                    + DatabaseHelper.OPTIONS_TABLE + " (OPTION_PARAM, OPTION_VALUE ) VALUES ('" + param + "','"
                    + value + "')";
            //Log.d("my", "insertGood= "+insertQuery);
            MainApplication.sdb.execSQL(insertQuery);
        } catch (SQLException e) {
            Log.d("my", "INS EROOR " + e.toString());
        }

    }




    //( BOOK_ID integer primary key not null, TITLE text not null, SRC text not null,  OFFSET  real not null, SIZE  real not null )
    public String getOption(String param) {
        ArrayList<String> values = new ArrayList<>();

        String selectQuery = " select  dg.OPTION_VALUE " +
                " from  " + OPTIONS_TABLE + " dg where dg.OPTION_PARAM = '" + param+"'";

        Log.d("my", "select  = " + selectQuery);

        SQLiteDatabase db = MainApplication.sdb;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    //int bk_id = cursor.getInt(0);
                    String component = cursor.getString(0);
                    values.add(component);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.d("my", "Get Port err=" + e.toString());
        }

        Log.d("my", "return bookComponent List=" + values.size());
        if (values.size() > 0) return values.get(0);
        else return null;
    }


    public String getOrInsertParamFromDB(String param, String defValue)
    {
        String str = getOption(param);
        if (str == null) insertOrReplaceOption(param, defValue);
        else {
            if (str.length() > 0) return str;
            else insertOrReplaceOption(param, defValue);
        }
        return defValue;
    }


}