package com.example.sever;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnleave;
    Button btnsend;
    EditText say;

    TextView hi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




    }

    private void initViewElement(){
        btnleave = (Button) findViewById(R.id.btnleave);
        btnsend = (Button) findViewById(R.id.btnsend);
        hi = (TextView) findViewById(R.id.hi);
        say = (EditText) findViewById(R.id.say);
    }


}