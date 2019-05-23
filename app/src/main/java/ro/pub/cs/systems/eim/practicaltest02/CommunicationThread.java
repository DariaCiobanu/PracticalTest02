package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class CommunicationThread extends Thread {
    private Socket socket;
    private ServerThread serverThread;
    private String command;

    public CommunicationThread(Socket socket, ServerThread serverThread) {
        this.socket = socket;
        this.serverThread = serverThread;
        //this.command = command;
    }
    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }

        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            String command = bufferedReader.readLine();
            Log.d(Constants.TAG,"Command is: " +command);
            HashMap<String, String> data = serverThread.getData();


            String[] commandParts = command.split(",");
            if(commandParts[0].equals("put")){
                Log.d(Constants.TAG,command);
                data.put(commandParts[1], commandParts[2]);
            }else if(commandParts[0].equals("get")){
                Log.d(Constants.TAG, "Get: "+command);

                if (data.containsKey(commandParts[1])) {
                    String val = data.get(commandParts[1]);

                    printWriter.println(val);
                    printWriter.flush();
                }
            }

        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
