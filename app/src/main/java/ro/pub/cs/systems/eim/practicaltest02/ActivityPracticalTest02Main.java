package ro.pub.cs.systems.eim.practicaltest02;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityPracticalTest02Main extends AppCompatActivity {

    private EditText ClientCmd;
    private EditText ServerPort;
    private Button ClientStart;
    private Button ServerStart;
    private TextView Result;

    private ServerThread serverThread;
    private ClientThread clientThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        ClientCmd = findViewById(R.id.client_cmd_edit_text);
        ServerPort = findViewById(R.id.server_port_edit_text);
        ClientStart = findViewById(R.id.client_connect_button);
        ServerStart = findViewById(R.id.connect_button);
        Result = findViewById(R.id.result);

        ClientStart.setOnClickListener(new ConnectClientClickListener());
        ServerStart.setOnClickListener(new ConnectServerClickListener());

    }
    private class ConnectClientClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String command = ClientCmd.getText().toString();
            if (command == null || command.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Command should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            clientThread = new ClientThread(command,Result);
            clientThread.startClient();
        }

    }
    private class ConnectServerClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = ServerPort.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.startServer();
        }

    }
}
