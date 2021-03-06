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
    
    //Metodo que solicita a adição de uma nova borda na Rede
    public String novaBorda(String host, String porta, String coordenadaX, String coordenadaY) throws IOException, ClassNotFoundException{
        conectar();
        saida = new ObjectOutputStream(server.getOutputStream());
        saida.writeObject("SERVER#NOVABORDA#"+host+"#"+porta+"#"+coordenadaX+"#"+coordenadaY);
        entrada = new ObjectInputStream(server.getInputStream());
        String s = (String) entrada.readObject();
        desconectar();
        return s;
    }
    
    //Metodo que solicita a remoção(desativação) de uma borda da rede
    public String removeBorda(String host) throws IOException, ClassNotFoundException{
        conectar();
        saida = new ObjectOutputStream(server.getOutputStream());
        saida.writeObject("SERVER#REMOVEBORDA#"+host);
        entrada  = new ObjectInputStream(server.getInputStream());
        String s = (String) entrada.readObject();
        desconectar();
        return s;
    }
    
    //Metodo usado para mandar para a nuvem os pacientes em risco
    public String enviaPacientes(String info) throws IOException, ClassNotFoundException{
        conectar();
        saida = new ObjectOutputStream(server.getOutputStream());
        String s = null;
        if(!info.equals("")){
            saida.writeObject("SERVER#SEND#"+info);
        }else{
            saida.writeObject("SERVER#SEND#null");
        }
        entrada  = new ObjectInputStream(server.getInputStream());
        s = (String) entrada.readObject();
        desconectar();
        return s;
    }
    
    //----------------------------------------------------------
    //Metodo responsavel por fazer conexão com servidor
    public void conectar() throws IOException{
        server = new Socket(endereco,porta);
    }
    
    //Metodo responsavel por fechar conexão com o servidor
    public void desconectar() throws IOException{
        saida.close();
        server.close();
    }    
}
