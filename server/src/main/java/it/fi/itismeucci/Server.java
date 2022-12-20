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
    public static ArrayList<Socket> sockets = new ArrayList<Socket>();     //Array contenente tutti i Thread che lavorano ai singoli client
    public static ArrayList<String> lista = new ArrayList<String>();    //Array con nomi dei client connessi 

    public Socket attendi(){
        try {
            System.out.print("Server partito\n");     //Comunicazione per inizio 
            server = new ServerSocket(6789);        //Si associa la porta al serverSocket 
            while(true){
            
                client=server.accept();                 //Si fa connettere un client al server
                lista.add(" ");     //Si aggiunge alla lista dei nomi uno spazio vacante che sarà rimpiazzato con il nome del client
                sockets.add(client);     //Si aggiunge il socket del client connesso alla lista dei socket
                Thread t= new ServerThread(client, lista.size() -1);     //Si crea un Thread a cui dare il client appena connesso
                t.start();      //Si avvia il Thread (Quindi la comunicazione)
            }          
        } catch (Exception e) {
            System.out.println(e.getMessage());     //Comunicazione d'errore
        }
        return client;
    }
}