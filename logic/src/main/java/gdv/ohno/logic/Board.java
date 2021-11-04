package gdv.ohno.logic;

import java.util.Vector;

import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Input;

public class Board {
    public Board(int size, int width) {
        _board = new Cell[size][size];
        _size = size;
        _windowWidth = width;
        _inix = _iniy = -_windowWidth / 2;
    }

    public int NextTo(int x, int y) {
        int next = 0;
        int left = x - 1, right = x + 1, up = y - 1, down = y + 1;

        boolean leftC = true, rightC = true, upC = true, downC = true;
        while (left >= 0 && leftC) {
            switch (_board[left][y].getType()) {
                case Blue:
                case FixedBlue:
                    next++;
                    left--;
                    break;
                case Empty:
                case Red:
                case FixedRed:
                    leftC = false;
                    break;
            }
        }
        while (right < _size && rightC) {
            switch (_board[right][y].getType()) {
                case Blue:
                case FixedBlue:
                    next++;
                    right++;
                    break;
                case Empty:
                case Red:
                case FixedRed:
                    rightC = false;
                    break;
            }
        }
        while (up >= 0 && upC) {
            switch (_board[x][up].getType()) {
                case Blue:
                case FixedBlue:
                    next++;
                    up--;
                    break;
                case Empty:
                case Red:
                case FixedRed:
                    upC = false;
                    break;
            }
        }
        while (down < _size && downC) {
            switch (_board[x][down].getType()) {
                case Blue:
                case FixedBlue:
                    next++;
                    down++;
                    break;
                case Empty:
                case Red:
                case FixedRed:
                    downC = false;
                    break;
            }
        }

        return next;
    }

    public void GenerateBoard() {
        int cellWidth = _windowWidth / _size;
        for (int i = 0; i < _board.length; i++) {
            for (int j = 0; j < _board[i].length; j++) {
                _board[i][j] = new Cell(i * cellWidth - (_windowWidth / 2), j * cellWidth - (_windowWidth / 2), cellWidth, cellWidth, j, Cell.Type.Empty);
                _board[i][j].setLogic(_logic);
            }
        }

        _board[0][2] = new Cell(0 * cellWidth - (_windowWidth / 2), 2 * cellWidth - (_windowWidth / 2), cellWidth, cellWidth, 0, Cell.Type.Red);
        _board[1][3] = new Cell(1 * cellWidth - (_windowWidth / 2), 3 * cellWidth - (_windowWidth / 2), cellWidth, cellWidth, 0, Cell.Type.Red);
        _board[1][1] = new Cell(1 * cellWidth - (_windowWidth / 2), 1 * cellWidth - (_windowWidth / 2), cellWidth, cellWidth, 1, Cell.Type.Blue);
        _board[2][0] = new Cell(2 * cellWidth - (_windowWidth / 2), 0 * cellWidth - (_windowWidth / 2), cellWidth, cellWidth, 2, Cell.Type.Blue);
        _board[3][0] = new Cell(3 * cellWidth - (_windowWidth / 2), 0 * cellWidth - (_windowWidth / 2), cellWidth, cellWidth, 4, Cell.Type.Blue);
        _board[2][2] = new Cell(2 * cellWidth - (_windowWidth / 2), 2 * cellWidth - (_windowWidth / 2), cellWidth, cellWidth, 2, Cell.Type.Blue);
    }

    public void render(Graphics g) {
        for (int i = 0; i < _board.length; i++) {
            for (int j = 0; j < _board[i].length; j++) {
                _board[i][j].render(g);
            }
        }
    }

    public void handleInput(Input.TouchEvent e) throws Exception {
        //Transformacion de la pulsacion a posicion del tablero
        float ratonx = (e.getPosX() - _logic._engine.getGraphics().getWidth() / 2) - _inix;
        float ratony = (_logic._engine.getGraphics().getHeight() / 2 - e.getPosY()) - _iniy;

        //Comprobar si la pulsacion esta dentro del tablero
        if (ratony > 0 && ratony < _windowWidth) {
            //Traduccion a casilla especifica
            int casillax = (int) (Math.abs(ratonx / (_windowWidth / _size)));
            int casillay = (int) (Math.abs(ratony / (_windowWidth / _size)));

            _board[casillax][casillay].handleInput(e);
        }
    }

    public boolean CheckWin() {
        int n = 0, next = 0;
        for (int i = 0; i < _size; i++) {
            for (int j = 0; j < _size; j++) {
                n = _board[i][j].getNumber();
                if (n > 0) {
                    next = NextTo(i, j);
                    if (next != n)
                        return false;

                }

            }
        }
        return true;
    }

    public void fillingWin() {
        for (int i = 0; i < _size; i++) {
            for (int j = 0; j < _size; j++) {
                if (_board[i][j].getType() == Cell.Type.Blue) {
                    _board[i][j].setNumber(NextTo(i, j));
                    _board[i][j].setType(Cell.Type.FixedBlue);

                } else if (_board[i][j].getType() == Cell.Type.Empty || _board[i][j].getType() == Cell.Type.Red) {
                    _board[i][j].setType(Cell.Type.FixedRed);
                }

            }
        }
    }

    public int getSize() {
        return _size;
    }

    /**
     * Returns the count of the specified cell type in the specified direction from the origin position
     * It goes recursively through all the board in the specified direction.
     *
     * @param pos  the initial pos.
     * @param dir  the direction to path in the board.
     * @param type the cell type to look for.
     * @return the total count.
     */
    public int countAdjacent(Vector2D pos, Vector2D dir, Cell.Type type) {
        int n = 0;

        Vector2D newPos = Vector2D.sum(pos, dir);
        if (!outOfBoard(newPos)) {
            if (_board[newPos.x][newPos.y].getType() == type) {
                n++;
                n += countAdjacent(newPos, dir, type);
            }
        }

        return n;
    }

    /**
     * Looks for the cell of the given type in the specified direction
     *
     * @param pos  the initial pos
     * @param dir  the direction to path
     * @param type the cell type to look for
     * @return the first cell of the given type in the specified direction
     */
    public Cell lookForCell(Vector2D pos, Vector2D dir, Cell.Type type) {
        Cell c = null;

        Vector2D newPos = Vector2D.sum(pos, dir);

        if (!outOfBoard(newPos)) {
            while (!outOfBoard(newPos)) {
                newPos = Vector2D.sum(newPos, dir);
                if (_board[newPos.x][newPos.y].getType() == type) {
                    c = _board[newPos.x][newPos.y];
                }
            }
        }

        return c;
    }

    /**
     * Gets the next cell in the given direction
     *
     * @param pos
     * @param dir
     * @return
     */
    public Cell nextCell(Vector2D pos, Vector2D dir) {
        Cell c = null;

        Vector2D newPos = Vector2D.sum(pos, dir);

        if (!outOfBoard(newPos)) {
            c = _board[newPos.x][newPos.y];
        }

        return c;
    }

    public Cell[][] getBoard() {
        return _board;
    }

    public void setLogic(Logic logic) {
        _logic = logic;
    }

    private boolean outOfBoard(Vector2D pos) {
        if (pos.x < 0 || pos.x >= _size || pos.y < 0 || pos.y >= _size)
            return true;
        else return false;
    }

    private int _inix;
    private int _iniy;

    private Logic _logic;
    private Cell[][] _board;
    private int _size;
    private int _windowWidth;
}

class Vector2D {
    public int x, y;

    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2D sum(Vector2D a, Vector2D b) {
        return new Vector2D(a.x + b.x, a.y + b.y);
    }
}
