package gdv.ohno.logic;

import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Input;

public class Cell extends GameObject {
    enum Type {Empty,Blue,Red,FixedBlue,FixedRed}
    private int[] Colors = {0xFFDCDCDC, 0xFF0000FF, 0xFFFF0000, 0xFF000044, 0xFF440000};
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

    public void update(float deltaTime) {

    }

    public void render(Graphics g) {
        g.save();
        g.setColor(Colors[_type.ordinal()]);
        g.fillCircle((int)_x, (int)_y, (int)_w);
        g.restore();
    }

    public void handleInput(Input.TouchEvent e) throws Exception {

    }

    private int _n;
    private Type _type;
}
