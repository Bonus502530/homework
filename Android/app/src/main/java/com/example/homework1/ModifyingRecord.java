package com.example.homework1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class ModifyingRecord extends AppCompatActivity {
    Button btncancel2;
    Button btnsend2;
    EditText name2;
    RadioButton male2;
    RadioButton female2;
    EditText age2;
    EditText height2;
    EditText weight2;
    String id;
    Boolean modify = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyingrecord);

        initViewElement();

        Intent intent = this.getIntent();;
        if(intent!=null){
            Bundle bundle = intent.getExtras();
            id = bundle.getString("id");
            //Log.d(na, "test");
            //name2.setText(na);
        }

        btncancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent();
                it.setClass(ModifyingRecord.this, MainActivity.class);
                startActivity(it);
            }
        });

        btnsend2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle2 = new Bundle();

                bundle2.putString("name", name2.getText().toString());
                bundle2.putBoolean("male", male2.isChecked());
                bundle2.putBoolean("female", female2.isChecked());

                int age2_value = Integer.parseInt(age2.getText().toString());
                bundle2.putInt("age", age2_value);

                int height2_value = Integer.parseInt(height2.getText().toString());
                bundle2.putInt("height", height2_value);

                int weight2_value = Integer.parseInt(weight2.getText().toString());
                bundle2.putInt("weight", weight2_value);

                bundle2.putString("id", id);

                bundle2.putBoolean("modify", modify);


                Intent it = new Intent();

                it.setClass(ModifyingRecord.this, Result.class);
                it.putExtras(bundle2);
                startActivity(it);
            }
        });
    }

    private void initViewElement(){
        btncancel2 = (Button) findViewById(R.id.btncancel2);
        btnsend2 = (Button) findViewById(R.id.btnsend2);
        name2 = (EditText) findViewById(R.id.name2);
        male2 = (RadioButton) findViewById((R.id.rbtnm2));
        female2 = (RadioButton) findViewById(R.id.rbtnf2);
        age2 = (EditText) findViewById((R.id.age2));
        height2 = (EditText) findViewById(R.id.height2);
        weight2 = (EditText) findViewById(R.id.weight2);
    }
}


