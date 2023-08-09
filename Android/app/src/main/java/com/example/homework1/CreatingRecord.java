package com.example.homework1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class CreatingRecord extends AppCompatActivity {

    Button btncancel;
    Button btnsend;
    EditText name;
    RadioButton male;
    RadioButton female;
    EditText age;
    EditText height;
    EditText weight;

    Boolean modify = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createrecord);

        initViewElement();

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(CreatingRecord.this, MainActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);
            }
        });

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                bundle.putString("name", name.getText().toString());
                bundle.putBoolean("male", male.isChecked());
                bundle.putBoolean("female", female.isChecked());

                int age_value = Integer.parseInt(age.getText().toString());
                bundle.putInt("age", age_value);

                int height_value = Integer.parseInt(height.getText().toString());
                bundle.putInt("height", height_value);

                int weight_value = Integer.parseInt(weight.getText().toString());
                bundle.putInt("weight", weight_value);

                bundle.putBoolean("modify", modify);

                Intent it = new Intent(CreatingRecord.this, Result.class);
                it.putExtras(bundle);
                startActivity(it);
            }
        });

    }

    private void initViewElement(){
        btncancel = (Button) findViewById(R.id.btncancel);
        btnsend = (Button) findViewById(R.id.btnsend);
        name = (EditText) findViewById(R.id.name1);
        male = (RadioButton) findViewById((R.id.rbtnm));
        female = (RadioButton) findViewById(R.id.rbtnf);
        age = (EditText) findViewById((R.id.age1));
        height = (EditText) findViewById(R.id.height1);
        weight = (EditText) findViewById(R.id.weight1);
    }
}
