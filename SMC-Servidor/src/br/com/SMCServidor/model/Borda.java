/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SMCServidor.model;

import java.io.Serializable;

/**
 *
 * @author Antonio Raian
 */
public class Borda implements Serializable{
    private String endereco, porta;//Atributos para conexão
    private final double cordenadaX, cordenadaY;//Atributos para encontrar a localização

    //Contrutor
    public Borda(String endereco, String porta, double cordenadaX, double cordenadaY) {
        this.endereco = endereco;
        this.porta = porta;
        this.cordenadaX = cordenadaX;
        this.cordenadaY = cordenadaY;
    }

    //Geters e Setrs
    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getPorta() {
        return porta;
    }

    public void setPorta(String porta) {
        this.porta = porta;
    }

    public double getCordenadaX() {
        return cordenadaX;
    }

    public double getCordenadaY() {
        return cordenadaY;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Borda){
            Borda outro = (Borda) obj;
            if(outro.getCordenadaX()!=this.getCordenadaX())
                return false;
            if(outro.getCordenadaY()!=this.getCordenadaY())
                return false;
            if(!this.getEndereco().equals(outro.getEndereco()))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return endereco + ", " + porta + ", " + cordenadaX + ", " + cordenadaY ;
    }
}
