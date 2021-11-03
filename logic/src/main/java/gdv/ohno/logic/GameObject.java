package gdv.ohno.logic;

import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Input;

public abstract class GameObject {

    public GameObject(int x, int y, float w, float h) {
        _x = x;
        _y = y;
        _w = w;
        _h = h;
    }

    public int getX() {return _x;}
    public int getY() {return _y;}
    public float getW() {return _w;}
    public float getH() {return _h;}

    public void setPos(int x, int y) {
        _x = x;
        _y = y;
    }

    public void setH(float h) {
        _h = h;
    }
    public void setW(float w) {
        _w= w;
    }

    public abstract void render(Graphics g);
    public abstract void update(float deltaTime);
    public abstract void handleInput(Input.TouchEvent e) throws Exception;

    protected int _x, _y;
    float _w, _h;
    boolean visible = true;

}
