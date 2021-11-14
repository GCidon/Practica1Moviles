package gdv.ohno.engine;

import java.util.ArrayList;

public interface Input {
    class TouchEvent {

        public TouchEvent(int i, int x, int y) {
            id = i;
            posX = x;
            posY = y;
        }

        public int getPosX(){return posX;}
        public int getPosY(){return posY;}

        public int getId(){return id;}

        int id;
        int posX;
        int posY;
    }

    ArrayList<TouchEvent> getTouchEvents();
}
