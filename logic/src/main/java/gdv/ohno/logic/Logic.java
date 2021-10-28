package gdv.ohno.logic;

import java.util.List;

import gdv.ohno.engine.Engine;
import gdv.ohno.engine.Font;
import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Input;

public class Logic implements gdv.ohno.engine.Logic {

    public Logic() {

    }

    public void update(float deltaTime) {

    }
    public void render(Graphics g) throws Exception {
        g.clear(0xFFFFFFFF);

        //g.setFont(_fonts[0]);
        //g.setColor(0xFFFFFFFF);
        //g.drawText("Level " + (_level + 1) + " - " + nivelActual._name, (int) (-300), (int) (-203));

        _board.render(g);
    }
    public void handleInput(List<Input.TouchEvent> te) throws Exception {

    }
    public void init() throws Exception {
        _engine.getGraphics().setBaseWidth(400);
        _engine.getGraphics().setBaseHeight(600);

        _board = new Board(2, (int)_engine.getGraphics().getWidth());
        _board.GenerateBoard();
    }
    public void getEngine(Engine engine) {
        _engine = engine;
    }

    Engine _engine;
    Board _board;
    Font _fonts[] = new Font[6];

}