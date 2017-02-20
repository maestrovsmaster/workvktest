package com.example.workvk.workvktest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class GridAdapter extends BaseAdapter {

    private LayoutInflater layoutinflater;
    private List<ItemObject> listStorage;
    private Context context;

    public GridAdapter(Context context, List<ItemObject> customizedListView) {
        this.context = context;
        layoutinflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder listViewHolder;
        if(convertView == null){
            listViewHolder = new ViewHolder();
            convertView = layoutinflater.inflate(R.layout.image_item_layout, parent, false);
            listViewHolder.textInListView = (TextView)convertView.findViewById(R.id.textView);
            listViewHolder.imageInListView = (ImageView)convertView.findViewById(R.id.imageView);
            convertView.setTag(listViewHolder);

               String imgUrl = listStorage.get(position).getContent();

            listViewHolder.parentRel =(RelativeLayout) convertView.findViewById(R.id.parentRelative);
            listViewHolder.imageInListView.setTag(imgUrl);

        Picasso.with(context).load(imgUrl).into(listViewHolder.imageInListView, new com.squareup.picasso.Callback() {
            public void onSuccess() {
            }
            public void onError() {} });

        }else{
            listViewHolder = (ViewHolder)convertView.getTag();
        }



        return convertView;
    }

    static class ViewHolder{
        TextView textInListView;
        ImageView imageInListView;
        RelativeLayout parentRel;
    }

}