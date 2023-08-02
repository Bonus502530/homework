package com.example.homework1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Result extends AppCompatActivity {

    Button btncancel3;
    Button btnsave;

    TextView name5;
    TextView bmi;
    TextView bmr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initViewElement();

        Intent it = this.getIntent();
        if(it != null){
            Bundle bundle3 = it.getExtras();
            if(bundle3 != null){
                String name3 = bundle3.getString("name");
                name5.setText(name3);
                int temp = 100;
                int a = bundle3.getInt("age");
                int h = bundle3.getInt("height");
                int w = bundle3.getInt("weight");
                double h_f = h / (float)temp;
                double BMI = w / (h_f * h_f);
                boolean male = bundle3.getBoolean("male");
                double BMR;
                if(male){
                    BMR = 66 + (13.7 * w + 5 * h - 6.8 * a);
                }
                else{
                    BMR = 655 + (9.6 * w + 1.8 * h - 4.7 * a);
                }
                String i = String.format("%.2f", BMI);
                String r = String.format("%.2f", BMR);
                bmi.setText(i);
                bmr.setText(r);

            }
        }

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
                Intent it = new Intent(Result.this, MainActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
