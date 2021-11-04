package gdv.ohno.logic;

import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Input;

public class Board {
    public Board(int size, int width){
        _board=new Cell[size][size];
        _size=size;
        _windowWidth = width;
        _inix = _iniy = -_windowWidth/2;
    }
    public int NextTo(int x,int y){
        int next = 0;
        int left = x-1, right = x+1,up = y-1, down = y+1;

        boolean leftC = true, rightC = true, upC = true, downC = true;
        while (left >= 0 && leftC)
        {
            switch (_board[left][y].getType())
            {
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
        while (right < _size && rightC)
        {
            switch (_board[right][y].getType())
            {
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
        while (up >=0 && upC)
        {
            switch (_board[x][up].getType())
            {
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
        while (down < _size && downC)
        {
            switch (_board[x][down].getType())
            {
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
    public void GenerateBoard(){
        int cellWidth = _windowWidth/_size;
        for(int i = 0; i < _board.length; i++) {
            for(int j = 0; j < _board[i].length; j++) {
                _board[i][j] = new Cell(i*cellWidth-(_windowWidth/2),j*cellWidth-(_windowWidth/2), cellWidth, cellWidth, j, Cell.Type.Empty);
                _board[i][j].setLogic(_logic);
            }
        }
    }
    public void render(Graphics g)
    {
        for(int i = 0; i < _board.length; i++) {
            for(int j = 0; j < _board[i].length; j++) {
                _board[i][j].render(g);
            }
        }
    }
    public void handleInput(Input.TouchEvent e) throws Exception {
        //Transformacion de la pulsacion a posicion del tablero
        float ratonx = (e.getPosX()-_logic._engine.getGraphics().getWidth()/2)-_inix;
        float ratony = (_logic._engine.getGraphics().getHeight()/2-e.getPosY())-_iniy;

        //Comprobar si la pulsacion esta dentro del tablero
        if(ratony > 0 && ratony < _windowWidth) {
            //Traduccion a casilla especifica
            int casillax = (int) (Math.abs(ratonx / (_windowWidth / _size)));
            int casillay = (int) (Math.abs(ratony / (_windowWidth / _size)));

            _board[casillax][casillay].handleInput(e);
        }
    }
    public boolean CheckWin(){
        int n = 0, next = 0;
        for (int i = 0; i < _size; i++)
        {
            for (int j = 0; j < _size; j++)
            {
                n =_board[i][j].getNumber();
                if (n > 0)
                {
                    next = NextTo( i, j);
                    if (next != n)
                        return false;

                }

            }
        }
        return true;
    }
    
    public void fillingWin()
    {
        for (int i = 0; i < _size; i++)
        {
            for (int j = 0; j < _size; j++)
            {
                if (_board[i][j].getType() == Cell.Type.Blue)
                {
                    _board[i][j].setNumber(NextTo(i,j));
                    _board[i][j].setType(Cell.Type.FixedBlue);

                }
                else if (_board[i][j].getType() == Cell.Type.Empty || _board[i][j].getType() == Cell.Type.Red )
                {
                    _board[i][j].setType(Cell.Type.FixedRed);
                }

            }
        }
    }

    public int getSize() { return _size; }

    public void setLogic(Logic logic) {_logic = logic;}

    private int _inix;
    private int _iniy;

    private Logic _logic;
    private Cell[][] _board;
    private int _size;
    private int _windowWidth;
}
