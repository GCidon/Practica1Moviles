package gdv.ohno.logic;

import org.graalvm.compiler.phases.verify.VerifyUsageWithEquals;

public class Hint {
    public Hint(int type, Vector2D pos) {
        switch (type) {
            case 1:
                _text = "Este número ya ve\ntodos sus puntos";
                break;
            case 2:
                _text = "Si pones un punto mas\nsuperas el número de visibles";
                break;
            case 3:
                _text = "En todos los casos posibles\nuna debería ser azul siempre";
                break;
            case 4:
                _text = "Este número ve\n demasiados puntos";
                break;
            case 5:
                _text = "Este número no ve\n suficientes puntos";
                break;
            case 6:
                _text = "Esta debería ser fácil ;)";
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

    private String _text;
    private Vector2D _cellPos;
}
