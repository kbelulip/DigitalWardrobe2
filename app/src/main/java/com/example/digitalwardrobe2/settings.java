package com.example.digitalwardrobe2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.amazonaws.mobile.client.AWSMobileClient;

public class settings extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button buttonHome = (Button) findViewById(R.id.button_Home);
        buttonHome.setOnClickListener(this);

        Button buttonLogout = (Button) findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_Home:
                Intent intent_goToActivityHome = new Intent(this, MainActivity.class);
                startActivity(intent_goToActivityHome);
                this.finish();
                break;
            case R.id.button_logout:
                AWSMobileClient.getInstance().signOut();
                Intent intent_signIn = new Intent(this, AuthenticationActivity.class);
                startActivity(intent_signIn);
                this.finish();
                break;
        }
    }

}
