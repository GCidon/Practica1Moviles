package gdv.ohno.engine;

import java.util.List;

public interface Input {
    class TouchEvent {
        public TouchEvent(int i, int x, int y) {
            posX = x;
            posY = y;
        }

        public int getPosX(){return posX;}
        public int getPosY(){return posY;}

        int posX;
        int posY;
    }

    List<TouchEvent> getTouchEvents();
}
