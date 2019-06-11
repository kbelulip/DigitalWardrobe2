package com.example.digitalwardrobe2;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Textfeld wird fett ausgegeben
        // TextView textView_header = (TextView) findViewById(R.id.textView_header);
        // textView_header.setPaintFlags(textView_header.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Button buttonMenue = (Button) findViewById(R.id.button_Menue);
        buttonMenue.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent_goToActivityMenue = new Intent(this, MenueAuswahl.class);
        startActivity(intent_goToActivityMenue);
        this.finish();
    }
}
