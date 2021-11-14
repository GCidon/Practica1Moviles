package gdv.ohno.logic;

import org.graalvm.compiler.hotspot.nodes.aot.PluginFactory_EncodedSymbolNode;
import org.graalvm.compiler.lir.amd64.vector.AMD64VectorMove;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Input;

public class Board {
    public Board(int size, int width) {
        _board = new Cell[size][size];
        _size = size;
        _windowWidth = width;
        _inix = _iniy = -_windowWidth / 2;
        _completed = 0;
        _moves = new Stack<Vector2D>();
        _fixedReds = new ArrayList<Vector2D>();
    }

    public void GenerateBoard() {
        int cellWidth = (int)(_windowWidth*0.9 / _size);

        //creamos una lista de numeros, indicando el indice de cada casilla
        ArrayList<Integer> listaRnd = new ArrayList<Integer>();
        for (int i = 0; i < _size * _size; i++) {
            listaRnd.add(i);
        }
        //aleatorizamos la lista
        Collections.shuffle(listaRnd);
        int minRed = (_size * _size) / 3;
        int posx, posy;
        int boardx, boardy;
        //rellenamos una porcion de la lista de casillas rojas (porcentaje indicado por minRed
        for (int i = 0; i < minRed; i++) {
            boardx = listaRnd.get(i) / _size;
            boardy = listaRnd.get(i) % _size;
            posx = boardx * cellWidth - (_windowWidth / 2) + (int)(_windowWidth*0.05);
            posy = boardy * cellWidth - (_windowWidth / 2) + (int)(_windowWidth*0.05);
            _board[boardx][boardy] = new Cell(posx, posy, cellWidth, cellWidth, 0, Cell.Type.Red, new Vector2D(boardx, boardy), false);
            _board[boardx][boardy].setLogic(_logic);
            _board[boardx][boardy].setBoard(this);
        }
        //rellenamos el resto de casillas azules
        for (int i = minRed; i < listaRnd.size(); i++) {
            boardx = listaRnd.get(i) / _size;
            boardy = listaRnd.get(i) % _size;
            posx = boardx * cellWidth - (_windowWidth / 2) + (int)(_windowWidth*0.05);
            posy = boardy * cellWidth - (_windowWidth / 2) + (int)(_windowWidth*0.05);
            _board[boardx][boardy] = new Cell(posx, posy, cellWidth, cellWidth, 0, Cell.Type.Blue, new Vector2D(boardx, boardy), false);
            _board[boardx][boardy].setLogic(_logic);
            _board[boardx][boardy].setBoard(this);
        }
        //corregimos las casillas que deberian ser rojas y aleatoriamente asignamos las casillas
        //fijas, tanto azules como rojas
        int nAlrededor;
        Random rnd = new Random();
        for (int j = 0; j < _size; j++) {
            for (int i = 0; i < _size; i++) {
                if (_board[i][j].getType() == Cell.Type.Blue) {
                    nAlrededor = countAdjacent(i, j);
                    if (nAlrededor == 0) {
                        _board[i][j].setType(Cell.Type.Red);
                    } else {
                        int rand = rnd.nextInt(3);
                        if (rand == 1) {
                            _board[i][j].setType(Cell.Type.Blue);
                            _board[i][j].setFixed(true);
                            _board[i][j].setNumber(nAlrededor);
                            _precompleted += 1;
                        }
                    }
                } else {
                    int rand = rnd.nextInt(3);
                    if (rand == 1) {
                        _board[i][j].setType(Cell.Type.Red);
                        _board[i][j].setFixed(true);
                        _precompleted += 1;
                        _fixedReds.add(new Vector2D(i,j));
                    }
                }
            }
        }
        //cambiamos las casillas que no sean fijas por casillas vacias
        for (int j = 0; j < _size; j++) {
            for (int i = 0; i < _size; i++) {
                if (!_board[i][j].isFixed()) {
                    _board[i][j].setType(Cell.Type.Empty);
                }
            }
        }

    }

    public void render(Graphics g) {
        for (int i = 0; i < _board.length; i++) {
            for (int j = 0; j < _board.length; j++) {
                _board[i][j].render(g);
            }
        }
    }

    public void handleInput(Input.TouchEvent e) throws Exception {
        //Transformacion de la pulsacion a posicion del tablero
        int aux1=(int)_logic._engine.getGraphics().getWidth();
        int aux2=(int)_logic._engine.getGraphics().getHeight();
        int aux3 = e.getPosX();
        int aux4 = e.getPosY();
        float ratonx = (e.getPosX() - _logic._engine.getGraphics().getWidth() / 2) - _inix;
        float ratony = (_logic._engine.getGraphics().getHeight() / 2 - e.getPosY()) - _iniy;

        System.out.println(aux1);
        System.out.println(aux2);
        System.out.println(aux3);
        System.out.println(aux4);
        System.out.println(ratonx);
        System.out.println(ratony);
        System.out.println("-------------------------------");

        //Comprobar si la pulsacion esta dentro del tablero
        if (ratony > 0 && ratony < _windowWidth && ratonx > 0 && ratonx < _windowWidth) {
            //Traduccion a casilla especifica
            int casillax = (int) (Math.abs(ratonx / (_windowWidth / _size)));
            int casillay = (int) (Math.abs(ratony / (_windowWidth / _size)));

            //Guardar movimiento
            if (!_board[casillax][casillay].isFixed())
                _moves.push(new Vector2D(casillax, casillay));

            _board[casillax][casillay].handleInput(e);
            //actualiza el contador de complecion
            if (_board[casillax][casillay].getType() == Cell.Type.Empty) _completed -= 1;
            if (_board[casillax][casillay].getType() == Cell.Type.Blue && !_board[casillax][casillay].isFixed()) _completed += 1;
        }
    }

    public boolean CheckWin() {
        //comprobamos si se corresponden los numeros de las casillas azules fijas a las casillas adyacentes
        int n = 0, next = 0;
        for (int j = 0; j < _size; j++) {
            for (int i = 0; i < _size; i++) {
                n = _board[i][j].getNumber();
                if (n > 0) {
                    next = countAdjacent(i, j);
                    if (next != n)
                        return false;
                } else if (n == 0) {
                    //comprobacion de casillas que deberian ser rojas
                    if (checkRed(i, j) && (_board[i][j].getType() != Cell.Type.Red && _board[i][j].getType() != Cell.Type.Red))
                        return false;
                }
            }
        }
        return true;
    }

    public void fillingWin() {
        for (int j = 0; j < _size; j++) {
            for (int i = 0; i < _size; i++) {
                if (_board[i][j].getType() == Cell.Type.Blue) {
                    _board[i][j].setNumber(countAdjacent(i, j));
                    _board[i][j].setFixed(true);

                } else if (_board[i][j].getType() == Cell.Type.Empty || _board[i][j].getType() == Cell.Type.Red) {
                    _board[i][j].setType(Cell.Type.Red);
                    _board[i][j].setFixed(true);
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
     * @param pos the initial pos.
     * @param dir the direction to path in the board.
     * @return the total count.
     */
    int count(Vector2D pos, Vector2D dir) {
        int n = 0;

        Vector2D newPos = Vector2D.sum(pos, dir);
        if (!outOfBoard(newPos)) {
            if (_board[newPos.x][newPos.y].getType() == Cell.Type.Blue) {
                n++;
                n += count(newPos, dir);
            }
        }

        return n;
    }

    //cuenta las casillas azules que ve la casilla indicada en las cuatro direcciones
    int countAdjacent(int x, int y) {
        int res = 0;

        res += count(new Vector2D(x, y), new Vector2D(0, 1));
        res += count(new Vector2D(x, y), new Vector2D(1, 0));
        res += count(new Vector2D(x, y), new Vector2D(-1, 0));
        res += count(new Vector2D(x, y), new Vector2D(0, -1));

        return res;
    }

    //comprueba si la casilla indicada no ve azules fijas, y por lo tanto, tiene que ser roja
    boolean checkRed(int x, int y) {

        if (findFixedBlue(new Vector2D(x, y), new Vector2D(0, 1))) return false;
        if (findFixedBlue(new Vector2D(x, y), new Vector2D(1, 0))) return false;
        if (findFixedBlue(new Vector2D(x, y), new Vector2D(-1, 0))) return false;
        if (findFixedBlue(new Vector2D(x, y), new Vector2D(0, -1))) return false;

        return true;
    }

    //metodo recursivo para encontrar una casilla azul fija en una direccion dada
    boolean findFixedBlue(Vector2D pos, Vector2D dir) {
        Vector2D newPos = Vector2D.sum(pos, dir);
        if (!outOfBoard(newPos)) {
            if (_board[newPos.x][newPos.y].getType() == Cell.Type.Blue && _board[newPos.x][newPos.y].isFixed()) {
                return true;
            } else if(_board[newPos.x][newPos.y].getType() == Cell.Type.Red) return false;
            else return findFixedBlue(newPos, dir);
        }
        return false;
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

    public boolean outOfBoard(Vector2D pos) {
        if (pos.x < 0 || pos.x >= _size || pos.y < 0 || pos.y >= _size)
            return true;
        else return false;
    }

    public int getCompletedPercentage() {
        return (_completed * 100) / ((_size * _size) - _precompleted);
    }

    public void undoMove() {
        if (!_moves.empty()) {
            Vector2D move = _moves.pop();
            _board[move.x][move.y].changeColor();
            _board[move.x][move.y].changeColor();
            if (_board[move.x][move.y].getType() == Cell.Type.Empty) _completed -= 1;
            if (_board[move.x][move.y].getType() == Cell.Type.Blue) _completed += 1;
        }
    }

    public void clickFixedReds() {
        if(!_fixedReds.isEmpty()) {
            for (Vector2D c : _fixedReds) _board[c.x][c.y].setClicked();
        }
    }

    public boolean areFixedClicked() {
        if(!_fixedReds.isEmpty()) return _board[_fixedReds.get(0).x][_fixedReds.get(0).y].isClicked();
        else return false;
    }

    private int _inix;
    private int _iniy;

    int _completed;
    int _precompleted;

    Stack<Vector2D> _moves;
    private Logic _logic;
    private Cell[][] _board;
    private ArrayList<Vector2D> _fixedReds;
    private int _size;
    private int _windowWidth;
}