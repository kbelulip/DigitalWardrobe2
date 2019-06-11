package com.example.digitalwardrobe2;

import android.content.Intent;
import android.net.Uri;
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

public class chooseImage extends AppCompatActivity implements View.OnClickListener{

    Button button_fotoAuswählen;
    ImageView imageView_ausgewähltesBild;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    Spinner spinner_Variant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);

        imageView_ausgewähltesBild = (ImageView)findViewById(R.id.imageView_ausgewähltesBild);

        button_fotoAuswählen = (Button)findViewById(R.id.button_fotoAuswählen);
        button_fotoAuswählen.setOnClickListener(this);

        Button button_abbrechen = (Button)findViewById(R.id.button_abbrechen);
        button_abbrechen.setOnClickListener(this);

        Button button_weiter = (Button)findViewById(R.id.button_weiter);
        button_weiter.setOnClickListener(this);

        spinner_Variant = findViewById(R.id.spinner_Variant);
        prepareSpinner();
        spinner_Variant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Galerie")) {
                    // do nothing
                }
                else {
                    if(parent.getItemAtPosition(position).equals("Foto")) {
                        Intent gotToActivity_captureFoto = new Intent(chooseImage.this, capturePicture.class);
                        startActivity(gotToActivity_captureFoto);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button_fotoAuswählen:
                openGallery();
                break;
            case R.id.button_abbrechen:
                Intent intent_goToActivityMenue = new Intent(this, MenueAuswahl2.class);
                startActivity(intent_goToActivityMenue);
                this.finish();
                break;
            case R.id.button_weiter:
                Intent intent_gotToActivityAddNewClothes = new Intent(this, addNewClothes.class);
                intent_gotToActivityAddNewClothes.putExtra("resId", R.id.imageView_ausgewähltesBild);
                startActivity(intent_gotToActivityAddNewClothes);
        }
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE);
        if (resultCode == RESULT_OK || resultCode == PICK_IMAGE) {
            imageUri = data.getData();
            imageView_ausgewähltesBild.setImageURI(imageUri);
        }
        imageUri = data.getData();
        imageView_ausgewähltesBild.setImageURI(imageUri);
    }

    private void prepareSpinner() {
        List<String> variants = new ArrayList<>();
        variants.add(0, "Galerie");
        variants.add("Foto");
        // Style and populate the spinner
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, variants);
        // Dropwdown layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_Variant.setAdapter(dataAdapter);
    }
}
