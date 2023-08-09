package com.example.sever;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class login extends AppCompatActivity {

    Button btnconnect;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViewElement();

        btnconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();

                bundle.putString("name", name.getText().toString());

                Intent it = new Intent(login.this, MainActivity.class);
                it.putExtras(bundle);
                startActivity(it);
            }
        });
    }

    private void initViewElement(){
        btnconnect = (Button) findViewById(R.id.btnconnect);
        name = (EditText) findViewById(R.id.name);
    }
}