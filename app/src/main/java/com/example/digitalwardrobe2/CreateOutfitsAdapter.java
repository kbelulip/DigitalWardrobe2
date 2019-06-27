package com.example.digitalwardrobe2;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.ListKleidungsQuery;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CreateOutfitsAdapter extends RecyclerView.Adapter<CreateOutfitsAdapter.MyViewHolder> {
    private List<ListKleidungsQuery.Item> mData = new ArrayList<>();
    private CreateOutfitsAdapter.OnItemClickListener mListener;
    private LayoutInflater mInflater;
    private static final String TAG = "CreateOutfitsAdapter";
    private AmazonS3 s3client = new AmazonS3Client(AWSMobileClient.getInstance());
    private Context test;

    // data is passed into the constructor
    CreateOutfitsAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.test = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.cardview_create_outfit, viewGroup, false);
        return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bindData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // resets the list with a new set of data
    public void setItems(List<ListKleidungsQuery.Item> items) {
        mData = items;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        String localUrl;
        ImageView imageview_Cloth;

        MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageview_Cloth = itemView.findViewById(R.id.imageview_Cloth);

            imageview_Cloth.setOnClickListener(new View.OnClickListener() {
                Boolean imageSelected = false;
                @Override
                public void onClick(View v) {
                    if(imageSelected == false) {
                        imageview_Cloth.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        imageSelected = true;
                    }
                    else {
                        imageview_Cloth.setColorFilter(null);
                        imageSelected = false;
                    }
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

        void bindData(ListKleidungsQuery.Item item) {
            Date today = new Date();
            Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));

            URL url = s3client.generatePresignedUrl("digitalwardrobe272101745e6b14d1f84c01ba2812efb86-master", item.foto(), tomorrow);
            Log.d(TAG, "!----URL----! "+ url);
            Picasso.with(test).load(String.valueOf(url)).into(imageview_Cloth);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(CreateOutfitsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
}
