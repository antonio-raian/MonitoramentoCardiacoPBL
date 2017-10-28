/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SMCServidorBorda.conection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Antonio Raian
 */
public class Conection {
   private Socket server; //Objeto responsável pela conexão com o servidor
    private ObjectOutputStream saida; //Objeto que fica esperando informação na saída do Cliente
    private ObjectInputStream entrada; //objeto para a entrada de informações do cliente
    private final String endereco;//Endereço do host de conexão
    private final int porta;//Porta de conexão
    
    //Construtor que recebe as informações da conexão
    public Conection(String endereco, String porta) {
        this.endereco = endereco;
        this.porta = Integer.parseInt(porta);
    }
    
    public String novaBorda(String host, String porta, String coordenadaX, String coordenadaY) throws IOException, ClassNotFoundException{
        conectar();
        saida = new ObjectOutputStream(server.getOutputStream());
        saida.writeObject("SERVER#NOVABORDA#"+host+"#"+porta+"#"+coordenadaX+"#"+coordenadaY);
        entrada = new ObjectInputStream(server.getInputStream());
        return (String) entrada.readObject();
    }
    
    public String removeBorda(String host) throws IOException, ClassNotFoundException{
        conectar();
        saida = new ObjectOutputStream(server.getOutputStream());
        saida.writeObject("SERVER#REMOVEBORDA#"+host);
        entrada  = new ObjectInputStream(server.getInputStream());
        return (String) entrada.readObject();
    }
    
    //----------------------------------------------------------
    //Metodo responsavel por fazer conexão com servidor
    public void conectar() throws IOException{
        server = new Socket(endereco,porta);
    }
    
    //Metodo responsavel por fechar conexão com o servidor
    public void desconectar() throws IOException{
        saida.close();
        entrada.close();
        server.close();
    }
}
