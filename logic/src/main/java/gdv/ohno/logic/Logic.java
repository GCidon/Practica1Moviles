package gdv.ohno.logic;

import java.util.List;

import gdv.ohno.engine.Engine;
import gdv.ohno.engine.Font;
import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Image;
import gdv.ohno.engine.Input;

public class Logic implements gdv.ohno.engine.Logic {

    enum GameState {MENU, LEVEL};

    public Logic() {

    }

    public void update(float deltaTime) {

    }

    public void render(Graphics g) throws Exception {
        switch (_state) {
            case MENU:
                _logicMenu.render(g);
                break;
            case LEVEL:
                _logicGame.render(g);
                break;
        }
    }
    public void handleInput(List<Input.TouchEvent> te) throws Exception {
        switch (_state) {
            case MENU:
                _logicMenu.handleInput(te);
                break;
            case LEVEL:
                _logicGame.handleInput(te);
                break;
        }
    }
    public void init() throws Exception {
        _engine.getGraphics().setBaseWidth(400);
        _engine.getGraphics().setBaseHeight(600);

        startGame(4);
    }

    public void startMenu() throws Exception {
        _state = GameState.MENU;
        _logicMenu = new LogicMenu(_engine, this);
        try {
            _logicMenu.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startGame(int size) throws Exception {
        _state = GameState.LEVEL;
        _logicGame = new LogicGame(_engine, this, size);
        try {
            _logicGame.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    Engine _engine;

    LogicGame _logicGame;
    LogicMenu _logicMenu;

    GameState _state;

}