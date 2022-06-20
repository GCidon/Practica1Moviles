package gdv.ohno.engine;

public interface Graphics {
    public Image newImage(String name) throws Exception;
    public Font newFont(String filename, float size, boolean isBold)throws Exception;
    public void setFont(Font font);

    public void clear(int color);
    public void translate(int x,int y);
    public void scale(float x);
    public void rotate(int angle);
    public void save();
    public void restore();

    //x, y posiciones originales - x2, y2 ancho y alto
    public void drawImage(Image image, int x, int y, int x2, int y2);

    //dx, dy, dx2, dy2 posiciones en pantalla - sx, sy, sx2, sy2 posiciones de imagen
    public void drawFramedImage(Image image, int dx, int dy, int dx2, int dy2, int sx, int sy, int sx2, int sy2);

    public void drawLine(int x1, int y1, int x2, int y2);

    public void setColor(int color);

    public void fillCircle(int x, int y, int r);

    public void fillRect(int x1, int y1, int x2, int y2);

    public void drawText(String text, int x, int y);

    public float getWidth();
    public float getHeight();

    public float getBaseWidth();
    public float getBaseHeight();

    public void setBaseWidth(float w);
    public void setBaseHeight(float h);

    public float getProportion();
}
