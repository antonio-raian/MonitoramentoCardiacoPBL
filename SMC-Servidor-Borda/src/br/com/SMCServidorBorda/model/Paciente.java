/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SMCServidorBorda.model;

import java.io.Serializable;

/**
 *
 * @author raymendesjr
 */
//Classe modelo para criação de novos pacientes do sistema
public class Paciente implements Serializable{
    private String nick, nome;//Atributos para identificação
    private int ritmo, movimento, pressaoSis, pressaoDi;//Dados sobre o monitoramento cardíaco
    private boolean prioridade;//Variavel que define se ele é ou não prioridade

    //Construtor
    public Paciente(String nick,String nome, int movimento, int ritmo, int sistole, int diastole) {
        this.nick = nick;
        this.nome = nome;
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
    
    /*Metodo toString que retorna todas as informações do 
    Paciente, pronto para ser lido pelo protocolo de comunicação*/
    @Override
    public String toString() { 
        return nick+ "#" + nome + "#" + movimento + "#" + ritmo + "#" + pressaoSis +"#"+pressaoDi;
    }
    
    //Metodo que compara se dois objetos são iguais
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Paciente){
            Paciente p =(Paciente) obj;
            if(this.nick.equals(p.nick))
                return true;
        }
        return false;
    }
}