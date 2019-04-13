package com.example.prady.walkytalky;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class Client implements Serializable {

    Socket clientSocket;
    DataInputStream din;
    DataOutputStream dout;


    public Client(String IP, int PORT) {
        try {
            clientSocket = new Socket(IP, PORT);
            din = new DataInputStream(clientSocket.getInputStream());
            dout = new DataOutputStream(clientSocket.getOutputStream());
            dout.writeUTF("Poloman");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage() throws IOException
    {
        return din.readUTF();
    }

    public boolean isConnected()
    {
        return clientSocket.isConnected();
    }
}
