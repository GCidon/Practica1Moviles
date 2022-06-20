package gdv.ohno.pcapp;

import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import gdv.ohno.logic.Logic;
import gdv.ohno.pcengine.PCEngine;
import gdv.ohno.pcengine.PCGraphics;

public class Main {
    public static void main(String[] args) {
        //Creamos los 3 elementos necesarios : Motor, graficos y logica
        Toolkit tk = Toolkit.getDefaultToolkit();
        PCEngine engine;
        PCGraphics _graphics;
        Logic _logic;
        boolean _exit = false;

        //Creamos y colocamos la ventana
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);

        frame.setSize(800, 600);

        frame.setIgnoreRepaint(true);
        frame.createBufferStrategy(2);
        BufferStrategy strat = frame.getBufferStrategy();

        //Inicializamos la logica
        _logic = new Logic();
        engine = new PCEngine(_logic, frame);

        //Comenzamos el bucle principal
        try {
            engine.running();
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (!_exit) {
        }

    }
}