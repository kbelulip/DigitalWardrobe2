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
import java.util.List;

import javax.annotation.Nonnull;

import type.ModelKleidungFilterInput;
import type.ModelStringFilterInput;

public class createOutfit extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    RecyclerView mRecyclerView;
    private ArrayList<ListKleidungsQuery.Item> mKleidungs;
    Spinner spinner_Variant;
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

        spinner_Variant = findViewById(R.id.spinner_Variant);
        prepareSpinner();
        spinner_Variant.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button_abbrechen:
                Intent intent_goToActivityMenue = new Intent(this, MenueAuswahl3.class);
                startActivity(intent_goToActivityMenue);
                this.finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_Variant:
                if (parent.getItemAtPosition(position).equals("OUTF. ERSTELLEN")) {
                    // do nothing
                } else {
                    if (parent.getItemAtPosition(position).equals("OUTF. ANZEIGEN")) {
                        Intent gotToActivity_captureFoto = new Intent(this, showOutfit.class);
                        startActivity(gotToActivity_captureFoto);
                    }
                }
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    private void prepareSpinner() {
        List<String> variants = new ArrayList<>();
        variants.add(0, "OUTF. ERSTELLEN");
        variants.add("OUTF. ANZEIGEN");
        // Style and populate the spinner
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, R.layout.spinner_layout, variants);
        // Dropwdown layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_Variant.setAdapter(dataAdapter);
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


}
