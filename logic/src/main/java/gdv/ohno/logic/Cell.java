package gdv.ohno.logic;

import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Input;

public class Cell extends GameObject {
    enum Type {Empty, Blue, Red}

    protected int[] Colors = {0xFFEEEEEE, 0xFF45CCFF, 0xFFFF3C54};

    public Cell(int x, int y, int w, int h, int n, Type type, Vector2D pos, boolean fixed) {
        super(x, y, w, h);
        _pos = pos;
        _n = n;
        _type = type;
        _fixed = fixed;
        _clicked = false;
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

        if (_n != 0) {
            g.setColor(0xFFFFFFFF);
            g.drawText(Integer.toString(_n), (int) (_x + _w / 3), (int) (-_y - _h / 3));
        }

        if(_type == Type.Red && _fixed && _clicked) {
            g.save();
            g.scale(-1);
            g.drawImage(_logic._logicGame.getImage(3), _x, _y, -(int) (_x + _w), -(int) (_y + _h));
            g.restore();
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
                else _clicked = !_clicked;
                break;
            case Red:
                if (!_fixed)
                    _type = Type.Empty;
                else _clicked = !_clicked;
                break;
            default:
                break;
        }
    }

    protected int _n;
    protected boolean _fixed = false, _hinted = false, _clicked = false;
    protected Type _type;
    protected Logic _logic;
    protected Vector2D _pos;
}


