package it.fi.itismeucci;

import java.io.*;
import java.net.*;
import com.fasterxml.jackson.databind.ObjectMapper;
public class Client extends Thread{
    
    String nomeServer = "localhost";
    BufferedReader tastiera;
    int portaServer = 6789;
    Socket socket;
    Messaggio messaggio;    
    ObjectMapper objectMapper;

    DataOutputStream outVersoServer;
    public Socket connetti(){
        try{

            socket = new Socket(nomeServer, portaServer);       //Si inizializza il socket del client 

            Thread ricezione = new Ricezione(socket);           //Si passa la ricezione del client ad un Thread specifico 
            ricezione.start();

            Thread comunicazione = new Comunicazione(socket);   //Si passa la comunicazione del client ad un Thread specifico 
            comunicazione.start();
            
            System.out.println("Client partito");               //Messaggio di avvio

        }

        catch(UnknownHostException e){
            System.err.println("Host sconosciuto"); //Messaggio di errore in caso di anomalia sconosciuta 
        }

        catch (Exception e){
            System.out.println("Errore di comunicazione");  //Messaggio di errore in caso anomalia nella comunicazione 
        }
        return socket;
    }
}
