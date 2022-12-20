package it.fi.itismeucci;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Client client = new Client();       //Si crea un nuovo client 
        client.connetti();                  //Si connette al Server (e passa la comunicazioen e ricezione a due Thread distinti)
    }
}
