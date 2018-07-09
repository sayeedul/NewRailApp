package com.sayeedul.newrailapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TrainDetailsActivity extends AppCompatActivity{

    private EditText Number;
    private TextView Name,day,yesorno,response;
    private Button get;
    private String url;
    String result,RUNS="",DAYS="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_main);

        get = (Button) findViewById(R.id.getpnrBTN);
        Number = (EditText) findViewById(R.id.numberET);
        Name = (TextView) findViewById(R.id.nameTV);
        day =(TextView) findViewById(R.id.daysTV);
        yesorno =(TextView) findViewById(R.id.yesnoTV);


        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String trainNumber = Number.getText().toString();
                url = "https://api.railwayapi.com/v2/name-number/train/" + trainNumber + "/apikey/vv2assld6o";

                new TrainDetailsActivity.DetailAsynTask().execute(url);
            }
        });

    }

    class DetailAsynTask extends AsyncTask<String,Integer,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // Toast.makeText(TrainDetailsActivity.this, "In onPre Execute", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            // So Request will be build here
            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(120, TimeUnit.SECONDS);
            client.setConnectTimeout(120, TimeUnit.SECONDS);

            FormEncodingBuilder formBuilder = new FormEncodingBuilder();
            formBuilder.add("page", "1");

            RequestBody body = formBuilder.build();
            Request request = new Request.Builder()
                    .url(strings[0])    //strings[0]
                    .build();
            Log.d("URL is ::", strings[0]); //strings[0]


            try {
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    result = response.toString();

                    if (result.equals("") || result.equalsIgnoreCase("null") || result.length() == 0) {
                        Toast.makeText(TrainDetailsActivity.this, "Error In Sending Request", Toast.LENGTH_SHORT).show();
                    }
                    result = response.body().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

           // Toast.makeText(TrainDetailsActivity.this, "In POST Execute", Toast.LENGTH_SHORT).show();

            try {
                JSONObject responseOBJ = new JSONObject(s);
                JSONObject trainOBJ = responseOBJ.getJSONObject("train");

                Name.setText(trainOBJ.getString("name"));
                JSONArray daysArray = trainOBJ.getJSONArray("days");

                for (int i = 0; i < daysArray.length(); i++) {
                    JSONObject daysobj = daysArray.getJSONObject(i);
                    String daycode = daysobj.getString("code");
                    String runs = daysobj.getString("runs");

                    RUNS = RUNS+"\t"+runs;
                    DAYS = DAYS+"   "+daycode;

                }

                day.setText(DAYS);
                yesorno.setText(RUNS);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


}
