package gdv.ohno.logic;

import org.graalvm.compiler.phases.verify.VerifyUsageWithEquals;

public class Hint {
    public Hint(int type, Vector2D pos) {
        switch (type) {
            case 1:
                _text = "El número ya tiene todas sus azules visibles";
                break;
            case 2:
                _text = "Si pones un azul más superas el número de visibles man";
                break;
            case 3:
                _text = "En todos los casos una es azularda siempre";
                break;
            case 4:
                _text = "Te calentaste con las asules";
                break;
            case 5:
                _text = "Te faltan asulitas";
                break;
            case 6:
                _text = "Esta me da que es roja";
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
