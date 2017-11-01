/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SMCSensores.conection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author raymendesjr
 */
//Classe resopnsável por fazer a conexão das telas com o servidor
public class ConectionSensor{
    private Socket serverTCP; //Objeto responsável pela conexão com o servidor via TCP
    private ObjectOutputStream saidaTCP; //Objeto que fica esperando informação na saída do Cliente via TCP
    private ObjectInputStream entradaTCP; //objeto para a entrada de informações do cliente via TCP
    private DatagramSocket serverUDP; //Objeto responsável pela conexão com o servidor
    private byte[] saidaUDP; //Objeto que envia informação para o servidor
    private final byte[] entradaUDP = new byte[1024]; //Objeto que recebe informação do servidor
    private InetAddress endBorda; //Variavel q armazena o endereço da conexão com a borda
    private int portaBorda;//Variável que armazena a porta da conexão de borda
    private InetAddress enderecoNuvem; //Variavel q armazena o endereço da conexão com a nuvem
    private int portaNuvem;//Variável que armazena a porta da conexão da nuvem

    //Construtor que recebe as informações de endereço e porta de conexão;
    public ConectionSensor(String endereco, int porta) throws UnknownHostException {
        this.enderecoNuvem = InetAddress.getByName(endereco);//Pega o endereço do host de conexão
        this.portaNuvem = porta;//Seta a porta numa variavel global
    }
    
    //Construtor que recebe as informações de endereço e porta de conexão;
    public ConectionSensor(String enderecoBorda, int portaBorda, String enderecoNuvem, int portaNuvem) throws UnknownHostException {
        this.endBorda = InetAddress.getByName(enderecoBorda);//Pega o endereço do host de conexão
        this.portaBorda = portaBorda;//Seta a porta numa variavel global
        this.enderecoNuvem = InetAddress.getByName(enderecoNuvem);//Pega o endereço do host de conexão
        this.portaNuvem = portaNuvem;
    }
    
    //O meio de comunicação com o servidor são sempre Strings com o formato:
    //SOLICITANTE#COMANDO#DADOS, se houver mais de um dado eles são enviados separados por "#"
    //Metodo que solicita o armazenamento de um novo paciente no sistema
    public String salvarSensor (String nick, String nome, String senha) throws IOException, ClassNotFoundException{
        //Concatena todas as infomações numa String e a transforma em uma cadeia de bytes
        saidaUDP = ("SENSOR#SALVAR#"+nick+"#"+nome+"#"+senha).getBytes();
        //Cria um pacote para envio ao servidor
        DatagramPacket sendInfo = new DatagramPacket(saidaUDP, saidaUDP.length, enderecoNuvem, portaNuvem);
        serverUDP.send(sendInfo);//Envia o pacote ao servidor
        serverUDP.receive(sendInfo);//Espera uma resposta
        
        return new String(sendInfo.getData(),0,sendInfo.getLength()); // retorna informação pra View
    }
    
    //Metodo que solicita a atualização de um paciente no sistema
    public String atualizarSensor (String nick, String nome, int move, int ritmo, int sistole, int diastole) throws IOException, ClassNotFoundException{
        //Concatena todas as infomações numa String e a transforma em uma cadeia de bytes
        saidaUDP = ("SENSOR#ATUALIZAR#"+nick+"#"+nome+"#"+move+"#"+ritmo+"#"+sistole+"#"+diastole).getBytes();
        //Cria um pacote para envio ao servidor
        DatagramPacket sendInfo = new DatagramPacket(saidaUDP, saidaUDP.length, endBorda, portaBorda);
        serverUDP.send(sendInfo);//Envia o pacote ao servidor
        serverUDP.receive(sendInfo);//Espera uma resposta
        
        return new String(sendInfo.getData(),0,sendInfo.getLength()); // retorna informação pra View
    }
    
    //Metodo que solicita uma lista de todos os pacientes do sistema
    public String listarSensores () throws IOException, ClassNotFoundException{
        //Concatena as informações necessárias para coletar a lista
        saidaUDP = ("SENSOR#LISTAR#").getBytes();
        //Cria um pacote para envio ao servidor
        DatagramPacket sendInfo = new DatagramPacket(saidaUDP, saidaUDP.length, endBorda, portaBorda);
        serverUDP.send(sendInfo);//Envia o pacote ao servidor
        sendInfo = new DatagramPacket(entradaUDP, entradaUDP.length);
        serverUDP.receive(sendInfo);//Espera uma resposta
                System.out.println(new String(sendInfo.getData(),0,sendInfo.getLength()));
        return new String(sendInfo.getData(),0,sendInfo.getLength()); // retorna informação pra View
    }
    
    //Metodo que solicita um paciente a partir de um nick
    public String getPaciente (String nick) throws IOException{
        //Concatena as informações necessárias para coletar o paciente pelo nick
        saidaUDP = ("SENSOR#GET_PACIENTE#"+nick).getBytes();
        //Cria um pacote para envio ao servidor
        DatagramPacket sendInfo = new DatagramPacket(saidaUDP, saidaUDP.length, endBorda, portaBorda);
        serverUDP.send(sendInfo);//Envia o pacote ao servidor
        sendInfo = new DatagramPacket(entradaUDP, entradaUDP.length);
        serverUDP.receive(sendInfo);//Espera uma resposta
        
        return new String(sendInfo.getData(),0,sendInfo.getLength()); // retorna informação pra View
    }
    
    //Metodo responsável por mudar o sensor de lugar
    public String mudarCoordenadas(String nick, String coordenadaX, String coordenadaY) throws IOException, ClassNotFoundException{
        serverTCP = new Socket(enderecoNuvem, portaNuvem);//Usa conexão TCP
        saidaTCP = new ObjectOutputStream(serverTCP.getOutputStream());
        saidaTCP.writeObject("SENSOR#MUDARCOORDENADA#"+nick+"#"+coordenadaX+"#"+coordenadaY);//Concatena as novas coordenadas e manda pra núvem
        entradaTCP = new ObjectInputStream(serverTCP.getInputStream());
        return (String)entradaTCP.readObject();//Retorna uma borda ou null (nesse caso usamos a nuvem como servidor)
    }
    
    //Metodo que envia as informaçãos de autenticação
    public String autentica(String nick, String senha, String coordenadaX, String coordenadaY) throws IOException, ClassNotFoundException{
        serverTCP = new Socket(enderecoNuvem, portaNuvem);//Usa conexão TCP
        saidaTCP = new ObjectOutputStream(serverTCP.getOutputStream());
        //Envia as informação do paciente e as coordenadas para aloca-lo a uma borda
        saidaTCP.writeObject("SENSOR#AUTENTICA#"+nick+"#"+senha+"#"+coordenadaX+"#"+coordenadaY);
        entradaTCP = new ObjectInputStream(serverTCP.getInputStream());
        return (String)entradaTCP.readObject();//Retorna uma borda ou null (nesse caso usamos a nuvem como servidor)
    }
    //----------------------------------------------------------
    //Metodo responsavel por fazer conexão com servidor
    public void conectar() throws IOException{
        serverUDP = new DatagramSocket();
    }
    
    //Metodo responsavel por fechar conexão com o servidor
    public void desconectar() throws IOException{
        serverUDP.close();
    }
}
