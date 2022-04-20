package gdv.ohno.logic;

import java.util.List;

import gdv.ohno.engine.Engine;
import gdv.ohno.engine.Font;
import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Image;
import gdv.ohno.engine.Input;

public class Logic implements gdv.ohno.engine.Logic {

    enum GameState {MENU, LEVEL}

    public Logic() {

    }

    public void update(float deltaTime) {
        if (_state == GameState.LEVEL && !_end) {
            //Comprobacion de victoria
            if (_logicGame.getBoard().CheckWin() && _logicGame.getBoard().getCompletedPercentage() == 100) {
                _logicGame.getBoard().fillingWin();
                _end = true;
                _logicGame.setEnd(true);
            }
        } else if (_state == GameState.LEVEL) {
            //Cambio de estado a menu principal
            if (_timer > _transitionTime) {
                startMenu();
                _logicMenu.setSelecting(true);
                _timer = 0.0;
                _end = false;
            } else {
                _timer += deltaTime;
            }
        }
    }

    public void render(Graphics g) {
        switch (_state) {
            case MENU:
                _logicMenu.render(g);
                break;
            case LEVEL:
                _logicGame.render(g);
                break;
        }
    }

    public void handleInput(List<Input.TouchEvent> te) {
        if (!_end) {
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

    public void init() {
        _engine.getGraphics().setBaseWidth(400);
        _engine.getGraphics().setBaseHeight(600);

        startMenu();
    }

    public void startMenu() {
        _state = GameState.MENU;
        _logicMenu = new LogicMenu(_engine, this);
        try {
            _logicMenu.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startGame(int size) {
        _state = GameState.LEVEL;
        _logicGame = new LogicGame(_engine, this, size);
        try {
            _logicGame.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void selectLevel() {
        _logicMenu._selecting = true;
    }

    void undo() {
        _logicGame.getBoard().undoMove();
    }

    void hint() {
        _logicGame.getHint();
    }

    //diferentes acciones para botones
    public void processButton(String action) {
        switch (action) {
            case "select":
                selectLevel();
                break;
            case "level4":
            case "level5":
            case "level6":
            case "level7":
            case "level8":
            case "level9":
                startGame(action.charAt(5)-'0');
                break;
            case "cancel":
                startMenu();
                break;
            case "undo":
                undo();
                break;
            case "hint":
                hint();
                break;
            default:
                break;
        }
    }

    public LogicGame getLogicGame() { return _logicGame; }

    public LogicMenu getLogicMenu() {
        return _logicMenu;
    }

    public void setEngine(Engine engine) {
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