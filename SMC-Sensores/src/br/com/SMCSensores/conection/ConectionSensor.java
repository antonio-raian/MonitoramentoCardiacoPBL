/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SMCSensores.conection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author raymendesjr
 */
//Classe resopnsável por fazer a conexão das telas com o servidor
public class ConectionSensor{
    private DatagramSocket server; //Objeto responsável pela conexão com o servidor
    private byte[] saida; //Objeto que envia informação para o servidor
    private final byte[] entrada = new byte[1024]; //Objeto que recebe informação do servidor
    private final InetAddress endereco; //Variavel q armazena o endereço da conexão
    private final int porta;//Variável que armazena a porta da conexão

    //Construtor que recebe as informações de endereço e porta de conexão;
    public ConectionSensor(String host, int porta) throws UnknownHostException {
        this.endereco = InetAddress.getByName(host);//Pega o endereço do host de conexão
        this.porta = porta;//Seta a porta numa variavel global
    }
    
    //O meio de comunicação com o servidor são sempre Strings com o formato:
    //SOLICITANTE#COMANDO#DADOS, se houver mais de um dado eles são enviados separados por "#"
    //Metodo que solicita o armazenamento de um novo paciente no sistema
    public String salvarSensor (String nick, String nome, int move, int ritmo, int sistole, int diastole) throws IOException, ClassNotFoundException{
        //Concatena todas as infomações numa String e a transforma em uma cadeia de bytes
        saida = ("SENSOR#SALVAR#"+nick+"#"+nome+"#"+move+"#"+ritmo+"#"+sistole+"#"+diastole).getBytes();
        //Cria um pacote para envio ao servidor
        DatagramPacket sendInfo = new DatagramPacket(saida, saida.length, endereco, porta);
        server.send(sendInfo);//Envia o pacote ao servidor
        server.receive(sendInfo);//Espera uma resposta
        
        return new String(sendInfo.getData(),0,sendInfo.getLength()); // retorna informação pra View
    }
    
    //Metodo que solicita a atualização de um paciente no sistema
    public String atualizarSensor (String nick, String nome, int move, int ritmo, int sistole, int diastole) throws IOException, ClassNotFoundException{
        //Concatena todas as infomações numa String e a transforma em uma cadeia de bytes
        saida = ("SENSOR#ATUALIZAR#"+nick+"#"+nome+"#"+move+"#"+ritmo+"#"+sistole+"#"+diastole).getBytes();
        //Cria um pacote para envio ao servidor
        DatagramPacket sendInfo = new DatagramPacket(saida, saida.length, endereco, porta);
        server.send(sendInfo);//Envia o pacote ao servidor
        server.receive(sendInfo);//Espera uma resposta
        
        return new String(sendInfo.getData(),0,sendInfo.getLength()); // retorna informação pra View
    }
    
    //Metodo que solicita uma lista de todos os pacientes do sistema
    public String listarSensores () throws IOException, ClassNotFoundException{
        //Concatena as informações necessárias para coletar a lista
        saida = ("SENSOR#LISTAR#").getBytes();
        //Cria um pacote para envio ao servidor
        DatagramPacket sendInfo = new DatagramPacket(saida, saida.length, endereco, porta);
        server.send(sendInfo);//Envia o pacote ao servidor
        sendInfo = new DatagramPacket(entrada, entrada.length);
        server.receive(sendInfo);//Espera uma resposta
                System.out.println(new String(sendInfo.getData(),0,sendInfo.getLength()));
        return new String(sendInfo.getData(),0,sendInfo.getLength()); // retorna informação pra View
    }
    
    //Metodo que solicita um paciente a partir de um nick
    public String getPaciente (String nick) throws IOException{
        //Concatena as informações necessárias para coletar o paciente pelo nick
        saida = ("SENSOR#GET_PACIENTE#"+nick).getBytes();
        //Cria um pacote para envio ao servidor
        DatagramPacket sendInfo = new DatagramPacket(saida, saida.length, endereco, porta);
        server.send(sendInfo);//Envia o pacote ao servidor
        sendInfo = new DatagramPacket(entrada, entrada.length);
        server.receive(sendInfo);//Espera uma resposta
        
        return new String(sendInfo.getData(),0,sendInfo.getLength()); // retorna informação pra View
    }
    //----------------------------------------------------------
    //Metodo responsavel por fazer conexão com servidor
    public void conectar() throws IOException{
        server = new DatagramSocket();
    }
    
    //Metodo responsavel por fechar conexão com o servidor
    public void desconectar() throws IOException{
        server.close();
    }
}
