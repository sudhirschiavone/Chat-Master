package it.fi.itismeucci;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Ricezione extends Thread{
    
    Socket Client;
    Messaggio messaggio;
    ObjectMapper objectMapper = new ObjectMapper();
    BufferedReader inDalServer;

    public Ricezione(Socket client){    //Costruttore che collega il Client al Thread di ricezione 
        Client = client;

        try{
            inDalServer = new BufferedReader(new InputStreamReader (Client.getInputStream()));  //Si prende il messaggio del server
        }
        catch(Exception e){
            System.out.println("Errore nella lettura del messaggio del server");    //Messaggio di errore nella lettura messaggio
        }
    }

    public void run(){

        try {

            while(Comunicazione.chiudi){
                String json = inDalServer.readLine();   //Si riceve e si "legge" il messaggio del server come stringa
                Messaggio messaggio = objectMapper.readValue(json,  Messaggio.class);  //Si deserializza il messaggio del Server
                
                if(messaggio.getID() == 0){
                    System.out.println("nickname accettato");
                    Comunicazione.nick = false;     //Si mette il nickname a false per non farlo inserire nuovamente 
                    Comunicazione.nickname = messaggio.getDestinatario();   //Si imposta il proprio nickname di base 
                }
                else if(messaggio.getID() == 1){
                    String[] nomi = messaggio.getTesto().split("///");      //Crea un array con tutti i nomi dei client separati dal testo "///"
                    for (int i = 0; i < nomi.length; i++) {     //Ciclo su tutti i nomi
                        System.out.println("-" + nomi[i]);     //Stampa della lista dei nomi
                    }
                }
                else if(messaggio.getID() == 4){
                    Comunicazione.chiudi = false;       //Si cambia la variabile così da poter chiudere la comunicazione
                }
                else if(messaggio.getID() == 3){
                    System.out.println("====================");
                    System.out.println("messaggio pubblico: \n" + messaggio.getNome() + ": " + messaggio.getTesto());   //Si comunica il tipo di messaggio, il mittente ed il testo
                    System.out.println("====================");
                }
                else{
                    System.out.println("====================");
                    System.out.println("messaggio Arrivato: \n" + messaggio.getNome() + ": " + messaggio.getTesto());   //Si comunica il tipo di messaggio, il mittente ed il testo
                    System.out.println("====================");
                }
            }
            
        } 
        catch (Exception e){
            System.out.println("Errore nella ricezione del messaggio"); //Messaggio in caso di errore
        }

    }

}
