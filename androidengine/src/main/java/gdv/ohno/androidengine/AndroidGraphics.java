package gdv.ohno.androidengine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import gdv.ohno.engine.Graphics;
import gdv.ohno.engine.Image;

public class AndroidGraphics implements Graphics {

    public void setCanvas(Canvas canvas) {
        _c = canvas;
        //grosor de lineas
        _p.setStrokeWidth((float) 1);
    }

    @Override
    public AndroidFont newFont(String filename, float size, boolean isBold)  {
        AndroidFont ret = new AndroidFont(filename, size, isBold, context);
        return ret;
    }

    @Override public void setFont(gdv.ohno.engine.Font font)
    {
        AndroidFont f = (AndroidFont)font;
        _p.setTypeface(f.f);
        _p.setFakeBoldText(f._isBold);
        _p.setTextSize(f._size);
    }

    public void clear(int color) {
        setColor(color);
        fillRect(-_c.getWidth()/2, -_c.getHeight()/2, _c.getWidth(), _c.getHeight());
    }

    public void translate(int x, int y) {
        _c.translate(x, y);
    }

    @Override
    public void scale(float x) {
        _c.scale(x, -x);
    }

    public void rotate(int angle) {
        _c.rotate(angle);
    }

    public void save() {
        _c.save();
    }

    public void restore() {
        _c.restore();
    }

    public void setColor(int color) {
        _p.setColor(color);
    }

    public void fillRect(int x1, int y1, int x2, int y2) {
        Rect r = new Rect(x1, y1, x2, y2);
        _c.drawRect(r, _p);
    }

    public void fillCircle(int x, int y, int r) {
        _c.drawCircle(x+r/2, y+r/2, r/2, _p);
    }

    @Override
    public void drawText(String text, int x, int y) {
        _c.scale(1, -1);
        _c.drawText(text, x, y, _p);
        _c.scale(1, -1);
    }

    public Image newImage(String name) {
        return new AndroidImage(name, context.getAssets());
    }

    public void drawImage(Image image, int x, int y, int x2, int y2) {
        AndroidImage img = (AndroidImage)image;
        Rect r = new Rect(x, y, x+x2, y+y2);
        Rect d = new Rect(0,0, img.getWidth(), img.getHeight());
        _c.drawBitmap(img.getImage(), d, r, _p);
    }

    public void drawFramedImage(Image image, int dx, int dy, int dx2, int dy2, int sx, int sy, int sx2, int sy2) {
        AndroidImage img = (AndroidImage)image;
        Rect r = new Rect(dx, dy, dx+dx2, dy+dy2);
        Rect d = new Rect(sx,sy, sx+sx2, sy+sy2);
        _c.drawBitmap(img.getImage(), d, r, _p);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        _c.drawLine(x1, y1, x2, y2, _p);
    }

    public float getWidth() {
        return _c.getWidth();
    }

    public float getHeight() {
        return _c.getHeight();
    }

    public float getBaseWidth() {
        return width_;
    }

    public float getBaseHeight() {
        return height_;
    }

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

    public void getContext(Context _context) {
        context = _context;
    }

    float width_;
    float height_;
    Canvas _c;
    Context context;
    Paint _p = new Paint();

}
