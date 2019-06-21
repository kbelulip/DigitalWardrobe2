package com.example.digitalwardrobe2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenueAuswahl2 extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menue_auswahl2);

        Button buttonMenue = (Button) findViewById(R.id.button_Menue);
        buttonMenue.setOnClickListener(this);

        Button button_menueHome = (Button) findViewById(R.id.button_menueHome);
        button_menueHome.setOnClickListener(this);

        Button button_menueAddClothes = (Button) findViewById(R.id.button_menueAddClothes);
        button_menueAddClothes.setOnClickListener(this);

        Button button_OutfitsErstellen = (Button) findViewById(R.id.button_menueOutfits);
        button_OutfitsErstellen.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button_Menue:
                Intent intent_goToActivityHome = new Intent(this, MainActivity.class);
                startActivity(intent_goToActivityHome);
                this.finish();
                break;
            case R.id.button_menueHome:
                Intent intent_goToActivityHome2 = new Intent(this, MainActivity.class);
                startActivity(intent_goToActivityHome2);
                this.finish();
                break;
            case R.id.button_menueAddClothes:
                Intent intent_goToActivityChooseImage = new Intent(this, chooseImage.class);
                startActivity(intent_goToActivityChooseImage);
                this.finish();
                break;
            case R.id.button_menueOutfits:
                Intent intent_goToActivityOutfits = new Intent(this, createOutfit.class);
                startActivity(intent_goToActivityOutfits);
                this.finish();
                break;
        }
    }
}
