package com.example.zenbo_hw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.asus.robotframework.API.RobotCallback;
import com.asus.robotframework.API.RobotCmdState;
import com.asus.robotframework.API.RobotCommand;
import com.asus.robotframework.API.RobotErrorCode;

import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
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


public class MainActivity extends RobotActivity {

    private ServerSocket serverSocket;
    TextView ip;
    TextView port;

    TextView ip2;
    TextView port2;

    public MainActivity(RobotCallback robotCallback, RobotCallback.Listen robotListenCallback){
        super(robotCallback, robotListenCallback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewElement();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(7100);
                    String serverIpAddress = getLocalIpAddress();
                    int serverPort = serverSocket.getLocalPort();

                    ip2.setText(serverIpAddress);
                    port2.setText(serverPort);

                    //Log.d("1", "test");
                    //startListening();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();




    }



    public static String getLocalIpAddress(){
        try{
            for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
                NetworkInterface intf = en.nextElement();
                for(Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();){
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if(!inetAddress.isLoopbackAddress() && !(inetAddress instanceof InetAddress)){
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


    public static RobotCallback robotCallback = new RobotCallback() {
        @Override
        public void onResult(int cmd, int serial, RobotErrorCode err_code, Bundle result) {
            super.onResult(cmd, serial, err_code, result);

            Log.d("RobotDevSample", "onResult:"
                    + RobotCommand.getRobotCommand(cmd).name()
                    + ", serial:" + serial + ", err_code:" + err_code
                    + ", result:" + result.getString("RESULT"));
        }

        @Override
        public void onStateChange(int cmd, int serial, RobotErrorCode err_code, RobotCmdState state) {
            super.onStateChange(cmd, serial, err_code, state);
        }

        @Override
        public void initComplete() {
            super.initComplete();

        }
    };

    public static RobotCallback.Listen robotListenCallback = new RobotCallback.Listen() {
        @Override
        public void onFinishRegister() {

        }

        @Override
        public void onVoiceDetect(JSONObject jsonObject) {

        }

        @Override
        public void onSpeakComplete(String s, String s1) {

        }

        @Override
        public void onEventUserUtterance(JSONObject jsonObject) {

        }

        @Override
        public void onResult(JSONObject jsonObject) {

        }

        @Override
        public void onRetry(JSONObject jsonObject) {

        }
    };

    public MainActivity(){
        super(robotCallback, robotListenCallback);
    }

    private void initViewElement(){
        ip = (TextView) findViewById(R.id.ip);
        port = (TextView) findViewById(R.id.port);
        ip2 = (TextView) findViewById(R.id.ip2);
        port2 = (TextView) findViewById(R.id.port2);
    }

}