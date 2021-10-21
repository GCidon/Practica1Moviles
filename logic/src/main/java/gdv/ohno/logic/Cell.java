package gdv.ohno.logic;

public class Cell {
    enum Type {Empty,Blue,Red,FixedBlue,FixedRed}
    public Cell(int n, Type type) {
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

    private int _n;
    private Type _type;
}
