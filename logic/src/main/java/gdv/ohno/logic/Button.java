package gdv.ohno.logic;

import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Input;

public class Button extends GameObject {
    public Button(float x, float y, float w, float h, String action, Logic logic) {
        super((int)x, (int)y, w, h);
        _action = action;
        _logic = logic;
    }

    public void render(Graphics g) {
        //Actualizamos la escala en cada frame para comprobar el tamaño de ventana nuevo (si lo hay)
        _scale = calculateSize();
    }

    public void update(float deltaTime) {

    }

    public void handleInput(Input.TouchEvent te) {
        //Transformamos las coordenadas del raton a las coordenadas transformadas y escaladas
        float _ratonx = (te.getPosX() - _logic._engine.getGraphics().getWidth()/2)/_scale;
        float _ratony = (_logic._engine.getGraphics().getHeight()/2 - te.getPosY())/_scale;

        //Y actualizamos tambien la altura y anchura del boton logico
        float _dx = _x + _w;
        float _dy = _y - _h;

        if(_ratonx > _x && _ratonx < _dx && _ratony > - _y && _ratony < -_dy) {
            _logic.processButton(_action);
        }
    }

    float calculateSize() {
        //Metodo para obtener la escala
        float aux1 = 0;
        float aux2 = 0;

        aux1 = (float) _logic._engine.getGraphics().getWidth() / (float) _logic._engine.getGraphics().getBaseWidth();
        aux2 = (float) _logic._engine.getGraphics().getHeight() / (float) _logic._engine.getGraphics().getBaseHeight();

        if (aux1 < aux2)
            return aux1;
        else return aux2;
    }

    float _scale;
    Logic _logic;
    public String _action;
}
