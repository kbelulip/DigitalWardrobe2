package com.example.digitalwardrobe2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.CreateKleidungMutation;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import type.CreateKleidungInput;

public class chooseImage extends AppCompatActivity implements View.OnClickListener{

    Button button_fotoAuswählen;
    ImageView imageView_ausgewähltesBild;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    Spinner spinner_Variant;
    private String photoPath;
    private static final String TAG = "chooseImage";

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

        Button button_hochladen = findViewById(R.id.button_hochladen);
        button_hochladen.setOnClickListener(this);

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
                break;
            case R.id.button_hochladen:
                uploadAndSave();
                break;
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

        Uri imageUri = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(imageUri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        // String picturePath contains the path of selected Image
        photoPath = picturePath;
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

    private CreateKleidungInput getCreateKleidungInput() {
        final String bezeichnung = ((EditText) findViewById(R.id.editText_bezeichnung)).getText().toString();
        final String kategorie = ((EditText) findViewById(R.id.editText_kategorie)).getText().toString();

        if (photoPath != null && !photoPath.isEmpty()){
            return CreateKleidungInput.builder()
                    .bezeichnung(bezeichnung)
                    .kategorie(kategorie)
                    .foto(getS3Key(photoPath)).build();
        } else {
            return CreateKleidungInput.builder()
                    .bezeichnung(bezeichnung)
                    .kategorie(kategorie)
                    .build();
        }
    }

    private void save() {
        CreateKleidungInput input = getCreateKleidungInput();

        CreateKleidungMutation addKleidungMutation = CreateKleidungMutation.builder()
                .input(input)
                .build();

        ClientFactory.appSyncClient().mutate(addKleidungMutation).enqueue(mutateCallback);
    }

    private void uploadAndSave(){
        if (photoPath != null) {
            // For higher Android levels, we need to check permission at runtime
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                Log.d(TAG, "READ_EXTERNAL_STORAGE permission not granted! Requesting...");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }

            // Upload a photo first. We will only call save on its successful callback.
            uploadWithTransferUtility(photoPath);
        } else {
            save();
        }
    }

    // Mutation callback code
    private GraphQLCall.Callback<CreateKleidungMutation.Data> mutateCallback = new GraphQLCall.Callback<CreateKleidungMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreateKleidungMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(chooseImage.this, "Added pet", Toast.LENGTH_SHORT).show();
                    chooseImage.this.finish();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform AddPetMutation", e);
                    Toast.makeText(chooseImage.this, "Failed to add pet", Toast.LENGTH_SHORT).show();
                    chooseImage.this.finish();
                }
            });
        }
    };

    //S3 ab hier
    private String getS3Key(String localPath) {
        //We have read and write ability under the public folder
        return "public/" + new File(localPath).getName();
    }

    public void uploadWithTransferUtility(String localPath) {
        String key = getS3Key(localPath);

        Log.d(TAG, "Uploading file from " + localPath + " to " + key);

        TransferObserver uploadObserver =
                ClientFactory.transferUtility().upload(
                        key,
                        new File(localPath));

        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                    Log.d(TAG, "Upload is completed. ");

                    // Upload is successful. Save the rest and send the mutation to server.
                    save();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                Log.d(TAG, "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
                Log.e(TAG, "Failed to upload photo. ", ex);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(chooseImage.this, "Failed to upload photo", Toast.LENGTH_LONG).show();
                    }
                });
            }

        });
    }
}
