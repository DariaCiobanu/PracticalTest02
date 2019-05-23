package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    private String command;
    private TextView result;
    public ClientThread(String command, TextView result){
        this.command = command;
        this.result = result;
    }
    public void startClient() {

        start();
        Log.v(Constants.TAG, "client started");
    }
    @Override
    public void run() {
        try {
            //create socket
            socket = new Socket("localhost", 5000);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            //write params
            printWriter.println(command);
            printWriter.flush();

            String resultText;

            //read response
            while ((resultText = bufferedReader.readLine()) != null) {
                final String finalizedWeatherInformation = resultText;
                result.post(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(finalizedWeatherInformation);
                    }
                });
            }


        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
