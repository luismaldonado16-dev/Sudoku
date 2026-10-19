package org.example;

public class Modelo {

    private int valor;
    private boolean fija;

    public Modelo(){
        this.valor = 0;
        this.fija = false;
    }

    public Modelo(int valor, boolean fija){
        this.valor = valor;
        this.fija = fija;
    }

    public int getValor(){
        return valor;
    }

    public boolean getFija(){
        return fija;
    }

    public void setValor(int valor) {
        if (!fija && valor >= 0 && valor <= 9) {
            this.valor = valor;
        }
    }
}