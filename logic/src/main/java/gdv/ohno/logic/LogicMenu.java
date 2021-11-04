package gdv.ohno.logic;

import java.util.ArrayList;
import java.util.List;

import gdv.ohno.engine.Engine;
import gdv.ohno.engine.Font;
import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Input;

public class LogicMenu {

    LogicMenu(Engine engine, Logic logica) {
        _engine = engine;
        _logica = logica;
        _buttons = new ArrayList();
    }

    //Iniciamos los botones
    public void init() throws Exception {
        Button easyGame = new Button(-305, 90, 400, 20, "game", _logica);
        _buttons.add(easyGame);
        Button diffGame = new Button(-305, 150, 400, 20, "diffgame", _logica);
        _buttons.add(diffGame);

        _fonts[0] = _engine.getGraphics().newFont("Fuentes/Bungee-Regular.ttf", 50, true);
        _fonts[1] = _engine.getGraphics().newFont("Fuentes/Bungee-Regular.ttf", 30, false);
    }

    //Renderizamos todo el texto
    public void render(Graphics g) throws Exception {
        g.clear(0xFFFFFFFF);
        g.save();

        g.restore();

        for(int i = 0; i < _buttons.size(); i++) {
            _buttons.get(i).render(g);
        }
    }

    //Input de los botones
    public void handleInput(List<Input.TouchEvent> te) throws Exception {
        for (int i = 0; i < _buttons.size(); i++) {
            for (int j = 0; j < te.size(); j++) {
                _buttons.get(i).handleInput(te.get(j));
            }
        }
    }

    public void update(float deltaTime) {

    }

    List<Button> _buttons;

    Logic _logica;
    Engine _engine;
    Font _fonts[] = new Font[6];
}
