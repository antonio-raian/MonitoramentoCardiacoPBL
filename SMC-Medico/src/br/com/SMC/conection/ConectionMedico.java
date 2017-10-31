/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SMC.conection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author Antonio Raian
 */
//Classe responsável por fazer a conexão entre as telas e o Servidor
public class ConectionMedico {
    private Socket serverTCP; //Objeto responsável pela conexão com o servidor via TCP
    private ObjectOutputStream saidaTCP; //Objeto que fica esperando informação na saída do Cliente via TCP
    private ObjectInputStream entradaTCP; //objeto para a entrada de informações do cliente via TCP
    private DatagramSocket serverUDP; //Objeto responsável pela conexão com o servidor via UDP
    private byte[] saidaUDP; //Objeto que envia informação para o servidor via UDP
    private final byte[] entradaUDP = new byte[1024]; //Objeto que recebe informação do servidor via UDP
    private final String endereco;//Endereço do host de conexão
    private final int porta;//Porta de conexão
    
    //Construtor que recebe as informações da conexão
    public ConectionMedico(String endereco, int porta) {
        this.endereco = endereco;
        this.porta = porta;
    }
    
    //O meio de comunicação com o servidor são sempre Strings com o formato:
    //SOLICITANTE#COMANDO#DADOS, se houver mais de um dado eles são enviados separados por "#"
    //Metodo que solicita a adição de um novo médico no sistema
    public String salvarMedico (String nome, String login, String senha) throws IOException{        
        conectaUDP();
        saidaUDP = ("MEDICO#SALVAR#"+nome+"#"+login+"#"+senha).getBytes();//Concatena todas as informações numa String e manda ao servidor
        //Cria um pacote para envio ao servidor
        DatagramPacket sendInfo = new DatagramPacket(saidaUDP, saidaUDP.length, InetAddress.getByName(endereco), porta);
        serverUDP.send(sendInfo);//Envia o pacote ao servidor
        serverUDP.receive(sendInfo);//Espera uma resposta
        
        return new String(sendInfo.getData(), 0, sendInfo.getLength()); // retorna informação pra View
    }
    
    //Metodo para fazer login no sistema, verifica a existencia e a exatidão do logine senha informado
    public boolean autentica (String login, String senha) throws IOException, ClassNotFoundException{
        conectarTCP();
        saidaTCP.writeObject("MEDICO#LOGIN#"+login+"#"+senha);//Concatena as informações e as envia para o servidor
        entradaTCP = new ObjectInputStream(serverTCP.getInputStream());//recebe informação advinda do servidor
        String s = (String) entradaTCP.readObject();//"quebra" a informação do servidor
        desconectar();//Desconecta
        if (s.equals("$LOGON$")){//Se a resposta for LOGON
            return true;//Valida como verdadeiro as informações de login
        }
        return false;
    }
    
    //Metodo que solicita do servidor a lista de pacientes prioritários
    public String listaPrioritarios() throws IOException{
        conectaUDP();
        saidaUDP =("MEDICO#LISTAR_PRIORITARIOS#").getBytes();//Solicita a lista de prioritários ao Servidor
        //Cria um pacote para envio ao servidor
        DatagramPacket sendInfo = new DatagramPacket(saidaUDP, saidaUDP.length, InetAddress.getByName(endereco), porta);
        serverUDP.send(sendInfo);//Envia o pacote ao servidor
        sendInfo = new DatagramPacket(entradaUDP, entradaUDP.length, InetAddress.getByName(endereco), porta);
        serverUDP.receive(sendInfo);//Espera uma resposta
        
        return new String(sendInfo.getData(), 0, sendInfo.getLength()); // retorna informação pra View
    }
    
    //Metodo que solicita as informações de um paciente a partir de um nick
    public String getPaciente (String nick) throws IOException{
        conectaUDP();
        saidaUDP =("MEDICO#GET_PACIENTE#"+nick+"#").getBytes();//Solicita a lista de prioritários ao Servidor
        //Cria um pacote para envio ao servidor
        DatagramPacket sendInfo = new DatagramPacket(saidaUDP, saidaUDP.length, InetAddress.getByName(endereco), porta);
        serverUDP.send(sendInfo);//Envia o pacote ao servidor
        sendInfo = new DatagramPacket(entradaUDP, entradaUDP.length, InetAddress.getByName(endereco), porta);
        serverUDP.receive(sendInfo);//Espera uma resposta
        
        return new String(sendInfo.getData(), 0, sendInfo.getLength()); // retorna informação pra View
    }
    
    public String getBordaPaciente(String nick) throws SocketException, IOException {
        conectaUDP();
        saidaUDP =("MEDICO#GET_BORDA#"+nick+"#").getBytes();//Solicita a lista de prioritários ao Servidor
        //Cria um pacote para envio ao servidor
        DatagramPacket sendInfo = new DatagramPacket(saidaUDP, saidaUDP.length, InetAddress.getByName(endereco), porta);
        serverUDP.send(sendInfo);//Envia o pacote ao servidor
        sendInfo = new DatagramPacket(entradaUDP, entradaUDP.length, InetAddress.getByName(endereco), porta);
        serverUDP.receive(sendInfo);//Espera uma resposta
        
        return new String(sendInfo.getData(), 0, sendInfo.getLength()); // retorna informação pra View
    }
    //----------------------------------------------------------
    //Metodo responsavel por fazer conexão com servidor
    public void conectarTCP() throws IOException{
        serverTCP = new Socket(endereco,porta);
        saidaTCP = new ObjectOutputStream(serverTCP.getOutputStream());
    }
    
    public void conectaUDP() throws SocketException{
        serverUDP = new DatagramSocket();
    }
    
    //Metodo responsavel por fechar conexão com o servidor
    public void desconectar() throws IOException{
        saidaTCP.close();
        entradaTCP.close();
        serverTCP.close();
    }
}
