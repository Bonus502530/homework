package com.example.androidsocket_client1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText name;
    EditText IP;
    EditText port;
    Button btnconnect;

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
                bundle.putString("IP", IP.getText().toString());
                bundle.putString("port", port.getText().toString());

                Intent it = new Intent();
                it.setClass(MainActivity.this, chat.class);
                it.putExtras(bundle);
                startActivity(it);
            }
        });
    }

    private void initViewElement(){
        name = (EditText) findViewById(R.id.name);
        IP = (EditText) findViewById(R.id.IP);
        port = (EditText) findViewById(R.id.port);
        btnconnect = (Button) findViewById(R.id.btnconnect);
    }
}