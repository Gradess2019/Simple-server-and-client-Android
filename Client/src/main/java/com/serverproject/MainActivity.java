package com.serverproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.net.Socket;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, SocketThreadListener{
    private SocketThread socketThread;
    private EditText etMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etMessage = (EditText) findViewById(R.id.etMessage);

        new AsyncRequest().execute(this);

    }

    class AsyncRequest extends AsyncTask<SocketThreadListener, String, String>{

        @Override
        protected String doInBackground(SocketThreadListener... arg) {
            Socket socket = null;
            try {
                socket = new Socket("10.130.204.121", 8189);
            } catch (IOException e) {
                e.printStackTrace();
            }
            socketThread = new SocketThread(arg[0], "Client thread", socket);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }

    @Override
    public void onClick(View v) {
        String msg = etMessage.getText().toString();
        if ("".equals(msg)) return;
        socketThread.sendMessage(msg);
    }

    void putLog(String msg) {
        if ("".equals(msg)) return;
        Log.d("MyMessage", msg);
    }

    /**
     * SocketThread listener methods
     */

    @Override
    public void onSocketThreadStart(SocketThread thread, Socket socket) {
        putLog("Socket start");
    }

    @Override
    public void onSocketThreadStop(SocketThread thread) {
        putLog("Socket stop");
    }

    @Override
    public void onSocketIsReady(SocketThread thread, Socket socket) {
        putLog("Socket ready");

    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        putLog("Receive String");
    }

    @Override
    public void onSocketThreadException(SocketThread thread, Exception e) {
        putLog("SocketException");
    }
}
