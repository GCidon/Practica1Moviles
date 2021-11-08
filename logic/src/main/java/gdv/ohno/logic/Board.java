package gdv.ohno.logic;

import org.graalvm.compiler.hotspot.nodes.aot.PluginFactory_EncodedSymbolNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
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
    }

    public void GenerateBoard() {
        int cellWidth = _windowWidth/_size;

        ArrayList<Integer> movida = new ArrayList<Integer>();
        for(int i = 0; i < _size*_size; i++) {
            movida.add(i);
        }
        Collections.shuffle(movida);
        int minRed = (_size*_size) / 3;
        int posx, posy;
        int boardx, boardy;
        for(int i = 0; i < minRed; i++) {
            boardx = movida.get(i)/_size;
            boardy = movida.get(i)%_size;
            posx = boardx*cellWidth-(_windowWidth/2);
            posy = boardy*cellWidth-(_windowWidth/2);
            _board[boardx][boardy] = new Cell(posx, posy, cellWidth, cellWidth, 0, Cell.Type.Red);
            _board[boardx][boardy].setLogic(_logic);
        }
        for(int i = minRed; i < movida.size(); i++) {
            boardx = movida.get(i)/_size;
            boardy = movida.get(i)%_size;
            posx = boardx*cellWidth-(_windowWidth/2);
            posy = boardy*cellWidth-(_windowWidth/2);
            _board[boardx][boardy] = new Cell(posx, posy, cellWidth, cellWidth, 0, Cell.Type.Blue);
            _board[boardx][boardy].setLogic(_logic);
        }

        int nAlrededor;
        Random rnd = new Random();
        for (int j = 0; j < _size; j++) {
            for (int i = 0; i < _size; i++) {
                if(_board[i][j].getType() == Cell.Type.Blue) {
                    nAlrededor = countAdjacent(i, j);
                    if(nAlrededor==0) {
                        _board[i][j].setType(Cell.Type.Red);
                    } else {
                        int rand = rnd.nextInt(3);
                        if(rand == 1) {
                            _board[i][j].setType(Cell.Type.FixedBlue);
                            _board[i][j].setNumber(nAlrededor);
                            _completed+=1;
                        }
                    }
                }
                else {
                    int rand = rnd.nextInt(3);
                    if(rand == 1) {
                        _board[i][j].setType(Cell.Type.FixedRed);
                        _completed+=1;
                    }
                }
            }
        }

        for (int j = 0; j < _size; j++) {
            for (int i = 0; i < _size; i++) {
                if(_board[i][j].getType() != Cell.Type.FixedBlue && _board[i][j].getType() != Cell.Type.FixedRed){
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
        for (int j = 0; j < _size; j++) {
            for (int i = 0; i < _size; i++) {
                n = _board[i][j].getNumber();
                if (n > 0) {
                    next = countAdjacent(i, j);
                    if (next != n)
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
                    _board[i][j].setType(Cell.Type.FixedBlue);

                } else if (_board[i][j].getType() == Cell.Type.Empty || _board[i][j].getType() == Cell.Type.Red) {
                    _board[i][j].setType(Cell.Type.FixedRed);
                }

            }
        }
    }

    public int getSize() { return _size; }
    /**
     *  Returns the count of the specified cell type in the specified direction from the origin position
     *  It goes recursively through all the board in the specified direction.
     * @param pos   the initial pos.
     * @param dir   the direction to path in the board.
     *
     * @return  the total count.
     */
    int count(Vector2D pos, Vector2D dir) {
        int n = 0;

        Vector2D newPos = Vector2D.sum(pos, dir);
        if (!outOfBoard(newPos)) {
            if (_board[newPos.x][newPos.y].getType() == Cell.Type.Blue || _board[newPos.x][newPos.y].getType() == Cell.Type.FixedBlue) {
                n++;
                n += count(newPos, dir);
            }
        }

        return n;
    }

    int countAdjacent(int x, int y) {
        int res = 0;

        res += count(new Vector2D(x, y), new Vector2D(0,1));
        res += count(new Vector2D(x, y), new Vector2D(1,0));
        res += count(new Vector2D(x, y), new Vector2D(-1,0));
        res += count(new Vector2D(x, y), new Vector2D(0,-1));

        return res;
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

    int _completed;

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
