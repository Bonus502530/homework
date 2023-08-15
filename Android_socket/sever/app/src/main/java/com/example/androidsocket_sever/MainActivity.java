package com.example.androidsocket_sever;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    Button btnconnect;

    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewElement();

        btnconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                bundle.putString("name", name.getText().toString());

                Intent it = new Intent();
                it.setClass(MainActivity.this, chat.class);
                it.putExtras(bundle);
                startActivity(it);
            }
        });


    }


    private void initViewElement(){
        name = (EditText) findViewById(R.id.name);
        btnconnect = (Button) findViewById(R.id.btnconnect);
    }
}


