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

public class capturePicture extends AppCompatActivity implements View.OnClickListener{
    ImageView imageView_ausgewähltesBild;
    Spinner spinner_Variant;
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
        spinner_Variant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Foto")) {
                    // do nothing
                }
                else {
                    if(parent.getItemAtPosition(position).equals("Galerie")) {
                        Intent gotToActivity_chooseImage = new Intent(capturePicture.this, chooseImage.class);
                        startActivity(gotToActivity_chooseImage);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void prepareSpinner() {
        List<String> variants = new ArrayList<>();
        variants.add(0, "Foto");
        variants.add("Galerie");
        // Style and populate the spinner
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, variants);
        // Dropwdown layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_Variant.setAdapter(dataAdapter);
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
