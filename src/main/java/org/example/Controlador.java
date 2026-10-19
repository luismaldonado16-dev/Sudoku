package org.example;

import java.util.Random;

public class Controlador {
    private int plantillas[][][] = {
            {
                    {9, 0, 0, 6, 0, 8, 0, 0, 1},
                    {0, 0, 2, 0, 0, 0, 6, 0, 0},
                    {0, 8, 0, 7, 3, 0, 0, 5, 0},
                    {3, 0, 0, 0, 2, 0, 0, 0, 7},
                    {0, 0, 0, 1, 0, 9, 0, 0, 0},
                    {8, 0, 0, 0, 4, 0, 0, 0, 9},
                    {0, 7, 0, 4, 9, 0, 0, 6, 0},
                    {0, 0, 6, 0, 0, 0, 7, 0, 0},
                    {2, 0, 0, 3, 0, 7, 0, 0, 8}
            },
            {
                    {7, 0, 0, 4, 0, 0, 0, 0, 5},
                    {8, 6, 0, 7, 0, 0, 0, 0, 0},
                    {3, 0, 0, 9, 0, 2, 0, 0, 0},
                    {0, 0, 0, 3, 0, 0, 0, 8, 1},
                    {1, 0, 9, 0, 0, 0, 6, 3, 2},
                    {0, 4, 0, 8, 1, 6, 0, 5, 0},
                    {9, 3, 0, 2, 8, 5, 7, 0, 0},
                    {5, 0, 0, 0, 4, 7, 1, 0, 0},
                    {4, 0, 0, 0, 0, 0, 0, 0, 0}
            },
            {
                    {1, 0, 0, 0, 0, 0, 0, 0, 6},
                    {0, 5, 0, 0, 7, 3, 0, 4, 0},
                    {4, 7, 0, 2, 0, 0, 0, 3, 9},
                    {0, 2, 0, 5, 0, 0, 9, 0, 0},
                    {0, 4, 0, 0, 8, 0, 0, 6, 0},
                    {0, 0, 9, 0, 0, 6, 0, 1, 0},
                    {3, 1, 0, 0, 0, 8, 0, 9, 7},
                    {0, 6, 0, 7, 1, 0, 0, 5, 0},
                    {7, 0, 0, 0, 0, 0, 0, 0, 1}
            }
    };
    private Modelo[][] tablero = new Modelo[9][9];
    private int tableroUsado;

    public Controlador(){
        this.tableroUsado = generarTablero();
    }

    public int generarTablero(){
        Random rng = new Random();
        Modelo casilla = new Modelo();
        int rngTablero = rng.nextInt(3);
        for(int i = 0; i < 9; i++){
            for(int o = 0; o < 9; o++){
                if(plantillas[rngTablero][i][o] != 0){
                    casilla = new Modelo(plantillas[rngTablero][i][o],true);
                    tablero[i][o] = casilla;
                }else{
                    casilla = new Modelo();
                    tablero[i][o] = casilla;
                }
            }
        }
        return rngTablero;
    }

    public void reiniciarTablero(){
        for(int i = 0; i < 9; i++){
            for(int o = 0; o < 9; o++){
                if (!tablero[i][o].getFija()) {
                    tablero[i][o].setValor(0);
                }
            }
        }
    }

    public int queTableroUso(){
        return this.tableroUsado;
    }

    public void nuevaPartida(){
        this.tableroUsado = generarTablero();
    }

    public boolean checarValidezFila(int numero, int fila, int columna){
        for(int i = 0; i < 9; i++){
            if(i == columna){
                continue;
            }else if(numero == tablero[fila][i].getValor()){
                return false;
            }
        }
        return true;
    }

    public boolean checarValidezColumna(int numero, int columna, int fila){
        for(int i = 0; i < 9; i++){
            if(i == fila){
                continue;
            }else if(numero == tablero[i][columna].getValor()){
                return false;
            }
        }
        return true;
    }

    public boolean checarValidezBloque(int numero, int fila, int columna) {
        int inicioFila = (fila / 3) * 3;
        int inicioColumna = (columna / 3) * 3;
        for (int i = inicioFila; i < inicioFila + 3; i++) {
            for (int j = inicioColumna; j < inicioColumna + 3; j++) {
                if(i == fila && j == columna){
                    continue;
                }else if (numero == tablero[i][j].getValor()) {
                    return false;
                }
            }
        }

        return true;
    }

    public int getValorCasilla(int fila, int columna){
        return tablero[fila][columna].getValor();
    }

    public boolean getFijaCasilla(int fila, int columna){
        return tablero[fila][columna].getFija();
    }

    public boolean colocarFicha(int fila, int columna, int numero){
        if(tablero[fila][columna].getFija()){
            return false;
        }

        if(numero < 1 || numero > 9){
            return false;
        }

        tablero[fila][columna].setValor(numero);
        return true;
    }

    public boolean checarUbicacionNumero(int fila, int columna, int numero){
        if(tablero[fila][columna].getValor() != 0 && checarValidezBloque(numero,fila,columna)
         && checarValidezColumna(numero, columna,fila) && checarValidezFila(numero,fila,columna)) {
            return true;
        }
        return false;
    }

    public void borrarNumero(int fila, int columna){
        tablero[fila][columna].setValor(0);
    }

    public boolean verificarVictoria(){
        if(!validarTableroLleno()){
            return false;
        }

        for(int i = 0; i < 9; i++){
            for(int o = 0; o < 9; o++){
                int numero = tablero[i][o].getValor();
                boolean verificador = checarUbicacionNumero(i,o,numero);
                if(verificador == false){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean tieneError(int fila, int columna){
        int numero = tablero[fila][columna].getValor();
        if(tablero[fila][columna].getFija() == false && numero != 0 && (!checarValidezBloque(numero,fila,columna)
                || !checarValidezColumna(numero, columna,fila) || !checarValidezFila(numero,fila,columna))) {
            return true;
        }
        return false;
    }

    public boolean validarTableroLleno(){
        for(int i = 0; i < 9; i++){
            for(int o = 0; o < 9; o++){
                int numero = tablero[i][o].getValor();
                if(numero == 0){
                    return false;
                }
            }
        }
        return true;
    }
}