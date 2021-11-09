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

    public void update(float deltaTime) throws Exception {
        if(_state == GameState.LEVEL && !_end) {
            if(_logicGame.getBoard().CheckWin() && _logicGame.getBoard().getCompletedPercentage() == 100) {
                _end = true;
                _logicGame.setEnd(true);
            }
        }
        else {
            if(_timer > _transitionTime) {
                startMenu();
                _logicMenu.setSelecting(true);
                _timer = 0.0;
                _end = false;
            }
            else {
                _timer += deltaTime;
            }
        }
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
        if(!_end) {
            switch (_state) {
                case MENU:
                    _logicMenu.handleInput(te);
                    break;
                case LEVEL:
                    _logicGame.handleInput(te);
                    break;
            }
        }
    }
    public void init() throws Exception {
        _engine.getGraphics().setBaseWidth(400);
        _engine.getGraphics().setBaseHeight(600);

        startMenu();
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

    void selectLevel() { _logicMenu._selecting = true; }

    void undo() { _logicGame.getBoard().undoMove(); }

    //diferentes acciones para botones
    public void processButton(String action) throws Exception {
        switch (action) {
            case "select":
                selectLevel();
                break;
            case "level4":
                startGame(4);
                break;
            case "level5":
                startGame(5);
                break;
            case "level6":
                startGame(6);
                break;
            case "level7":
                startGame(7);
                break;
            case "level8":
                startGame(8);
                break;
            case "level9":
                startGame(9);
                break;
            case "cancel":
                startMenu();
                break;
            case "undo":
                undo();
                break;
            case "hint":
                //to do
                break;
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

    boolean _end = false;
    double _timer = 0.0;
    double _transitionTime = 5.0;

}