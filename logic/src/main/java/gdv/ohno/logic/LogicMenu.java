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
        _selecting = false;
    }

    public void init() throws Exception {
        //Fuentes
        _fonts[0] = _engine.getGraphics().newFont("fonts/JosefinSans-Bold.ttf", 50, true);
        _fonts[1] = _engine.getGraphics().newFont("fonts/Molle-Regular.ttf", 100, false);
        _fonts[2] = _engine.getGraphics().newFont("fonts/JosefinSans-Bold.ttf", 15, true);
        _fonts[3] = _engine.getGraphics().newFont("fonts/JosefinSans-Bold.ttf", 25, true);

        //Imagenes
        _images[0] = _engine.getGraphics().newImage("sprites/close.png");
        _images[1] = _engine.getGraphics().newImage("sprites/q42.png");

        //Botones
        _buttons.add(new Button(-70, 30, 110, 50, "select", _logica));
        _buttons.add(new Button(-120, 0, 75, 75, "level4", _logica));
        _buttons.add(new Button(-40, 0, 75, 75, "level5", _logica));
        _buttons.add(new Button(40, 0, 75, 75, "level6", _logica));
        _buttons.add(new Button(-120, 80, 75, 75, "level7", _logica));
        _buttons.add(new Button(-40, 80, 75, 75, "level8", _logica));
        _buttons.add(new Button(40, 80, 75, 75, "level9", _logica));
        _buttons.add(new Button(-20, 220, 50, 50, "cancel", _logica));
    }

    //Renderizamos el texto
    public void render(Graphics g) {
        g.clear(0xFFFFFFFF);

        g.setFont(_fonts[1]);
        g.setColor(0xFF000000);
        g.drawText("Oh no", -140, (int) (-g.getBaseHeight() / 3));

        //pantalla inicial
        if(!_selecting) {
            g.setFont(_fonts[0]);
            g.drawText("Jugar", -35, 0);

            g.setFont(_fonts[2]);
            g.setColor(0xFFC1C1C1);
            g.drawText("Un juego copiado a Q42", -70, 150);
            g.drawText("Creado por Martin Kool", -70, 170);

            g.save();
            g.scale(-1);
            g.rotate(180);
            g.drawImage(_images[1], -20, 200, 50, 75);
            g.restore();
        }
        //seleccion de tamaño de tablero
        else {
            g.setFont(_fonts[3]);
            g.drawText("Elija el tamaño a jugar", -120, -100);

            int inix = -120;
            int iniy = 0;
            int color = 0xFF000000;

            //casillas de eleccion de tamaño de tablero
            for(int i = 0; i < 6; i++) {
                if(i >= 3) iniy = 80;
                if(i%2 == 0) color = 0xFF45CCFF;
                else color = 0xFFFF3C54;
                g.setColor(color);
                int posx = inix+((i%3)*80);
                g.fillCircle(posx, -iniy, 75);
                g.setColor(0xFFFFFFFF);
                g.setFont(_fonts[0]);
                g.drawText(String.valueOf(i+4), posx+25, iniy-25);
            }

            g.save();
            g.scale(-1);
            g.rotate(180);
            g.drawImage(_images[0], -15, 190, 25, 25);
            g.restore();
        }

        if(!_selecting) _buttons.get(0).render(g);
        else {
            for(int i = 1; i < _buttons.size(); i++) {
                _buttons.get(i).render(g);
            }
        }
    }

    //Input de los botones
    public void handleInput(List<Input.TouchEvent> te) {
        for (int j = 0; j < te.size(); j++) {
            if(!_selecting) _buttons.get(0).handleInput((te.get(j)));
            else {
                for (int i = 1; i < _buttons.size(); i++) {
                    _buttons.get(i).handleInput((te.get(j)));
                }
            }
        }
    }

    public void setSelecting(boolean aux) {
        _selecting = aux;
    }

    List<Button> _buttons;

    Logic _logica;
    Engine _engine;
    Font _fonts[] = new Font[4];
    Image _images[] = new Image[5];

    boolean _selecting = false;
}
