package com.example.digitalwardrobe2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.amplify.generated.graphql.ListKleidungsQuery;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyAdapterFlipper extends BaseAdapter {

    private List<ListKleidungsQuery.Item> mData = new ArrayList<>();
    private LayoutInflater fInflater;
    private String localUrl;
    private TextView txt_bezeichnung;
    private ImageView image_view;
    private Context test;
    private static final String TAG = "FlipperAdapter";
    private AmazonS3 s3client = new AmazonS3Client(AWSMobileClient.getInstance());

    // data is passed into the constructor
    MyAdapterFlipper(Context context) {
        test = context;
        fInflater = LayoutInflater.from(context);
        Log.i(TAG, "!----FlippAdapter wurde erzeugt----!");
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    // resets the list with a new set of data
    public void setItems(List<ListKleidungsQuery.Item> items) {
        mData = items;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Log.i(TAG, "!----getView wurde ausgeführt----!");
        view = fInflater.inflate(R.layout.view_flipper_item, null);
        txt_bezeichnung = view.findViewById(R.id.label);
        image_view = view.findViewById(R.id.image);
        bindData(mData.get(position));
        return view;
    }

    private void bindData(ListKleidungsQuery.Item item) {
        txt_bezeichnung.setText(item.bezeichnung());
        //String url = "https://s3.amazonaws.com/digitalwardrobe272101745e6b14d1f84c01ba2812efb86-master/"+item.foto();
        //Log.d(TAG, "!----URL----! "+ url);

        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));

        URL url = s3client.generatePresignedUrl("digitalwardrobe272101745e6b14d1f84c01ba2812efb86-master", item.foto(), tomorrow);
        Log.d(TAG, "!----URL----! "+ url);
        Picasso.with(test).load(String.valueOf(url)).into(image_view);

    }

}
