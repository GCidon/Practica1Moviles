package gdv.ohno.androidengine;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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
        return _input;
    }

    public void update(double deltaTime) {
        logica.update((float) deltaTime);
    }

    public void handleInput() {
        logica.handleInput(_input.getTouchEvents());
    }

    public void render(Graphics g) {
        _ag.clear(0xFF000000);
        _ag.translate((int) g.getWidth() / 2, (int) g.getHeight() / 2);
        _ag.scale(g.getProportion());
        logica.render(g);
    }

    //Bucle principal
    public boolean running() {
        _input = new AndroidInput();
        _input.setEngine(this);
        _input.init();
        _ag = new AndroidGraphics();
        _ag.getContext(context);
        logica.setEngine(this);
        logica.init();
        _running = true;
        double lastTime = System.nanoTime();
        _sv.setOnTouchListener(_input._listener);
        while (_running) {
            canvas = _holder.lockCanvas();
            _ag.setCanvas(canvas);
            setSizes(canvas.getWidth(), canvas.getHeight());
            double currentTime = System.nanoTime();
            double deltaTime = (currentTime - lastTime) / 1e9;
            update(deltaTime);
            handleInput();
            while (!_holder.getSurface().isValid());
            try {
                if(canvas != null) {
                    render(_ag);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            _holder.unlockCanvasAndPost(canvas);
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

    void setSizes(int w, int h) {
        _windowWidth = w;
        _windowHeight = h;
    }

    public int getWidth() { return _windowWidth; }
    public int getHeight() { return _windowHeight; }

    public float getProportion() {
        return _ag.getProportion();
    }

    Thread _renderThread;

    volatile boolean _running = false;

    Canvas canvas;
    Context context;
    Logic logica;
    AndroidGraphics _ag;
    SurfaceHolder _holder;
    SurfaceView _sv;
    AndroidInput _input;

    int _windowWidth;
    int _windowHeight;

}
