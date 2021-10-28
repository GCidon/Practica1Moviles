package gdv.ohno.logic;

import java.util.List;

import gdv.ohno.engine.Engine;
import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Input;

public class Logic implements gdv.ohno.engine.Logic {

    public Logic() {

    }

    public void update(float deltaTime) {

    }
    public void render(Graphics g) throws Exception {
        g.clear(0xFF000000);
        g.setColor(0xFFFFAAFF);
        g.fillRect(0,0, 100, 100);
    }
    public void handleInput(List<Input.TouchEvent> te) throws Exception {

    }
    public void init() throws Exception {
        _engine.getGraphics().setBaseWidth(640);
        _engine.getGraphics().setBaseHeight(480);
    }
    public void getEngine(Engine engine) {
        _engine = engine;
    }

    Engine _engine;

}