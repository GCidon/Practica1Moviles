package gdv.ohno.logic;

//Clase de textos de pistas
public class Hint {
    public Hint(int type, Vector2D pos) {
        switch (type) {
            case 1:
                _text = "Este número ya ve\ntodos sus puntos";
                break;
            case 2:
                _text = "Un punto más en alguna\ndirección superaría el número";
                break;
            case 3:
                _text = "En todos los casos posibles\nuna debería ser azul siempre";
                break;
            case 4:
                _text = "Este número ve\ndemasiados puntos";
                break;
            case 5:
                _text = "Este número no ve\nsuficientes puntos";
                break;
            case 6:
                _text = "Esta debería ser fácil ;)";
                break;
            case 7:
                _text = "Un punto azul siempre debe\nver al menos otro azul";
                break;
            case 8:
                _text = "Solo queda una\ndireccion posible";
                break;
            case 9:
                _text = "Debe tener al menos\nun azul con numero";
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
