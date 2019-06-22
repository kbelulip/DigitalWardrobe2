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
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateOutfitsAdapter extends RecyclerView.Adapter<CreateOutfitsAdapter.MyViewHolder> {
    private List<ListKleidungsQuery.Item> mData = new ArrayList<>();
    private CreateOutfitsAdapter.OnItemClickListener mListener;
    private LayoutInflater mInflater;
    private static final String TAG = "CreateOutfitsAdapter";

    // data is passed into the constructor
    CreateOutfitsAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
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

            if (item.foto() != null) {
                if (localUrl == null) {
                    downloadWithTransferUtility(item.foto());
                } else {
                    imageview_Cloth.setImageBitmap(BitmapFactory.decodeFile(localUrl));
                }
            }
            else
                imageview_Cloth.setImageBitmap(null);
        }

        private void downloadWithTransferUtility(final String photo) {
            final String localPath = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + photo;

            TransferObserver downloadObserver =
                    ClientFactory.transferUtility().download(
                            photo,
                            new File(localPath));

            // Attach a listener to the observer to get state update and progress notifications
            downloadObserver.setTransferListener(new TransferListener() {

                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (TransferState.COMPLETED == state) {
                        // Handle a completed upload.
                        localUrl = localPath;
                        imageview_Cloth.setImageBitmap(BitmapFactory.decodeFile(localPath));
                    }
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                    int percentDone = (int) percentDonef;

                    Log.d(TAG, "   ID:" + id + "   bytesCurrent: " + bytesCurrent + "   bytesTotal: " + bytesTotal + " " + percentDone + "%");
                }

                @Override
                public void onError(int id, Exception ex) {
                    // Handle errors
                    Log.e(TAG, "Unable to download the file.", ex);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(CreateOutfitsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
}
