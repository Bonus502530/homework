package com.example.homework1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Result extends AppCompatActivity {

    Button btncancel3;
    Button btnsave;

    String name3;
    int a;
    int h;
    int w;
    String i;
    String r;
    String id;
    TextView name5;
    TextView bmi;
    TextView bmr;
    String name_ans;
    Double Ans;

    Boolean male;

    Boolean modify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initViewElement();

        Intent it = this.getIntent();
        if(it != null){
            Bundle bundle3 = it.getExtras();
            if(bundle3 != null){

                modify = bundle3.getBoolean("modify");
                if(modify){
                    id = bundle3.getString("id");
                }

                name3 = bundle3.getString("name");
                name5.setText(name3);
                int temp = 100;
                a = bundle3.getInt("age");
                h = bundle3.getInt("height");
                w = bundle3.getInt("weight");
                double h_f = h / (float)temp;
                double BMI = w / (h_f * h_f);
                male = bundle3.getBoolean("male");
                double BMR;
                if(male){
                    BMR = 66 + (13.7 * w + 5 * h - 6.8 * a);
                }
                else{
                    BMR = 655 + (9.6 * w + 1.8 * h - 4.7 * a);
                }
                i = String.format("%.2f", BMI);
                r = String.format("%.2f", BMR);
                bmi.setText(i);
                bmr.setText(r);

                name_ans = name3;
                Ans = BMR;
            }
        }

        //Log.d(id, "test");
        //Log.d(name3, "test");
        //Log.d(String.valueOf(male), "test");
        //Log.d(String.valueOf(a), "test");
        //Log.d(String.valueOf(h), "test");
        //Log.d(String.valueOf(w), "test");
        //Log.d(i, "test");
        //Log.d(r,"test1");
        //Log.d(String.valueOf(modify),"test2");

        btncancel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Result.this, MainActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //if(!modify){

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url;
                                if (modify) {
                                    url = new URL("http://140.115.200.131/myapp/update.php");
                                    //Log.d("hi", "te");
                                } else {
                                    url = new URL("http://140.115.200.131/myapp/insert_record.php");
                                    //Log.d("hihi", "test");
                                }

                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("POST");
                                connection.setDoOutput(true);
                                connection.setDoInput(true);
                                connection.setUseCaches(false);
                                connection.connect();

                                //Log.d(id, "test");
                                //Log.d(name3, "test");
                                //Log.d(String.valueOf(male), "test");
                                //Log.d(String.valueOf(a), "test");
                                //Log.d(String.valueOf(h), "test");
                                //Log.d(String.valueOf(w), "test");
                                //Log.d(i, "test");
                                //Log.d(r,"test");

                                OutputStream outputStream = connection.getOutputStream();
                                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                                Boolean isMale = male;
                                String data;

                                if(!modify) {
                                    //Log.d("hihihi", "test3");
                                    data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name3, "UTF-8") + "&" +
                                            URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(isMale ? "male" : "female", "UTF-8") + "&" +
                                            URLEncoder.encode("age", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(a), "UTF-8") + "&" +
                                            URLEncoder.encode("height", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(h), "UTF-8") + "&" +
                                            URLEncoder.encode("weight", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(w), "UTF-8") + "&" +
                                            URLEncoder.encode("bmi", "UTF-8") + "=" + URLEncoder.encode(i, "UTF-8") + "&" +
                                            URLEncoder.encode("bmr", "UTF-8") + "=" + URLEncoder.encode(r, "UTF-8");
                                }

                                else{
                                    //Log.d("hihihihi", "test4");
                                    //Log.d(id,"test5");
                                    data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" +
                                            URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name3, "UTF-8") + "&" +
                                            URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(isMale ? "male" : "female", "UTF-8") + "&" +
                                            URLEncoder.encode("age", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(a), "UTF-8") + "&" +
                                            URLEncoder.encode("height", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(h), "UTF-8") + "&" +
                                            URLEncoder.encode("weight", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(w), "UTF-8") + "&" +
                                            URLEncoder.encode("bmi", "UTF-8") + "=" + URLEncoder.encode(i, "UTF-8") + "&" +
                                            URLEncoder.encode("bmr", "UTF-8") + "=" + URLEncoder.encode(r, "UTF-8");
                                }

                                bufferedWriter.write(data);
                                bufferedWriter.flush();
                                bufferedWriter.close();
                                outputStream.close();

                                int responseCode = connection.getResponseCode();

                                if (responseCode == HttpURLConnection.HTTP_OK) {
                                    // Data inserted successfully
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Handle success if needed
                                        }
                                    });
                                } else {
                                    // Error inserting data
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Handle error if needed
                                        }
                                    });
                                }

                                connection.disconnect();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                //}

/*                if(modify){
                    try {
                        URL url = new URL("http://192.168.0.116/myapp/update.php");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        connection.setUseCaches(false);
                        connection.connect();

                        OutputStream outputStream = connection.getOutputStream();
                        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");

                        Log.d(id, "test");
                        Log.d(name3, "test");
                        Log.d(String.valueOf(male), "test");
                        Log.d(String.valueOf(a), "test");
                        Log.d(String.valueOf(h), "test");
                        Log.d(String.valueOf(w), "test");
                        Log.d(i, "test");
                        Log.d(r,"test");


                        String postData =  URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" +
                                URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name3, "UTF-8") + "&" +
                                URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(male ? "male" : "female", "UTF-8") + "&" +
                                URLEncoder.encode("age", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(a), "UTF-8") + "&" +
                                URLEncoder.encode("height", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(h), "UTF-8") + "&" +
                                URLEncoder.encode("weight", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(w), "UTF-8") + "&" +
                                URLEncoder.encode("bmi", "UTF-8") + "=" + URLEncoder.encode(i, "UTF-8") + "&" +
                                URLEncoder.encode("bmr", "UTF-8") + "=" + URLEncoder.encode(r, "UTF-8");

                        writer.write(postData);
                        writer.flush();
                        writer.close();
                        outputStream.close();

                        int responseCode = connection.getResponseCode();

                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            // Data inserted successfully
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Handle success if needed
                                }
                            });
                        } else {
                            // Error inserting data
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Handle error if needed
                                }
                            });
                        }


                        connection.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/

                Intent it = new Intent(Result.this, MainActivity.class);
                startActivity(it);
            }
        });
    }


    private void initViewElement(){
        btncancel3 = (Button) findViewById(R.id.btncancel3);
        btnsave = (Button) findViewById(R.id.btnsave);
        name5 = (TextView) findViewById(R.id.name5);
        bmi = (TextView) findViewById(R.id.bmi);
        bmr = (TextView) findViewById(R.id.bmr);
    }
}


