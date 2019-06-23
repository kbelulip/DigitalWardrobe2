package com.example.digitalwardrobe2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.amazonaws.amplify.generated.graphql.ListKleidungsQuery;
import com.apollographql.apollo.api.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class uploadCreatedOutfit extends AppCompatActivity implements View.OnClickListener {
    ViewFlipper v_flipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_created_outfit);

        Intent intent_getChoosenImages = getIntent();
        ArrayList<String> choosenImages = intent_getChoosenImages.getStringArrayListExtra("AryLst_ChoosenImages");

        Button buttonMenue = (Button) findViewById(R.id.button_Menue);
        buttonMenue.setOnClickListener(this);

        Button buttonSettings = (Button) findViewById(R.id.button_Settings);
        buttonSettings.setOnClickListener(this);

        Button buttonHome = findViewById(R.id.button_Home);
        buttonHome.setOnClickListener(this);

        Button btn_zuruek = findViewById(R.id.button_abbrechen);
        btn_zuruek.setOnClickListener(this);

        TextView textView_showArray = findViewById(R.id.textView_showArrayList);
        textView_showArray.setText(Arrays.deepToString(choosenImages.toArray()));

        v_flipper = findViewById(R.id.v_flipper);
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

    private void flipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(4000);
        v_flipper.setAutoStart(true);

        //animation
        v_flipper.setInAnimation(this, android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }
}



//tes