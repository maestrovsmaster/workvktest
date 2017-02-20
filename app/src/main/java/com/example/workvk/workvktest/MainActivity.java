package com.example.workvk.workvktest;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.vk.sdk.api.model.VKApiModel;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.util.VKUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CardView avatarCard;
    ImageView avatarImageView;
    TextView nameTextView;
    TextView cityTextView;

    ExpandableHeightGridView gridview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Автор");
       // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dark_gray)));

      //  getSupportActionBar().setDisplayOptions(getSupportActionBar().getDisplayOptions() | android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
       // ImageView imageView = new ImageView(getSupportActionBar().getThemedContext());
       // imageView.setScaleType(ImageView.ScaleType.CENTER);
       // imageView.setImageResource(R.drawable.logoforactionbar);
        android.support.v7.app.ActionBar.LayoutParams layoutParams =
                new android.support.v7.app.ActionBar.LayoutParams(android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT,
                        android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT, Gravity.END | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 20;
      //  imageView.setLayoutParams(layoutParams);
      //  getSupportActionBar().setCustomView(imageView);

       // getSupportActionBar().setTitle(getResources().getString(R.string.register_new));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //!!!!!!!!!!!!!!!!!!!!!!!!!!!!! BACK ICON!!!!!!!!!!!!!!!!!
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setIcon(android.R.color.transparent);


        avatarCard = (CardView) findViewById(R.id.avatarCard);
        avatarImageView = (ImageView) findViewById(R.id.avatarImageView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        cityTextView = (TextView) findViewById(R.id.cityTextView);

        gridview = (ExpandableHeightGridView) findViewById(R.id.gridview);

        gridview.setExpanded(true);


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
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_200, contacts, city, country, photos"));
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
                loadUserPhotos();
            }
            public void onError() {}
        });

    }

    private void loadUserPhotos()
    {
        Log.d("my","load photos...");

        VKRequest request2 = new VKRequest("photos.get", VKParameters.from(VKApiConst.OWNER_ID, 1).from(VKApiConst.ALBUM_ID, "wall"),  VKPhotoArray.class);

        request2.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKList list = (VKList) response.parsedModel;


                ArrayList<String>  imageResIds = new ArrayList<String>();

                VKPhotoArray vkPhotoArray = (VKPhotoArray) response.parsedModel;

                int i = 0;
                for (VKApiPhoto vkPhoto : vkPhotoArray) {

                    imageResIds.add(vkPhoto.photo_604);
                    i++;
                }



                ArrayList<ItemObject> allItems = new ArrayList<ItemObject>();

                for(String imgUrl: imageResIds)
                {
                    ItemObject item = new ItemObject(imgUrl,"imgUrl");
                    allItems.add(item);
                }
                GridAdapter customAdapter = new GridAdapter(MainActivity.this, allItems);
                gridview.setAdapter(customAdapter);

                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String url="";


                        Log.d("my","vv = "+view.getClass().toString());
                        ImageView imgVR = (ImageView) view.findViewById(R.id.imageView);
                        if(imgVR.getTag()!=null) {
                            Log.d("my"," tag = "+imgVR.getTag());
                            url=(String)imgVR.getTag();
                        }
                        else  Log.d("my"," tag is null");

                        Bitmap bmp = ((BitmapDrawable)imgVR.getDrawable()).getBitmap();
                       // Toast.makeText(MainActivity.this, "Position: " + position, Toast.LENGTH_SHORT).show();

                        if(url.length()>0) {
                            LayoutInflater inflater = getLayoutInflater();
                            View dialoglayout = inflater.inflate(R.layout.full_photo_dialog, null);
                            ImageView imgV = (ImageView) dialoglayout.findViewById(R.id.photo);

                           /* Picasso.with(MainActivity.this).load(url).into(imgV, new com.squareup.picasso.Callback() {
                                public void onSuccess() {
                                    avatarCard.setVisibility(View.VISIBLE);
                                    loadUserPhotos();
                                }
                                public void onError() {}
                            });*/

                            imgV.setImageBitmap(bmp);

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setView(dialoglayout);

                            builder.show();

                        }
                    }
                });



            }
        });
    }

}
