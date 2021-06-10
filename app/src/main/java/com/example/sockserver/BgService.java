package com.example.sockserver;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BgService extends Service {

    private static String TAG = "com.example.sockserver";
    public static String ServerIP = "";
    Thread myThread;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Inside onStartCommand of the BGService...");

        Toast.makeText(this, "BGService started by user.", Toast.LENGTH_LONG).show();
        myThread = new Thread(new BgService.MyServerThread());
        myThread.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Inside onDestroy of the BGService...");
        Toast.makeText(this, "Service destroyed by user.", Toast.LENGTH_LONG).show();
    }


    class MyServerThread implements Runnable {

        Socket s;
        ServerSocket ss;
        InputStreamReader isr;
        BufferedReader br;
        String message;

        Handler h = new Handler();

        @Override
        public void run() {
            try {
                ServerIP = getLocalIpAddress();
                Log.i(TAG, ServerIP);
                //InetAddress inetAddress = InetAddress.getByName(getLocalIpAddress());
                InetAddress inetAddress = InetAddress.getByName("10.0.2.15");  //Working IP for EMULATOR -- 8/6/2021
                Log.i(TAG, inetAddress.getHostAddress());

                ss = new ServerSocket(8086, 0, inetAddress);
                Log.i(TAG, String.valueOf(ss));

                while (true)
                {
                    Log.i(TAG, "Before accept");
                    s = ss.accept();
                    Log.i(TAG, "After accept");
                    Log.i(TAG, String.valueOf(s));
                    isr = new InputStreamReader(s.getInputStream());
                    br = new BufferedReader(isr);
                    message = br.readLine();

                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, message);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    });

                    s.close();
                }
            } catch(IOException e)
            {
                e.printStackTrace();
            }

        }
    }

    private String getLocalIpAddress() throws UnknownHostException {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        assert (wifiManager) != null;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipInt = wifiInfo.getIpAddress();
        return InetAddress.getByAddress(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array()).getHostAddress();
    }

}
