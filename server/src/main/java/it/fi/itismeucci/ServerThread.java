package it.fi.itismeucci;

import java.io.*;
import java.net.*;
import java.rmi.ServerError;
import java.rmi.server.ServerRef;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerThread extends Thread
{
    ServerSocket server = null;
    Socket client;
    Messaggio messaggio;
    BufferedReader inDalClient;
    DataOutputStream outVersoClient;
    String MessageAsString;
    int scelta;
    ObjectMapper objectMapper = new ObjectMapper();
    public static String nomeClient;
    boolean chiusura = true;
    int posizione = 0;

    public ServerThread(Socket s, int posizione){
        client = s;
        this.posizione = posizione;     //Si passa la posizione del client all'interno della lista dei socket e dei nomi
    }

    public void run(){      
        try{
            outVersoClient = new DataOutputStream(client.getOutputStream());        //Si crea la periferica di output verso il client 
            inDalClient = new BufferedReader(new InputStreamReader (client.getInputStream()));      //Si crea la periferica di input dal client
            System.out.println("un client si è unito alla chat ");       //Messaggio di prova per confermare l'arrivo dei messaggi del client

            while(chiusura){

                String json = inDalClient.readLine();                                   //Si trasforma la stringa ricevuta dal client in una stringa 
                Messaggio messaggio = objectMapper.readValue(json,  Messaggio.class);   //Si deserializza il messaggio del client 

                switch(messaggio.getID()){
                    case 0:
                        boolean presenza = false;       //Si regola la presenza del nome del client a false come base 

                        for(int i = 0; i < Server.lista.size(); i++){       //Ciclo per il controllo del nome del client
                            if(Server.lista.get(i).equals(messaggio.getNome())){        //Controllo la presenza o meno di un nome già esistente
                                presenza = true;        //Controllo della presenza del nickname non appena il client invia il proprio
                            }
                        }
                        System.out.println("Si è connesso "+ messaggio.getNome());
                        if(!presenza){
                            Server.lista.set(posizione, messaggio.getNome());   //Si imposta la posizione ed il nome del client nella lista 
                            messaggio.setID(0);     //Si regola l'ID a 0 solo per il controllo dell'ID
                            messaggio.setDestinatario(messaggio.getNome());     //Si imposta come destinatario del messaggio il client che ha fatto la richiesta del nickname
                            String MessageAsString = objectMapper.writeValueAsString(messaggio);    //Si trasforma il messaggio da inviare in una stringa da deserializzare
                            outVersoClient.writeBytes(MessageAsString + "\n");         //Si inivia la stringa al client
                        }

                    break;

                    case 1:     //Visualizzazione lista partecipanti
                        messaggio.setDestinatario(messaggio.getNome());     //Si imposta come destinatario il client che ha fatto la richiesta
                        String nomi = " ";      //Si crea una stringa che conterrà tutti i nomi dei client connessi
                        for (int i = 0; i < Server.lista.size(); i++) {     //Ciclo di controllo dei nomi dei client
                            nomi += Server.lista.get(i) + " ///";       //Si aggiunge il nome alla stringa separandolo dal successivo con "///"
                        }
                        messaggio.setTesto(nomi);       //Si invia come testo del pojo una stringa contenente tutti i nomi dei client
                        String MessageAsString = objectMapper.writeValueAsString(messaggio);    //Si trasforma il messaggio da inviare in una stringa da deserializzare
                        outVersoClient.writeBytes(MessageAsString + "\n");         //Si inivia la stringa al client

                    break;

                    case 2:     //Comunicazione con un singolo client specifico

                        DataOutputStream outVersoDestinatario;      //Si crea un nuovo stream di output per la comunicazione a singolo o alla chat pubblica

                        for (int i = 0; i < Server.lista.size(); i++) {     //Ciclo di controllo dei nomi dei client 
                            if(Server.lista.get(i).equals(messaggio.getDestinatario())){       //Controllo se il nome della lista corrisponde a quello del destinatario per inviare ad esso il messaggio
                                outVersoDestinatario = new DataOutputStream(Server.sockets.get(i).getOutputStream());       //Si imposta come destinatario del messaggio il socket con la stessa posizioen del client con il nome del destinatario
                                MessageAsString = objectMapper.writeValueAsString(messaggio);    //Si trasforma il messaggio da inviare in una stringa da deserializzare
                                outVersoDestinatario.writeBytes(MessageAsString + "\n");         //Si inivia la stringa al client
                                break;      //Si interrompe la ricerca dopo aver trovato il destinatario scelto
                            }
                        }

                    break;

                    case 3:     //Comunicazione Broadcast 

                        for (int i = 0; i < Server.lista.size(); i++) {     //Ciclo per il controllo di tutti i client connessi
                            outVersoDestinatario = new DataOutputStream(Server.sockets.get(i).getOutputStream());   //Si invia il messaggio a tutti i socket della lista, collegando a loro l'outputStream
                            MessageAsString = objectMapper.writeValueAsString(messaggio);    //Si trasforma il messaggio da inviare in una stringa da deserializzare
                            outVersoDestinatario.writeBytes(MessageAsString + "\n");        //Si invia il messaggio alla chat pubblica
                        }

                    break;

                    case 4:     //Caso di uscita dalla chat 
                        MessageAsString = objectMapper.writeValueAsString(messaggio);    //Si trasforma il messaggio da inviare in una stringa da deserializzare
                        outVersoClient.writeBytes(MessageAsString + "\n");      //Invio di un messaggio vuoto al client per terminare la connessione
                        System.out.println("Termine comunicazione con " + Server.lista.get(posizione));    //Avviso di termine del servizio
                        Server.lista.remove(posizione);     //Si rimuove l'indice della posizione del client disconnesso dall'array contenente i nomi
                        Server.sockets.remove(posizione);   //Si rimuove l'indice della posizione del client disconnesso dall'array contenente i socket dei client
                        chiusura = false;       //Si cambia la variabile così da non effettuare più controlli ed interrompere la comunicazione
                    break;
                }
            }
            client.close();
        }
        catch(Exception e){     //Messaggio di errore durante la comunicazione 
            System.out.println("Errore durante l'invio di un messaggio");
        }
    }    
}
