package br.com.SMCServidor.controller;

import br.com.SMCServidor.model.Borda;
import br.com.SMCServidor.model.MedicoNuvem;
import br.com.SMCServidor.model.PacienteNuvem;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Antonio Raian
 */
//Controlador do sistema, onde é armazenada as informações dos pacientes e dos médicos
public class ControllerNuvem {
    private LinkedList<PacienteNuvem> pacientes; //Lista q armazena todos os pacientes com sensores
    private LinkedList<String> emRisco; //Lista que armazena nick e nome dos pacientes em risco
    private LinkedList<MedicoNuvem> medicos; //Lista que armazena os médicos
    private LinkedList<Borda> bordas; //Lista que armazena os servidores de bordas
    private Timer timer = new Timer();//Thread responsável por atualizar altomaticamente
    private File diretorio = new File("Arguivos");
    
    //Construtor
    public ControllerNuvem() throws IOException, ClassNotFoundException{
        //Instancia de uma nova lista de prioridade e a criação de um comparador para a mesma
        pacientes = new LinkedList<>();//Inicia a lista de pacientes
        emRisco = new LinkedList<String>();//Inicia lista de pacientes em risco
        medicos = new LinkedList<>();//Instancia de uma nova lista de medicos
        bordas = new LinkedList<>();//Instancia de uma nova lista de servidores de borda
        init();
    }
    
    //Metodos dos sensores---------------------------
    //Método que cria e armazena um novo PacienteNuvem
    public String salvarSensor(String nick, String nome, String senha) throws IOException{
        PacienteNuvem p;
        if(!existPaciente(nick)){//Verifica se o nick já existe no sistema
            p = new PacienteNuvem(nick, nome, senha, 0, 0, 0, 0);
            pacientes.add(p);
            return "CADASTRADO";
        }
        return null;//retorna a borda
    }
    
    //Metodo pra atualizar as informações do PacienteNuvem
    public boolean atualizarDadosPaciente(String nick, String movimento, String ritmo, String sistole, String diastole) throws IOException {
        for(PacienteNuvem p:pacientes){
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
        for(PacienteNuvem p:pacientes){
            if(p.getNick().equals(nick)){
                borda = alocarPaciente(Double.parseDouble(cordenadaX), Double.parseDouble(cordenadaY));
                p.setBorda(borda);
            }
        }
        return borda;
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
        for(PacienteNuvem p:pacientes){
            if(p.getNick().equals(nick)&&p.getSenha().equals(senha))
                return p.getNick()+"#"+p.getNome()+"#"+alocarPaciente(Integer.parseInt(x), Integer.parseInt(y));
        }
        return null;
    }

    //----------------------------------
     //Metodos dos medicos----------------------------
    //Metodo que armazena o médico no sistema
    public MedicoNuvem salvarMedico(String nome, String login, String senha){
        MedicoNuvem m = null;
        if(!existMedico(login)){//Verifica se já existe esse login no sistema
            m = new MedicoNuvem(nome, login, senha);
            medicos.add(m);//Adiciona o médico à lista de medicos cadastrados
        }
        return m;
    }
    
    //Metodo que verifica se o médico está cadastrado no listema
    public MedicoNuvem autenticaMedico(String login, String senha){
        MedicoNuvem m = null;
        for(MedicoNuvem x : medicos){
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
        for(PacienteNuvem x:pacientes){
            if(x.isPrioridade())//Verifica se o paciente está em risco
                s+=x.getNick()+"-"+x.getNome()+"#"; //E o adiciona já codificado para o protocolo do servidor
        }
        return s;
    }
    //-----------------------------------------------
    //Metodo que retorna todos os pacientes cadastrados
    public String listarSensores(){
        String str = "";
        for(PacienteNuvem x:pacientes){//Para cada paciente do sistema ele armazena uma string com os padroes do protocolo de comunicação
            str += x.getNick()+"-"+x.getNome()+"#";
        }
        return str;
    }
    
    //Encontra um paciente e retorna suas informações
    public String getPaciente(String nick){
        for(PacienteNuvem x:pacientes){
            if(x.getNick().equals(nick))//Verifica o nick
                return x.toString();
        }
        return null;
    }
    //Metodo que define se o paciente é prioritário
    //usei valores arbitrarios
    private boolean prioridade(PacienteNuvem p){
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
        for(MedicoNuvem x:medicos){
            if(x.getLogin().equals(login))
                return true;
        }
        return false;
    }
    
    //Metodo que verifica se o nick do paciente está cadastrado no sistema
    public boolean existPaciente(String nick){
        for(PacienteNuvem x:pacientes){
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
    
    public void removeBorda(String host) {
        for(Borda b:bordas){
            if(b.getEndereco().equals(host)){
                bordas.remove(b);
                break;
            }
        }
    }
    
    public String[] getBordas(){
        String[] str = new String[bordas.size()];
        int i =0;
        for(Borda b:bordas){
            str[i]=b.toString();
            i++;
        }
        return str;
    }

    public String getPacienteBorda(String nick) {
        String borda = null;
        for(PacienteNuvem p:pacientes){
            if(p.getNick().equals(nick))
                borda = p.getBorda();
        }
        return borda;
    }

    public void setPacientesRisco(String pacientes) {
        String[] str = pacientes.split("#");
        for(String s:str){
            emRisco.add(s);
        }
    }
    
    //Persistencia-----------------
    //Metodo que lê os arquivos e carrega as informações na memória flash
    private void init() throws IOException, ClassNotFoundException{
        diretorio.mkdir();//Cria um diretório
        
        File arq = new File(diretorio, "Bordas.txt");//Cria Arquivo com o titulo "BORDAS"
        if(!arq.exists()){//Verifica se existe
            arq.createNewFile();//Se não existe cria um novo arquivo
        }else{
            lerArquivo(arq, bordas);
        }
        
        arq = new File(diretorio, "Pacientes.txt");//Cria Arquivo com o titulo "PACIENTES"
        if(!arq.exists()){//Verifica se existe
            arq.createNewFile();//Se não existe cria um novo arquivo
        }else
            lerArquivo(arq, pacientes);
        
        arq = new File(diretorio, "Medicos.txt");//Cria Arquivo com o titulo "MEDICOS"
        if(!arq.exists()){//Verifica se existe
            arq.createNewFile();//Se não existe cria um novo arquivo
        }else
            lerArquivo(arq, medicos);
        
        arq = new File(diretorio, "Em_Risco.txt");//Cria Arquivo com o titulo "EM_RISCO"
        if(!arq.exists()){//Verifica se existe
            arq.createNewFile();//Se não existe cria um novo arquivo
        }else
            lerArquivo(arq, emRisco);
    }
    
    private void salvarArquivo(String nome, LinkedList dados) throws FileNotFoundException, IOException{
        File arq = new File(diretorio,nome);
        arq.createNewFile();
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(arq)));			
        oos.writeObject(dados);
        oos.close();
    }
    
    private void lerArquivo(File arq, LinkedList lista) throws IOException, ClassNotFoundException{
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arq));//Usa o Stream pra ler os arquivos
        lista = (LinkedList<Borda>) ois.readObject();//Lê e armazena a lista na memória flash
    }
    
    //THREAD QUE SALVA OS DADOS NOS ARQUIVOS A CADA 40 SEGUNDOS
    private void enviaInfo(String host, String porta){
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    salvarArquivo("Bordas.txt", bordas);
                    salvarArquivo("Pacientes.txt", pacientes);
                    salvarArquivo("Medicos.txt", medicos);
                    salvarArquivo("Em_Risco.txt", emRisco);
                } catch (IOException ex) {
                    System.out.println("Salvou não!");
                }                
            }
        }, 30000, 40000);
    }
}