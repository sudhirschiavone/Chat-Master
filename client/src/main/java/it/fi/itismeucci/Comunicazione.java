package it.fi.itismeucci;

import java.io.*;
import java.net.*;

import javax.xml.transform.SourceLocator;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Comunicazione extends Thread{
    
    Socket Server;
    Messaggio messaggio;
    BufferedReader tastiera;
    ObjectMapper objectMapper = new ObjectMapper();
    String MessageAsString;
    DataOutputStream outVersoServer;
    static boolean nick = true;
    static String nickname;
    static boolean chiudi = true;

    public Comunicazione(Socket Server){    //Costruttore che collega il Client al Thread di comunicazione 
        this.Server = Server;
    }

    public void run(){

        try{
            tastiera = new BufferedReader(new InputStreamReader(System.in)); 
            outVersoServer = new DataOutputStream(Server.getOutputStream());

            while(nick == true){
                System.out.println("Inserisci il tuo nickname");
                Messaggio m = new Messaggio();
                m.setID(0);
                m.setNome(tastiera.readLine());
                String MessageAsString = objectMapper.writeValueAsString(m);    //Si invia solo il nickanme al server
                outVersoServer.writeBytes(MessageAsString + "\n"); 
                try {  Thread.sleep(2000);  } catch (Exception e) {   }
            }

            while(chiudi){

                Messaggio message = new Messaggio();

                System.out.println("=====MENU=====");
                System.out.println("1 per visualizzare la lista completa dei partecipanti");    //Sintassi: 1
                System.out.println("2 per comunicare con un partecipante scelto");              //Sintassi: 2 nome messaggio
                System.out.println("3 per inviare un messaggio a tutti i partecipanti");        //Sintassi: 3 messaggio
                System.out.println("4 per uscire dalla chat");                                  //Sintassi: 4

                System.out.println("Inserisci il codice");
                message.setID(Integer.parseInt(tastiera.readLine()));   //Si inserisce il comando da tastiera
                message.setNome(nickname);      //Si imposta il nickname di base immesso in precedenza

                if(message.getID() == 1){       //Messaggio per aver scelto il comando 1
                    System.out.println("Lista dei client:");
                }
                else if(message.getID() == 3){      //Messaggio per aver scelto il comando 3
                    System.out.println("Inserisci il testo del tuo messaggio");
                    message.setTesto(tastiera.readLine());
                }
                else if(message.getID() == 4){      //Messaggio per aver scelto il comando 4
                    System.out.println("Disconnessione");
                }
                else{       //Messaggio per aver scelto il comando 2 
                   
                    System.out.println("Inserisci il nome del destinatario");
                    message.setDestinatario(tastiera.readLine());           //Si inserisce il destinatario del messaggio da tastiera 
                    System.out.println("Inserisci il testo del tuo messaggio");
                    message.setTesto(tastiera.readLine());          //Si inserisce da tastiera il messaggio da inviare
                }
                
                String MessageAsString = objectMapper.writeValueAsString(message);    //Si trasforma il Pojo in una stringa da deserializzare
                outVersoServer.writeBytes(MessageAsString + "\n");     //Si invia il messaggio trasformato in stringa al server
                try {  Thread.sleep(2000);  } catch (Exception e) {   }     //Tempo di attesa del Thread di 2 secondi 
            }

        }
        catch(Exception e){
            System.out.println("Errore nell'invio del messaggio"+e.getMessage());  //Messaggio in caso di errore
        }
    }


}
