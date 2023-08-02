package com.example.homework1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    Button btncreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewElement();
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent it = new Intent(MainActivity.this, CreatingRecord.class);
                    startActivity(it);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initViewElement(){
        btncreate = (Button) findViewById(R.id.btncreate);
    }
}
