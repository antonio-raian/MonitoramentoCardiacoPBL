/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SMCServidor.conection;

import br.com.SMCServidor.controller.Controller;
import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author raymendesjr
 */
//Classe responsável por criar as Threads para o servidor TCP
public class ServerTCP implements Runnable{
    private final ServerSocket servidor; //Objeto responsavel por criar conexões (Abrir portas)
    private final Controller ctrl; //Variavel que recebe objeto que contem as infomações do sistema
    
    //Construtor
    public ServerTCP(int porta, Controller ctrl) throws IOException {
        //Informa a porta que o servidor vai tá "ouvindo"
        servidor = new ServerSocket(porta);//Abre a conexão para uma determinada porta
        System.out.println("TCP: Ouvindo a porta "+porta);
        this.ctrl = ctrl;
        new Thread(this).start();//Criando e iniciando uma thread principal
    }
    @Override
    public void run() {
        try{
            while(!servidor.isClosed()){//Laço de repetição para a criação de várias threads a medida que receber novas conexões
                // aceitando a conexão com o cliente e inicializando uma thread
                //Ao receber uma conexão, cria-se uma thread do tipo AtividadeServidor que irá tratar as informações recebidas
                new AtividadeServidorPrincipal(servidor.accept(),ctrl).start();
                System.out.println("Mais um cliente TCP atendido!");
            }
        }catch(Exception e){
            System.exit(1);
        }
    }
    
    //Metodo que "fecha" a conexão do servidor TCP
    public void stop() throws IOException{
        servidor.close();        
    }
}