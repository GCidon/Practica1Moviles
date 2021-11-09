package gdv.ohno.engine;

import java.util.List;

public interface Logic {
    public void update(float deltaTime) throws Exception;
    public void render(Graphics g) throws Exception;
    public void handleInput(List<Input.TouchEvent> te) throws Exception;
    public void init() throws Exception;
    public void getEngine(Engine engine);
}
