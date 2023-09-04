package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.GsonBuilder;

public class MainActivity extends AppCompatActivity {
    PrintWriter out;
    Socket socket;

    String prop = null;
    String clothes = null;
    String foreground = null;
    String mask = null;
    String background = "playground.jpg";

    public MainActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SendMessage(setMaterialMsg());

    }

    private String setMaterialMsg() {
        HashMap<String, Object> attribute = new HashMap<String, Object>();
        attribute.put("instruction", "Scene");
        HashMap<String, Object> materials = new HashMap<String, Object>();
        materials.put("background", background==null ? null:background);
        materials.put("foreground", foreground==null ? null:foreground);
        materials.put("prop", prop==null ? null:prop);
        materials.put("clothes", clothes==null ? null:clothes);
        materials.put("mask", mask==null ? null:mask);
        materials.put("shift_x", 80.0);
        materials.put("shift_y", 80.0);
        materials.put("scale", 1.0);
        materials.put("puppet", null);
        materials.put("material", materials);
        Log.e("hahatest", new GsonBuilder().create().toJson(attribute));
        return new GsonBuilder().create().toJson(attribute);


    }

    public void SendMessage(final String msg){
        Runnable runnable2 = (Runnable) () -> {
            try {
                socket = new Socket("192.168.0.80", 8001);
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                out.println(msg);
                Log.d("socket", msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        Thread thread2 = new Thread(runnable2);
        thread2.start();
    }
}