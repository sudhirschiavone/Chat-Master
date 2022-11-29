package it.fi.itismeucci;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread{
    ServerSocket server = null;
    Socket client;
    BufferedReader inDalCliente;
    DataOutputStream outVersoClient;
    private static ArrayList<Thread> threads = new ArrayList<Thread>(); 
    
    public Socket attendi(){
        try {
            System.out.print("Server partito");
            server = new ServerSocket(6789);
            client=server.accept();
            Thread t= new ServerThread(client);
            threads.add(t);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return client;
    }
}