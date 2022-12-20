package it.fi.itismeucci;

public class App 
{
    public static void main( String[] args )
    {
        Server server = new Server();   //Si crea il server
        server.attendi();               //Si mette il server in attesa, così da far connettere ad esso tutti i client
    }
}
