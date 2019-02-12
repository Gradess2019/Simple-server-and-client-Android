package com.server;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements SocketThreadListener, ServerSocketThreadListener {

    private ServerSocketThread serverSocketThread;
    private EditText etLog;
    @SuppressLint("SimpleDateFormat")
    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss: ");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etLog = (EditText) findViewById(R.id.log);
        start(8189);
    }

    public void start(int port) {
        if (serverSocketThread != null && serverSocketThread.isAlive()) {
            putLog("Server is already running");
            return;
        }
        serverSocketThread = new ServerSocketThread(this, "Server", port, 2000);
    }

    public void stop() {
        if (serverSocketThread == null || !serverSocketThread.isAlive()) {
            putLog("Server is not running");
            return;
        }
        serverSocketThread.interrupt();
    }

    private void putLog(String msg) {
        msg = dateFormat.format(System.currentTimeMillis()) + Thread.currentThread().getName() + ": " + msg;
        Log.d("MyMessage", msg);
        etLog.append(msg + "\n");
    }

    /**
     * Server Socket Thread Listener methods
     */

    @Override
    public void onServerThreadStart(ServerSocketThread thread) {
        putLog("Server started");
    }

    @Override
    public void onServerThreadStop(ServerSocketThread thread) {
        putLog("Server stopped");
    }

    @Override
    public void onServerSocketCreated(ServerSocketThread thread, ServerSocket serverSocket) {
        putLog("Server socket created");
    }

    @Override
    public void onSocketAccepted(ServerSocketThread thread, Socket socket) {
        putLog("Client connected: " + socket);
        String threadName = "SocketThread: " + socket.getInetAddress() + ":" + socket.getPort();
        new SocketThread(this, threadName, socket);
    }

    @Override
    public void onAcceptTimeout(ServerSocketThread thread, ServerSocket serverSocket) {
//        putLog("Server is alive");
    }

    @Override
    public void onServerThreadException(ServerSocketThread thread, Exception e) {
        putLog("Exception: " + e.getClass().getName() + ": " + e.getMessage());
    }

    /**
     * Socket Thread Listener methods
     */

    @Override
    public synchronized void onSocketThreadStart(SocketThread thread, Socket socket) {
        putLog("SocketThread started");
    }

    @Override
    public synchronized void onSocketThreadStop(SocketThread thread) {
        putLog("SocketThread stopped");
    }

    @Override
    public synchronized void onSocketIsReady(SocketThread thread, Socket socket) {
        putLog("SocketThread is ready");
    }

    @Override
    public synchronized void onReceiveString(SocketThread thread, Socket socket, String msg) {
        thread.sendMessage("echo: " + msg);
    }

    @Override
    public synchronized void onSocketThreadException(SocketThread thread, Exception e) {
        putLog("Exception: " + e.getClass().getName() + ": " + e.getMessage());
    }
}
