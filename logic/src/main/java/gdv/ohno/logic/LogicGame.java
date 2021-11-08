package gdv.ohno.logic;

import java.util.ArrayList;
import java.util.List;

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
        _fonts[0] = _engine.getGraphics().newFont("fonts/JosefinSans-Bold.ttf", 50, true);
        _fonts[1] = _engine.getGraphics().newFont("fonts/Molle-Regular.ttf", 30, false);

        _images[0] = _engine.getGraphics().newImage("sprites/eye.png");
        _images[1] = _engine.getGraphics().newImage("sprites/close.png");
        _images[2] = _engine.getGraphics().newImage("sprites/history.png");

        _board = new Board(_boardSize, (int) _engine.getGraphics().getWidth());
        _board.setLogic(_logica);
        //_board.GenerateBoard();
        _manager = new HintManager(_board);

        _manager.solve();
    }

    public void render(Graphics g) {
        g.clear(0xFFFFFFFF);

        g.setFont(_fonts[0]);
        g.setColor(0xFF000000);
        String sizeText = _board.getSize() + "x" + _board.getSize();
        g.drawText(sizeText, (int) (-40), (int) (-220));

        g.save();
        g.scale(-1);
        g.drawImage(_images[0], -75, -220, -50, -50);
        g.drawImage(_images[1], 125, -220, -50, -50);
        g.drawImage(_images[2], 25, -220, -50, -50);
        g.restore();

        _board.render(g);
    }

    public void handleInput(List<Input.TouchEvent> te) throws Exception {
        for (Input.TouchEvent e : te) {
            _board.handleInput(e);
        }
    }

    public void getEngine(Engine engine) {
        _engine = engine;
    }

    public Board getBoard() {
        return _board;
    }

    int _boardSize;

    List<Button> _buttons;
    Font _fonts[] = new Font[2];
    Image _images[] = new Image[5];
    Board _board;
    HintManager _manager;

    Engine _engine;
    Logic _logica;
}
