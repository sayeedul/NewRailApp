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

public class PNRStatusActivity extends AppCompatActivity {

    private EditText PNR;
    private TextView NAME,NUMBER,DATE,FROM,TO,CLASS,CHART;
    private Button findPnr;
    private String url;
    private String result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pnr_main);

        PNR = (EditText)findViewById(R.id.pnrET);
        NAME = (TextView)findViewById(R.id.trainNameTV);
        NUMBER = (TextView)findViewById(R.id.trainNumTV);
        DATE = (TextView)findViewById(R.id.dateTV);
        FROM = (TextView)findViewById(R.id.fromTV);
        TO = (TextView)findViewById(R.id.toTV);
        CLASS = (TextView)findViewById(R.id.classTV);
        CHART = (TextView)findViewById(R.id.chartTV);
        findPnr=(Button)findViewById(R.id.getpnrBTN);


        findPnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pnrNumber = PNR.getText().toString().trim();
                url = "https://api.railwayapi.com/v2/pnr-status/pnr/"+ pnrNumber +"/apikey/5xd242sgk5";

                new PNRStatusActivity.PNRAsynTask().execute(url);
            }
        });
    }


    class PNRAsynTask extends AsyncTask<String,Integer,String> {

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
                        Toast.makeText(PNRStatusActivity.this, "Error In Sending Request", Toast.LENGTH_SHORT).show();
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

                NAME.setText(trainOBJ.getString("name"));
                NUMBER.setText(trainOBJ.getString("number"));

                JSONObject fromstation = responseOBJ.getJSONObject("boarding_point");
                FROM.setText(fromstation.getString("name"));

                JSONObject tostation = responseOBJ.getJSONObject("reservation_upto");
                TO.setText(tostation.getString("name"));

               // JSONObject dateOBJ = responseOBJ.getString("doj");
                DATE.setText(responseOBJ.getString("doj"));

                JSONObject classobj = responseOBJ.getJSONObject("journey_class");
                CLASS.setText(classobj.getString("code"));

                String bool = String.valueOf(responseOBJ.getBoolean("chart_prepared"));
                CHART.setText(bool);

                }

                catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
