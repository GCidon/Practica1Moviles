package gdv.ohno.logic;

import java.util.List;

import gdv.ohno.engine.Engine;
import gdv.ohno.engine.Font;
import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Image;
import gdv.ohno.engine.Input;

public class Logic implements gdv.ohno.engine.Logic {

    public Logic() {

    }

    public void update(float deltaTime) {

    }
    public void render(Graphics g) throws Exception {
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
        for(Input.TouchEvent e: te) {
            _board.handleInput(e);
        }
    }
    public void init() throws Exception {
        _engine.getGraphics().setBaseWidth(400);
        _engine.getGraphics().setBaseHeight(600);

        _fonts[0] = _engine.getGraphics().newFont("fonts/JosefinSans-Bold.ttf", 50, true);
        _fonts[1] = _engine.getGraphics().newFont("fonts/Molle-Regular.ttf", 30, false);

        _images[0] = _engine.getGraphics().newImage("sprites/eye.png");
        _images[1] = _engine.getGraphics().newImage("sprites/close.png");
        _images[2] = _engine.getGraphics().newImage("sprites/history.png");

        _board = new Board(4, (int)_engine.getGraphics().getWidth());
        _board.setLogic(this);
        _board.GenerateBoard();
    }

    //diferentes acciones para botones
    public void processButton(String action) {
        switch (action) {
            default:
                break;
        }
    }

    public void getEngine(Engine engine) {
        _engine = engine;
    }
    public Board getBoard() {
        return _board;
    }

    Engine _engine;
    private Board _board;
    Font _fonts[] = new Font[6];
    Image _images[] = new Image[5];

}