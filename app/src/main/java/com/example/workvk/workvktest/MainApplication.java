package com.example.workvk.workvktest;

import android.app.Application;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

/**
 * Created by Vasily on 16.02.2017.
 */

public class MainApplication extends Application {



    @Override
    public void onCreate() {
        super.onCreate();

        ///VK
        VKSdk.initialize(getApplicationContext());



          vkAccessTokenTracker.startTracking();
         VKSdk.initialize(this);

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
