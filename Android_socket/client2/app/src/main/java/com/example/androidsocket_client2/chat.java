package com.example.androidsocket_client2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class chat extends AppCompatActivity {

    Button btnleave;
    Button btnsend;

    TextView hi;
    TextView chat_part;

    EditText say;

    String name;

    Socket serverSocket;

    String IP;
    String port;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.d("test", "1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initViewElement();

        Intent it = this.getIntent();

        if (it != null) {
            Bundle bundle = it.getExtras();
            if (bundle != null) {
                IP = bundle.getString("IP");
                port = bundle.getString("port");
                name = bundle.getString("name");
                String tmp = hi.getText().toString();
                tmp += name;
                hi.setText(tmp);
            }
        }

        if(IP != null && port != null){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        serverSocket = new Socket(IP, Integer.parseInt(port));

                        //serverSocket = new Socket("10.0.2.2", 56789);
                        //Log.d("1", "test");
                        //if(serverSocket.isConnected())
                        //Log.d("true", "test");

                        //tell server that i just join
                        try {
                            JSONObject jsonMessage = new JSONObject();
                            try {
                                jsonMessage.put("sender", name);
                                jsonMessage.put("content", "");
                                jsonMessage.put("status", "serverIsConn");
                            } catch (JSONException e) {
                                //Log.d("json", "test");
                                e.printStackTrace();
                            }

                            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));
                            out.write(jsonMessage.toString());
                            out.newLine();
                            out.flush();
                        }
                        catch(Exception e){
                            //Log.d("connect", "test");
                            e.printStackTrace();
                        }

                        //get the message from server
                        new ServerHandler(serverSocket).start();
                    }
                    catch (IOException e){
                        //Log.d("error_connect", "test");
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }





        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = say.getText().toString();
                say.setText(""); // Clear the input field

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //send the message to server
                        try {
                            JSONObject jsonMessage = new JSONObject();
                            try {
                                jsonMessage.put("sender", name);
                                jsonMessage.put("content", message);
                                jsonMessage.put("status", "serverIsConn");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String tmp = chat_part.getText().toString();
                                    chat_part.setText(tmp + '\n' + name + ": " + message);
                                }
                            });

                            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));
                            out.write(jsonMessage.toString());
                            out.newLine();
                            out.flush();
                            //Log.d("out", "test");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                //if server close cannot send message
                if(serverSocket.isClosed()){
                    String tmp = chat_part.getText().toString();
                    chat_part.setText(tmp + '\n' + "Sever had closed. Please leave the room.");
                    //Log.d("no_out", "test");
                }
                else{
                    thread.start();
                }

            }
        });

        btnleave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //tell the server i am left by json
                        try {
                            JSONObject jsonMessage = new JSONObject();
                            try {
                                jsonMessage.put("sender", name);
                                jsonMessage.put("content", "");
                                jsonMessage.put("status", "serverIsNotConn");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));
                            out.write(jsonMessage.toString());
                            out.newLine();
                            out.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                if(!serverSocket.isClosed()) {
                    thread.start();
                }

                Intent it = new Intent();
                it.setClass(chat.this, MainActivity.class);
                startActivity(it);
            }
        });

    }

    public class ServerHandler extends Thread{
        Socket server;
        BufferedReader in;

        public ServerHandler(Socket socket) {
            super();
            this.server = socket;
            try {
                in = new BufferedReader(new InputStreamReader(server.getInputStream(), StandardCharsets.UTF_8));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            super.run();
            String message;
            // Handle server messages and show on the chat
            try {
                while ((message = in.readLine()) != null) {
                    // Convert the message to JSON format
                    JSONObject jsonMessage = new JSONObject(message);
                    String sender = jsonMessage.getString("sender");
                    String content = jsonMessage.getString("content");
                    String status = jsonMessage.getString("status");

                    if(status.equals("serverIsConn")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String tmp = chat_part.getText().toString();
                                chat_part.setText(tmp + '\n' + sender + ": " + content);
                            }
                        });
                    }
                    if(status.equals("serverIsNotConn")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String tmp = chat_part.getText().toString();
                                chat_part.setText(tmp + '\n' + sender + ": Server has closed. Please left the room.");
                            }
                        });

                        server.close();
                        break;
                    }
/*                    else{
                        Log.d("error", "test");
                    }*/
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }


    private void initViewElement() {
        btnleave = (Button) findViewById(R.id.btnleave);
        hi = (TextView) findViewById(R.id.hi);
        chat_part = (TextView) findViewById(R.id.chat_part);
        say = (EditText) findViewById(R.id.say);
        btnsend = (Button) findViewById(R.id.btnsend);
    }
}

