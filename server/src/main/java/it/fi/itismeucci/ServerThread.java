package it.fi.itismeucci;

import java.io.*;
import java.net.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerThread extends Thread
{
    ServerSocket server = null;
    Socket client;
    BufferedReader inDalCliente;
    DataOutputStream outVersoClient;
    ObjectMapper objectMapper = new ObjectMapper();


    public ServerThread(Socket s){
        client = s;
    }

    public void run(){      //Viene affidata la comunicazione al Thread 

        try{

            inDalCliente = new BufferedReader(new InputStreamReader (client.getInputStream()));     //Inizializzazione periferica per lettura messaggi dal client 
            outVersoClient = new DataOutputStream(client.getOutputStream());        //Inizializzazione periferica per inviare al client 

                System.out.println("Inoltro della lista degli acquisti");       //Messaggio per informare dell'inoltro della lista acquisti
                
                System.out.println("Grazie per aver usufruito del servizio");       //Saluto da parte del server e chiusura comunicazioni
            }
        catch(Exception e){     //Errore
            System.out.println(e.getMessage());
            System.out.println("Errore durante l'istanza del server");
            System.exit(1);
        }

    }
        
}
