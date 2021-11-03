package gdv.ohno.logic;

public class Hint {
    public Hint(int type) {
        _type = type;

        switch (type) {
            case 1:
                _text = "El número ya tiene todas sus celdas visibles";
                break;
            case 2:
                _text = "Si pones un azul más superas el número de visibles man";
                break;
            default:
                break;
        }
    }

    public void setText(String s) {
        _text = s;
    }

    public String getText() {
        return _text;
    }

    public int getType() {
        return _type;
    }

    private String _text;
    private int _type;
}
