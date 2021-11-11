package gdv.ohno.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
        int size = 10 * (10 - _boardSize);
        if (size < 30) size = 30;
        _fonts[0] = _engine.getGraphics().newFont("fonts/JosefinSans-Bold.ttf", size, true);
        _fonts[1] = _engine.getGraphics().newFont("fonts/JosefinSans-Bold.ttf", 20, true);
        _fonts[2] = _engine.getGraphics().newFont("fonts/JosefinSans-Bold.ttf", size/2, true);

        _images[0] = _engine.getGraphics().newImage("sprites/eye.png");
        _images[1] = _engine.getGraphics().newImage("sprites/close.png");
        _images[2] = _engine.getGraphics().newImage("sprites/history.png");
        _images[3] = _engine.getGraphics().newImage("sprites/lock.png");

        _buttons.add(new Button(-125, 275, 50, 50, "cancel", _logica));
        _buttons.add(new Button(-25, 275, 50, 50, "undo", _logica));
        _buttons.add(new Button(75, 275, 50, 50, "hint", _logica));

        _board = new Board(_boardSize, (int) _engine.getGraphics().getBaseWidth());
        _board.setLogic(_logica);
        _manager = new HintManager(_board);

        //_manager.solve();
        //_board.fillingWin();
    }

    public void render(Graphics g) {
        g.clear(0xFFFFFFFF);

        g.setFont(_fonts[0]);
        g.setColor(0xFF000000);
        String text;
        if (!_end && !_hint) {
            //texto de tamaño de tablero
            text = _board.getSize() + "x" + _board.getSize();
            g.drawText(text, (int) (-40), (int) (-220));
        } else if (_hint && !_end) {
            g.setFont(_fonts[2]);
            String[] ss = _hintText.split("\n");
            for(int i = 0; i < ss.length; i++) {
                g.drawText(ss[i], ss[i].length()*-6, -220+i*30);
            }
        } else {
            //texto de ganar partida
            text = "Well done!";
            g.drawText(text, (int) (-120), (int) (-200));
        }

        //texto de porcentaje
        g.setFont(_fonts[1]);
        g.setColor(0xFFC1C1C1);
        String completedText = _board.getCompletedPercentage() + "%";
        g.drawText(completedText, (int) (-10), (int) (215));

        //imagenes de botones
        g.save();
        g.scale(-1);
        g.drawImage(_images[0], -75, -220, -50, -50);
        g.drawImage(_images[1], 125, -220, -50, -50);
        g.drawImage(_images[2], 25, -220, -50, -50);
        g.restore();

        //tablero
        g.setFont(_fonts[0]);
        _board.render(g);

        for (int i = 0; i < _buttons.size(); i++) {
            _buttons.get(i).render(g);
        }
    }

    public void handleInput(List<Input.TouchEvent> te) throws Exception {
        for (Input.TouchEvent e : te) {
            _hint = false;
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

    public void getHint() {
        _hintText = _manager.getHint().getText();
        _hint = true;
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

    List<Button> _buttons;
    Font _fonts[] = new Font[3];
    Image _images[] = new Image[5];
    Board _board;
    HintManager _manager;
    String _hintText = "";

    Engine _engine;
    Logic _logica;
}
