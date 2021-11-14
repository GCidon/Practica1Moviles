package gdv.ohno.pcengine;

import gdv.ohno.engine.Input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class PCInput implements Input {
    class MouseEventHandler implements MouseListener {
        public MouseEventHandler() {
        }

        //Solo detectaremos la pulsacion estandar del raton, ya que para el juego no es necesaria la deteccion de otra distinta
        public void mousePressed(MouseEvent e) {
            TouchEvent aux = new TouchEvent(e.getButton(), e.getX(), e.getY());
            _touchEvents.add(aux);
        }

        public void mouseClicked(MouseEvent e) {

        }

        public void mouseReleased(MouseEvent e) {

        }

        public void mouseEntered(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {

        }

    }

    public PCInput() {
    }

    @Override
    synchronized public ArrayList<TouchEvent> getTouchEvents() {
        ArrayList<TouchEvent> aux = new ArrayList<TouchEvent>(_touchEvents);
        _touchEvents.clear();
        return aux;
    }

    public void init() {
        _handler = new MouseEventHandler();
        _touchEvents = new ArrayList();
    }

    MouseEventHandler _handler;
    List<TouchEvent> _touchEvents;
}
