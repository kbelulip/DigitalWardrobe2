package com.example.digitalwardrobe2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.amazonaws.amplify.generated.graphql.ListSchranksQuery;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ListSchranksQuery.Item> mData = new ArrayList<>();
    private List<ArrayList<String>> flipperArray;
    private OnItemClickListener mListener;
    private LayoutInflater mInflater;
    private static final String TAG = "MyAdapter";
    private AmazonS3 s3client = new AmazonS3Client(AWSMobileClient.getInstance());
    private Context test;


    // data is passed into the constructor
    MyAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        test = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        Log.d(TAG, "------ViewHolder-----1");
        return new ViewHolder(view, mListener);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.bindData(flipperArray.get(position));
        }catch (Exception e) {
            Log.d(TAG, "reudiger Fehler"+ e.toString());
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // resets the list with a new set of data
    public void setItems(List<ListSchranksQuery.Item> items) {
        mData = items;
        flipperArray = generateArray(mData);
    }

    public List<ArrayList<String>>  generateArray(List<ListSchranksQuery.Item> items){
        List<ListSchranksQuery.Item> mKleidungs = items;

        ArrayList<String> oIds = new ArrayList<>();
        List<ArrayList<String>> pics = new ArrayList<>();

        for (ListSchranksQuery.Item current : mKleidungs){
            if (oIds.contains(current.outfit().id())) {
                continue;
            }else{
                oIds.add(current.outfit().id());
            }
        }

        for (String oId : oIds){
            ArrayList<String> pfade = new ArrayList<>();
            int i = 0;

            for (ListSchranksQuery.Item current : mKleidungs){
                if (i == 0){
                    pfade.add(current.outfit().user());
                    Log.d(TAG, "!----USER----! "+ current.outfit().user());
                    i++;
                }
                if (current.outfit().id().equals(oId)) {
                    pfade.add(current.kleider().foto());
                    i++;
                }else{
                    continue;
                }
            }

            pics.add(pfade);

        }
        Log.d(TAG, "!----FlipperArray----! "+ Arrays.deepToString(pics.toArray()));
        return pics;
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_bezeichnung;
        ViewFlipper image_view;
        public ImageView imvLike;

        ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            txt_bezeichnung = itemView.findViewById(R.id.textView_userName);
            image_view = itemView.findViewById(R.id.AVF);
            image_view.setFlipInterval(5000);
            image_view.setAutoStart(true);
            imvLike = itemView.findViewById(R.id.clickableImv_Like);

            imvLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

        void bindData(ArrayList<String> item) {
            Log.d(TAG, "!----bindData----! "+ item.toString());



            ArrayList<String> buffer = item;
            for (int j = 0; j < buffer.size(); j++){
                if(j == 0){
                    txt_bezeichnung.setText(buffer.get(j));
                }else{
                    Date today = new Date();
                    Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));

                    URL url = s3client.generatePresignedUrl("digitalwardrobe272101745e6b14d1f84c01ba2812efb86-master", buffer.get(j), tomorrow);
                    Log.d(TAG, "!----URL----! "+ url);
                    ImageView imageForFlipper = new ImageView(test);
                    Picasso.with(test).load(String.valueOf(url)).into(imageForFlipper);
                    image_view.addView(imageForFlipper);
                }

            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
//H