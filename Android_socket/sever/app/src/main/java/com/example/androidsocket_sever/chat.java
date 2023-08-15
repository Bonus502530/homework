package com.example.androidsocket_sever;



import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;



public class chat extends AppCompatActivity {

    Button btnleave;
    Button btnsend;

    TextView hi;
    TextView chat;

    EditText say;

    String name;

    private ServerSocket serverSocket;

    private List<Socket> clients = new ArrayList<Socket>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initViewElement();

        Intent it = this.getIntent();

        if(it != null){
            Bundle bundle = it.getExtras();
            if (bundle != null) {
                name = bundle.getString("name");
                String tmp = hi.getText().toString();
                tmp += name;
                hi.setText(tmp);
            }
        }


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(7100);
                    String serverIpAddress = getLocalIpAddress();
                    int serverPort = serverSocket.getLocalPort();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chat.setText(name + " start(" + serverIpAddress + ":" + serverPort + ")");
                        }
                    });
                    //Log.d("1", "test");
                    startListening();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = say.getText().toString();
                say.setText(""); // Clear the input field

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            JSONObject jsonMessage = new JSONObject();
                            try{
                                jsonMessage.put("sender", name);
                                jsonMessage.put("content", message);
                                jsonMessage.put("status", "serverIsConn");
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String tmp = chat.getText().toString();
                                    chat.setText(tmp + '\n' + name + ": " + message);
                                }
                            });

                            for (Socket socket : clients) {
                                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                out.write(jsonMessage.toString());
                                out.newLine();
                                out.flush();
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });

        btnleave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //tell other client that server had left
                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            JSONObject jsonMessage = new JSONObject();

                            try{
                                jsonMessage.put("sender", name);
                                jsonMessage.put("content", "");
                                jsonMessage.put("status", "serverIsNotConn");
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }

                            for (Socket socket : clients) {
                                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                out.write(jsonMessage.toString());
                                out.newLine();
                                out.flush();
                            }

                            clients.clear();
                            serverSocket.close();

                            Intent it = new Intent();
                            it.setClass(chat.this, MainActivity.class);
                            startActivity(it);

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });

    }

    private void startListening() {
        try {
            while (!serverSocket.isClosed()) {
                Socket client = null;
                try {
                    //Log.d("3", "test");

                    client = serverSocket.accept();

                    //String clientIp = client.getRemoteSocketAddress().toString();
                    //int clientPort = client.getPort();
                    //Log.d("Server", "New client connected: " + clientIp + ":" + clientPort);

                    clients.add(client);

                    //to get the message which client send
                    new ClientHandler(client).start();
                } catch (IOException e) {
                    //Log.d("test", e.toString());
                    e.printStackTrace();
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String message = in.readLine();

                JSONObject jsonMessage = new JSONObject(message);
                String client_name = jsonMessage.getString("sender");

                String clientIp = client.getRemoteSocketAddress().toString();
                int clientPort = client.getPort();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String tmp = chat.getText().toString();
                        chat.setText(tmp + '\n' + client_name + "( " + clientIp + " : " + clientPort + " ) join." + '\n' + name + ": Welcome" + client_name + "join us.");
                    }
                });

                //client join message
                jsonMessage = new JSONObject();
                try {
                    jsonMessage.put("sender", name);
                    jsonMessage.put("content", "welcome " + client_name);
                    jsonMessage.put("status", "serverIsConn");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //server to every client
                for (Socket socket : clients) {
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    out.write(jsonMessage.toString());
                    out.newLine();
                    out.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getLocalIpAddress(){
        try{
            for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
                NetworkInterface intf = en.nextElement();
                for(Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();){
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if(!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()){
                        return inetAddress.getHostAddress();
                    }
                }
            }
        }
        catch (SocketException ex){
            Log.e("WifiPreference IpAddress", ex.toString());
        }
        return null;
    }

    public class ClientHandler extends Thread{
        Socket clientSocket;
        BufferedReader in;
        BufferedWriter out;

        public ClientHandler(Socket socket) {
            super();
            this.clientSocket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            super.run();
            String message;
            // Handle client messages and broadcasting
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
                                String tmp = chat.getText().toString();
                                chat.setText(tmp + '\n' + sender + ": " + content);
                            }
                        });

                        jsonMessage = new JSONObject();
                        try{
                            jsonMessage.put("sender", sender);
                            jsonMessage.put("content", content);
                            jsonMessage.put("status", "serverIsConn");
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                        for(Socket socket : clients){
                            if(socket != clientSocket){
                                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                out.write(jsonMessage.toString());
                                out.newLine();
                                out.flush();
                            }
                        }
                    }
                    else{
                        clients.remove(clientSocket);

                        jsonMessage = new JSONObject();

                        try{
                            jsonMessage.put("sender", name);
                            jsonMessage.put("content", sender + " had left.");
                            jsonMessage.put("status", "serverIsConn");
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String tmp = chat.getText().toString();
                                chat.setText(tmp + '\n' + name + ": "+ sender + " had left.");
                            }
                        });

                        for(Socket socket : clients){
                            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            out.write(jsonMessage.toString());
                            out.newLine();
                            out.flush();
                        }
                        clientSocket.close();
                        break;
                    }
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

    private void initViewElement(){
        btnleave = (Button) findViewById(R.id.btnleave);
        hi = (TextView) findViewById(R.id.hi);
        chat = (TextView) findViewById(R.id.chat);
        say = (EditText) findViewById(R.id.say);
        btnsend = (Button) findViewById(R.id.btnsend);
    }

}



