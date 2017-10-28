/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SMC.conection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Antonio Raian
 */
//Classe responsável por fazer a conexão entre as telas e o Servidor
public class ConectionMedico {
    private Socket server; //Objeto responsável pela conexão com o servidor
    private ObjectOutputStream saida; //Objeto que fica esperando informação na saída do Cliente
    private ObjectInputStream entrada; //objeto para a entrada de informações do cliente
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
    public String salvarMedico (String nome, String login, String senha) throws IOException, ClassNotFoundException{        
        saida.writeObject("MEDICO#SALVAR#"+nome+"#"+login+"#"+senha);//Concatena todas as informações numa String e manda ao servidor
        
        entrada = new ObjectInputStream(server.getInputStream());//recebe informação advinda do servidor
        String s = (String) entrada.readObject();//"quebra" a informação do servidor
        desconectar();//Desconecta
        return s; // retorna informação pra View
    }
    
    //Metodo para fazer login no sistema, verifica a existencia e a exatidão do logine senha informado
    public boolean autentica (String login, String senha) throws IOException, ClassNotFoundException{
        saida.writeObject("MEDICO#LOGIN#"+login+"#"+senha);//Concatena as informações e as envia para o servidor
        entrada = new ObjectInputStream(server.getInputStream());//recebe informação advinda do servidor
        String s = (String) entrada.readObject();//"quebra" a informação do servidor
        desconectar();//Desconecta
        if (s.equals("$LOGON$")){//Se a resposta for LOGON
            return true;//Valida como verdadeiro as informações de login
        }
        return false;
    }
    
    //Metodo que solicita do servidor a lista de pacientes prioritários
    public String listaPrioritarios() throws IOException, ClassNotFoundException{
        saida.writeObject("MEDICO#LISTAR_PRIORITARIOS#");//Solicita a lista de prioritários ao Servidor
        entrada = new ObjectInputStream(server.getInputStream());//recebe informação advinda do servidor
        String str = (String) entrada.readObject();//"quebra" a informação do servidor
        desconectar();//Desconecta
        return str;
    }
    
    //Metodo que solicita as informações de um paciente a partir de um nick
    public String getPaciente (String nick) throws IOException, ClassNotFoundException{
        saida.writeObject("MEDICO#GET_PACIENTE#"+nick+"#");//Concatena informações e solicita ao servidor
        entrada = new ObjectInputStream(server.getInputStream());//recebe informação advinda do servidor
        String str = (String) entrada.readObject();//"quebra" a informação do servidor
        desconectar();//Desconecta
        return str;
    }
    //----------------------------------------------------------
    //Metodo responsavel por fazer conexão com servidor
    public void conectar() throws IOException{
        server = new Socket(endereco,porta);
        saida = new ObjectOutputStream(server.getOutputStream());
    }
    
    //Metodo responsavel por fechar conexão com o servidor
    public void desconectar() throws IOException{
        saida.close();
        entrada.close();
        server.close();
    }
}
