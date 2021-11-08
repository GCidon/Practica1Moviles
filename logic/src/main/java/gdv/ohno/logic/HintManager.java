package gdv.ohno.logic;

public class HintManager {
    public HintManager(Board board) {
        _board = board;
    }

    private void solve() {
        for (Cell[] column : _board.getBoard()) {
            for (Cell t : column) {
                // Para las pistas de las celdas azules fijas del mapa
                if (t.getType() == Cell.Type.Blue && t.getNumber() > 0) {
                    // PISTA 1
                    if (fillWithWalls(t)) {
                        System.out.println("PISTAAAAA 1 lol");
                        _hint = new Hint(1);
                    }
                    // PISTA 2

                    // PISTA 3
                }

            }
        }
    }

    public boolean fillWithWalls(Cell c) {
        //Si un número tiene ya visibles el número de celdas que dice, se puede poner paredes en los extremos
        int count = 0;

        // contar las celdas azules adyacentes en todas las direcciones
        count += _board.countAdjacent(c.getX(), c.getY());

        if (count == c.getNumber())
            return true;
        return false;
    }

    /**
     * _dirs[0] = SOUTH, DOWN
     * _dirs[1] = NORTH, UP
     * _dirs[2] = WEST, LEFT
     * _dirs[3] = EAST, RIGHT
     */
    private Vector2D[] _dirs = {new Vector2D(0, 1), new Vector2D(0, -1), new Vector2D(1, 0), new Vector2D(-1, 0)};
    private Board _board;
    private Hint _hint;
}
