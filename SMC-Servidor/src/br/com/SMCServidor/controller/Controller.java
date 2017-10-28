package br.com.SMCServidor.controller;

import br.com.SMCServidor.model.Borda;
import br.com.SMCServidor.model.Medico;
import br.com.SMCServidor.model.Paciente;
import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author Antonio Raian
 */
//Controlador do sistema, onde é armazenada as informações dos pacientes e dos médicos
public class Controller {
    private LinkedList<Paciente> pacientes; //Lista q armazena todos os pacientes com sensores
    private LinkedList<Medico> medicos; //Lista que armazena os médicos
    private LinkedList<Borda> bordas; //Lista que armazena os servidores de bordas
    
    //Construtor
    public Controller(){
        //Instancia de uma nova lista de prioridade e a criação de um comparador para a mesma
        pacientes = new LinkedList<>();//Inicia a lista de pacientes
        medicos = new LinkedList<>();//Instancia de uma nova lista de medicos
        bordas = new LinkedList<>();//Instancia de uma nova lista de servidores de borda
    }
    
    //Metodos dos sensores---------------------------
    //Método que cria e armazena um novo Paciente
    public String salvarSensor(String nick, String nome, String senha) throws IOException{
        Paciente p;
        if(!existPaciente(nick)){//Verifica se o nick já existe no sistema
            p = new Paciente(nick, nome, senha, 0, 0, 0, 0);
            pacientes.add(p);
            return "CADASTRADO";
        }
        return null;//retorna a borda
    }
    
    //Metodo pra atualizar as informações do Paciente
    public boolean atualizarDadosPaciente(String nick, String movimento, String ritmo, String sistole, String diastole) throws IOException {
        for(Paciente p:pacientes){
            if(p.getNick().equals(nick)){//Verifica se já existe um paciente com mesmo nick
                p.setMovimento(Integer.parseInt(movimento));
                p.setRitmo(Integer.parseInt(ritmo));
                p.setSistole(Integer.parseInt(sistole));
                p.setDiastole(Integer.parseInt(diastole));
                p.setPrioridade(prioridade(p));
                return true;
            } 
        }
        return false;
    }
    
    public String atualizarLocalSensor(String nick,String cordenadaX, String cordenadaY){
        String borda=null;
        for(Paciente p:pacientes){
            if(p.getNick().equals(nick)){
                borda = alocarPaciente(Double.parseDouble(cordenadaX), Double.parseDouble(cordenadaY));
                p.setBorda(borda);
            }
        }
        return borda;
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
    
    //--------------------------------------------------------
    public boolean novaBorda(String endereco, String porta, double x, double y){
        Borda b = new Borda(endereco, porta, x, y);
        if(bordas.contains(b))
            return false;
        else{
            bordas.add(b);
            return true;
        }
    }
    
    public String alocarPaciente(double x, double y){
        String menorDistancia = null;
        double menor = 9999;
        double valor;
        if(!bordas.isEmpty()){
            for(Borda b:bordas){
                valor = Math.sqrt(Math.pow(b.getCordenadaX()-x,2)+Math.pow(b.getCordenadaY()-y,2));
                if(valor<menor){
                    menorDistancia = b.getEndereco()+"#"+b.getPorta();
                }
            }
        }
        return menorDistancia;
    }

    public String autenticaSensor(String nick, String senha, String x, String y) {
        for(Paciente p:pacientes){
            if(p.getNick().equals(nick)&&p.getSenha().equals(senha))
                return alocarPaciente(Integer.parseInt(x), Integer.parseInt(y));
        }
        return null;
    }

    public void removeBorda(String host) {
        for(Borda b:bordas){
            if(b.getEndereco().equals(host)){
                bordas.remove(b);
                break;
            }
        }
    }
}
