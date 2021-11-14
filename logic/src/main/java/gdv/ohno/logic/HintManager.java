package gdv.ohno.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class HintManager {
    public HintManager(Board board) {
        _board = board;
        _hints = new ArrayList<>();
    }

    public Hint getHint() {
        _hints.clear();

        for (Cell[] column : _board.getBoard()) {
            for (Cell c : column) {
                // Para las pistas de las celdas azules fijas del mapa
                if ((c.getType() == Cell.Type.Blue) && c.getNumber() > 0) {
                    // PISTA 1
                    if (fillWithWalls(c)) {
                        _hints.add(new Hint(1, c.getPos()));
                    } else {
                        for (Vector2D dir : _dirs) {
                            // PISTA 2
                            // si el numero de azules despues de poner en azul una celda gris, supera
                            // el numero indicado por la celda c es porque la siguiente en esa dir tiene que ser roja
                            if (greaterThanNumber(c, dir)) {
                                _hints.add(new Hint(2, c.getPos()));
                            }
                            // PISTA 3
                            if (mustBeBlue(c, dir)) {
                                _hints.add(new Hint(3, c.getPos()));
                            }
                            // PISTA 8
                            if (oneDirectionLeft(c, dir)) {
                                _hints.add(new Hint(8, c.getPos()));
                            }
                        }
                    }
                    // PISTAS 4
                    if (excesiveBlue(c)) {
                        _hints.add(new Hint(4, c.getPos()));
                    }
                    // PISTA 5
                    else if (excesiveRed(c) || _board.countAdjacent(c.getPos().x, c.getPos().y) < c.getNumber()) {
                        _hints.add(new Hint(5, c.getPos()));
                    }
                }
                // Pistas de las celdas grises
                else if (c.getType() == Cell.Type.Empty) {
                    // PISTA 6
                    if (checkRed(c)) {
                        _hints.add(new Hint(6, c.getPos()));
                    }
                } else if (c.getType() == Cell.Type.Blue) {
                    // PISTA 7
                    if (checkRed(c)) {
                        _hints.add(new Hint(7, c.getPos()));
                    } else if (mustBeRed(c)) {
                        _hints.add(new Hint(9, c.getPos()));
                    }
                }
            }
        }

        int h = rnd.nextInt(_hints.size());

        if (!_hints.isEmpty())
            return _hints.get(h);

        return null;
    }


    /**
     * Check if the given cell has all the blue cells comparing with the number of the specified cell.
     *
     * @param c Cell to check.
     * @return true if the count of all the adjacent blue cells is equal to the number of the given cell.
     */
    private boolean fillWithWalls(Cell c) {
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
                Vector2D pos = c.getPos();
                Cell aux = _board.nextCell(pos, dir);
                while (aux != null) {
                    if (aux.getType() == Cell.Type.Empty)
                        _board.getBoard()[aux.getPos().x][aux.getPos().y].setType(Cell.Type.Red);
                    pos = Vector2D.sum(pos, dir);
                    aux = _board.nextCell(pos, dir);
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
    private boolean greaterThanNumber(Cell c, Vector2D dir) {
        // Comprueba los adyacentes y si hay un gris que si se transforma en azul supera
        // el numero de la celda observada devuelve true
        int count = 0;
        Vector2D nextPos = Vector2D.sum(c.getPos(), dir);
        // se comprueba si la siguiente celda esta dentro del tablero
        while (!_board.outOfBoard(nextPos) && _board.getBoard()[nextPos.x][nextPos.y].getType() != Cell.Type.Red) {
            Cell.Type type = _board.getBoard()[nextPos.x][nextPos.y].getType();

            // si es gris se cambia a azul y se cuentan las adyacentes de la celda inicial
            if (type == Cell.Type.Empty) {
                _board.getBoard()[nextPos.x][nextPos.y].setType(Cell.Type.Blue);
                count = _board.countAdjacent(c.getPos().x, c.getPos().y);
            }
            // se devuelve la celda al color inicial
            _board.getBoard()[nextPos.x][nextPos.y].setType(type);

            // Si la celda es azul sigue hasta que encuentre una gris
            nextPos = Vector2D.sum(nextPos, dir);

            if (count > c.getNumber())
                return true;
        }
        return false;
    }

    /**
     * Checks if the adjacent blue cells is greater than the number of the actual cell
     *
     * @param c the actual cell to check
     * @return true if the count is greater
     */
    private boolean excesiveBlue(Cell c) {
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
    private boolean excesiveRed(Cell c) {
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
    private boolean checkRed(Cell c) {
        int count = 0;
        for (Vector2D dir : _dirs) {
            Cell next = _board.nextCell(c.getPos(), dir);
            if (next != null && next.getType() != Cell.Type.Red) {
                count++;
            }
        }
        if (count <= 0)
            return true;
        return false;
    }

    private boolean mustBeRed(Cell c) {
        boolean hadFBlue = false;
        for (Vector2D dir : _dirs) {
            Cell next = _board.nextCell(c.getPos(), dir);
            while (next != null && next.getType() != Cell.Type.Red && !hadFBlue) {
                if (next.isFixed())
                    hadFBlue = true;
                next = _board.nextCell(next.getPos(), dir);
            }
        }


        if (!hadFBlue)
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
    private boolean mustBeBlue(Cell c, Vector2D dir) {
        int count = 0;

        Vector2D nextPos = Vector2D.sum(c.getPos(), dir);
        if (_board.outOfBoard(nextPos)) return false;
        if (_board.getBoard()[nextPos.x][nextPos.y].getType() == Cell.Type.Blue) return false;

        for (Vector2D otherDir : _dirs) {
            if (!Vector2D.isEqual(dir, otherDir)) {
                count += countCellUntilType(c, otherDir, Cell.Type.Red);
            }
        }


        if (count < c.getNumber())
            return true;
        return false;
    }

    /**
     * Checks if the given cell has only one direction left to mark blues on.
     *
     * @param c
     * @param dir
     * @return
     */
    private boolean oneDirectionLeft(Cell c, Vector2D dir) {
        Cell aux = null;
        Vector2D nextPos = Vector2D.sum(c.getPos(), dir);
        if (_board.outOfBoard(nextPos) || _board.getBoard()[nextPos.x][nextPos.y].getType() == Cell.Type.Red)
            return false;

        for (Vector2D other : _dirs) {
            // busca en las otras 3 direcciones y si
            // encuentra una gris existe mas de una direccion posible
            if (!Vector2D.isEqual(dir, other)) {
                aux = _board.lookForCell(c.getPos(), other, Cell.Type.Empty);
                if (aux != null)
                    return false;
            }
        }

        if (aux == null && _board.countAdjacent(c.getPos().x, c.getPos().y) < c.getNumber())
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
    private ArrayList<Hint> _hints;
    private Random rnd = new Random();
}
