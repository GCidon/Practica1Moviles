package gdv.ohno.logic;

import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Input;

public class Cell extends GameObject {
    enum Type {Empty,Blue,Red,FixedBlue,FixedRed}
    private int[] Colors = {0xFFDCDCDC, 0xFF0000FF, 0xFFFF0000, 0xFF0000FF, 0xFFFF0000};
    public Cell(int x, int y, int w, int h, int n, Type type) {
        super(x, y, w, h);
        _n=n;
        _type=type;
    }

    public int getNumber(){return _n;}
    public Type getType(){return _type;}
    public void setNumber(int n){
        _n=n;
    }
    public void setType(Type t){
        _type=t;
    }
    public void setLogic(Logic logic) { _logic = logic; }

    public void update(float deltaTime) {

    }

    public void render(Graphics g) {
        g.setColor(Colors[_type.ordinal()]);
        g.fillCircle((int)_x, (int)_y, (int)_w);

        if(_n!=0) {
            g.setColor(0xFF000000);
            g.drawText(Integer.toString(_n), (int) (_x + _w / 3), (int) (_y + _h / 1.5));
        }
    }

    public void handleInput(Input.TouchEvent e) throws Exception {
        switch (_type) {
                case Empty:
                    _type = Type.Blue;
                    break;
                case Blue:
                    _type = Type.Red;
                    break;
                case Red:
                    _type = Type.Empty;
                    break;
                default:
                    break;
        }
    }

    private int _n;
    private Type _type;
    private Logic _logic;
}
