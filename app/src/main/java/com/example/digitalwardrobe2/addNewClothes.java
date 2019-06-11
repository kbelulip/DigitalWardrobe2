package com.example.digitalwardrobe2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class addNewClothes extends AppCompatActivity {

    ImageView imageView_ausgewähltesBild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_clothes);

        imageView_ausgewähltesBild = (ImageView)findViewById(R.id.imageView_ausgewähltesBild);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            int resid = bundle.getInt("resId");
            imageView_ausgewähltesBild.setImageResource(resid);
        }
    }
}
