package gdv.ohno.engine;

import java.util.List;

public interface Logic {
    public void update(float deltaTime);
    public void render(Graphics g) ;
    public void handleInput(List<Input.TouchEvent> te) ;
    public void init() ;
    public void setEngine(Engine engine);
}
