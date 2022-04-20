package gdv.ohno.androidengine;

import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

import gdv.ohno.engine.Input;

public class AndroidInput implements Input {
    class TouchListener implements View.OnTouchListener {
        TouchListener(AndroidEngine engine) {
            _engine = engine;
        }

        @Override
        //Solo detectaremos la pulsacion standard de la pantalla, ya que para el juego no es necesaria la deteccion de otra distinta
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                Input.TouchEvent aux = new Input.TouchEvent(event.getActionIndex(), (int)event.getX(), (int)event.getY());
                _touchEvents.add(aux);
            } return true;
        }

        AndroidEngine _engine;
    }

    @Override
    synchronized public ArrayList<Input.TouchEvent> getTouchEvents() {
        ArrayList<Input.TouchEvent> aux = new ArrayList<Input.TouchEvent>(_touchEvents);
        _touchEvents.clear();
        return aux;
    }

    AndroidInput() {
    }

    public void init() {
        _listener = new TouchListener(_engine);
        _touchEvents = new ArrayList();
    }

    public void setEngine(AndroidEngine engine) {
        _engine = engine;
    }

    public TouchListener _listener;
    List<Input.TouchEvent> _touchEvents;
    AndroidEngine _engine;
}
