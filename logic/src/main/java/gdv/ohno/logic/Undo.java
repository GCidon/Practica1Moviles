package gdv.ohno.logic;

//Clase para textos de deshacer movimiento
public class Undo {
    public Undo(int type, Vector2D pos) {
        switch (type) {
            case 1:
                _text = "Esta casilla\nvuelve a ser azul";
                break;
            case 2:
                _text = "Esta casilla\nvuelve a ser roja";
                break;
            case 3:
                _text = "Esta casilla\nvuelve a estar vacia";
                break;
            case 4:
                _text = "Nada que deshacer";
                break;
            default:
                _text = "";
                break;
        }
        _cellPos = pos;
    }

    public String getText() {
        return _text;
    }

    public Vector2D getPos() {
        return _cellPos;
    }

    private String _text;
    private Vector2D _cellPos;
}
