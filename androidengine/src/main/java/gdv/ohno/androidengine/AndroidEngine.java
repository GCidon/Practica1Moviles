package gdv.ohno.androidengine;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

import gdv.ohno.engine.Engine;
import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Input;
import gdv.ohno.engine.Logic;

public class AndroidEngine implements Engine, Runnable {

    public AndroidEngine(Logic _logic, SurfaceView sv) {
        logica = _logic;
        _sv = sv;
        _holder = sv.getHolder();
    }

    @Override
    public Graphics getGraphics() {
        return _ag;
    }

    @Override
    public Input getInput() {
        return null;
    }

    public InputStream openInputStream(String filename) throws IOException {

        return context.getAssets().open(filename);
    }

    public void update(double deltaTime) {
        logica.update((float) deltaTime);
    }

    public void handleInput() throws Exception {
        logica.handleInput(_input.getTouchEvents());
    }

    public void render(Graphics g) throws Exception {
        //calculateSize
        _ag.clear(0xFF000000);
        _ag.translate((int) g.getWidth() / 2, (int) g.getHeight() / 2);
        _ag.scale(g.calculateSize());
        logica.render(g);
    }

    //Bucle principal
    public boolean running() throws Exception {
        //Inicializamos varialbes y clases que usaremos
        _input = new AndroidInput();
        _ag = new AndroidGraphics();
        _ag.getContext(context);
        canvas = _holder.lockCanvas();
        _ag.setCanvas(canvas);
        _locked = true;
        logica.init();
        _running = true;
        double lastTime = System.nanoTime();
        _sv.setOnTouchListener(_input._listener);
        while (_running) {
            if (!_locked) {
                canvas = _holder.lockCanvas();
                _ag.setCanvas(canvas);
                _locked = true;
            }
            double currentTime = System.nanoTime();
            double deltaTime = (currentTime - lastTime) / 1e9;

            update(deltaTime);
            handleInput();

            while (!_holder.getSurface().isValid());
            try {
                if (canvas != null) {
                    render(_ag);
                    _holder.unlockCanvasAndPost(canvas);
                    _locked = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            lastTime = currentTime;
        }

        return true;
    }


    public void getContext(Context _context) {
        context = _context;
    }


    @Override

    //Sistema de threads y ejecuta el runnning()
    public void run() {

        if (_renderThread != Thread.currentThread()) {
            throw new RuntimeException("run() should not be called directly");
        }

        while (_running && _sv.getWidth() == 0) {
        }


        try {
            running();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Pausa
    public void pause() {
        if (_running) {
            _running = false;
            while (true) {
                try {
                    _renderThread.join();
                    _renderThread = null;
                    break;
                } catch (InterruptedException ie) {
                    // Esto no debería ocurrir nunca...
                }
            }
        }
    }

    public void resume() {
        if (!_running) {
            _running = true;
            _renderThread = new Thread(this);
            _renderThread.start();
        }
    }


    Thread _renderThread;

    boolean _running = false;
    boolean _locked = false;

    Canvas canvas;
    Context context;
    Logic logica;
    AndroidGraphics _ag;
    SurfaceHolder _holder;
    SurfaceView _sv;
    AndroidInput _input;

}
