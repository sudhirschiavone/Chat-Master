package it.fi.itismeucci;


import com.fasterxml.jackson.databind.ObjectMapper;

public class Messaggio {

    ObjectMapper objectMapper = new ObjectMapper();

    int ID = 0;
    String nome; 
    String destinatario;
    String testo;

    //GETTER

    public int getID() {
        return ID;
    }

    public String getNome() {
        return nome;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getTesto() {
        return testo;
    }

    //SETTER

    public void setID(int iD) {
        ID = iD;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
    
    public void setTesto(String testo) {
        this.testo = testo;
    }

    //COSTRUTTORE

    public Messaggio(int iD, String nome, String destinatario, String testo) {
        ID = iD;
        this.nome = nome;
        this.destinatario = destinatario;
        this.testo = testo;
    }

    public Messaggio (){}

    @Override
    public String toString() {
        return "Messaggio [ID=" + ID + ", nome=" + nome + ", destinatario=" + destinatario + ", testo=" + testo + "]";
    }

}
