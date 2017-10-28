/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SMCServidorBorda.conection;

import br.com.SMCServidorBorda.controller.Controller;
import br.com.SMCServidorBorda.model.Medico;
import br.com.SMCServidorBorda.model.Paciente;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Antonio Raian
 */
//Classe (thread) responsável por todas as ações do servior, responsável por decifrar informações vindas dos clientes
public class AtividadeServidor extends Thread{
    private Socket clienteTCP;//Variavel responsável por receber a conexão do servidor TCP
    private DatagramSocket clienteUDP;//Variavel responsável por receber a conexão do servidor UDP
    private ObjectInputStream entrada;//Objetos que armazena informações advindas do cliente
    private ObjectOutputStream saida; // Objeto usado para enviar mensagens aos clientes TCP
    private DatagramPacket entradaUDP; //Objeto usado para enviar mensagens aos clientes UDP;
    private final Controller ctrl;//Nosso objeto que contem as listas e informações salvas do sistema
    private final String str; //String usada pra receber as requisições dos clientes
    
    //Construtor que permite conexão TCP
    public AtividadeServidor(Socket socket, Controller ctrl) throws IOException, ClassNotFoundException {
        clienteTCP = socket;//Recebe a conexão
        this.ctrl = ctrl;//Seta o objeto que contem as informações do Sistema
        
        entrada = new ObjectInputStream(clienteTCP.getInputStream());//Decifra as informações vindas do cliente
        System.out.println("Cliente TCP: "+clienteTCP.getInetAddress()+":"+clienteTCP.getPort());
        str = (String) entrada.readObject();//Transforma o objeto passado em String
        System.out.println("Recebido: "+str);
    }
    
    //Construtor que permite conexão UDP
    public AtividadeServidor(DatagramSocket socket, DatagramPacket packet, Controller ctrl) throws IOException{
        this.ctrl = ctrl;//Seta o objeto que contem as informações do Sistema
        this.entradaUDP = packet; //Recebe o pacote enviado ao servidor
        clienteUDP = socket;//Recebe o meio de comunicação com o cliente
        
        System.out.println("Conectou");
        
        System.out.println("Cliente UDP: "+entradaUDP.getAddress()+":"+entradaUDP.getPort());
        str = new String(entradaUDP.getData(),0,entradaUDP.getLength());//Transforma o objeto passado em String
        System.out.println("Recebido: "+str);
    }
    //Metodo responsável pelas atividades da Thread
    @Override
    public void run(){
        try{
            String[] array = str.split("#"); //"Quebra" a String onde encontrar #

            switch (array[0]) { //Testa o comando
                case "SENSOR"://Caso a primeira informação passada seja SENSOR
                    switch(array[1]){//Verifica o segundo comando
                        case "SALVAR":
                            salvarSensor(array[2], array[3],array[4], array[5], array[6], array[7]);
                            break;
                        case "ATUALIZAR":
                            atualizarSensor(array[2], array[3],array[4], array[5], array[6], array[7]);
                            break;
                        case "LISTAR":
                            listarSensores();
                            break;
                        case "GET_PACIENTE":
                            getPacienteUDP(array[2]);
                            break;
                    }
                    break;
                case "MEDICO"://Caso a primeira informação passada seja SENSOR
                    switch(array[1]){//Verifica o segundo comando
                        case "SALVAR":
                            salvarMedico(array[2], array[3], array[4]);
                            break;
                        case "LOGIN":
                            autenticaMedico(array[2], array[3]);
                            break;
                        case "LISTAR_PRIORITARIOS":
                            listarPrioritarios();
                            break;
                        case "GET_PACIENTE":
                            getPacienteTCP(array[2]);
                            break;
                        default:
                            break;
                    }
            }
            AtividadeServidor.currentThread().interrupt();//Interrompe a Thread, fazendo ela deixar de existir
        } catch (IOException ex) {
            Logger.getLogger(AtividadeServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Ações para o servior UDP que faz conexão com os sensores
    //Metodo que se conecta com o Controlador para armazenar um novo paciente
    private void salvarSensor(String nick, String nome, String movimento, String ritmo, String sistole, String diastole) throws IOException{
        Paciente p = ctrl.salvarSensor(nick, nome, movimento, ritmo, sistole, diastole);
        String s;
        if(p==null){//Caso o paciente retornado seja nulo, houve algum erro, a mensagem enviada ao cliente é Falha
            s = "$FALHA$";
        }else{//Se for diferente de nulo, ele foi cadastrado
            s = "$CADASTRADO$";
        }
        //Cria um pacote e concatena a String de retorno
        DatagramPacket sendResult = new DatagramPacket(s.getBytes(), s.getBytes().length, entradaUDP.getAddress(),entradaUDP.getPort());
        clienteUDP.send(sendResult);//Envia o pacote criado para o cliente
        
        System.out.println("Dados Salvos: "+nick+", "+nome+", "+movimento+", "+ritmo+", "+sistole+"/"+diastole);
    }
    //Metodo que se conecta com o Controlador para atualizar informações de um paciente
    private void atualizarSensor(String nick, String nome, String movimento, String ritmo, String sistole, String diastole) throws IOException {
        //Metodo de atualização retorna true ou false
        boolean p = ctrl.atualizarSensor(nick, nome, movimento, ritmo, sistole, diastole);
        String s;
        if(!p){//Se o retorno for false, não foi possível atualizar os dados do paciente
            s = "$FALHA$";//A mensagem enviada é FALHA
        }else{//Se for true, a atualização foi bem sucedida
            s = "$ATUALIZADO$";//A mensagem enviada é ATUALIZADO
        }
        //Cria um pacote e concatena a String de retorno
        DatagramPacket sendResult = new DatagramPacket(s.getBytes(), s.getBytes().length, entradaUDP.getAddress(),entradaUDP.getPort());
        clienteUDP.send(sendResult);;//Envia o pacote criado para o cliente
        
        System.out.println("Dados Atualizados para: "+nick+", "+nome+", "+movimento+", "+ritmo+", "+sistole+"/"+diastole);
    }
    
    //Metodo que solicita a lista de pacientes ao controlador
    private void listarSensores() throws IOException {
        String s = ctrl.listarSensores();//Concatena todos os pacientes numa string separado por "#"
        System.out.println(s);
        if(s.equals("")){//Se retornar uma String vazia, é por que não existem pacientes cadastrados
            s = "$FALHA$";
        }
        //Cria um pacote e concatena a String de retorno
        DatagramPacket sendResult = new DatagramPacket(s.getBytes(), s.getBytes().length, entradaUDP.getAddress(),entradaUDP.getPort());
        clienteUDP.send(sendResult);;//Envia o pacote criado para o cliente
    }
    
    //Metodo que solicita um paciente pelo nick
    private void getPacienteUDP(String nick) throws IOException{
        String s = ctrl.getPaciente(nick);//O metodo retorna uma string com todas as informações do paciente separada por #
        if(s==null){//Se essa String for nula, não encontrou o paciente
            s = "$FALHA$";//E o retorno é FALHA
        }
        //Cria um pacote e concatena a String de retorno
        DatagramPacket sendResult = new DatagramPacket(s.getBytes(), s.getBytes().length, entradaUDP.getAddress(),entradaUDP.getPort());
        clienteUDP.send(sendResult);;//Envia o pacote criado para o cliente
    }

    //Ações para o Servidor TCP que faz conexão com o Medico
    //Metodo que pede pra salvar um novo médico
    private void salvarMedico(String nome, String login, String senha) throws IOException{
        saida = new ObjectOutputStream(clienteTCP.getOutputStream());//Estabelece uma forma de conectar-se ao cliente
        Medico m = ctrl.salvarMedico(nome, login, senha);//Solicita a criação de um novo médico no sistema
        if(m == null){//Se o retorno for nulo
            saida.writeObject("$EXISTENTE$");//Envia uma mensagem de EXISTENTE ao cliente
        }else{//Se for diferente de nulo
            saida.writeObject("$CADASTRADO$");//Envia uma mensagem de CADASTRADO ao cliente
        }
        saida.close();//Encerra a conexão com o cliente
    }
    
    //Metodo que faz a autenticação do login do Medico
    private void autenticaMedico(String login, String senha) throws IOException{
        saida = new ObjectOutputStream(clienteTCP.getOutputStream());//Estabelece uma forma de conectar-se ao cliente
        Medico m = ctrl.autenticaMedico(login, senha);//Tenta fazer o login de um médico
        if(m == null){//Se a resposta for nula
            saida.writeObject("$INEXISTENTE$");//Envia uma mensagem de INEXISTENTE ao cliente
        }else{//Se não for nula
            saida.writeObject("$LOGON$");//Envia uma mensagem de LOGON ao cliente
        }
        saida.close();//Encerra a conexão com o cliente
    }
    
    //Metodo que solicita um paciente a partir de um nick
    private void getPacienteTCP(String nick) throws IOException{
        saida = new ObjectOutputStream(clienteTCP.getOutputStream());//Estabelece uma forma de conectar-se ao cliente
        String s = ctrl.getPaciente(nick);//Solicita o paciente pelo nick
        if(s==null){//Se a resposta for nula
            saida.writeObject("$NAO_ENCONTRADO$");//Envia uma mensagem de NAO_ENCONTRADO ao cliente
        }else{
            saida.writeObject(s);//Envia uma string com as informações do paciente para o cliente
        }
        saida.close();//Encerra a conexão com o cliente
    }
    
    //Metodo que solicita os pacientes em risco
    private void listarPrioritarios() throws IOException{
        saida = new ObjectOutputStream(clienteTCP.getOutputStream());//Estabelece uma forma de conectar-se ao cliente
        String s = ctrl.listarPrioritarios();//Pede a lista dos pacientes em risco
        if("".equals(s)){//Se for uma string branco
            saida.writeObject("$VAZIO$");//Envia uma mensagem de VAZIO ao cliente
        }else{//Se não for nula
            saida.writeObject(s);//envia uma string com o nick e o nome dos pacientes em risco separados por "#"
        }
        saida.close();//Encerra a conexão com o cliente
    }
}