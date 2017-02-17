package com.example.workvk.workvktest;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

/**
 * Created by Vasily on 16.02.2017.
 */

public class MainApplication extends Application {


    public static SQLiteDatabase sdb;
    public static DatabaseHelper dbHelper;

    public static final String PARAM_VK_ACCESS_TOKEN="VK_ACCESS_TOKEN";
    public static String VK_ACCESS_TOKEN ="";

    @Override
    public void onCreate() {
        super.onCreate();



        ///VK
        VKSdk.initialize(getApplicationContext());



          vkAccessTokenTracker.startTracking();
         VKSdk.initialize(this);


           /* new Thread(new Runnable() {
                @Override
                public void run() {
                    dbHelper = new DatabaseHelper(getApplicationContext(), "mydatabase.db", null, 1);
                    sdb = dbHelper.getReadableDatabase();
                    Log.d("my", "SDB + " + dbHelper.getReadableDatabase().toString());
                    MainApplication.VK_ACCESS_TOKEN = dbHelper.getOrInsertParamFromDB(MainApplication.PARAM_VK_ACCESS_TOKEN, "");
                    Log.d("my"," access token ="+MainApplication.VK_ACCESS_TOKEN);
                }
            }).start();*/
    }


    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
// VKAccessToken is invalid
            }
        }
    };
}
