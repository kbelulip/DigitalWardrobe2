package com.example.digitalwardrobe2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class capturePicture extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    ImageView imageView_ausgewähltesBild;
    Spinner spinner_Variant;
    Spinner spinner_Kategorie;
    Spinner spinner_Anlass;
    Spinner spinner_Farbe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_picture);

        imageView_ausgewähltesBild = (ImageView)findViewById(R.id.imageView_ausgewähltesBild);

        Button button_abbrechen = (Button)findViewById(R.id.button_abbrechen);
        button_abbrechen.setOnClickListener(this);

        Button button_fotoAufnehmen = (Button) findViewById(R.id.button_fotoAufnehmen);
        button_fotoAufnehmen.setOnClickListener(this);

        spinner_Variant = findViewById(R.id.spinner_Variant);
        prepareSpinner();
        spinner_Variant.setOnItemSelectedListener(this);

        spinner_Kategorie = findViewById(R.id.spinner_kategorie);
        prepareSpinnerClothes(spinner_Kategorie, R.array.spinner_kategorie);
        spinner_Kategorie.setOnItemSelectedListener(this);

        spinner_Anlass = findViewById(R.id.spinner_anlass);
        prepareSpinnerClothes(spinner_Anlass, R.array.spinner_anlass);
        spinner_Anlass.setOnItemSelectedListener(this);

        spinner_Farbe = findViewById(R.id.spinner_farbe);
        prepareSpinnerClothes(spinner_Farbe, R.array.spinner_farbe);
        spinner_Farbe.setOnItemSelectedListener(this);
    }

    private void prepareSpinner() {
        List<String> variants = new ArrayList<>();
        variants.add(0, "FOTO");
        variants.add("GALERIE");
        // Style and populate the spinner
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, R.layout.spinner_layout, variants);
        // Dropwdown layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_Variant.setAdapter(dataAdapter);
    }

    private void prepareSpinnerClothes(Spinner spinner, int array) {
        // Style and populate the spinner
        ArrayAdapter dataAdapter;
        dataAdapter = ArrayAdapter.createFromResource(this, array, R.layout.spinner_clothes_layout);
        // Dropwdown layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button_fotoAufnehmen:
                openCamera();
                break;
            case R.id.button_abbrechen:
                Intent intent_goToActivityMenue = new Intent(this, MenueAuswahl2.class);
                startActivity(intent_goToActivityMenue);
                this.finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_Variant:
                if (parent.getItemAtPosition(position).equals("GALERIE")) {
                    // do nothing
                } else {
                    if (parent.getItemAtPosition(position).equals("FOTO")) {
                        Intent gotToActivity_captureFoto = new Intent(this, capturePicture.class);
                        startActivity(gotToActivity_captureFoto);
                    }
                }
                break;
            case R.id.spinner_kategorie:
                if (position == 1) {
                    Toast.makeText(getApplicationContext(), "Kategorie", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.spinner_anlass:
                if (position == 1) {
                    Toast.makeText(getApplicationContext(), "Anlass", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.spinner_farbe:
                if (position == 1) {
                    Toast.makeText(getApplicationContext(), "Farbe", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    private void openCamera() {
        Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCamera, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        imageView_ausgewähltesBild.setImageBitmap(bitmap);
    }
}
