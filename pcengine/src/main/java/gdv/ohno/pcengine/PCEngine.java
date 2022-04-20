package gdv.ohno.pcengine;

import gdv.ohno.engine.Engine;
import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Input;
import gdv.ohno.engine.Logic;

import javax.swing.JFrame;

public class PCEngine implements Engine {
    public PCEngine(Logic _logic, JFrame frame) {
        logica = _logic;
        _frame = frame;
        logica.setEngine(this);
    }

    public Graphics getGraphics() {
        return _graphics;
    }

    public Input getInput() {
        return _input;
    }

    public void update(double deltaTime) {
        logica.update((float) deltaTime);
    }

    public void render(Graphics g) {
        _graphics.clear(0xFFFFFFFF);
        _graphics.translate((int) g.getWidth() / 2, (int) g.getHeight() / 2);
        _graphics.scale(g.getProportion());
        logica.render(g);
    }

    public void handleInput() {
        logica.handleInput(_input.getTouchEvents());
    }

    //Bucle principal del juego
    public boolean running() {

        _graphics = new PCGraphics(_frame);
        _input = new PCInput();
        _input.init();
        _frame.addMouseListener(_input._handler);
        boolean _running = true;

        long lastTime = System.nanoTime();
        logica.init();

        //Bucle principal
        while (_running) {
            long currentTime = System.nanoTime();
            double nanoElapsedTime = currentTime - lastTime;
            double deltaTime = nanoElapsedTime / 1e9;
            lastTime = currentTime;

            update(deltaTime);
            handleInput();

            do {
                do {
                    graphicsAwt = _frame.getBufferStrategy().getDrawGraphics();
                    _graphics.setGraphics(graphicsAwt);
                    try {
                        render(_graphics);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        graphicsAwt.dispose();
                    }
                } while (_frame.getBufferStrategy().contentsRestored());
                _frame.getBufferStrategy().show();
            } while (_frame.getBufferStrategy().contentsLost());
        }
        return true;
    }

    public float getProportion() {
        return _graphics.getProportion();
    }

    protected PCGraphics _graphics;
    Logic logica;
    protected PCInput _input;
    java.awt.Graphics graphicsAwt;

    JFrame _frame;
}