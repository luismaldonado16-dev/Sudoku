package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {

    private static final String COLOR_NORMAL = "white";
    private static final String COLOR_FIJA = "#e6e6e6";
    private static final String COLOR_ERROR = "#f4b6b6"; // rojo tenue

    private Controlador controlador;
    private final Button[][] botones = new Button[9][9];

    @Override
    public void start(Stage stage) {
        controlador = new Controlador();

        GridPane cuadricula = new GridPane();
        cuadricula.setAlignment(Pos.CENTER);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Button boton = new Button();
                boton.setPrefSize(55, 55);
                boton.setMinSize(55, 55);
                final int fila = i;
                final int columna = j;
                boton.setOnAction(e -> alPresionarCasilla(fila, columna));
                botones[i][j] = boton;
                cuadricula.add(boton, j, i);
            }
        }

        Button btnNueva = new Button("Nueva partida");
        btnNueva.setOnAction(e -> {
            controlador.nuevaPartida();
            repintarTablero();
        });

        Button btnReiniciar = new Button("Reiniciar tablero");
        btnReiniciar.setOnAction(e -> {
            controlador.reiniciarTablero();
            repintarTablero();
        });

        HBox barra = new HBox(10, btnNueva, btnReiniciar);
        barra.setAlignment(Pos.CENTER);

        VBox raiz = new VBox(15, cuadricula, barra);
        raiz.setAlignment(Pos.CENTER);
        raiz.setPadding(new Insets(20));

        repintarTablero();

        stage.setTitle("Sudoku");
        stage.setScene(new Scene(raiz));
        stage.setResizable(false);
        stage.show();
    }

    private void alPresionarCasilla(int fila, int columna) {
        // Las casillas fijas no abren el numpad
        if (controlador.getFijaCasilla(fila, columna)) {
            return;
        }

        Optional<Integer> resultado = mostrarNumpad();
        if (resultado.isEmpty()) {
            return; // el jugador canceló
        }

        int numero = resultado.get();
        if (numero == 0) {
            controlador.borrarNumero(fila, columna);
        } else {
            controlador.colocarFicha(fila, columna, numero);
        }

        // Se repintan las 81 casillas, no solo la modificada
        repintarTablero();

        if (controlador.verificarVictoria()) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Sudoku");
            alerta.setHeaderText("¡Felicidades!");
            alerta.setContentText("Completaste el Sudoku correctamente.");
            alerta.showAndWait();
        }
    }

    // Devuelve 1-9 para colocar, 0 para borrar, o vacío si se cancela
    private Optional<Integer> mostrarNumpad() {
        Dialog<Integer> dialogo = new Dialog<>();
        dialogo.setTitle("Elige un número");
        dialogo.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        GridPane numpad = new GridPane();
        numpad.setHgap(5);
        numpad.setVgap(5);
        numpad.setPadding(new Insets(10));
        numpad.setAlignment(Pos.CENTER);

        for (int n = 1; n <= 9; n++) {
            Button b = new Button(String.valueOf(n));
            b.setPrefSize(50, 50);
            b.setStyle("-fx-font-size: 18px;");
            final int valor = n;
            b.setOnAction(e -> {
                dialogo.setResult(valor);
                dialogo.close();
            });
            numpad.add(b, (n - 1) % 3, (n - 1) / 3);
        }

        Button borrar = new Button("Borrar");
        borrar.setPrefHeight(40);
        borrar.setMaxWidth(Double.MAX_VALUE);
        borrar.setOnAction(e -> {
            dialogo.setResult(0);
            dialogo.close();
        });
        numpad.add(borrar, 0, 3, 3, 1);

        dialogo.getDialogPane().setContent(numpad);
        return dialogo.showAndWait();
    }

    private void repintarTablero() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                actualizarCasilla(i, j);
            }
        }
    }

    private void actualizarCasilla(int fila, int columna) {
        Button boton = botones[fila][columna];
        int valor = controlador.getValorCasilla(fila, columna);
        boolean fija = controlador.getFijaCasilla(fila, columna);

        boton.setText(valor == 0 ? "" : String.valueOf(valor));

        String fondo;
        if (fija) {
            fondo = COLOR_FIJA;
        } else if (controlador.tieneError(fila, columna)) {
            fondo = COLOR_ERROR;
        } else {
            fondo = COLOR_NORMAL;
        }

        // Bordes más gruesos entre bloques de 3x3
        double arriba = (fila % 3 == 0) ? 2.5 : 0.5;
        double izquierda = (columna % 3 == 0) ? 2.5 : 0.5;
        double abajo = (fila == 8) ? 2.5 : 0.5;
        double derecha = (columna == 8) ? 2.5 : 0.5;

        boton.setStyle(
                "-fx-background-color: " + fondo + ";"
                        + "-fx-background-radius: 0;"
                        + "-fx-border-color: black;"
                        + "-fx-border-width: " + arriba + " " + derecha + " " + abajo + " " + izquierda + ";"
                        + "-fx-font-size: 20px;"
                        + "-fx-font-weight: " + (fija ? "bold" : "normal") + ";"
                        + "-fx-text-fill: " + (fija ? "black" : "#1a4fb5") + ";"
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}