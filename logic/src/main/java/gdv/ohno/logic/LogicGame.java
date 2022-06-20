package gdv.ohno.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import gdv.ohno.engine.Engine;
import gdv.ohno.engine.Font;
import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Image;
import gdv.ohno.engine.Input;

public class LogicGame {
    LogicGame(Engine engine, Logic logic, int size) {
        _engine = engine;
        _logica = logic;
        _boardSize = size;
        _buttons = new ArrayList();
    }

    public void init() throws Exception {
        //Auxiliares para tamaños de fuente
        int size = 10 * (10 - _boardSize);
        if (size < 30) size = 30;

        //Fuentes
        _fonts[0] = _engine.getGraphics().newFont("fonts/JosefinSans-Bold.ttf", size, true);
        _fonts[1] = _engine.getGraphics().newFont("fonts/JosefinSans-Bold.ttf", 20, true);
        _fonts[2] = _engine.getGraphics().newFont("fonts/JosefinSans-Bold.ttf", size / 2, true);
        _fonts[3] = _engine.getGraphics().newFont("fonts/JosefinSans-Bold.ttf", 50, true);

        //Imagenes
        _images[0] = _engine.getGraphics().newImage("sprites/eye.png");
        _images[1] = _engine.getGraphics().newImage("sprites/close.png");
        _images[2] = _engine.getGraphics().newImage("sprites/history.png");
        _images[3] = _engine.getGraphics().newImage("sprites/lock.png");

        //Botones
        _buttons.add(new Button(-125, 275, 50, 50, "cancel", _logica));
        _buttons.add(new Button(-25, 275, 50, 50, "undo", _logica));
        _buttons.add(new Button(75, 275, 50, 50, "hint", _logica));

        int relatedSize;
        relatedSize = (int)_engine.getGraphics().getBaseWidth();

        _board = new Board(_boardSize, relatedSize);
        _board.setLogic(_logica);
        _board.GenerateBoard();

        _manager = new HintManager(_board);
    }

    public void render(Graphics g) {
        g.clear(0xFFFFFFFF);

        g.setFont(_fonts[3]);
        g.setColor(0xFF000000);
        String text;
        if (!_end && !_hint) {
            //Texto de tamaño de tablero
            text = _board.getSize() + "x" + _board.getSize();
            g.drawText(text, text.length() * -12, (int)-g.getBaseHeight()/3-10);
        } else if (_hint && !_end) {
            //Texto de pista o deshacer
            g.setFont(_fonts[1]);
            String[] ss = _hintText.split("\n");
            for (int i = 0; i < ss.length; i++) {
                g.drawText(ss[i], ss[i].length() * -5, -250 + i * 30);
            }
        } else {
            //Texto de ganar partida
            g.setFont((_fonts[3]));
            text = "¡Bien hecho!";
            g.drawText(text, text.length() * -12, (int)-g.getBaseHeight()/3-10);
        }

        //Texto de porcentaje
        g.setFont(_fonts[1]);
        g.setColor(0xFFC1C1C1);
        String completedText = _board.getCompletedPercentage() + "%";
        g.drawText(completedText, (int) (-10), (int) (215));

        //Imagenes de botones
        g.save();
        g.scale(-1);
        g.rotate(180);
        g.drawImage(_images[0], 75, 220, 50, 50);
        g.drawImage(_images[1], -125, 220, 50, 50);
        g.drawImage(_images[2], -25, 220, 50, 50);
        g.restore();

        //Tablero
        g.setFont(_fonts[0]);
        _board.render(g);

        //Necesario para escalado
        for (int i = 0; i < _buttons.size(); i++) {
            _buttons.get(i).render(g);
        }
    }

    public void handleInput(List<Input.TouchEvent> te) {
        for (Input.TouchEvent e : te) {
            if(_undo) {
                _hint = false;
                _undo = false;
                if(_lastHinted.x != -1) _board.getBoard()[_lastHinted.x][_lastHinted.y].setHinted(_hint);
            }
            if (_hint) {
                _hint = false;
                _board.getBoard()[_lastHinted.x][_lastHinted.y].setHinted(_hint);
            }
            _board.handleInput(e);
            for (int i = 0; i < _buttons.size(); i++) {
                _buttons.get(i).handleInput(e);
            }
        }
    }

    public void getEngine(Engine engine) {
        _engine = engine;
    }

    public Board getBoard() {
        return _board;
    }

    //Devuelve una pista aleatoria, activa la casilla correspondiente y cambia el texto
    public void getHint() {
        Hint hint = _manager.getHint();
        _hintText = hint.getText();
        _lastHinted = hint.getPos();
        _hint = true;
        _board.getBoard()[_lastHinted.x][_lastHinted.y].setHinted(_hint);
    }

    //Deshace el ultimo movimiento, activa la casilla correspondiente y cambia el texto
    public void getUndo(int type, Vector2D pos) {
        Undo undo = new Undo(type, pos);
        _hintText = undo.getText();
        _hint = true;
        _undo = true;
        _lastHinted = pos;
        if(pos.x != -1) _board.getBoard()[pos.x][pos.y].setHinted(_undo);
    }

    public void setEnd(boolean aux) {
        _end = aux;
    }

    public Image getImage(int n) {
        return _images[n];
    }

    int _boardSize;

    boolean _end = false;
    boolean _hint = false;
    boolean _undo = false;

    List<Button> _buttons;
    Font _fonts[] = new Font[4];
    Image _images[] = new Image[5];
    Board _board;
    HintManager _manager;
    String _hintText = "";
    Vector2D _lastHinted = null;

    Engine _engine;
    Logic _logica;
}
