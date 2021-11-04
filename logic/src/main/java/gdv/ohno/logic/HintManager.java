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
                    else if (greaterThanNumber(t)) {
                        System.out.println("PISTA 2 y tal");
                        _hint = new Hint(2);
                    }
                    // PISTA 3
                }

            }
        }
    }

    public boolean fillWithWalls(Cell c) {
        // Si un número tiene ya visibles el número de celdas que dice, se puede poner paredes en los extremos
        int count = 0;

        // contar las celdas azules adyacentes en todas las direcciones
        count += _board.countAdjacent(new Vector2D(c.getX(), c.getY()), _dirs[0], Cell.Type.Blue);
        count += _board.countAdjacent(new Vector2D(c.getX(), c.getY()), _dirs[1], Cell.Type.Blue);
        count += _board.countAdjacent(new Vector2D(c.getX(), c.getY()), _dirs[2], Cell.Type.Blue);
        count += _board.countAdjacent(new Vector2D(c.getX(), c.getY()), _dirs[3], Cell.Type.Blue);

        if (count == c.getNumber())
            return true;
        return false;
    }

    public boolean greaterThanNumber(Cell c) {
        // Comprueba los adyacentes y si hay un gris que si se transforma en azul supera
        // el numero de la celda observada devuelve true
        int count = 0;

        // Devuelve la primera celda gris en la direccion dada
        Cell emptyCell = _board.lookForCell(new Vector2D(c.getX(), c.getY()), _dirs[0], Cell.Type.Empty);
        if (emptyCell != null) {
            System.out.println("Hay un gris socio por ahi vacio socio");
        }

        if (count > c.getNumber())
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
