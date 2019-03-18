package com.example.supremumeinzelaufgabe;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

public class SocketClientThread implements Runnable{
    String request;
    String response;

    public SocketClientThread(String request) {
        this.request = request;
    }

    @Override
    public void run() {
        Socket clientSocket = null;
        try {
            // client socket, connect to server
            InetAddress address = InetAddress.getByName(new URL("https://se2-isys.aau.at/").getHost());
            clientSocket = new Socket(address, 53212);

            // output stream attached to socket
            DataOutput outToServer = new DataOutputStream(clientSocket.getOutputStream());

            // input stream attached to socket
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // send line to server
            outToServer.writeBytes(request + '\n');

            // read line from server
            this.response = inFromServer.readLine();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getResponse() {
        return response;
    }
}
