package com.example.digitalwardrobe2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.CreateOutfitMutation;

public class OutfitOption extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_option);

        Button btn_NavigOutfitAnzeigen = findViewById(R.id.button_NavigOutfitAnzeigen);
        btn_NavigOutfitAnzeigen.setOnClickListener(this);

        Button btn_NavigOutfitErstellen = findViewById(R.id.button_NavigOutfitErstellen);
        btn_NavigOutfitErstellen.setOnClickListener(this);

        Button buttonMenue = (Button) findViewById(R.id.button_Menue);
        buttonMenue.setOnClickListener(this);

        Button buttonSettings = (Button) findViewById(R.id.button_Settings);
        buttonSettings.setOnClickListener(this);

        Button buttonHome = findViewById(R.id.button_Home);
        buttonHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_NavigOutfitErstellen:
                Intent intent_goToActivityCreateOutfit = new Intent(this, createOutfit.class);
                startActivity(intent_goToActivityCreateOutfit);
                this.finish();
                break;
            case R.id.button_NavigOutfitAnzeigen:
                Intent intent_goToActivityShowOutfit = new Intent(this, showOutfit.class);
                startActivity(intent_goToActivityShowOutfit);
                this.finish();
                break;
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
        }
    }
}
