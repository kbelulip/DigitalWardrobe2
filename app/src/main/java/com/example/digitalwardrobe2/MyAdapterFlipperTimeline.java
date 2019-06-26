package com.example.digitalwardrobe2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.amplify.generated.graphql.ListSchranksQuery;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterFlipperTimeline extends BaseAdapter {

    private List<ListSchranksQuery.Item> mData = new ArrayList<>();
    private LayoutInflater fInflater;
    private TextView txt_bezeichnung;
    private ImageView image_view;
    private Context test;
    private static final String TAG = "FlipperAdapter";
    private AmazonS3 s3client = new AmazonS3Client(AWSMobileClient.getInstance());

    // data is passed into the constructor
    MyAdapterFlipperTimeline(Context context) {
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
    public void setItems(List<ListSchranksQuery.Item> items) {
        mData = items;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Log.i(TAG, "!----getView wurde ausgeführt----!");
        view = fInflater.inflate(R.layout.view_flipper_timeline_item, null);
        txt_bezeichnung = view.findViewById(R.id.label);
        image_view = view.findViewById(R.id.image);
        bindData(mData.get(position));
        return view;
    }

    private void bindData(ListSchranksQuery.Item item) {
        //txt_bezeichnung.setText(item.);

        Log.d(TAG, "!----Was ist im Item drin?----! "+ item.toString());

       // Date today = new Date();
       // Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));

        //URL url = s3client.generatePresignedUrl("digitalwardrobe272101745e6b14d1f84c01ba2812efb86-master", item.foto(), tomorrow);
       // Log.d(TAG, "!----URL----! "+ url);
       // Picasso.with(test).load(String.valueOf(url)).into(image_view);

    }

}
