package gdv.ohno.logic;

import java.util.ArrayList;
import java.util.List;

import gdv.ohno.engine.Engine;
import gdv.ohno.engine.Font;
import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Image;
import gdv.ohno.engine.Input;

public class LogicMenu {

    LogicMenu(Engine engine, Logic logica) {
        _engine = engine;
        _logica = logica;
        _buttons = new ArrayList();
    }

    public void init() throws Exception {
        _fonts[0] = _engine.getGraphics().newFont("fonts/JosefinSans-Bold.ttf", 50, true);
        _fonts[1] = _engine.getGraphics().newFont("fonts/Molle-Regular.ttf", 30, false);

        _images[0] = _engine.getGraphics().newImage("sprites/lock.png");
        _images[1] = _engine.getGraphics().newImage("sprites/q42.png");
    }

    //Renderizamos todo el texto
    public void render(Graphics g) throws Exception {
        g.clear(0xFFFFFFFF);

        g.save();
        g.scale(-1);
        g.rotate(180);
        g.drawImage(_images[1], (int)g.getBaseWidth()/2, (int)g.getBaseHeight()/2, 200, 200);
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
    Font _fonts[] = new Font[2];
    Image _images[] = new Image[5];
}
