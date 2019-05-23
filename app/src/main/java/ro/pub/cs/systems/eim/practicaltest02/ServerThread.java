package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread {
    private ServerSocket serverSocket;
    private HashMap<String, String> data = new HashMap<>();

    public ServerThread(int port) {
        try {
            serverSocket = new ServerSocket(port);

        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void startServer() {
        start();
        Log.v(Constants.TAG, "server started");
    }
    public void stopThread() {
        interrupt();
        if (serverSocket != null) {
            try {
                serverSocket.close();
                Log.v(Constants.TAG, "Server closed");
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
    }
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Log.i(Constants.TAG, "[SERVER THREAD] Waiting for a client invocation...");
                Socket socket = serverSocket.accept();
                Log.i(Constants.TAG, "[SERVER THREAD] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());


                CommunicationThread communicationThread = new CommunicationThread( socket, this);
                communicationThread.start();
            }
        } catch (Exception clientProtocolException) {
            Log.e(Constants.TAG, "[SERVER THREAD] An exception has occurred: " + clientProtocolException.getMessage());
            if (Constants.DEBUG) {
                clientProtocolException.printStackTrace();
            }
        }
    }

    public synchronized void putData(String key, String value) {
        this.data.put(key, value);
    }

    public synchronized HashMap<String, String> getData() {
        return data;
    }

}
