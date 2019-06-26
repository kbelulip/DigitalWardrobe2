package com.example.digitalwardrobe2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.CreateOutfitMutation;
import com.amazonaws.amplify.generated.graphql.ListKleidungsQuery;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import type.CreateOutfitInput;
import type.ModelKleidungFilterInput;
import type.ModelStringFilterInput;

public class uploadCreatedOutfit extends AppCompatActivity implements View.OnClickListener {

    AdapterViewFlipper AVF;
    MyAdapterFlipper mAdapter;

    private ArrayList<ListKleidungsQuery.Item> mKleidungs;
    private ArrayList<ListKleidungsQuery.Item> buffer;
    private String outfitId;
    private ArrayList<String> choosenImages;
    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_created_outfit);

        Intent intent_getChoosenImages = getIntent();
        choosenImages = intent_getChoosenImages.getStringArrayListExtra("AryLst_ChoosenImages");

        Button buttonMenue = (Button) findViewById(R.id.button_Menue);
        buttonMenue.setOnClickListener(this);

        Button buttonSettings = (Button) findViewById(R.id.button_Settings);
        buttonSettings.setOnClickListener(this);

        Button buttonHome = findViewById(R.id.button_Home);
        buttonHome.setOnClickListener(this);

        Button btn_zuruek = findViewById(R.id.button_abbrechen);
        btn_zuruek.setOnClickListener(this);

        AVF = findViewById(R.id.AVF);

        mAdapter = new MyAdapterFlipper(this);
        AVF.setAdapter(mAdapter);

        AVF.setFlipInterval(2000);
        AVF.setAutoStart(true);

        //TextView textView_showArray = findViewById(R.id.textView_showArrayList);
        //textView_showArray.setText(Arrays.deepToString(choosenImages.toArray()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_Menue:
                Intent intent_goToActivityMenue = new Intent(this, MenueAuswahl3.class);
                startActivity(intent_goToActivityMenue);
                this.finish();
                break;
            case R.id.button_Settings:
                Intent intent_goToSettings = new Intent(this, settings.class);
                startActivity(intent_goToSettings);
                this.finish();
                break;
            case R.id.button_Home:
                Toast.makeText(getApplicationContext(), "Keine Funktion hinterlegt", Toast.LENGTH_LONG).show();
                break;
            case R.id.button_abbrechen:
                Intent intent_goToChooseImages = new Intent(this, createOutfit.class);
                startActivity(intent_goToChooseImages);
                this.finish();
                break;
        }
    }

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

            buffer = new ArrayList<>();

            for (ListKleidungsQuery.Item current : mKleidungs) {
                 if (choosenImages.contains(current.id())){
                     buffer.add(current);
                 }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.setItems(buffer);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }
    };

    private void save() {


        CreateOutfitInput input = CreateOutfitInput.builder()
                .anlass("Test")
                .build();

        CreateOutfitMutation addOutfitMutation = CreateOutfitMutation.builder()
                .input(input)
                .build();

        ClientFactory.appSyncClient().mutate(addOutfitMutation).enqueue(mutateCallback);

    }

    // Mutation callback code
    private GraphQLCall.Callback<CreateOutfitMutation.Data> mutateCallback = new GraphQLCall.Callback<CreateOutfitMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreateOutfitMutation.Data> response) {
            outfitId = response.data().toString();
            Log.i(TAG, "Callback von der Outfit Mutation: " + outfitId);
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform AddPetMutation", e);
                    Toast.makeText(uploadCreatedOutfit.this, "Failed to add pet", Toast.LENGTH_SHORT).show();
                    uploadCreatedOutfit.this.finish();
                }
            });
        }
    };
}