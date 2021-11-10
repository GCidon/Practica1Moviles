package gdv.ohno.logic;

import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Input;

public class Cell extends GameObject {
    enum Type {Empty, Blue, Red}

    private int[] Colors = {0xEEEEEE, 0x45CCFF, 0xFF3C54};

    public Cell(int x, int y, int w, int h, int n, Type type, Vector2D pos, boolean fixed) {
        super(x, y, w, h);
        _pos = pos;
        _n = n;
        _type = type;
        _fixed = fixed;
    }

    public int getNumber() {
        return _n;
    }

    public boolean isFixed() {
        return _fixed;
    }

    public Type getType() {
        return _type;
    }

    public Vector2D getPos() {
        return _pos;
    }

    public void setPos(Vector2D pos) {
        _pos = pos;
    }

    public void setNumber(int n) {
        _n = n;
    }

    public void setType(Type t) {
        _type = t;
    }

    public void setFixed(boolean f) {
        _fixed = f;
    }

    public void setLogic(Logic logic) {
        _logic = logic;
    }

    public void setHinted(boolean h) {
        _hinted = h;
    }

    public void update(float deltaTime) {

    }

    public void render(Graphics g) {
        if (_hinted) {
            g.setColor(0xFF000000);
            g.fillCircle((int) _x, (int) _y, (int) _w + 3);
        }

        g.setColor(Colors[_type.ordinal()]);
        g.fillCircle((int) _x, (int) _y, (int) _w);

        if (_n != 0 && _type == Type.Blue) {
            g.setColor(0xFFFFFFFF);
            g.drawText(Integer.toString(_n), (int) (_x + _w / 3), (int) (-_y - _h / 3));
        }
    }

    public void handleInput(Input.TouchEvent e) throws Exception {
        changeColor();
    }

    public void changeColor() {
        switch (_type) {
            case Empty:
                _type = Type.Blue;
                break;
            case Blue:
                if (!_fixed)
                    _type = Type.Red;
                break;
            case Red:
                if (!_fixed)
                    _type = Type.Empty;
                break;
            default:
                break;
        }
    }

    private int _n;
    private boolean _fixed, _hinted = false;
    private Type _type;
    private Logic _logic;
    private Vector2D _pos;
}


