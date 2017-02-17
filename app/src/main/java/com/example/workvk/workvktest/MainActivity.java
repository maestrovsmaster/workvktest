package com.example.workvk.workvktest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiCity;
import com.vk.sdk.api.model.VKApiCountry;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.util.VKUtil;

public class MainActivity extends AppCompatActivity {

    CardView avatarCard;
    ImageView avatarImageView;
    TextView nameTextView;
    TextView cityTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        avatarCard = (CardView) findViewById(R.id.avatarCard);
        avatarImageView = (ImageView) findViewById(R.id.avatarImageView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        cityTextView = (TextView) findViewById(R.id.cityTextView);


        VKAccessToken token = VKAccessToken.currentToken();
        if(token==null)
        {
            Log.d("my","token is null");
            VKSdk.login(MainActivity.this, VKScope.WALL, VKScope.PHOTOS);

        }else{
            String  tokenStr = token.accessToken;
            Log.d("my","token  = "+tokenStr);

            vkUser();

        }



    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
// Пользователь успешно авторизовался
                String token = res.accessToken;
                Log.d("my","success! token = "+token);
                vkUser();
            }
            @Override
            public void onError(VKError error) {
// Произошла ошибка авторизации (например, пользователь запретил авторизацию)
                Log.d("my","error!");
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    VKApiUserFull user;

    private void vkUser()
    {
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_200, contacts, city, country"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                user = ((VKList<VKApiUserFull>)response.parsedModel).get(0);

                if(user!=null) {
                    Log.d("my", " >>>" + user.first_name + " " + user.last_name + " " + user.mobile_phone + " " + user.photo_200);
                    updateUI();
                }
            }
        });
    }

    private void updateUI()
    {
        String fullName = user.first_name + " " + user.last_name;
        String imageUrl= user.photo_200;



        Log.d("my","usr="+user.toString());
        VKApiCountry country = user.country;
        if(country==null){
            Log.d("my","country=null");
        }else{
            Log.d("my","country = "+country.toString());
        }

        VKApiCity city = user.city;
        if(city==null){
            Log.d("my","city=null");
        }else{
            Log.d("my","city = "+city.toString());
            String userCity=city.toString();
            cityTextView.setVisibility(View.VISIBLE);
            cityTextView.setText(userCity);
        }


        nameTextView.setText(fullName);



        Picasso.with(MainActivity.this).load(imageUrl).into(avatarImageView, new com.squareup.picasso.Callback() {
            public void onSuccess() {
                avatarCard.setVisibility(View.VISIBLE);
            }
            public void onError() {}
        });

    }

}
