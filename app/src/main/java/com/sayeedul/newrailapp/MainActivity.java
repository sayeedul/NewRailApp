package com.sayeedul.newrailapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button pnr,detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pnr = (Button)findViewById(R.id.pnrBTN);
        detail = (Button)findViewById(R.id.detailBTN);

        pnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,PNRStatusActivity.class);
                startActivity(i);
            }
        });

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this,TrainDetailsActivity.class);
                startActivity(in);
            }
        });
    }
}

