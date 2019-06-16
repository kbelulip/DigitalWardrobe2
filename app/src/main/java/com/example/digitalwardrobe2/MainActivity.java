package com.example.digitalwardrobe2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amazonaws.amplify.generated.graphql.ListKleidungsQuery;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;

import javax.annotation.Nonnull;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    RecyclerView mRecyclerView;
    MyAdapter mAdapter;

    private ArrayList<ListKleidungsQuery.Item> mKleidungs;
    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        Button buttonMenue = (Button) findViewById(R.id.button_Menue);
        buttonMenue.setOnClickListener(this);

        Button buttonSettings = (Button) findViewById(R.id.button_Settings);
        buttonSettings.setOnClickListener(this);

        ClientFactory.init(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_Menue:
                Intent intent_goToActivityMenue = new Intent(this, MenueAuswahl.class);
                startActivity(intent_goToActivityMenue);
                this.finish();
                break;
            case R.id.button_Settings:
                Intent intent_goToSettings = new Intent(this, settings.class);
                startActivity(intent_goToSettings);
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

        ClientFactory.appSyncClient().query(ListKleidungsQuery.builder().filter(new).userID().build())
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
