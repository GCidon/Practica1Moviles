package gdv.ohno.pcengine;

import gdv.ohno.engine.Font;
import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Image;

import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;

public class PCGraphics implements Graphics {
    public PCGraphics(JFrame frame) {
        _frame = frame;
        _font = new PCFont[25];

    }

    public void setGraphics(java.awt.Graphics graphics) {
        _graphics = graphics;

    }

    @Override
    public PCFont newFont(String filename, float size, boolean isBold) {
        return new PCFont(filename, size, isBold);
    }

    @Override
    public void setFont(Font font) {
        PCFont f = (PCFont)font;
        _graphics.setFont(f.getFont());
    }

    public void clear(int color) {
        setColor(color);
        fillRect((int)-getWidth(), (int)-getHeight(), (int) getWidth(), (int) getHeight());
    }

    public void translate(int x, int y) {
        _graphics.translate(x, y);
    }

    public void scale(float x) {
        ((Graphics2D) _graphics).scale(x, -x);
    }

    public void rotate(int angle) {
        ((Graphics2D) _graphics).rotate((Math.toRadians(angle)));
    }

    public void save() {
        _state = ((Graphics2D) _graphics).getTransform();
    }

    public void restore() {
        ((Graphics2D) _graphics).setTransform(_state);
    }

    public void setColor(int color) {
        _graphics.setColor(new Color(color));
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        _graphics.drawLine(x1, y1, x2, y2);
    }

    public void fillRect(int x1, int y1, int x2, int y2) {
        _graphics.fillRect(x1, y1, x2 - x1, y2 - y1);
    }

    public void drawText(String text, int x, int y) {
        ((Graphics2D) _graphics).scale(1, -1);
        _graphics.drawString(text, x, y);
        ((Graphics2D) _graphics).scale(1, -1);
    }

    @Override
    public void fillCircle(int x, int y, int r) {
        _graphics.fillOval(x, y, r, r);
    }

    @Override
    public Image newImage(String name) {
        return new PCImage(name);
    }

    @Override
    public void drawImage(Image image, int x, int y, int x2, int y2) {
        PCImage img = (PCImage)image;
        _graphics.drawImage(img.getImage(), x, y, x2, y2, null);
    }

    @Override
    public void drawFramedImage(Image image, int dx, int dy, int dx2, int dy2, int sx, int sy, int sx2, int sy2) {
        PCImage img = (PCImage)image;
        _graphics.drawImage(img.getImage(), dx, dy, dx2, dy2, sx, sy, sx2, sy2, null);
    }

    public float getWidth() { return _frame.getWidth(); }

    public float getHeight() {
        return _frame.getHeight();
    }

    @Override
    //Metodo util para el correcto reescalado de la pantalla. Devuelve el el ancho o el alto dependiendo de cual es mayor
    public float getProportion() {
        float aux1 = 0;
        float aux2 = 0;

        aux1 = (float) getWidth() / (float) getBaseWidth();
        aux2 = (float) getHeight() / (float) getBaseHeight();

        if (aux1 < aux2)
            return aux1;
        else return aux2;
    }

    @Override
    public void setBaseWidth(float w) {
        width_ = w;
    }

    @Override
    public void setBaseHeight(float h) {
        height_ = h;
    }

    @Override
    public float getBaseWidth() {
        return width_;
    }

    @Override
    public float getBaseHeight() {
        return height_;
    }

    float width_;
    float height_;

    //Array de fuentes creadas
    PCFont[] _font;
    JFrame _frame;
    java.awt.Graphics _graphics;
    AffineTransform _state;
}
