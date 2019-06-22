package com.example.digitalwardrobe2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.ListKleidungsQuery;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import type.ModelKleidungFilterInput;
import type.ModelStringFilterInput;

public class createOutfit extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<String> slideImages = new ArrayList<>();
    RecyclerView mRecyclerView;
    private ArrayList<ListKleidungsQuery.Item> mKleidungs;
    CreateOutfitsAdapter mAdapter;
    private final String TAG = "CreateOutfit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_outfit);

        mRecyclerView = findViewById(R.id.recyclerView_createOutfits);
        // use a linear layout manager
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // specify an adapter (see also next example)
        mAdapter = new CreateOutfitsAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        Button button_abbrechen = (Button)findViewById(R.id.button_abbrechen);
        button_abbrechen.setOnClickListener(this);

        Button button_weiter = findViewById(R.id.button_ErstelleOutfit);
        button_weiter.setOnClickListener(this);

        mAdapter.setOnItemClickListener(new CreateOutfitsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                fillArrayListSliderImages(mKleidungs.get(position).id());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button_abbrechen:
                Intent intent_goToActivityMenue = new Intent(this, OutfitOption.class);
                startActivity(intent_goToActivityMenue);
                this.finish();
                break;
            case R.id.button_ErstelleOutfit:
                Intent intent_goToUploadOutfit = new Intent(this, uploadCreatedOutfit.class);
                intent_goToUploadOutfit.putExtra("AryLst_ChoosenImages", this.slideImages);
                startActivity(intent_goToUploadOutfit);
                this.finish();
                break;
        }
    }


    /**
     * Zeigen aller Bilder
     */
    @Override
    public void onResume() {
        super.onResume();


        // Query list data when we return to the screen
        query();
    }

    public void query(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            Log.d(TAG, "WRITE_EXTERNAL_STORAGE permission not granted! Requesting...");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);
        }

        ClientFactory.appSyncClient().query(ListKleidungsQuery.builder().filter(ModelKleidungFilterInput.builder().user(ModelStringFilterInput.builder().eq(AWSMobileClient.getInstance().getUsername()).build()).build()).build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback);
    }

    private GraphQLCall.Callback<ListKleidungsQuery.Data> queryCallback = new GraphQLCall.Callback<ListKleidungsQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListKleidungsQuery.Data> response) {

            mKleidungs = new ArrayList<>(response.data().listKleidungs().items());
            Log.i(TAG, "Retrieved list items: " + mKleidungs.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.setItems(mKleidungs);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }
    };


    /*
    Methode zum Hinzufügen der ausgewählten Bilder in eine ArrayList
     */
    private void fillArrayListSliderImages(String imageID) {
        if(slideImages.contains(imageID)) {
            slideImages.remove(imageID);
            Toast.makeText(getApplicationContext(), imageID+" wurde der aus der Liste entfernt"+
                            System.getProperty("line.sperator") + System.getProperty("line.sperator") +
                            Arrays.deepToString(slideImages.toArray()),
                    Toast.LENGTH_LONG).show();
        }
        else {
            slideImages.add(imageID);
            Toast.makeText(getApplicationContext(), imageID+" wurde der Liste hinzugefügt"+
                            System.getProperty("line.sperator") + System.getProperty("line.sperator")+
                            Arrays.deepToString(slideImages.toArray()),
                    Toast.LENGTH_LONG).show();
        }
    }


}
