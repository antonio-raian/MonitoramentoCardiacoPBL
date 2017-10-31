/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SMCServidor.model;

import java.io.Serializable;

/**
 *
 * @author raymendesjr
 */
//Classe modelo para criação de novos pacientes do sistema
public class PacienteNuvem implements Serializable{
    private String nick, nome, senha;//Atributos para identificação
    private int ritmo, movimento, pressaoSis, pressaoDi;//Dados sobre o monitoramento cardíaco
    private boolean prioridade;//Variavel que define se ele é ou não prioridade
    private String borda;

    //Construtor
    public PacienteNuvem(String nick,String nome, String senha, int movimento, int ritmo, int sistole, int diastole) {
        //this.id = contador++;
        this.nick = nick;
        this.nome = nome;
        this.senha = senha;
        this.movimento = movimento;
        this.ritmo = ritmo;
        this.pressaoSis = sistole;
        this.pressaoDi = diastole;
        this.prioridade = false;
    }

    //Geters and Seters
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getMovimento() {
        return movimento;
    }

    public void setMovimento(int movimento) {
        this.movimento = movimento;
    }

    public int getRitmo() {
        return ritmo;
    }

    public void setRitmo(int ritmo) {
        this.ritmo = ritmo;
    }

    public int getSistole() {
        return pressaoSis;
    }

    public void setSistole(int sistole) {
        this.pressaoSis = sistole;
    }
    public int getDiastole() {
        return pressaoDi;
    }

    public void setDiastole(int diastole) {
        this.pressaoDi = diastole;
    }

    public boolean isPrioridade() {
        return prioridade;
    }

    public void setPrioridade(boolean prioridade) {
        this.prioridade = prioridade;
    }
    
    public String getBorda() {
        return borda;
    }

    public void setBorda(String borda) {
        this.borda = borda;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    /*Metodo toString que retorna todas as informações do 
    PacienteNuvem, pronto para ser lido pelo protocolo de comunicação*/
    @Override
    public String toString() { 
        return nick+ "#" + nome + "#" + movimento + "#" + ritmo + "#" + pressaoSis +"#"+pressaoDi;
    }
    
    //Metodo que compara se dois objetos são iguais
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PacienteNuvem){
            PacienteNuvem p =(PacienteNuvem) obj;
            if(this.nick.equals(p.nick))
                return true;
        }
        return false;
    }
}