/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SMCServidorBorda.controller;

import br.com.SMCServidorBorda.conection.Conection;
import br.com.SMCServidorBorda.model.Medico;
import br.com.SMCServidorBorda.model.Paciente;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Antonio Raian
 */
//Controlador do sistema, onde é armazenada as informações dos pacientes e dos médicos
public class Controller {
    private PriorityQueue<Paciente> pacientes; //Lista q armazena todos os pacientes com sensores
    private LinkedList<Medico> medicos; //Lista que armazena os médicos
    private Stack<String> risco;
    Timer timer = new Timer();//Thread responsável por atualizar altomaticamente
    private Conection conexao;
    
    //Construtor
    public Controller(String host, String porta){
        //Instancia de uma nova lista de prioridade e a criação de um comparador para a mesma
        pacientes = new PriorityQueue<>(new Comparator<Paciente>() {
            //método que ordena a lista, colocando os pacientes prioritários no inicio
            @Override
            public int compare(Paciente o1, Paciente o2) {
                if(o1.isPrioridade()){
                    if(o2.isPrioridade()){
                        return 0;
                    }else{
                        return -1;
                    }
                }else if(o2.isPrioridade()){
                    return 1;
                }else{
                    return 0;
                }
            }
        }); //Inicia a lista de pacientes
        medicos = new LinkedList<>();//Instancia de uma nova lista de medicos
        risco = new Stack<>();
        enviaInfo(host, porta);
    }
    
    //Metodos dos sensores---------------------------
    //Método que cria e armazena um novo Paciente
    public Paciente salvarSensor(String nick, String nome, String movimento, String ritmo, String sistole, String diastole) throws IOException{
        Paciente p = null;
        if(!existPaciente(nick)){//Verifica se o nick já existe no sistema
            p = new Paciente(nick, nome, Integer.parseInt(movimento), Integer.parseInt(ritmo), Integer.parseInt(sistole), Integer.parseInt(diastole));
            p.setPrioridade(prioridade(p));//Verifica se é prioritário ou não
            pacientes.add(p);
        }
        return p;
    }
    
    //Metodo pra atualizar as informações do Paciente
    public boolean atualizarSensor(String nick, String nome, String movimento, String ritmo, String sistole, String diastole) throws IOException {
        //Cria um novo paciente com as informações passadas
        Paciente p = new Paciente(nick, nome, Integer.parseInt(movimento), Integer.parseInt(ritmo), Integer.parseInt(sistole), Integer.parseInt(diastole));
        if(prioridade(p)){
            risco.push(p.getNick()+" - "+p.getNome());
            p.setPrioridade(true);
        }else{
            p.setPrioridade(false);
        }        
        if(pacientes.contains(p)){//Verifica se já existe um paciente com mesmo nick
            pacientes.remove(p);//Remove as informações anteriores do paciente            
            pacientes.add(p);//E adiciona as novas
            return true;
        }else{
            pacientes.add(p);
            return true;
        }
    }
    
    public String getPacientesRisco(){
        String s="";
        while(!risco.isEmpty()){
            s+=risco.pop()+"#";
        }
        return s;
    }
    //----------------------------------
    
    //Metodos dos medicos----------------------------
    //Metodo que armazena o médico no sistema
    public Medico salvarMedico(String nome, String login, String senha){
        Medico m = null;
        if(!existMedico(login)){//Verifica se já existe esse login no sistema
            m = new Medico(nome, login, senha);
            medicos.add(m);//Adiciona o médico à lista de medicos cadastrados
        }
        return m;
    }
    
    //Metodo que verifica se o médico está cadastrado no listema
    public Medico autenticaMedico(String login, String senha){
        Medico m = null;
        for(Medico x : medicos){
            if(x.getLogin().equals(login)&&x.getSenha().equals(senha)){//Verifica se os dados de login e senha conferem com os cadastros
                m = x;
                break;
            }
        }
        return m;
    }
    
    //Metodo que retorna uma lista contendo os prioritários do sistema (os pacientes de risco)
    public String listarPrioritarios(){
        String s = "";
        for(Paciente x:pacientes){
            if(x.isPrioridade())//Verifica se o paciente está em risco
                s+=x.getNick()+"-"+x.getNome()+"#"; //E o adiciona já codificado para o protocolo do servidor
        }
        return s;
    }
     
    //-----------------------------------------------
    //Metodo que retorna todos os pacientes cadastrados
    public String listarSensores(){
        String str = "";
        for(Paciente x:pacientes){//Para cada paciente do sistema ele armazena uma string com os padroes do protocolo de comunicação
            str += x.getNick()+"-"+x.getNome()+"#";
        }
        return str;
    }
    
    //Encontra um paciente e retorna suas informações
    public String getPaciente(String nick){
        for(Paciente x:pacientes){
            if(x.getNick().equals(nick))//Verifica o nick
                return x.toString();
        }
        return null;
    }
    //Metodo que define se o paciente é prioritário
    //usei valores arbitrarios
    private boolean prioridade(Paciente p){
        switch (p.getMovimento()){//Para os movimento adotem um padrão numérico
            case 0://0 o paciente está em repouso
                if(p.getRitmo()<40||p.getRitmo()>100){//Se ele estiver com os batimentos menores que 40 ou maiores que 100 é um risco
                    return true;                    
                }else if(p.getSistole()<10 || p.getSistole()>14){//ou se estiver com a pressão maior q 14 ou menor q 10, tbm é um risco
                    return true;
                }
                break;
            case 1://1 o paciente está fazendo atividades normais do dia a dia
                if(p.getRitmo()<60||p.getRitmo()>110){//Se ele estiver com os batimentos menores que 60 ou maiores que 110 é um risco
                    return true;                    
                }else if(p.getSistole()<10 || p.getSistole()>14){
                    return true;
                }
                break;
            case 2://2 o paciente está praticando esportes ou algo que é considerado muito esforço
                if(p.getRitmo()<90||p.getRitmo()>150){//Se ele estiver com os batimentos menores que 90 ou maiores que 150 é um risco
                    return true;                    
                }else if(p.getSistole()<10 || p.getSistole()>14){
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }
    
    //Metodo que verifica se o login do médico está cadastrado no sistema
    public boolean existMedico(String login){
        for(Medico x:medicos){
            if(x.getLogin().equals(login))
                return true;
        }
        return false;
    }
    
    //Metodo que verifica se o nick do paciente está cadastrado no sistema
    public boolean existPaciente(String nick){
        for(Paciente x:pacientes){
            if(x.getNick().equals(nick))
                return true;
        }
        return false;
    }
    
    //Metodo que retorna todos os conectados na borda
    public String[] getAll(){
        String[] todos = new String[pacientes.size()+medicos.size()];
        int i = 0;
        for(Paciente p:pacientes){
            todos[i] = p.getNome();
            i++;
        }
        for(Medico m:medicos){
            todos[i] = m.getNome();
            i++;
        }
        return todos;
    }
    
    //THREAD QUE ATUALIZA AS INFORMAÇÕES DOS USUÁRIOS EM RISCO DA NUVEM
    private void enviaInfo(String host, String porta){
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                conexao = new Conection(host, porta);
                try {
                    conexao.enviaPacientes(getPacientesRisco());
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }, 30000, 20000);
    }
}
