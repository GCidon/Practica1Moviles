package gdv.ohno.logic;

public class HintManager {
    public HintManager(Board board) {
        _board = board;
    }

    public void solve() {
        for (Cell[] column : _board.getBoard()) {
            for (Cell c : column) {
                // Para las pistas de las celdas azules fijas del mapa
                if (c.getType() == Cell.Type.Blue && c.getNumber() > 0) {
                    // PISTA 1
                    if (fillWithWalls(c)) {
                        System.out.println("PISTAAAAA 1 el numero ya ve todos los azulardos");
                        closeCellWithWalls(c);
                        _hint = new Hint(1);
                    } else {
                        for (Vector2D dir : _dirs) {
                            // PISTA 2
                            // si el numero de azules despues de poner en azul una celda gris, supera
                            // el numero indicado por la celda c es porque la siguiente en esa dir tiene que ser roja
                            if (greaterThanNumber(c, dir)) {
                                System.out.println("PISTA 2 si pones azul donde no toca igual te pasas");
                                _board.getBoard()[c.getPos().x + dir.x][c.getPos().y + dir.y].setType(Cell.Type.Red);
                                _hint = new Hint(2);
                            }
                            // PISTA 3
                            if (mustBeBlue(c, dir)) {
                                System.out.println("PISTA 3 en todos los casos una es azularda siempre");
                                _board.getBoard()[c.getPos().x + dir.x][c.getPos().y + dir.y].setType(Cell.Type.Blue);
                            }
                        }
                    }
                    // PISTAS 4
                    if (excesiveBlue(c)) {
                        System.out.println("PISTA 4 te calentaste con las asules");
                    }
                    // PISTA 5
                    else if (excesiveRed(c)) {
                        System.out.println("PISTA 5 te faltan asulitas");
                    }
                }
                // Pistas de las celdas grises
                else if (c.getType() == Cell.Type.Empty) {
                    // PISTA 6
                    if (checkRed(c)) {
                        c.setType(Cell.Type.Red);
                        System.out.println("PISTA 6 jefe tiene que ser roja no hay otra");
                    }
                }

                // Las 8, 9 y 10 son opcionales porque las pistas anteriores ya las cubren
            }
        }

        // mientras no este resuelto se sigue resolviendo
        /*if (!isSolved()) {
            solve();
        }*/

    }

    /**
     * Check if the given cell has all the blue cells comparing with the number of the specified cell.
     *
     * @param c Cell to check.
     * @return true if the count of all the adjacent blue cells is equal to the number of the given cell.
     */
    public boolean fillWithWalls(Cell c) {
        // Si un número tiene ya visibles el número de celdas que dice, se puede poner paredes en los extremos
        int count = 0;

        // contar las celdas azules adyacentes en todas las direcciones
        count += _board.countAdjacent(c.getPos().x, c.getPos().y);

        if (count == c.getNumber())
            return true;
        return false;
    }

    /**
     * If Hint 1 is true we swap to red the rest of the empty cells around the given cell
     *
     * @param c cell to fill with red around
     */
    private void closeCellWithWalls(Cell c) {
        if (!_board.outOfBoard(c.getPos())) {
            for (Vector2D dir : _dirs) {
                Vector2D auxPos = c.getPos();
                auxPos.x += dir.x;
                auxPos.y += dir.y;
                if (!_board.outOfBoard(auxPos) && _board.getBoard()[auxPos.x][auxPos.y].getType() == Cell.Type.Empty) {
                    _board.getBoard()[auxPos.x][auxPos.y].setType(Cell.Type.Red);
                }
            }
        }
    }

    /**
     * Check if the next cell in the given direction is empty and depending on the total count of adjacent blues
     * return true or false
     *
     * @param c   initial cell
     * @param dir direction to check
     * @return true if the count is greater than the number of the initial cell
     */
    public boolean greaterThanNumber(Cell c, Vector2D dir) {
        // Comprueba los adyacentes y si hay un gris que si se transforma en azul supera
        // el numero de la celda observada devuelve true
        int count = 0;
        Vector2D nextPos = Vector2D.sum(c.getPos(), dir);
        // se comprueba si la siguiente celda esta dentro del tablero
        if (_board.outOfBoard(nextPos)) return false;

        Cell.Type type = _board.getBoard()[nextPos.x][nextPos.y].getType();
        // Si la celda es azul no se puede ambiar asi que devuelve false
        if (type == Cell.Type.Blue) return false;

        // si es gris se cambia a azul y se cuentan las adyacentes de la celda inicial
        _board.getBoard()[nextPos.x][nextPos.y].setType(Cell.Type.Blue);
        count = _board.countAdjacent(c.getPos().x, c.getPos().y);
        // se devuelve la celda al color inicial
        _board.getBoard()[nextPos.x][nextPos.y].setType(type);


        if (count > c.getNumber())
            return true;
        return false;
    }

    /**
     * Checks if the adjacent blue cells is greater than the number of the actual cell
     *
     * @param c the actual cell to check
     * @return true if the count is greater
     */
    public boolean excesiveBlue(Cell c) {
        int count = _board.countAdjacent(c.getPos().x, c.getPos().y);
        if (count > c.getNumber())
            return true;
        return false;
    }

    /**
     * If the number of adjacent blues is less than the number of the given cell
     * and the cell is closed it has too much reds
     *
     * @param c the actual cell to check
     * @return true if the count is less
     */
    public boolean excesiveRed(Cell c) {
        int count = 0;
        for (Vector2D dir : _dirs) {
            count += countCellUntilType(c, dir, Cell.Type.Red);
        }

        if (count < c.getNumber()) return true;
        return false;
    }

    /**
     * Counts the number of cells until is found the cell of the given type or
     * it is out of the board
     *
     * @param c    the first cell
     * @param dir  the direction to path
     * @param type the type of the cell to find
     * @return the count of cells pathed
     */
    private int countCellUntilType(Cell c, Vector2D dir, Cell.Type type) {
        int count = 0;
        Vector2D pos = c.getPos();
        if (!_board.outOfBoard(pos)) {
            while (_board.nextCell(pos, dir) != null && _board.nextCell(pos, dir).getType() != type) {
                count++;
                pos = Vector2D.sum(pos, dir);
            }
        }

        return count;
    }

    /**
     * Checks in all directions if there is an adjacent red cell
     *
     * @param c the actual cell
     * @return true if the cell needs to be red
     */
    public boolean checkRed(Cell c) {
        int count = 0;
        for (Vector2D dir : _dirs) {
            Cell next = _board.nextCell(c.getPos(), dir);
            if (next != null && next.getType() != Cell.Type.Red)
                count++;
        }

        if (count <= 0)
            return true;
        return false;
    }

    /**
     * Check if there is an empty cell that must be blue in all cases to close the
     * specified cell and the given direction
     *
     * @param c   the cell to check
     * @param dir the direction to check
     * @return true if a blue is needed in the specified direction
     */
    public boolean mustBeBlue(Cell c, Vector2D dir) {
        return false;
    }

    /**
     * Checks if the puzzle is solved
     *
     * @return true if no empty cell remains
     */
    public boolean isSolved() {
        for (Cell[] column : _board.getBoard()) {
            for (Cell t : column) {
                {
                    if (t.getType() == Cell.Type.Empty)
                        return false;
                }
            }
        }
        return true;
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
